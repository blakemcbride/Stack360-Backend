/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
 */
package com.arahant.utils;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.proxy.HibernateProxy;
import org.kissweb.StringUtils;

public final class HibernateSessionUtil {

	private static final Logger logger = Logger.getLogger(HibernateSessionUtil.class);
	private Session session;
	private static AtomicInteger nOpenedSessions = new AtomicInteger(0);
	private static boolean showOpenSessions = false;
	boolean inTransaction = false;
	private org.hibernate.Transaction transaction;
	private boolean doingDelete = false;
    boolean aiIntegrate = !ArahantSession.disableJess;
	private boolean autoFlush = true;
	private HashMap queuedHistoryItems = new HashMap();
	private HashMap queuedHistoryInserts = new HashMap();
	private HashMap queuedWizardProjectUpdates = new HashMap();
	private HashMap benefitJoinsToSend = new HashMap();
	private CompanyDetail company;
	private Person currentPerson;
	private org.kissweb.database.Connection kissConnection;

	public void noAutoFlush() {
		autoFlush = false;
	}

	public boolean currentlyArahantUser() {
		return getCurrentPerson().getPersonId().equals("00000-0000000000");
//		return getCurrentPerson().getProphetLogin().getUserLogin().equalsIgnoreCase(ArahantSession.systemName());
	}

	public String getArahantPersonId() {
		return createCriteria(Person.class)
				.selectFields(Person.PERSONID)
				.eq(Person.FNAME, ArahantSession.systemName())
				.stringVal();
	}

	public boolean internalUser() {
		return (currentlySuperUser() || getCurrentPerson().getOrgGroupType() == ArahantConstants.COMPANY_TYPE);
	}

	public boolean externalUser() {
		return !internalUser();
	}

	public HibernateScrollUtil scroll(String query) {
		checkSession();
		return new HibernateScrollUtil(session.createQuery(query).scroll());
	}

	public void setCurrentCompany(CompanyDetail cd) {
		company = cd;
	}

	public CompanyDetail getCurrentCompany() {
		if (company == null)
			company = getFirstNoFilter(CompanyDetail.class);
		return company;
	}

	public void useAIIntegrate() {
		aiIntegrate = !ArahantSession.disableJess;
	}

	private static void adjustSessionCount(int n) {
		if (n == 1)
			nOpenedSessions.incrementAndGet();
		else if (n == -1)
			nOpenedSessions.decrementAndGet();
		if (showOpenSessions)
			System.out.println("Open sessions is " + nOpenedSessions.get());
	}

	public boolean isOpen() {
		return session != null && session.isOpen();
	}

	/**
	 *
	 * @throws Throwable
	 */
	@Override
	@SuppressWarnings("FinalizeDeclaration")
	protected void finalize() throws Throwable {
		try {
			if (session != null && session.isOpen()) {
				logger.warn("Open session in finalize", new Throwable());
				session.close();
				adjustSessionCount(-1);
			}
		} finally {
			super.finalize();
		}
	}

	/**
	 * Close the Hibernate session. Generally you don't want to call this. This
	 * method is good when you got the hsu with new HibernateSessionUtil()
	 * rather than ArahantSession.getHSU().
	 *
	 * Use ArahantSession.closeHSU() because this also takes the instance off
	 * the thread local storage. If the thread local storage is used again, you risk
	 * getting the same instance when the thread is used again by tomcat.
	 */
	final public void close() {
		if (session != null && session.isOpen()) {
			session.clear();
	//		session.flush();
			session.close();
			adjustSessionCount(-1);
		}
		session = null;
		KissConnection.delete();
		//	HibernateUtil.getSessionFactory().close();
	}

	@SuppressWarnings("deprecation")
	public HibernateSessionUtil() {
		session = HibernateUtil.getSessionFactory().openSession();

		int max = 5;
		//Is there a bug in hibernate that doesn't really open sessions?
		while (!session.isOpen() && max > 0) {
			session = HibernateUtil.getSessionFactory().openSession();
			max--;
			logger.error("We think the session is open but its really not. (" + max + ")", new Throwable());
		}

		adjustSessionCount(1);
		KissConnection.set(((SessionImpl)session).connection());
		checkSession();
	}

	public HibernateSessionUtil(boolean aiIntegrate) {
		this();
		this.aiIntegrate = !ArahantSession.disableJess && aiIntegrate;
	}

	public boolean aiIntegrate() {
		return aiIntegrate;
	}

	public void dontAIIntegrate() {
		aiIntegrate = false;
	}

	private void checkSession() {
		if (!inTransaction)
			beginTransaction();
	}

	@SuppressWarnings("unchecked")
	final public HibernateCriteriaUtil createCriteria(String str) {
		try {
			return createCriteria((Class) Class.forName(str));
		} catch (ClassNotFoundException e) {
			throw new ArahantException("Unknown class name passed into create criteria - " + str, e);
		}
	}

	final public <T extends IArahantBean> HibernateCriteriaUtil<T> createCriteria(final Class<T> clazz) {
		checkSession();
		return new HibernateCriteriaUtil<T>(session, clazz, aiIntegrate);
	}

	final public <T extends IArahantBean> HibernateCriteriaUtil<T> createCriteria(final Class<T> clazz, final String alias) {
		checkSession();
		return new HibernateCriteriaUtil<T>(session, clazz, alias, aiIntegrate);
	}

	final public <T extends IArahantBean> HibernateCriteriaUtil<T> createCriteriaNoCompanyFilter(final Class<T> clazz) {
		checkSession();
		HibernateCriteriaUtil<T> c = new HibernateCriteriaUtil<T>(session, clazz, aiIntegrate, true);
		return c;
	}

	final public <T extends IArahantBean> HibernateCriteriaUtil<T> createCriteriaNoCompanyFilter(final Class<T> clazz, final String alias) {
		checkSession();
		HibernateCriteriaUtil<T> c = new HibernateCriteriaUtil<T>(session, clazz, alias, aiIntegrate, true);
		return c;
	}

	final public Session getSession() {
		return session;
	}

	final public void beginTransaction() {
		if (session == null || !session.isOpen()) {
			if (session == null)
				logger.error("HibernateSessionUtil.beginTransaction -- session not open (session == null)!", new Throwable());
			else
				logger.error("HibernateSessionUtil.beginTransaction -- session not open (!isOpen())!", new Throwable());
			session = HibernateUtil.getSessionFactory().openSession();
			adjustSessionCount(1);
			KissConnection.set(((SessionImpl)session).connection());
		}

		if (!inTransaction)
			transaction = session.beginTransaction();

		//	session.getTransaction().setTimeout(60*60*24); //24 hour timeout
		session.clear();
		inTransaction = true;
	}

	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> List<T> doQuery(final String query) {
		checkSession();
		return session.createQuery(query).list();
	}

	final public Query getQuery(final String query) {
		checkSession();
		return session.createQuery(query);
	}

	public void doSQL(String sql) throws SQLException {
		Connection con = getConnection();
		Statement st = con.createStatement();
		st.execute(sql);
		st.close();
	}

	final public List doSQLQuery(final String query) {
		try {
			checkSession();
			return session.createSQLQuery(query).list();
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}
	}

