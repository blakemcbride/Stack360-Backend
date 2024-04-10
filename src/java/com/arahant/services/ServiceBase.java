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
package com.arahant.services;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.business.BProphetLogin;
import com.arahant.business.interfaces.RightNames;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantInfo;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.main.UserCache;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import com.arahant.utils.dynamicclassloader.ArahantClassLoader;
import com.arahant.utils.dynamicwebservices.DataObject;
import com.arahant.utils.dynamicwebservices.DataObjectMap;
import com.itextpdf.text.DocumentException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.exception.DataException;

public class ServiceBase implements ArahantConstants, RightNames {

	private static final ArahantLogger logger = new ArahantLogger(ServiceBase.class);
	public HibernateSessionUtil hsu;
	protected static final String wsStatusProperty = "_wsStatus";
	protected static final String wsMessageProperty = "_wsMessage";
	private static Class cls = null;
	private static Method meth = null;
    private static final Hashtable<String,String> screenMap = new Hashtable<String,String>();
    private static Calendar dateLastUpdate = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    private TransmitInputBase tibCache;

    @Resource
	private WebServiceContext wsContext;
//    or use the following:
//    private WebServiceContext wsContext;
//    @Resource
//    public void setContext(WebServiceContext context) {
//        this.wsContext = context;
//    }
	
	
	protected void checkNoLogin(final TransmitInputBase tib) throws ArahantLoginException {
		if (tib.frontEndType == null)
			tib.frontEndType = "Flash";
	}

	/**
	 * To be used in cases when it's a normal login.
	 *
	 * Returns true if logged in.
	 * Returns false if more than one company and company not specified
	 * Throws an exception otherwise.
	 *
	 * @param tib
	 * @return
	 * @throws ArahantLoginException
	 */
	public boolean checkLogin(final TransmitInputBase tib) throws ArahantLoginException {
		return checkLogin(tib, UserCache.LoginType.NORMAL);
	}

	/**
	 * This is mainly used externally when it's not a normal login.
	 *
	 * Returns true if logged in.
	 * Returns false if more than one company and company not specified
	 * Throws an exception otherwise.
	 *
	 * @param tib
	 * @param loginType
	 * @return
	 * @throws ArahantLoginException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkLogin(final TransmitInputBase tib, final UserCache.LoginType loginType) throws ArahantLoginException {

        final String timedOutMsg = "Login timed out; please re-login.";
	    tibCache = tib;
	    //System.out.println("checkLogin 1 " + tib._service + " " + tib._method);
		try {
			if (tib.frontEndType == null)
				tib.frontEndType = "Flash";
			hsu = ArahantSession.getHSU();
			if (hsu == null || !hsu.isOpen())
				hsu = ArahantSession.openHSU(true);
			hsu.clear();  //  sometimes tomcat gives us the same instance.  We need to be sure hibernate is not holding on to another state
			//  DO NOT REMOVE THE FOLLOWING CODE !!
			if (HibernateUtil.x4) {
				if (cls == null)
					cls = new ArahantClassLoader().loadArahantClassFromFile(true, FileSystemUtils.getSourcePath() + "data3");
				Class<Object>[] ca = new Class[]{};
				if (meth == null)
					meth = cls.getMethod("syncHibernateEnvironment", ca);
				if (meth == null) {
					System.out.println("System sync error");
					System.exit(0);
				}
				meth.invoke(cls);
			}

			String caller = getCaller();
			logger.debug("Handling " + caller);

			ArahantSession.runawayServiceChecker.addService(Thread.currentThread(), caller);

			/*
			 * Normally the hsu would not be opened unless the web service needed an hsu
			 * before the checkLogin() call.  In that case just use the one that was already opened.
			 */

			//This is here to keep Hibernate from calling the onFlushDirty function in the ArahantBeanInterceptor class
			//It was creating person history records prematurely in the employer home page (modify task)
			//Now, onFlushDirty is ONLY called when a transaction is being committed.
			//It will NOT update the database in the middle of a session anymore.
			{ // Debugging issue where the session is supposed to be open but it's really not
				Session s = hsu.getSession();
				if (!s.isOpen()) {
					logger.error("ServiceBase.checkLogin() - session is closed!", new Throwable());
					hsu = ArahantSession.openHSU(true);
					s = hsu.getSession();
				}
				s.setFlushMode(FlushMode.COMMIT);
			}
			//hsu.getSession().setFlushMode(FlushMode.COMMIT);
			//notify engine of what is being called
			//StackTraceElement ste = new Throwable().getStackTrace()[1];

