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

import com.arahant.beans.IAuditedBean;
import com.arahant.beans.Person;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.timertasks.CheckRunawayServices;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.JessException;
import jess.Rete;
import org.w3c.dom.Document;

public class ArahantSession {

	private static final transient ArahantLogger logger = new ArahantLogger(ArahantSession.class);

	/*  The security system requires JESS in order to figure out the groups.  */
	public final static boolean disableJess = false;

	/*
	 * ATTENTION! Be careful about ThreadLocal storage. See clearSession below.
	 */
	private static final ThreadLocal<HibernateSessionUtil> hsuTS = new ThreadLocal<HibernateSessionUtil>();
	private static final InheritableThreadLocal<Boolean> aiRun = new InheritableThreadLocal<Boolean>() {

		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	private static final ThreadLocal<Rete> aiTS = new ThreadLocal<Rete>();
//	private static final ThreadLocal<Set<String>> mappedTS = new ThreadLocal<Set<String>>();
    private static final ThreadLocal<HashMap<IAuditedBean, Object[]>> historyTS = new ThreadLocal<HashMap<IAuditedBean, Object[]>>();
	private static final ThreadLocal<List<String>> returnMessages = new ThreadLocal<List<String>>();
	private static final ThreadLocal<Connection> conTS = new ThreadLocal<Connection>();
	private static final ThreadLocal<Integer> calcDate = new ThreadLocal<Integer>();
	private static final ThreadLocal<Integer> returnCode = new ThreadLocal<Integer>();
	private static final ThreadLocal<StackTraceElement []> stackAtOpen = new ThreadLocal<>();
	public static boolean multipleCompanySupport = false;
	public static CheckRunawayServices runawayServiceChecker = new CheckRunawayServices();
	private static Date upTime;
	private static String location;
	private static boolean fastKeys;
	private static final Rete baseCopy = new Rete();
	private static byte[] config;
	private static boolean initializing = !ArahantSession.disableJess;
	private static int maxComboReturn = 0;
	private static int maxSearchReturn = 0;
	private static String systemNameCache;  // Arahant or Unifi or Stack360

	static {
		openHSU();
		if (!disableJess)
			reloadRules();
	}

	/**
	 * This is the best method to close a Hibernate session (if it was opened by ArahantSession.openHSU())
	 * <br>
	 * ThreadLocal storage doesn't achieve the affect some people want -
	 * variables local to the thread. The reason for this is that Tomcat re-uses
	 * old threads. Therefore, you may start a thread and use the thread local
	 * storage you defined. The next time the thread is started it may re-use the
	 * old thread. If this happens all of your ThreadLocal variables will contain
	 * values from the previous run of the same thread. The following method is
	 * important because it resets the ThreadLocal values.
	 */
	public static void clearSession() {
		HibernateSessionUtil hsu = hsuTS.get();
		if (hsu != null  &&  hsu.isOpen())
			hsu.close();
		hsuTS.set(null);
		KissConnection.delete();
		//resetAI();
		setAI(null);
//		aiRun.set(false);
		calcDate.set(null);
		returnCode.set(null);
		returnMessages.set(null);
		historyTS.set(null);
		try {
			if (conTS.get() != null)
				conTS.get().close();
			conTS.set(null);
		} catch (Throwable ex) {
			Logger.getLogger(ArahantSession.class.getName()).log(Level.WARNING, "Failed to close a connection", ex);
		}
	}

	public static void addReturnMessage(String message) {
		if (returnMessages.get() == null)
			returnMessages.set(new LinkedList<String>());

		returnMessages.get().add(message);
	}

	public static void setReturnCode(int code) {
		returnCode.set(code);
	}

	public static int getReturnCode() {
		Integer x = returnCode.get();
		if (x == null)
			return 0;
		return x;
	}

	public static HibernateSessionUtil getHSU() {
//		return getHSU(true);
		return hsuTS.get();  //  don't change Jess setting
	}

	public static HibernateSessionUtil getHSU(boolean useAI) {
		//	waitUntilReady();
		final HibernateSessionUtil hsu = hsuTS.get();
		/*
		 * We don't want to auto-open because we need to know exactly when it's
		 * opened so that we can be sure to close it!
		 *
		 * if (hsu == null) { return setHSU(new HibernateSessionUtil(useAI)); }
		 *
		 */
		if (hsu != null)
			if (useAI)
				hsu.useAIIntegrate();
			else
				hsu.dontAIIntegrate();
		return hsu;
	}

	/**
	 * This object should not be disposed or closed.  It is done automatically by Hibernate.
	 */
	public static org.kissweb.database.Connection getKissConnection() {
		return KissConnection.get();
	}

	public static HibernateSessionUtil openHSU() {
		return openHSU(true);
	}

	public static HibernateSessionUtil openHSU(boolean useAI) {
		HibernateSessionUtil hsu = hsuTS.get();

		if (hsu != null && hsu.isOpen()) {
			logger.error("ArahantSession.openHSU() -- HSU is already opened!", new Throwable());
			StackTraceElement [] stack = stackAtOpen.get();
			String s = Arrays.toString(stack);
			logger.error("HSU was opened here: " + s);
			return hsu;
		}
		if (hsu == null || !hsu.isOpen()) {
			stackAtOpen.set(Thread.currentThread().getStackTrace());
			hsu = new HibernateSessionUtil(useAI);
			return setHSU(hsu);
		}
		return hsu;
	}

	/**
	 * This closes an HSU and removes it from the thread local storage (prevents it from being used if the thread is reused!).
	 *
	 * The method ArahantSession.clearSession() is better to use.
	 */
	public static void closeHSU() {
		final HibernateSessionUtil hsu = hsuTS.get();
		if (hsu != null)
			hsu.close();  // this will null out the session if already closed
		if (hsu != null)
			setHSU(null);
		stackAtOpen.set(null);
	}

	public static Connection getConnection() {
		//    Connection con=null;
		Connection con = conTS.get();
		if (con == null)
			try {

				InputStream hcfg = ArahantSession.class.getClassLoader().getResourceAsStream("hibernate.cfg.xml");

				Document doc = DOMUtils.createDocument(hcfg);

				String db = DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.url\"]");

				Class.forName(DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.driver_class\"]"));

				Properties props = new Properties();
				props.put("user", DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.username\"]"));
				props.put("password", DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.password\"]"));


				try {
					con = DriverManager.getConnection(db, props);
					con.setAutoCommit(true);
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					conTS.set(con);

				} catch (Error e) {
					logger.error("Failed to connect to database at " + db, e);
				}

				hcfg.close();
			} catch (Throwable e) {
				logger.error("Can't find hibernate config file", e);
			}
		try {

			//   }
			if (con != null  &&  con.isClosed()) {
				//throw new ArahantException("Allocated a closed connection!");
				conTS.set(null);
				return getConnection();
			}
		} catch (Exception ex) {
			Logger.getLogger(ArahantSession.class.getName()).log(Level.SEVERE, null, ex);
		}
		return con;
	}

	public static HashMap<IAuditedBean, Object[]> getHistory() {
		HashMap<IAuditedBean, Object[]> hist = historyTS.get();
		if (hist == null) {
			hist = new HashMap<IAuditedBean, Object[]>();
			historyTS.set(hist);
		}
		return hist;
	}

	public static Date getUpTime() {
		return upTime;
	}

	public static void storeSecurityRules() {
//System.out.println("Doing security Rules***********************");
		try {
			//		JessBean jb = new JessBean(new Rete());
			//		jb.loadTableFacts("security_group_hierarchy");
			//		jb.loadTableFacts("rights_association");
			//		jb.loadScript("SecurityGroupRules.jess");

			//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//		jb.getAIEngine().bsave(bos);
			//synchronized(securitySave)
			{
				///			securitySave = bos.toByteArray();
			}
		} catch (Exception e) {
			logger.error("Can't store security rules.", e);
		}

	}

	public static Rete getSecurityAI() {
		if (disableJess)
			return null;
		try {
			Rete r = new Rete();
			//synchronized(securitySave)
			{
				//r.bload(new ByteArrayInputStream(securitySave));
				JessBean jb = new JessBean(r);
				jb.loadTableFacts("security_group_hierarchy");
				jb.loadTableFacts("rights_association");
				jb.loadScript("SecurityGroupRules.jess");
			}
			return r;
		} catch (Exception e) {
			throw new ArahantException("Can't load security rules", e);
		}
	}

	public static void loadMinimumRules(Rete r) {
		if (ArahantSession.disableJess)
			return;
		try {
//			logger.info("load minimum");
			r.batch("jess/scriptlib.clp");

//			logger.info("Defining classes");
			try {
				r.defclass("OrgGroup", "com.arahant.beans.OrgGroup", null);
				r.eval("(set-nonvalue-class com.arahant.beans.OrgGroup)");
			} catch (Exception e) {
				//If i get an exception here, I already loaded all of these
				return;
			}

			String specialRelationships[] = new String[]{"CompanyBase", "ClientCompany", "ClientContact", "CompanyDetail",
					"Employee", "VendorCompany", "VendorContact", "Person", "OrgGroup"};
//			logger.info("class defines");
			/*
			 * These are done specially because they have inheritance
			 * relationships. This describes those relationships. They are then
			 * skipped in the auto part following
			 */
			r.defclass("Person", "com.arahant.beans.Person", null);
			r.eval("(set-nonvalue-class com.arahant.beans.Person)");
			r.defclass("CompanyBase", "com.arahant.beans.CompanyBase", "OrgGroup");
			r.eval("(set-nonvalue-class com.arahant.beans.CompanyBase)");
			r.defclass("ClientCompany", "com.arahant.beans.ClientCompany", "CompanyBase");
			r.eval("(set-nonvalue-class com.arahant.beans.ClientCompany)");
			r.defclass("ClientContact", "com.arahant.beans.ClientContact", "Person");
			r.eval("(set-nonvalue-class com.arahant.beans.ClientContact)");
			r.defclass("CompanyDetail", "com.arahant.beans.CompanyDetail", "CompanyBase");
			r.eval("(set-nonvalue-class com.arahant.beans.CompanyDetail)");
			r.defclass("Employee", "com.arahant.beans.Employee", "Person");
			r.eval("(set-nonvalue-class com.arahant.beans.Employee)");
			r.defclass("VendorCompany", "com.arahant.beans.VendorCompany", "CompanyBase");
			r.eval("(set-nonvalue-class com.arahant.beans.VendorCompany)");
			r.defclass("VendorContact", "com.arahant.beans.VendorContact", "Person");
			r.eval("(set-nonvalue-class com.arahant.beans.VendorContact)");

			HashSet<String> skps = new HashSet<String>();
			skps.addAll(Arrays.asList(specialRelationships));

			for (Class c : HibernateUtil.getClasses("com.arahant.beans")) {
				if (skps.contains(c.getSimpleName()))
					continue;
				r.defclass(c.getSimpleName(), c.getName(), null);
				r.eval("(set-nonvalue-class " + c.getName() + ")");
			}
//			logger.info("classes defined");

		} catch (Exception e) {
			JessUtils.reportError(e, logger);
		}
	}

	public static void reloadRules() {
		//I need to register all my classes with the engine
		synchronized (baseCopy) {
			try {

				//	logger.info("loading rules");
				Rete r = baseCopy;

				//	logger.info("loading minimum rules");
				loadMinimumRules(r);

				//	logger.info("loading business rules");
				r.batch("BusinessRules.jess");

				storeSecurityRules();

				Connection con = getHSU().getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("select prop_value from property where prop_name='Use Quickbooks'");

				if (rs.next()) {

					String val = rs.getString(1);

					if (val.trim().equalsIgnoreCase("TRUE"))
						// logger.info("Using Quickbooks integration");
						r.batch("Quickbooks.jess");
				}
				//else
				//    logger.info("Not using Quickbooks integration.  Set system property 'Use Quickbooks' to TRUE to activate.  Set to FALSE to turn off this message.");

				rs.close();
				st.close();

				//	logger.info("loading custom rules");
				r.batch("Custom.jess");


				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				//	logger.info("saving rules to cache");
				baseCopy.bsave(bos);

				config = bos.toByteArray();

				//	logger.info("rules loaded");
				initializing = false;

			} catch (Exception e) {
				JessUtils.reportError(e, logger);
			}
		}
	}

	public static HibernateSessionUtil setHSU(final HibernateSessionUtil hsu) {
		if (hsu == null)
			setAI(null);
		hsuTS.set(hsu);
		return hsu;
	}

	public static void setCalcDate(int calcDate) {
		ArahantSession.calcDate.set(calcDate);
		AICmd("(assert (ebjCalcDate " + calcDate + "))");
	}

	public static void setCalcDateNoAI(int calcDate) {
		ArahantSession.calcDate.set(calcDate);
	}

	public static int getCalcDate() {
		if (ArahantSession.calcDate.get() == null)
			return 0;
		return ArahantSession.calcDate.get().intValue();
	}

	@SuppressWarnings("SleepWhileInLoop")
	public static void waitUntilReady() {
		//	logger.info("In wait until ready");
		while (initializing)
			try {
				//		logger.info("Not ready, sleeping");
				Thread.sleep(1000);
			} catch (Exception e) {
				continue;
			}
	}

	public static Rete getAITemplate() {
		if (disableJess)
			return null;

		if (initializing)
			return baseCopy;

		final Rete ai;
		synchronized (baseCopy) {
			ai = aiTS.get();
			if (ai == null) {
				Rete r = new Rete();

				try {
					//				 turn on debugging
					//		r.watchAll();

					r.bload(new ByteArrayInputStream(config));

				} catch (Exception e) {
					logger.error(e);
				}

				return setAI(r);
			}
		}
		return ai;
	}

	public static Rete getAI() {
		if (disableJess)
			return null;

		if (initializing)
			return baseCopy;

		final Rete ai = aiTS.get();

		synchronized (baseCopy) {
			if (ai == null) {
				Rete r = new Rete();

				try {
					//				 turn on debugging
					//	r.watchAll();

					r.bload(new ByteArrayInputStream(config));
					// load standard scripts
				} catch (Exception e) {
					logger.error(e);
				}
				return setAI(r);
			}
		}
		return ai;
	}

	public static Rete setAI(Rete rete) {
		if (disableJess)
			return null;
		if (rete == null && aiTS.get() != null)  //clearing the old one out
			try {
				aiTS.get().halt();
				//	aiTS.get().clear();  Never call clear - it calls a GC!
			} catch (Exception e) {
				logger.error(e);
			}
		aiTS.set(rete);
		return rete;
	}

	/*
	 *
	 * public static Set<String> getMappedClasses() { final Set<String> mapped =
	 * mappedTS.get(); if (mapped == null) { mappedTS.set(new
	 * HashSet<String>()); return getMappedClasses(); } return mapped; }
	 *
	 *
	 *
	 */
	public static void runAI() throws ArahantException {
		if (disableJess)
			return;
		try {
			boolean running;
			try {
				running = aiRun.get();
			} catch (Exception e) {
				running = false;
			}
			if (!running) {
				aiRun.set(true);
				ArahantSession.getAI().run();
				aiRun.set(false);
			}
		} catch (JessException e) {
			if (e.getCause() instanceof ArahantException)
				throw (ArahantException) e.getCause();
			logger.logger.error(e);
		}
	}

	/**
	 * puts command in engine and runs engine
	 *
	 */
	public static void AIEval(String cmd) {
		if (disableJess)
			return;
		try {
			ArahantSession.getAI().eval(cmd);
			runAI();
		} catch (ArahantException e) {
			throw e;
		} catch (Exception e) {
			logger.logger.error(e);
		}
	}

	/**
	 * Puts command in engine, but does not run command
	 *
	 * @param cmd
	 */
	public static void AICmd(String cmd) {
		if (disableJess)
			return;
		try {
			ArahantSession.getAI().eval(cmd);
		} catch (Exception e) {
			logger.logger.error(e);
		}
	}

	public static void resetAI() {
		if (disableJess)
			return;
		try {
			Rete r = new Rete();
			aiTS.set(r);
			r.bload(new ByteArrayInputStream(config));
			// load standard scripts
			//	r.batch("jess/scriptlib.clp");
			//	r.batch("WilliamsonCountyRules.jess");
		} catch (Exception e) {
			logger.logger.error(e);
		}
	}

	public static String getReturnMessage() {
		List<String> l = returnMessages.get();
		if (l == null)
			return "";
		if (l.size() < 1)
			return "";

		StringBuilder ret = new StringBuilder();

		for (String msg : l)
			ret.append(msg).append("\n");

		return ret.toString().trim();
	}

	public static Person getCurrentPerson() {
		return getHSU().getCurrentPerson();
	}

	public static int getMaxComboReturn() {
		if (maxComboReturn == 0)
			maxComboReturn = BProperty.getInt(StandardProperty.COMBO_MAX);
		return maxComboReturn;
	}

	public static int getMaxSearchReturn() {
		if (maxSearchReturn == 0)
			maxSearchReturn = BProperty.getInt(StandardProperty.SEARCH_MAX);
		return maxSearchReturn;
	}

	public static boolean getFastKeys() {
		return fastKeys;
	}

	public static void setFastKeys(boolean fast) {
		fastKeys = fast;
	}

	static void setLocation(String realPath) {
		location = realPath;
	}

	public static String getLocation() {
		return location;
	}

	static void setUpTime() {
		upTime = new Date();
	}

	/**
	 * This gets the name of the system.  It usually returns ArahantSession.systemName() or "Stack360".
	 * If the value is changed a system reboot is required.  Logout/login will not work.
	 */
	public static String systemName() {
		if (systemNameCache != null)
			return systemNameCache;
		return systemNameCache = BProperty.get(StandardProperty.System_Name, "Stack360");  // For now, this must be "Arahant" because that is what the username is in the database
	}
}