	final public String getFirstSQLString(final String query, final String column) {
		try {
			checkSession();
			Object ret = null;

			final List l = session.createSQLQuery(query)
					.addScalar(column, new org.hibernate.type.StringType())
					.setMaxResults(1)
					.list();
			if (aiIntegrate)
				ArahantSession.runAI();
			if (l.size() > 0)
				ret = l.get(0);
			if (ret == null)
				return null;

			return ret.toString();
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}
	}

	final public Object getFirstSQL(final String query) {
		try {
			checkSession();
			Object ret = null;

			final List l = session.createSQLQuery(query).setMaxResults(1).list();
			if (aiIntegrate)
				ArahantSession.runAI();
			if (l.size() > 0)
				ret = l.get(0);
			return ret;
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> T getFirst(final String query) {
		try {
			checkSession();
			T ret = null;

			final List<T> l = session.createQuery(query).setMaxResults(1).list();
			if (aiIntegrate)
				ArahantSession.runAI();
			if (l.size() > 0)
				ret = l.get(0);
			return ret;
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}
	}

	final public void commitTransaction() throws ArahantException {
		try {
			//Deal with enrollment emails to be sent out
			HashMap emails = benefitJoinsToSend;
			benefitJoinsToSend = new HashMap();

			for (Object o : emails.entrySet()) {
				Map.Entry entry = (Map.Entry) o;
				SendThread thread = new SendThread((BenefitEmailInfo) entry.getValue());
				thread.start();
			}

			if (aiIntegrate)
				ArahantSession.runAI();
			flush();
			if (inTransaction) {
				transaction.commit();
				inTransaction = false;
			}

			//Deal with inserting history records
			HashMap hash = queuedHistoryInserts;
			queuedHistoryInserts = new HashMap();
			HashMap wpHash = queuedWizardProjectUpdates;
			queuedWizardProjectUpdates = new HashMap();

			Iterator i = hash.entrySet().iterator();
			if (i.hasNext()) {
				HibernateSessionUtil historyHSU = ArahantSession.getHSU();
				historyHSU.beginTransaction();
				while (i.hasNext()) {
					Map.Entry entry = (Map.Entry) i.next();
					historyHSU.insert((ArahantHistoryBean) entry.getValue());
				}
				historyHSU.commitTransaction();
			}

			Iterator i2 = wpHash.entrySet().iterator();
			if (i2.hasNext()) {
				HibernateSessionUtil historyHSU = ArahantSession.getHSU();
				historyHSU.beginTransaction();
				while (i2.hasNext()) {
					Map.Entry entry = (Map.Entry) i2.next();
					String key = (String) entry.getKey();
					String historyId = key.substring(0, key.indexOf(":"));
					BHRBenefitJoinH bjh = new BHRBenefitJoinH(historyId);
					WizardProject wp = (WizardProject) entry.getValue();
					wp.setBenefitJoinH(bjh.getBean());
					historyHSU.update(wp);
				}
				historyHSU.commitTransaction();
			}

		} catch (final Exception ex) {
			logger.error(ex);
			if (doingDelete)
				throw new ArahantException("Could not delete.  Item in use.", ex);
			else {
				/* There is a flakey bug in the benefit wizard causing this error every now and then.  Typically, if the
				 * user just retries it works.  Since we can't duplicate the bug we'll have to give the user a better
				 * message for now.
				 */
				for (StackTraceElement elm : ex.getStackTrace())
					if (elm.getClassName().equals("com.arahant.services.standard.wizard.benefitWizard.BenefitWizardOps"))
						throw new ArahantException("The system was not able to process your request.  Please click OK and try again.", ex);
				throw new ArahantException("Could not complete request.  Item violates a database constraint.  This is possibly missing data, or a duplicate entry.", ex);
			}
		}
	}

	final public <T extends IArahantBean> String checkMaxEmployees(final T o) throws ArahantException {
		if (o instanceof Employee)
			return checkMaxEmployees();
		else
			return "";
	}

	final public String checkMaxEmployees() throws ArahantException {
		final int maxEmp = this.getCurrentCompany().getMaxEmployees();
		if (maxEmp == 0)
			return "";
		int currentEmp = this.createCriteria(Employee.class).activeEmployee().count();
		if (currentEmp >= maxEmp)
			try {
				File errorFile = new File(FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/Too_Many_Employees_Error.txt");
				final String defaultErrMessage = "<TEXTFORMAT LEADING=\"2\"><P ALIGN=\"LEFT\"><FONT FACE=\"Tahoma\" SIZE=\"16\" COLOR=\"#000000\" LETTERSPACING=\"0\" KERNING=\"0\">You have already reached the maximum allowable number of active employees in the system.  Contact " + ArahantSession.systemName() + " to upgrade your system to allow entry of more employees.</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING=\"2\"><P ALIGN=\"LEFT\"><FONT FACE=\"Tahoma\" SIZE=\"11\" COLOR=\"#000000\" LETTERSPACING=\"0\" KERNING=\"0\"></FONT></P></TEXTFORMAT><TEXTFORMAT LEADING=\"2\"><P ALIGN=\"LEFT\"><FONT FACE=\"Tahoma\" SIZE=\"16\" COLOR=\"#0000FF\" LETTERSPACING=\"0\" KERNING=\"0\"><A HREF=\"http://arahant.com/contact-arahant.php\" TARGET=\"_blank\"><U>Contact Arahant</U></A></FONT></P></TEXTFORMAT>";
				if (!errorFile.exists()) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(errorFile));
					writer.write(defaultErrMessage);
					writer.flush();
					writer.close();
					return defaultErrMessage;
				} else {
					BufferedReader reader = new BufferedReader(new FileReader(errorFile));
					String errMessage = reader.readLine().trim();
					if (StringUtils.isEmpty(errMessage))
						errMessage = defaultErrMessage;
					reader.close();
					return errMessage;
				}
			} catch (IOException ioe) {
				throw new ArahantException("\"ErrorMessage.txt\" file not found and could not be created.");
			}
		else
			return "";
	}

	final public void rollbackTransaction() {
		if (session.isOpen()  &&  inTransaction)
			transaction.rollback();
		//if I rollback the transaction, need to clear out saved history
		ArahantSession.getHistory().clear();
		inTransaction = false;
		queuedHistoryInserts = new HashMap<>();
	}

	final public <T extends IArahantBean> T saveOrUpdate(final T o) {
		if (o == null)
			return null;
		checkSession();

		if (o instanceof IAuditedBean) {
			IAuditedBean ia = (IAuditedBean) o;
			handleHistoryRecord(ia);
		}

		session.saveOrUpdate(o);
		if (aiIntegrate)
			ArahantSession.runAI();
		//flush(); //leave this flush here, otherwise saves won't happen before a delete
		//when you update and then delete record
		//took it back out and put in flush before delete

		if (o instanceof ArahantSaveNotify && aiIntegrate) {
			//ArahantSession.getAI().watchAll();
			ArahantSaveNotify n = (ArahantSaveNotify) o;
			ArahantSession.AICmd("(assert (saving " + n.notifyClassName() + " \"" + n.notifyId() + "\"))");
			// ArahantSession.AIEval("(facts)");

		}
		return o;
	}

	final public <T extends IArahantBean> T update(final T o) {
		if (o == null)
			return null;
		checkSession();

		if (o instanceof IAuditedBean) {
			IAuditedBean ia = (IAuditedBean) o;
			handleHistoryRecord(ia);
		}

		session.update(o);

		if (aiIntegrate)
			ArahantSession.runAI();
		//flush(); //leave this flush here, otherwise saves won't happen before a delete
		//when you update and then delete record
		//took it back out and put in flush before delete

		if (o instanceof ArahantSaveNotify && aiIntegrate) {
			//ArahantSession.getAI().watchAll();
			ArahantSaveNotify n = (ArahantSaveNotify) o;
			ArahantSession.AICmd("(assert (saving " + n.notifyClassName() + " \"" + n.notifyId() + "\"))");
			// ArahantSession.AIEval("(facts)");

		}
		return o;
	}

	final public <T extends IArahantBean> T insert(final T o) {
		if (o == null)
			return null;
		checkSession();

		if (aiIntegrate)
			ArahantSession.runAI();

		if (o instanceof IAuditedBean) {
			IAuditedBean ia = (IAuditedBean) o;
			handleHistoryRecord(ia);
		}

		String maxEmp = checkMaxEmployees(o);
		if (!StringUtils.isEmpty(maxEmp))
			throw new ArahantWarning(maxEmp);
		session.save(o);

//		add this to the engine
		//	((IArahantBean)entity).linkToEngine();
//		tell the AI engine I'm saving one of whatever this is
		if (o instanceof ArahantSaveNotify && aiIntegrate) {
			ArahantSaveNotify n = (ArahantSaveNotify) o;
			ArahantSession.AICmd("(assert (saving " + n.notifyClassName() + " \"" + n.notifyId() + "\"))");
		}
		if (autoFlush)
			flush();
		return o;
	}

	/**
	 * Deletes all the records in a table. HQL references tables by class name
	 * not the actual table name therefore we pass in the class object.
	 *
	 * @param myTable
	 */
	final public void deleteAllRecords(Class myTable) {
		String hql = String.format("delete from %s", myTable.getName());
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}

	final public <T extends IArahantBean> T delete(final T o) throws ArahantDeleteException {
		//flush();
		try {
			if (o == null)
				return o;
			checkSession();
//			if (autoFlush)
//				flush();

			if (o instanceof IAuditedBean) {
				IAuditedBean ia = (IAuditedBean) o;
				handleDeleteHistoryRecord(ia);
			}

			session.delete(o);

			//doingDelete=true;
//			if (autoFlush)
//				flush();
			return o;
		} catch (final Exception e) {
			logger.error(e);
			throw new ArahantDeleteException(e);
		}
	}

	final public <T extends IArahantBean> Collection<T> delete(final Collection<T> c) throws ArahantDeleteException {
		if (c == null)
			return c;
		for (T t : c)
			delete((IArahantBean) t);
		return c;
	}

	final public <T extends IArahantBean> Collection<T> delete(final HibernateCriteriaUtil<T> cu) throws ArahantDeleteException {
		return delete(cu.list());
	}

	final public <T extends IArahantBean> T delete(final Class<T> clazz, final String key) throws ArahantDeleteException {
		if (key == null)
			return null;
		return delete(get(clazz, key));
	}

	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> T persist(final T o) {
		session.persist(o);
		return o;
	}

	final public void flush() {
		//System.out.println(session.toString());
		session.flush();
	}

	final public <T extends IArahantBean> T merge(final T o) {
		session.merge(o);
		return o;
	}

	final public <T extends IArahantBean> T refresh(final T o) {
		if (o == null)
			return null;
		session.refresh(o);
		return o;
	}

	public List<String> compareCorresponding(final Object o1, final Object o2) {
		List<String> ret = new LinkedList<String>();
		if (o1 == null || o2 == null)
			return ret;

		if (!o1.getClass().equals(o2.getClass()))
			return ret;

		final Method[] mems = o1.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {

				if (name.equals("getEventSetDescriptors")
						|| name.equals("getPropertyDescriptors"))
					continue;

				//	final Class pTypes[]=new Class[1];
				//	pTypes[0]=element.getReturnType();
				try {

					Object r1 = element.invoke(o1, (Object[]) null);
					Object r2 = element.invoke(o2, (Object[]) null);

					if (!r1.equals(r2)) {
						logger.info(name.substring(3) + "-" + r1 + "!=" + r2);
						ret.add(name.substring(3));
					}
				} catch (final Throwable ignored) {
				}
			}

		}
		return ret;
	}