			//logger.debug("Notifying AI engine of call "+getCaller());

			//ArahantSession.AICmd("(assert (executing "+(ste.getClassName()+"."+ste.getMethodName()).replace('.', '-')+"))");


			if (loginType == UserCache.LoginType.APPLICANT && Utils.isDemo() && tib.user.equals("demo")) {
				// This is a demo system with a demo applicant logging in

				final Connection db = KissConnection.get();
				Record rec = db.fetchOne("select aa.person_id, pl.user_login, pl.user_password " +
						"from applicant_application aa " +
						"inner join previous_employment pe " +
						"  on aa.person_id = pe.person_id " +
						"inner join prophet_login pl " +
						"  on aa.person_id = pl.person_id " +
						"where aa.phase = 0 " +
						"      and pl.can_login = 'Y' " +
						"      and pl.user_type = 'A'");
				if (rec == null)
					throw new ArahantLoginException("No viable demo applicant");
				final String password = rec.getString("user_password");
				if (!password.equals(tib.password))
					throw new ArahantLoginException("Invalid Username and/or Password. (0)");
				tib.user = rec.getString("user_login");
			}


			if (tib.uuid != null && !tib.uuid.isEmpty()) {
				// regular service login.  Use uuid only.
				UserCache.UserData ud = UserCache.findUuid(tib.uuid);
				if (ud == null)
					throw new ArahantException(timedOutMsg);
				if (ud.loginType != loginType)
					throw new ArahantLoginException("Invalid Username and/or Password. (1)");
				CompanyDetail cd = hsu.get(CompanyDetail.class, ud.companyId);
				if (cd == null) {
					UserCache.removeUuid(tib.uuid);
					throw new ArahantLoginException("Invalid Username and/or Password. (2)");
				}
				hsu.setCurrentCompany(cd);

				Person p = hsu.get(Person.class, ud.personId);
				if (p == null) {
					UserCache.removeUuid(tib.uuid);
					throw new ArahantLoginException("Invalid Username and/or Password. (3)");
				}
				hsu.setCurrentPerson(p);
				Employee e = hsu.get(Employee.class, ud.personId);
				if (e != null) {
					BEmployee bemp = new BEmployee(e);
					if (loginType != UserCache.LoginType.APPLICANT) {
						if (bemp.getCurrentStatus().getActive() != 'Y') {
							UserCache.removeUuid(tib.uuid);
							throw new ArahantLoginException("Invalid Username and/or Password. (4)");
						}
						if (bemp.isAutoOvertimeLogout())
							bemp.checkOvertimeLogout();
					} else {
						// is applicant login
						if (bemp.getCurrentStatus().getActive() == 'Y') {
							UserCache.removeUuid(tib.uuid);
							throw new ArahantLoginException("User is active employee.");
						}
					}
				}
				try {
					recordScreenBeingUsed(tib, hsu, caller);
				} catch (Exception ignored) {
				}
				return true;
			}


			// Else, initial login process.  Use user, password, contextCompanyId


			//  The checkPassword below uses the "currentCompany" so it must be set first if possible
			if (!isEmpty(tib.getContextCompanyId()))
				hsu.setCurrentCompany(hsu.get(CompanyDetail.class, tib.getContextCompanyId()));
			else {
				ProphetLogin login = hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, tib.getUser()).first();
				if (login == null)
					throw new ArahantLoginException("Invalid Username and/or Password. (5)");
				Person p = login.getPerson();
				hsu.setCurrentPerson(p);
				BPerson bp = new BPerson(p);
				List<CompanyDetail> compList = bp.getAllowedCompanies();
				if (compList.size() == 1) {
					String companyId = compList.get(0).getCompanyId();
					CompanyDetail cd = hsu.get(CompanyDetail.class, companyId);
					hsu.setCurrentCompany(cd);
				} else
					return false;  //  more than one company
			}
			// password takes priority over uuid
            if (tib.password != null && !tib.password.isEmpty()  &&  tib.user != null  &&  !tib.user.isEmpty()) {
                if (BProphetLogin.checkPassword(tib.getUser(), tib.getPassword(), loginType)) {
                    //see if this person should be auto-logged out due to overtime
                    if (BPerson.getCurrent().isEmployee()) {
                        BEmployee bemp = BPerson.getCurrent().getBEmployee();
                        if (bemp.isActive() != 0 && loginType != UserCache.LoginType.APPLICANT)
                        	throw new ArahantLoginException("Invalid Username and/or Password. (6)");
						if (bemp.isActive() == 0 && loginType == UserCache.LoginType.APPLICANT)  // don't allow active employees to login the applicant system
							throw new ArahantLoginException("Invalid Login. (6)");
                        if (bemp.isAutoOvertimeLogout())
                            bemp.checkOvertimeLogout();
                    }

                    try {
                        recordScreenBeingUsed(tib, hsu, caller);
                    } catch (Exception ignored) {
                    }
                    return true;
                }
            }
		} catch (ArahantLoginException e) {
			throw e;
		} catch (ArahantException s) {
		    String msg1 = s.getMessage();
		    if (msg1 == null  ||  !msg1.equals(timedOutMsg))
                logger.error(s);
			Throwable t = s.getCause();
			String msg = t == null ? s.getMessage() : t.getMessage();
			throw new ArahantLoginException(msg);
		} catch (final Exception e) {
			logger.error(e);
		}