	/**
	 * Copies all beans from one object to another
	 *
	 * @param to
	 * @param from
	 * @return returns the 'to' object
	 */
	public static Object copyCorresponding(final Object to, final Object from) {
		return copyCorresponding(to, from, (String[]) null);
	}

	/**
	 * Copies all beans from one object to another except the fields listed in
	 * excludeFields
	 *
	 * @param to
	 * @param from
	 * @param excludeFields
	 * @return returns the 'to' object
	 */
	public static Object copyCorresponding(final Object to, final Object from, String... excludeFields) {
		if (from == null)
			return to;
		if (to == null)
			return null;

		HashSet<String> excludes = new HashSet<String>();

		if (excludeFields != null)
			for (String exclude : excludeFields) {
				String n = "get" + exclude.substring(0, 1).toUpperCase();
				if (exclude.length() > 1)
					n += exclude.substring(1);
				excludes.add(n);
			}

		final Method[] mems = from.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (excludes.contains(name))
				continue;
			if (name.startsWith("get")) {
				final String findName = "s" + name.substring(1);
				final Class [] pTypes = new Class[1];
				pTypes[0] = element.getReturnType();
				try {
					final Method meth = to.getClass().getMethod(findName, pTypes);
					if (meth == null)
						continue;
					final Object [] args = new Object[1];
					args[0] = element.invoke(from, (Object[]) null);
					Object obj = args[0];
//					if (obj.getClass() == String.class  ||
//							obj.getClass() == Integer.class  ||
//							obj.getClass() == Long.class  ||
//							obj.getClass() == Float.class  ||
//							obj.getClass() == Double.class ||
//							obj.getClass() == Boolean.class ||
//							obj.getClass() == Character.class ||
//							obj.getClass() == Short.class)
					if (!(obj instanceof Collection))
						meth.invoke(to, args);
				} catch (final Throwable ignored) {

				}
			}
		}
		return to;
	}

	/*
	 final public <T extends IArahantBean>HibernateCriteriaUtil<T> createCriteria(final Class<T> clazz)
	 {
	 checkSession();
	 return new HibernateCriteriaUtil<T>(session,clazz);
	 }

	 */
	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> T get(final String className, final String key) {
		try {
			return get((Class<T>) Class.forName(className), key);
		} catch (ClassNotFoundException e) {
			logger.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> T get(final Class<T> clazz, final String key) {
		try {
			if (key == null)
				return null;

			checkSession();

			T val = (T) session.get(clazz, key);

			if (aiIntegrate)
				ArahantSession.runAI();

			if (val instanceof AuditedBean)
				((AuditedBean) val).saveOriginalBean();
			return val;
		} catch (final ObjectNotFoundException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	final public <T extends IArahantBean> T get(final Class<T> clazz, final Serializable key) {
		try {
			if (key == null)
				return null;

			checkSession();

			T val = (T) session.get(clazz, key);

			if (val instanceof AuditedBean)
				((AuditedBean) val).saveOriginalBean();

			if (aiIntegrate)
				ArahantSession.runAI();

			return val;
		} catch (final ObjectNotFoundException e) {
			return null;
		}
	}

	final public <T extends IArahantBean> List<T> getAll(final Class<T> clazz) {
		checkSession();
		List<T> l = new HibernateCriteriaUtil<T>(session, clazz, aiIntegrate).list();
		if (aiIntegrate)
			ArahantSession.runAI();
		return l;
	}

	final public <T extends IArahantBean> T getFirstNoFilter(final Class<T> clazz) {
		try {
			checkSession();
			HibernateCriteriaUtil<T> hcu = new HibernateCriteriaUtil<T>(session, clazz, aiIntegrate, true);
			T val = hcu.first();
			return val;
		} catch (final Exception e) {
			return null;
		}
	}

	final public <T extends IArahantBean> T getFirst(final Class<T> clazz) {
		try {
			checkSession();
			HibernateCriteriaUtil<T> hcu = new HibernateCriteriaUtil<T>(session, clazz, aiIntegrate);
			T val = hcu.first();
			return val;
		} catch (final Exception e) {
			return null;
		}
	}
	/*
	 final public void startBatchMode()
	 {
	 session.setFlushMode(FlushMode.ALWAYS);

	 Transaction t=session.beginTransaction();
	 SQLQuery sq= session.createSQLQuery("");

	 }
	 */

	/**
	 * @param itemsToUpdate
	 */
	final public <T extends IArahantBean> void update(final Collection<T> itemsToUpdate) {
		for (T itm : itemsToUpdate)
			saveOrUpdate(itm);
	}

	final public Person getCurrentPerson() {
		//	if (currentPerson==null) //must be running as Arahant
		//		setCurrentPersonToArahant();
		return currentPerson;
	}

	final public void setCurrentPerson(final Person p) {
		this.currentPerson = p;
	}

	final public <T extends IArahantBean> void insert(final Collection<T> itemsToUpdate) {
		for (T itm : itemsToUpdate)
			insert(itm);
	}

	final public boolean currentlySuperUser() {
		if (currentlyArahantUser())
			return true;
		return BRight.checkAIRight(this, Right.RIGHT_SUPER_USER) == ArahantConstants.ACCESS_LEVEL_WRITE;
	}

	final public boolean currentlySuperUser(String companyId) {
		if (currentlyArahantUser())
			return true;
		return BRight.checkAIRight(this, Right.RIGHT_SUPER_USER, companyId) == ArahantConstants.ACCESS_LEVEL_WRITE;
	}

	final public String getSuperUserPersonId() {
		return "00000-0000000000";
		/*
		return createCriteria(ProphetLogin.class)
				.eq(ProphetLogin.USERLOGIN, ArahantSession.systemName())
				.first().getPersonId();
		 */
	}

	/**
	 * This erases a record from Hibernate cache to save memory but
	 * does not delete the row from the SQL table.
	 *
	 * @param sgh
	 */
	final public void evict(final IArahantBean sgh) {
		session.evict(sgh);
	}

	final public void clear() {
		session.clear();
		/*
		 try
		 {
		 session.clear();
		 }
		 catch (org.hibernate.SessionException e)
		 {
		 //session probably closed, open a new one
		 session = HibernateUtil.getSessionFactory().openSession();
		 adjustSessionCount(1);

		 t=session.beginTransaction();

		 isOpen=true;
		 }
		 * */
	}

	/**
	 * @return
	 */
	@SuppressWarnings("deprecation")
	final public Connection getConnection() {
		SessionImpl sessionImpl = (SessionImpl) session;
		return sessionImpl.connection();
//		return session.connection();
	}

	/**
	 * This object should not be disposed or closed.  It is done automatically by Hibernate.
	 */
	public org.kissweb.database.Connection getKissConnection() {
		return KissConnection.get();
	}

	@SuppressWarnings("unchecked")
	final public boolean exists(final Class clazz, final String personId) {
		return get(clazz, personId) != null;
	}

	/**
	 * @param string
	 * @return
	 */
	final public Query createQuery(final String string) {
		return session.createQuery(string);
	}

	/**
	 * @param string
	 * @param max
	 * @return
	 */
	final public List doQuery(final String string, final int max) {

		List ret = session.createQuery(string).setMaxResults(max).list();
		if (aiIntegrate)
			ArahantSession.runAI();
		return ret;
	}

	/**
	 * @param sql
	 */
	final public void executeSQL(final String sql) {
		session.createSQLQuery(sql).executeUpdate();
	}

	/**
	 *
	 */
	public void setCurrentPersonToArahant() {
		setCurrentPerson(createCriteriaNoCompanyFilter(Person.class).eq(Person.PERSONID, "00000-0000000000").first());
//		setCurrentPerson(createCriteriaNoCompanyFilter(Person.class).eq(Person.FNAME, ArahantSession.systemName()).first());
	}

	@SuppressWarnings("unchecked")
	public void handleHistoryRecord(IAuditedBean modifiedBean) {
		Session tempSession = HibernateUtil.getSessionFactory().openSession();
		adjustSessionCount(1);

		if (modifiedBean instanceof HibernateProxy) //Hibernate creates insane internal classes i.e. 'PersonPending$$EnhancerByCGLIB23u9840'
		{
			System.out.println("Cannot create history record out of proxy object " + modifiedBean.getClass().getName());
			tempSession.close();
			adjustSessionCount(-1);
			return;
		}
		//IAuditedBean originalBean2 = (IAuditedBean) tempSession.get(modifiedBean.getClass(), (Serializable) modifiedBean.keyValue());
		IAuditedBean originalBean = null;
		if (((ArahantBean) modifiedBean).getOriginalBean() != null)
			originalBean = (IAuditedBean) ((ArahantBean) modifiedBean).getOriginalBean();
//		if(hasDifference(originalBean, originalBean2))
//		{
//			System.out.println("OMG THEY ARE DIFFERENT AND THEY SHOULDNT BE");
//		}
		if (originalBean == null) {
			modifiedBean.setRecordChangeDate(new java.util.Date());
			modifiedBean.setRecordChangeType('N');
			Person pers = ArahantSession.getHSU().getCurrentPerson();

			if (pers != null)
				modifiedBean.setRecordPersonId(pers.getPersonId());
			else  //it is the Arahant User
				modifiedBean.setRecordPersonId(ArahantSession.getHSU().getArahantPersonId());
			sendAddToEmailQueue(modifiedBean);
		} else if (hasDifference(modifiedBean, originalBean)) {
			sendEditToEmailQueue(modifiedBean, originalBean);
			if (originalBean.getRecordChangeType() == 'N') {
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object

						try {
							final Class [] getterInputTypes = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class [] setterInputTypes = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object [] args = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}
						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('N');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						if (originalBean instanceof HrBenefitJoin)
							for (WizardProject wp : BWizardProject.adjustWizardProjects(originalBean))
								queuedWizardProjectUpdates.put(hb.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

						//tempSession.save(hb);
						//ArahantSession.getHSU().insert(hb);
						queuedHistoryInserts.put(hb.getClass().getName() + hb.getHistory_id(), hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						logger.error(e);
					}

					//change the current record information for the later history creation

					modifiedBean.setRecordChangeDate(new java.util.Date());
					modifiedBean.setRecordChangeType('M');
					Person per = ArahantSession.getHSU().getCurrentPerson();
					if (per == null)
						modifiedBean.setRecordPersonId(originalBean.getRecordPersonId());
					else
						modifiedBean.setRecordPersonId(per.getPersonId());
				}
			} else if (originalBean.getRecordChangeType() == 'M')
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object

						try {
							final Class [] getterInputTypes = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class [] setterInputTypes = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object [] args = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}

						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('M');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						if (originalBean instanceof HrBenefitJoin)
							for (WizardProject wp : BWizardProject.adjustWizardProjects(originalBean))
								queuedWizardProjectUpdates.put(hb.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

						//tempSession.save(hb);
						//ArahantSession.getHSU().insert(hb);
						queuedHistoryInserts.put(hb.getClass().getName() + hb.getHistory_id(), hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						logger.error(e);
					}

					//change the current record information for the later history creation

					modifiedBean.setRecordChangeDate(new java.util.Date());
					modifiedBean.setRecordChangeType('M');
					Person per2 = ArahantSession.getHSU().getCurrentPerson();
					if (per2 == null)
						modifiedBean.setRecordPersonId(originalBean.getRecordPersonId());
					else
						modifiedBean.setRecordPersonId(per2.getPersonId());
				}
		}
		tempSession.flush();
		tempSession.close();
		adjustSessionCount(-1);
	}

	@SuppressWarnings("unchecked")
	public void handleHistoryRecord(IAuditedBean modifiedBean, IAuditedBean originalBean) {

		if (modifiedBean instanceof HibernateProxy) //Hibernate creates insane internal classes i.e. 'PersonPending$$EnhancerByCGLIB23u9840'
		{
			System.out.println("Cannot create history record out of proxy object " + modifiedBean.getClass().getName());
			return;
		}
		//IAuditedBean originalBean = (IAuditedBean) tempSession.get(modifiedBean.getClass(), (Serializable) modifiedBean.keyValue());
		if (originalBean == null) {
			modifiedBean.setRecordChangeDate(new java.util.Date());
			modifiedBean.setRecordChangeType('N');
			Person pers = ArahantSession.getHSU().getCurrentPerson();

			if (pers != null)
				modifiedBean.setRecordPersonId(pers.getPersonId());
			else  //it is the Arahant User
				modifiedBean.setRecordPersonId(ArahantSession.getHSU().getArahantPersonId());
		} else if (hasDifference(modifiedBean, originalBean))
			if (originalBean.getRecordChangeType() == 'N') {
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object

						try {
							final Class [] getterInputTypes = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class [] setterInputTypes = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object [] args = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}

						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('N');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						if (originalBean instanceof HrBenefitJoin)
							for (WizardProject wp : BWizardProject.adjustWizardProjects(originalBean))
								queuedWizardProjectUpdates.put(hb.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

						//tempSession.save(hb);
						//ArahantSession.getHSU().insert(hb);
						queuedHistoryInserts.put(hb.getClass().getName() + hb.getHistory_id(), hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						logger.error(e);
					}

					//change the current record information for the later history creation

					originalBean.setRecordChangeDate(new java.util.Date());
					originalBean.setRecordChangeType('M');
					Person per = ArahantSession.getHSU().getCurrentPerson();
					if (per == null)
						originalBean.setRecordPersonId(originalBean.getRecordPersonId());
					else
						originalBean.setRecordPersonId(per.getPersonId());
				}
			} else if (originalBean.getRecordChangeType() == 'M')
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object

						try {
							final Class [] getterInputTypes = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class [] setterInputTypes = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object [] args = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}

						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('M');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						if (originalBean instanceof HrBenefitJoin)
							for (WizardProject wp : BWizardProject.adjustWizardProjects(originalBean))
								queuedWizardProjectUpdates.put(hb.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

						//tempSession.save(hb);
						//ArahantSession.getHSU().insert(hb);
						queuedHistoryInserts.put(hb.getClass().getName() + hb.getHistory_id(), hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						logger.error(e);
					}

					//change the current record information for the later history creation

					originalBean.setRecordChangeDate(new java.util.Date());
					originalBean.setRecordChangeType('M');
					Person per2 = ArahantSession.getHSU().getCurrentPerson();
					if (per2 == null)
						originalBean.setRecordPersonId(originalBean.getRecordPersonId());
					else
						originalBean.setRecordPersonId(per2.getPersonId());
				}
	}

	@SuppressWarnings("unchecked")
	private void handleDeleteHistoryRecord(IAuditedBean bean) {
		//could be a potential bug if setters were called before actually deleting (intention of changes)
		if (queuedHistoryItems.get(bean.getClass().getName() + bean.keyValue().toString()) == null)
			try {
				ArahantHistoryBean hb = bean.historyObject();
				hb.copy(bean); //copy everything into your history object

				try {
					final Class [] getterInputTypes = new Class[0];
					final Method getter = bean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

					final Class [] setterInputTypes = new Class[1];
					setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
					final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

					if (getter != null && setter != null) {
						final Object [] args = new Object[1];
						args[0] = getter.invoke(bean, (Object[]) null);
						setter.invoke(hb, args);
					}
				} catch (Exception e) {
					//not an instance of HrBenefitJoin so continue
				}

				//set the history information
				hb.setRecordChangeDate(bean.getRecordChangeDate());
				hb.setRecordChangeType(bean.getRecordChangeType());
				hb.setRecordPersonId(bean.getRecordPersonId());

				//set the history information if it didnt exist (shouldn't ever hit this)
				if (hb.getRecordChangeDate() == null)
					hb.setRecordChangeDate(new java.util.Date());
				if (hb.getRecordChangeType() == 0)
					hb.setRecordChangeType('M');
				if (hb.getRecordPersonId() == null)
					hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
				hb.generateId();

				if (bean instanceof HrBenefitJoin)
					for (WizardProject wp : BWizardProject.adjustWizardProjects(bean))
						queuedWizardProjectUpdates.put(hb.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

				//tempSession.save(hb);
				//ArahantSession.getHSU().insert(hb);
				queuedHistoryInserts.put(hb.getClass().getName() + hb.getHistory_id(), hb);
			} catch (Exception e) {
				logger.error(e);
			}

		//change the current record information for the later history creation

		try {
			ArahantHistoryBean hb2 = bean.historyObject();
			hb2.copy(bean); //copy everything into your history object

			try {
				final Class [] getterInputTypes = new Class[0];
				final Method getter = bean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

				final Class [] setterInputTypes = new Class[1];
				setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
				final Method setter = hb2.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

				if (getter != null && setter != null) {
					final Object [] args = new Object[1];
					args[0] = getter.invoke(bean, (Object[]) null);
					setter.invoke(hb2, args);
				}
			} catch (Exception e) {
				//not an instance of HrBenefitJoin so continue
			}
			hb2.setRecordChangeDate(new java.util.Date());
			hb2.setRecordChangeType('D');
			Person pers = ArahantSession.getHSU().getCurrentPerson();

			if (pers != null)
				hb2.setRecordPersonId(pers.getPersonId());
			else  //it is the Arahant User
				hb2.setRecordPersonId(ArahantSession.getHSU().getArahantPersonId());
			hb2.generateId();

			if (bean instanceof HrBenefitJoin)
				for (WizardProject wp : BWizardProject.adjustWizardProjects(bean))
					queuedWizardProjectUpdates.put(hb2.getHistory_id() + ":" + wp.getWizardProjectId(), wp);

			//tempSession.save(hb);
			//ArahantSession.getHSU().insert(hb);
			queuedHistoryInserts.put(hb2.getClass().getName() + hb2.getHistory_id(), hb2);
			queuedHistoryItems.put(bean.getClass().getName() + bean.keyValue().toString(), bean);

			if (hb2 instanceof HrBenefitJoinH)
				sendDeleteToEmailQueue(hb2);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/*	public void saveOrUpdateAll()
	 {
	 session.sav\\\
	 }
	 */

	private static boolean hasDifference(final Object to, final Object from) {
		boolean ret = false;

		if (from == null)
			return to != null;

		final Method[] mems = from.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {

				{
					Annotation[] annotations = element.getAnnotations();
					String annName;
					boolean isDBColumn = true;
					for (Annotation ann : annotations) {
						annName = ann.toString();
//						System.out.println("************* Annotation for " + from.getClass().getName() + ":" + name + ": " + annName);
						if (annName.startsWith("@javax.persistence.Column("))
							break;
						else if (annName.startsWith("@javax.persistence.Transient(")
								|| annName.startsWith("@javax.persistence.ManyToOne(")
								|| annName.startsWith("@javax.persistence.JoinColumn(")
								|| annName.startsWith("@javax.persistence.OneToMany(")
								|| annName.startsWith("@javax.persistence.ManyToMany(")
								|| annName.startsWith("@javax.persistence.OneToOne(")) {
							isDBColumn = false;
							break;
						}
					}
					if (!isDBColumn)
						//System.out.println("Skipping non-column method " + name);
						continue;
				}


				final Class [] pTypes = new Class[0];
				try {
					final Method meth = to.getClass().getMethod(name, pTypes);
					if (meth == null)
						continue;
					final Object [] args = new Object[2];
					args[0] = element.invoke(from, (Object[]) null);
					args[1] = element.invoke(to, (Object[]) null);
					Object obj = args[0];
					Object obj2 = args[1];
					if (obj.getClass() == String.class
							|| obj.getClass() == Integer.class
							|| obj.getClass() == Long.class
							|| obj.getClass() == Float.class
							|| obj.getClass() == Double.class
							|| obj.getClass() == Boolean.class
							|| obj.getClass() == Character.class
							|| obj.getClass() == Short.class)
						//System.out.println("TESTING: " + obj.toString() + " -- " + obj2.toString() + "  :)");
						if (!obj.equals(obj2))
							//System.out.println("Difference Detected: " + obj.toString() + " -- " + obj2.toString() + "  :)");
							ret = true;
					//System.out.println("Copying " + from.getClass().getSimpleName() + "." + name + " (" + args[0].toString() + ") to " + to.getClass().getSimpleName() + "." + meth.getName());

				} catch (final Throwable ignored) {
				}
			}

		}
		return ret;
	}

	public class BenefitEmailInfo {

		List<HrBenefitJoin> benefitJoins;
		List<HrBenefitJoinH> benefitJoinDeletes;
		List<List> persons = new ArrayList<>();
		List<List> personsDeletes = new ArrayList<>();
		List<Integer> bcrDates = new ArrayList<>();
		List<Integer> bcrDatesDeletes = new ArrayList<>();
		String admin, subject, type, bcrId;

		public BenefitEmailInfo(String a, String s, String t, List<HrBenefitJoin> l, String b, String c, String p, int d) {
			admin = a;
			subject = s;
			type = t;
			bcrId = b;
			bcrDates.add(d);
			@SuppressWarnings("unchecked")
			List<String> plist = new ArrayList();
			plist.add(c);
			plist.add(p);
			persons.add(plist);
			benefitJoins = l;
			benefitJoinDeletes = new ArrayList<HrBenefitJoinH>();
		}

		public BenefitEmailInfo(String a, String s, String t, List<HrBenefitJoinH> l, String b, String c, String p, int d, boolean f) {
			admin = a;
			subject = s;
			type = t;
			bcrId = b;
			bcrDatesDeletes.add(d);
			@SuppressWarnings("unchecked")
			List<String> plist = new ArrayList<>();
			plist.add(c);
			plist.add(p);
			personsDeletes.add(plist);
			benefitJoinDeletes = l;
			benefitJoins = new ArrayList<HrBenefitJoin>();
		}

		public List<Integer> getBcrDates() {
			return bcrDates;
		}

		public void setBcrDates(List<Integer> bcrDates) {
			this.bcrDates = bcrDates;
		}

		public List<Integer> getBcrDatesDeletes() {
			return bcrDatesDeletes;
		}

		public void setBcrDatesDeletes(List<Integer> bcrDatesDeletes) {
			this.bcrDatesDeletes = bcrDatesDeletes;
		}

		public List<List> getPersonsDeletes() {
			return personsDeletes;
		}

		public void setPersonsDeletes(List<List> personsDeletes) {
			this.personsDeletes = personsDeletes;
		}

		public List<List> getPersons() {
			return persons;
		}

		public void setPersons(List<List> persons) {
			this.persons = persons;
		}

		public String getBcrId() {
			return bcrId;
		}

		public void setBcrId(String bcrId) {
			this.bcrId = bcrId;
		}

		public void setAdmin(String admin) {
			this.admin = admin;
		}

		public void setBenefitJoins(List<HrBenefitJoin> benefitJoins) {
			this.benefitJoins = benefitJoins;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<HrBenefitJoin> getBenefitJoinList() {
			return benefitJoins;
		}

		public String getAdmin() {
			return admin;
		}

		public String getSubject() {
			return subject;
		}

		public String getType() {
			return type;
		}

		public List<HrBenefitJoinH> getBenefitJoinDeletes() {
			return benefitJoinDeletes;
		}

		public void setBenefitJoinDeletes(List<HrBenefitJoinH> benefitJoinDeletes) {
			this.benefitJoinDeletes = benefitJoinDeletes;
		}
	}

	static class SendThread extends Thread {

		BenefitEmailInfo info;
		String policyMessage;
		String dependentMessage;
		String benefitMessage;

		public SendThread(BenefitEmailInfo i) {
			super();
			setDaemon(true);
			info = i;
			policyMessage = "";
			dependentMessage = "";
			benefitMessage = "";
		}

		@Override
		public void run() {
			ArahantSession.openHSU();
			final String endline = "\r\n";
			try {
				if (info.getBenefitJoinList() != null && info.getBenefitJoinList().size() > 0)
					for (HrBenefitJoin j : info.getBenefitJoinList()) {
						BHRBenefitJoin bj = new BHRBenefitJoin(j);
						if (StringUtils.isEmpty(info.getBcrId())) {
							int x = 0;
						}
						BHRBenefitChangeReason bc = new BHRBenefitChangeReason(info.getBcrId());
						//List personList = info.getPersons().get(info.getBenefitJoinList().indexOf(j));
						String coveredPersonId = (String) (info.getPersons().get(info.getBenefitJoinList().indexOf(j)).get(0));
						String payingPersonId = (String) (info.getPersons().get(info.getBenefitJoinList().indexOf(j)).get(1));
						int bcrDate = info.getBcrDates().get(info.getBenefitJoinList().indexOf(j));
						BPerson coveredPerson = new BPerson(coveredPersonId);

						if (coveredPersonId.equals(payingPersonId))
							policyMessage = "Employee: " + coveredPerson.getNameLFM() + endline
									+ "Employee SSN: " + coveredPerson.getSsn() + endline
									+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
									+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
									+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
									+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
									+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
									+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
									+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + //QE date
									"";
						else
							dependentMessage += "Covered Person: " + coveredPerson.getNameLFM() + endline
									+ "Covered SSN: " + coveredPerson.getSsn() + endline
									+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
									+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
									+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
									+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
									+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
									+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
									+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + endline + //QE date
									"";
						if (StringUtils.isEmpty(benefitMessage)) {
							BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
							BEmployee be = new BEmployee(payingPersonId);


							String sb = "";
							sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
							sb += ("Transaction Type: " + info.getType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
							if (bh.getBean().getProvider() != null)
								sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline; //Carrier
							else
								sb += ("Carrier: (Not Specified)") + endline;
							sb += ("Policy Owner: " + be.getNameFML()) + endline;
							sb += ("Policy Owner SSN: " + be.getSsn()) + endline;
							sb += ("Benefit: " + bj.getBenefitConfig().getBenefitName()) + endline;
							sb += ("Level: " + bj.getBenefitConfigName()) + endline;
							if (bj.getUsingCOBRA()) {
								sb += ("Cobra: " + "Yes") + endline;
								sb += ("Accepted Cobra Date: " + DateUtils.getDateFormatted(bj.getAcceptedDateCOBRA())) + endline;
								sb += ("Max Cobra Months:" + bj.getMaxMonthsCOBRA()) + endline;
							}
							sb += ("Policy: " + bh.getGroupId()) + endline;
							sb += ("Plan: " + bh.getPlan()) + endline;
							sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
							sb += ("Plan Name: " + bh.getPlanName()) + endline;
							sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
							sb += ("Email: " + be.getPersonalEmail()) + endline;
							sb += ("Company: " + be.getCompanyName()) + endline;

							benefitMessage = sb;
						}
					}
				else if (info.getBenefitJoinDeletes() != null && info.getBenefitJoinDeletes().size() > 0)
					for (HrBenefitJoinH bj : info.getBenefitJoinDeletes()) {
						BHRBenefitChangeReason bc = new BHRBenefitChangeReason(info.getBcrId());
						String coveredPersonId = (String) (info.getPersonsDeletes().get(info.getBenefitJoinDeletes().indexOf(bj)).get(0));
						String payingPersonId = (String) (info.getPersonsDeletes().get(info.getBenefitJoinDeletes().indexOf(bj)).get(1));
						int bcrDate = info.getBcrDatesDeletes().get(info.getBenefitJoinDeletes().indexOf(bj));
						BPerson coveredPerson = new BPerson(coveredPersonId);

						if (bj.getCoveredPersonId().equals(bj.getPayingPersonId()))
							policyMessage = "Employee: " + coveredPerson.getNameLFM() + endline
									+ "Employee SSN: " + coveredPerson.getSsn() + endline
									+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
									+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
									+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
									+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
									+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
									+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
									+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + //QE date
									"";
						else
							dependentMessage += "Covered Person: " + coveredPerson.getNameLFM() + endline
									+ "Covered SSN: " + coveredPerson.getSsn() + endline
									+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
									+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
									+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
									+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
									+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
									+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
									+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + endline + //QE date
									"";
						if (StringUtils.isEmpty(benefitMessage)) {
							BHRBenefit bh = new BHRBenefit(bj.getHrBenefitConfig().getBenefitId());
							BEmployee be = new BEmployee(payingPersonId);


							String sb = "";
							sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
							sb += ("Transaction Type: " + info.getType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
							if (bh.getBean().getProvider() != null)
								sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline; //Carrier
							else
								sb += ("Carrier: (Not Specified)") + endline;
							sb += ("Policy Owner: " + be.getNameFML()) + endline;
							sb += ("Policy Owner SSN: " + be.getSsn()) + endline;
							sb += ("Benefit: " + bj.getHrBenefitConfig().getHrBenefit().getName()) + endline;
							sb += ("Level: " + bj.getHrBenefitConfig().getName()) + endline;
							if (bj.getUsingCOBRA() == 'Y') {
								sb += ("Cobra: " + "Yes") + endline;
								sb += ("Accepted Cobra Date: " + DateUtils.getDateFormatted(bj.getAcceptedDateCOBRA())) + endline;
								sb += ("Max Cobra Months:" + bj.getMaxMonthsCOBRA()) + endline;
							}
							sb += ("Policy: " + bh.getGroupId()) + endline;
							sb += ("Plan: " + bh.getPlan()) + endline;
							sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
							sb += ("Plan Name: " + bh.getPlanName()) + endline;
							sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
							sb += ("Email: " + be.getPersonalEmail()) + endline;
							sb += ("Company: " + be.getCompanyName()) + endline;

							benefitMessage = sb;
						}
					}

				if (BProperty.getBoolean("TestEnvironment"))
					info.setSubject("[TEST] " + info.getSubject());
			} catch (Exception e) {
				logger.error(e);
			}
			ArahantSession.clearSession();    //  this closes the HSU session
		}
	}

	@SuppressWarnings("unchecked")
	private void sendAddToEmailQueue(Object entity) {
		if (entity instanceof HrBenefitJoin && BProperty.getBoolean("DRCMessaging")) {
			BHRBenefitJoin bbj = new BHRBenefitJoin((HrBenefitJoin) entity);

			if (bbj.getBenefitApproved() && bbj.getBenefitConfig() != null) //dont deal with declines
				if (bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					if (!StringUtils.isEmpty(BProperty.get(StandardProperty.AdminEmail)))
						if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId())) {
							BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
							if (!bei.getBenefitJoinList().contains(bbj.getBean())) {
								bei.getBenefitJoinList().add(bbj.getBean());
								bei.setBcrId(bbj.getBenefitChangeReasonId());
								bei.getBcrDates().add(bbj.getChangeReasonDate());
								@SuppressWarnings("unchecked")
								List<String> persons = new ArrayList<>();
								persons.add(bbj.getCoveredPersonId());
								persons.add(bbj.getPayingPersonId());
								bei.getPersons().add(persons);
							} else {
								bei.setType("New");
								bei.setSubject("New Benefit Enrollment");
							}
						} else {
							List<HrBenefitJoin> newList = new ArrayList<>();
							newList.add(bbj.getBean());
							benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(StandardProperty.AdminEmail), "New Benefit Enrollment", "New", newList, bbj.getBenefitChangeReasonId(), bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bbj.getChangeReasonDate()));
						}
					else
						logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
		}
	}

	@SuppressWarnings("unchecked")
	private void sendDeleteToEmailQueue(Object entity) {
		if (entity instanceof HrBenefitJoinH && BProperty.getBoolean("DRCMessaging")) {
			HrBenefitJoinH bbj = (HrBenefitJoinH) entity;

			if (bbj.getBenefitApproved() == 'Y' && bbj.getHrBenefitConfig() != null) //dont deal with declines
				if (bbj.getHrBenefitConfig().getHrBenefit().getProcessType() == 'H')
					if (!StringUtils.isEmpty(BProperty.get(BProperty.get(StandardProperty.AdminEmail))))
						if (bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() > DateUtils.now()) //if the end date is set in the future, email as a future term
							if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId())) {
								BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId());
								if (!bei.getBenefitJoinDeletes().contains(bbj)) {
									bei.getBenefitJoinDeletes().add(bbj);
									String bcrId = "";
									if (bbj.getBenefitChangeReason() != null && bbj.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
										bcrId = bbj.getBenefitChangeReason().getHrBenefitChangeReasonId();

									if (StringUtils.isEmpty(bcrId) && bbj.getLifeEvent() != null && bbj.getLifeEvent().getLifeEventId() != null)
										bcrId = bbj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
									bei.setBcrId(bcrId);
									int bcrDate = bbj.getBenefitChangeReason() != null ? bbj.getBenefitChangeReason().getEffectiveDate() : bbj.getLifeEvent().getEventDate();
									bei.getBcrDatesDeletes().add(bcrDate);
									List<String> persons = new ArrayList<>();
									persons.add(bbj.getCoveredPersonId());
									persons.add(bbj.getPayingPersonId());
									bei.getPersonsDeletes().add(persons);
								} else {
									bei.setType("Future Terminate");
									bei.setSubject("Modify Benefit Enrollment");
								}
							} else {
								List<HrBenefitJoinH> newList = new ArrayList<HrBenefitJoinH>();
								newList.add(bbj);
								String bcrId = "";
								if (bbj.getBenefitChangeReason() != null && bbj.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
									bcrId = bbj.getBenefitChangeReason().getHrBenefitChangeReasonId();

								if (StringUtils.isEmpty(bcrId) && bbj.getLifeEvent() != null && bbj.getLifeEvent().getLifeEventId() != null)
									bcrId = bbj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
								int bcrDate = bbj.getBenefitChangeReason() != null ? bbj.getBenefitChangeReason().getEffectiveDate() : bbj.getLifeEvent().getEventDate();
								benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(BProperty.get(StandardProperty.AdminEmail)), "Terminate Benefit Enrollment", "Future Terminate", newList, bcrId, bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bcrDate, true));
							} //benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Future Terminate", bbj.getBean()));
						else if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId())) {
							BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId());
							if (!bei.getBenefitJoinDeletes().contains(bbj)) {
								bei.getBenefitJoinDeletes().add(bbj);
								String bcrId = "";
								if (bbj.getBenefitChangeReason() != null && bbj.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
									bcrId = bbj.getBenefitChangeReason().getHrBenefitChangeReasonId();

								if (StringUtils.isEmpty(bcrId) && bbj.getLifeEvent() != null && bbj.getLifeEvent().getLifeEventId() != null)
									bcrId = bbj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
								bei.setBcrId(bcrId);
								int bcrDate = bbj.getBenefitChangeReason() != null ? bbj.getBenefitChangeReason().getEffectiveDate() : bbj.getLifeEvent().getEventDate();
								bei.getBcrDatesDeletes().add(bcrDate);
								List<String> persons = new ArrayList<>();
								persons.add(bbj.getCoveredPersonId());
								persons.add(bbj.getPayingPersonId());
								bei.getPersonsDeletes().add(persons);
							} else {
								bei.setType("Terminate");
								bei.setSubject("Terminate Benefit Enrollment");
							}
						} else {
							List<HrBenefitJoinH> newList = new ArrayList<HrBenefitJoinH>();
							newList.add(bbj);
							String bcrId = "";
							if (bbj.getBenefitChangeReason() != null && bbj.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
								bcrId = bbj.getBenefitChangeReason().getHrBenefitChangeReasonId();

							if (StringUtils.isEmpty(bcrId) && bbj.getLifeEvent() != null && bbj.getLifeEvent().getLifeEventId() != null)
								bcrId = bbj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
							int bcrDate = bbj.getBenefitChangeReason() != null ? bbj.getBenefitChangeReason().getEffectiveDate() : bbj.getLifeEvent().getEventDate();
							benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getHrBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(BProperty.get(StandardProperty.AdminEmail)), "Terminate Benefit Enrollment", "Terminate", newList, bcrId, bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bcrDate, true));
						} //benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Terminate", bbj.getBean()));
					else
						logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
		}
	}

	@SuppressWarnings("unchecked")
	private void sendEditToEmailQueue(Object entity, Object origEntity) {
		if (entity instanceof HrBenefitJoin && BProperty.getBoolean("DRCMessaging")) {
			BHRBenefitJoin bbj = new BHRBenefitJoin((HrBenefitJoin) entity);

			if (bbj.getBenefitApproved() && bbj.getBenefitConfig() != null) //dont deal with declines
				if (bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					if (!StringUtils.isEmpty(BProperty.get(BProperty.get(StandardProperty.AdminEmail)))) {
						if (bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() >= DateUtils.now()) //if the end date is set in the future, email as a future term
							if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId())) {
								BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
								if (!bei.getBenefitJoinList().contains(bbj.getBean())) {
									bei.getBenefitJoinList().add(bbj.getBean());
									bei.setBcrId(bbj.getBenefitChangeReasonId());
									bei.getBcrDates().add(bbj.getChangeReasonDate());
									@SuppressWarnings("unchecked")
									List<String> persons = new ArrayList<>();
									persons.add(bbj.getCoveredPersonId());
									persons.add(bbj.getPayingPersonId());
									bei.getPersons().add(persons);
								} else {
									bei.setType("Future Terminate");
									bei.setSubject("Modify Benefit Enrollment");
								}
							} else {
								List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
								newList.add(bbj.getBean());
								benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(BProperty.get(StandardProperty.AdminEmail)), "Modify Benefit Enrollment", "Future Terminate", newList, bbj.getBenefitChangeReasonId(), bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bbj.getChangeReasonDate()));
							} //benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Future Terminate", bbj.getBean()));
						else if (bbj.getPolicyEndDate() == 0) //make sure this isn't a delete that is being modified first
							if (bbj.getBenefitApproved() && ((HrBenefitJoin) origEntity).getBenefitApproved() == 'N')
								if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId())) {
									BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
									if (!bei.getBenefitJoinList().contains(bbj.getBean())) {
										bei.getBenefitJoinList().add(bbj.getBean());
										bei.setBcrId(bbj.getBenefitChangeReasonId());
										bei.getBcrDates().add(bbj.getChangeReasonDate());
										List<String> persons = new ArrayList<>();
										persons.add(bbj.getCoveredPersonId());
										persons.add(bbj.getPayingPersonId());
										bei.getPersons().add(persons);
									} else {
										bei.setType("New");
										bei.setSubject("New Benefit Enrollment");
									}
								} else {
									List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
									newList.add(bbj.getBean());
									benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(BProperty.get(StandardProperty.AdminEmail)), "New Benefit Enrollment", "New", newList, bbj.getBenefitChangeReasonId(), bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bbj.getChangeReasonDate()));
								} //benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", bbj.getBean()));
							else if (benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId())) {
								BenefitEmailInfo bei = (BenefitEmailInfo) benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
								if (!bei.getBenefitJoinList().contains(bbj.getBean())) {
									bei.getBenefitJoinList().add(bbj.getBean());
									bei.setBcrId(bbj.getBenefitChangeReasonId());
									bei.getBcrDates().add(bbj.getChangeReasonDate());
									List<String> persons = new ArrayList<>();
									persons.add(bbj.getCoveredPersonId());
									persons.add(bbj.getPayingPersonId());
									bei.getPersons().add(persons);
								} else {
									bei.setType("Modify");
									bei.setSubject("Modify Benefit Enrollment");
								}
							} else {
								List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
								newList.add(bbj.getBean());
								benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get(BProperty.get(StandardProperty.AdminEmail)), "Modify Benefit Enrollment", "Modify", newList, bbj.getBenefitChangeReasonId(), bbj.getCoveredPersonId(), bbj.getPayingPersonId(), bbj.getChangeReasonDate()));
							} //benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Modify", bbj.getBean()));
					} else
						logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
		}
	}
}