//	logger.info("Done with checkLogin");
		throw new ArahantLoginException("Invalid Username and/or Password. (7)");
	}

	private void recordScreenBeingUsed(TransmitInputBase tib, HibernateSessionUtil hsu, String caller) throws SQLException {
        String packageName;
	    if ("com.arahant.servlets.REST.doPost".equals(caller))
	        return;  //  use recordScreenBeingUsedHTML() instead
        else {
	        packageName = caller.substring(0, caller.lastIndexOf('.'));
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
            if ("com.arahant.services.main".equals(packageName))
                return;
        }
        String person_id = hsu.getCurrentPerson().getPersonId();
        String ip = getRemoteAddr();
        if (updateScreenMap(person_id, ip, packageName))
			return;
        String packageName2 = packageName.replaceAll("\\.", "/");
	    packageName2 = packageName2.replace("/services/", "/app/screen/");
        String screen_id = getScreenId(hsu, packageName2);
        if (screen_id == null)
			return;
        updateScreenTable(hsu, person_id, screen_id);
    }

    private static void updateScreenTable(HibernateSessionUtil hsu, String person_id, String screen_id) throws SQLException {
        Date now = new Date(System.currentTimeMillis());
        Connection c = KissConnection.get();
        /*
           I had a problem with the following line hanging.  What caused it was a prior SOAP service did not
           execute a finishService()
         */
        c.execute("update screen_history set time_out=? where person_id=? and time_out is null", now, person_id);

        Record rec = c.newRecord("screen_history");
        rec.set("person_id", person_id);
        rec.set("screen_id", screen_id);
        rec.setDateTime("time_in", now);
        rec.addRecord();
        rec.close();
    }

    private static synchronized boolean updateScreenMap(String person_id, String ip, String packageName) {
        String uniquePerson = person_id + "\1" + ip;
        Calendar currentDate = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        long hours = TimeUnit.MILLISECONDS.toHours(Math.abs(dateLastUpdate.getTimeInMillis() - currentDate.getTimeInMillis()));
        if (hours > 3)
            screenMap.clear();
        if (screenMap.containsKey(uniquePerson)) {
            String priorScreenId = screenMap.get(uniquePerson);
            if (priorScreenId.equals(packageName))
                return true;  // user already in the screen
        }
        screenMap.put(uniquePerson, packageName);
        dateLastUpdate = currentDate;
        return false;  // new screen for user
    }

    private static String getScreenId(HibernateSessionUtil hsu, String packageName2) {
        List<Screen> scrn = hsu.createCriteria(Screen.class).like(Screen.FILENAME, packageName2 + "/%").list();
        if (scrn.isEmpty())
            return null;
        return scrn.get(0).getScreenId();
    }

    public static void recordScreenBeingUsedHTML(HibernateSessionUtil hsu, String ip, String packageName) throws SQLException {

        String person_id = hsu.getCurrentPerson().getPersonId();

        if (updateScreenMap(person_id, ip, packageName))
            return;

        String packageName2 = packageName.replaceAll("\\.", "/");
        packageName2 = packageName2.replace("/services/", "/");
        String screen_id = getScreenId(hsu, packageName2);
        if (screen_id == null)
            return;

        updateScreenTable(hsu, person_id, screen_id);
    }

    public static void recordScreenBeingUsedDynamic(HibernateSessionUtil hsu, String ip, String packageName) throws SQLException {
        String person_id = hsu.getCurrentPerson().getPersonId();

        if (updateScreenMap(person_id, ip, packageName))
            return;

        String packageName2 = packageName.replaceAll("\\.", "/");
        packageName2 = packageName2.replace("/dynamic/services/", "/app/dynamicScreen/");
        String screen_id = getScreenId(hsu, packageName2);
        if (screen_id == null)
            return;

        updateScreenTable(hsu, person_id, screen_id);
    }

	public String getCaller() {
		try {
			throw new Exception("");
		} catch (Exception e) {
			return e.getStackTrace()[2].getClassName()
					+ "."
					+ e.getStackTrace()[2].getMethodName();
		}
	}

	protected void handleError(final HibernateSessionUtil hsu, final Throwable e, final TransmitReturnBase ret, final ArahantLogger logger) {
		handleError(hsu, e, ret, null, logger, getCaller());
	}

	protected void handleError(final HibernateSessionUtil hsu, final Throwable e, final DataObjectMap outDOM, final ArahantLogger logger) {
		handleError(hsu, e, null, outDOM, logger, getCaller());
	}

	private static void setWsStatus(TransmitReturnBase ret, DataObjectMap outDOM, int code) {
		if (ret != null)
			ret.setWsStatus(code);
		else
			outDOM.put(wsStatusProperty, code);
	}

	private static void setWsMessage(TransmitReturnBase ret, DataObjectMap outDOM, String msg) {
		if (ret != null)
			ret.setWsMessage(msg);
		else
			outDOM.put(wsMessageProperty, msg);
	}

	private static String getWsMessage(TransmitReturnBase ret, DataObjectMap outDOM) {
		if (ret != null)
			return ret.getWsMessage();
		else {
			DataObject d = outDOM.get(wsMessageProperty);
			if (d == null)
				return "";
			else
				return (String) d.getValue();
		}
	}

	/**
	 * This method handles the reporting of errors
	 *
	 * @param hsu the session
	 * @param e the exception
	 * @param ret the transmit return
	 * @param logger the logger to log to
	 */
	private void handleError(final HibernateSessionUtil hsu, final Throwable e, final TransmitReturnBase ret, final DataObjectMap outDOM, final ArahantLogger logger, String caller) {
		try {
			if (!(e instanceof ArahantWarning)  &&  !(e instanceof ArahantInfo) && !(e.getCause() instanceof ArahantWarning)) {
				logger.error("Error in " + caller);
				logger.error(e.getMessage());
				SQLException se = null;
				if (e instanceof SQLException)
					se = (SQLException) e;
				else if (e instanceof org.hibernate.exception.ConstraintViolationException)
					se = ((org.hibernate.exception.ConstraintViolationException) e).getSQLException();
				if (se != null)
					while (se.getNextException() != null) {
						se = se.getNextException();
						logger.error("Nested exception :" + se.getMessage());
//						logger.error(se);
					}
				if (e instanceof DataException) {
					DataException de = (DataException) e;
					logger.error(de.getSQL());
				}
				logger.error(e);
			}	

			if (e instanceof ArahantLoginException)
				setWsStatus(ret, outDOM, 2);
			else
				setWsStatus(ret, outDOM, 3);

			try {
				ArahantSession.getHSU().rollbackTransaction();
			} catch (final Exception exc) {
				logger.error(exc);
			}

			if (e instanceof DocumentException)
				setWsMessage(ret, outDOM, "Failed Report Generation: Contact Administrator");

			if (e.getCause() instanceof ArahantWarning)
				setWsMessage(ret, outDOM, e.getCause().getMessage());
			else
				setWsMessage(ret, outDOM, e.getMessage());

			if (e instanceof DocumentException)
				setWsMessage(ret, outDOM, "Failed Report Generation: Contact Administrator");

			if (e instanceof ArahantDeleteException)
				setWsMessage(ret, outDOM, "Can not delete, item is in use.");

			if (isEmpty(getWsMessage(ret, outDOM)))
				setWsMessage(ret, outDOM, "The server has encountered a problem with your request.\n" + e.getClass().getName() + " problem encountered on server.");

			if ((e.getMessage().contains("JDBC")) && (e.getMessage().contains("batch")) && (e.getMessage().contains("update")))
				setWsMessage(ret, outDOM, "Please enter all required data.");

			if (e instanceof ArahantWarning)
				return;

			if (e instanceof ArahantInfo) {
				setWsStatus(ret, outDOM, 1);
				return;
			}
			if (isEmpty(getWsMessage(ret, outDOM)))
				setWsMessage(ret, outDOM, "The server has encountered a problem with your request.");
		} catch (final Throwable t) {
			//logger.error(t);
		} finally {
			try {
				ArahantSession.clearSession();
				ArahantSession.setAI(null);  //  since this is not done in clearSession()
			} catch (Exception ex) {
			}
		}
		ArahantSession.runawayServiceChecker.removeService(Thread.currentThread());
		this.hsu = null;
	}

	public void changePassword(String oldPw, String newPw) {
	    if (tibCache != null  &&  tibCache.uuid != null && !tibCache.uuid.isEmpty()) {
            UserCache.UserData ud = UserCache.findUuid(tibCache.getUuid());
            if (ud != null  &&  ud.password.equals(oldPw))
                ud.password = newPw;
        }
    }

	double roundToTenths(final double x) {
		return (double) Math.round(x * 10) / 10;
	}

	protected boolean isEmpty(final String str) {
		return (str == null || str.equals(""));
	}

	protected double roundToHundredths(final double x) {
		return (double) Math.round(x * 100) / 100;
	}

	/**
	 * @throws ArahantException
	 */
	protected void finishService(final TransmitReturnBase ret) throws ArahantException {
		finishService(ret, null);
	}

	/**
	 *
	 * @param outDOM
	 * @throws ArahantException
     */
	protected void finishService(DataObjectMap outDOM) throws ArahantException {
		finishService(null, outDOM);
	}

	/**
	 *
	 * @param ret
	 * @param outDOM
	 * @throws ArahantException
     */
	private void finishService(final TransmitReturnBase ret, DataObjectMap outDOM) throws ArahantException {
		ArahantSession.runAI();
		ArahantSession.getHSU().commitTransaction();
		//	hsu.commitTransaction();
		//	hsu.close();
		if (!isEmpty(ArahantSession.getReturnMessage())) {
			if (ArahantSession.getReturnCode() != 0)
				setWsStatus(ret, outDOM, ArahantSession.getReturnCode());
			else
				setWsStatus(ret, outDOM, 1); //tell front end to show message
			setWsMessage(ret, outDOM, ArahantSession.getReturnMessage());
		} else if (outDOM != null) {
			outDOM.put(wsStatusProperty, 0);
			outDOM.put(wsMessageProperty, "");
		}
		/* The following two lines are important.  Java re-uses threads.  This means that when a new web service is called it may
		 * be using an old thread AND the old thread's ThreadLocal storage.  This call makes sure that the ThreadLocal storage
		 * is cleared.
		 */
		ArahantSession.clearSession();  //  this closes the HSU session & clears the thread local storage
		ArahantSession.setAI(null);  //  since this is not done in clearSession()

		ArahantSession.runawayServiceChecker.removeService(Thread.currentThread());
		//System.out.println("Completing " + tibCache._service + " " + tibCache._method);
		logger.debug("Completed " + getCaller());
		hsu = null;
	}

	protected void setSubjectPerson(String employeeId) throws ArahantLoginException {
		ArahantSession.AICmd("(assert (subject-employee-id \"" + employeeId + "\"))");
	}

	public String getRemoteHost() {
		if (wsContext == null)
			return "localhost";
		MessageContext msgx = wsContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest) msgx.get(MessageContext.SERVLET_REQUEST);
		return req.getRemoteHost();
	}

	public String getRemoteAddr() {
		if (wsContext == null)
			return "localhost";
		MessageContext msgx = wsContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest) msgx.get(MessageContext.SERVLET_REQUEST);
		return req.getRemoteAddr();
	}

	public String getRequestURL() {
		if (wsContext == null)
			return "localhost";
		MessageContext mc = wsContext.getMessageContext();
		HttpServletRequest hsr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return new String(hsr.getRequestURL());
	}

	public boolean isLocalHost() {
		final String url = getRequestURL().toUpperCase();
		return url.contains("LOCALHOST");
	}

	protected abstract static class AsynchRequest extends Thread {

		protected HibernateSessionUtil hsu;
		private String currentPersonId;

		public AsynchRequest(String currentPersonId) {
			this.currentPersonId = currentPersonId;
			setDaemon(true);
		}

		@Override
		public void run() {
			hsu = ArahantSession.openHSU();
			hsu.beginTransaction();
			Person p = hsu.get(Person.class, currentPersonId);
			hsu.setCurrentPerson(p);

			try {
				doRequest();
				hsu.commitTransaction();
			} catch (Exception e) {
				if (!(e instanceof ArahantWarning))
					logger.error(e);
				BMessage.send(hsu.getCurrentPerson(), hsu.getCurrentPerson(), "Task Failed", e.getMessage());
				hsu.rollbackTransaction();
			}

			ArahantSession.clearSession();    //  this closes the HSU session

			hsu = null;
		}

		protected abstract void doRequest() throws Exception;
	}
}
