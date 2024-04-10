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
 *
 */
package com.arahant.utils;

import com.arahant.beans.*;
import com.arahant.business.BProperty;
import com.arahant.business.BVendorCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.interfaces.adp.ADPInterfaceDown;
import com.arahant.lisp.ABCL;
import com.arahant.remote.Server;
import com.arahant.sql.Database;
import com.arahant.timertasks.AutoLogTime;
import com.arahant.timertasks.CheckOvertimeLogouts;
import com.arahant.timertasks.CheckRunawayServices;
import com.arahant.timertasks.CobraGuard;
import com.arahant.timertasks.DeleteExpiredObjects;
import com.arahant.timertasks.EvaluationEmailNotifications;
import com.arahant.timertasks.HRInvoicing;
import com.arahant.timertasks.SendEdiTask;
import com.arahant.timertasks.WmCoNotifyLateBilling;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kissweb.Cron;
import org.kissweb.database.Record;
import org.kissweb.restServer.GroovyClass;
import org.kissweb.restServer.MainServlet;
import org.w3c.dom.Document;

/**
 *  This is where the whole system starts.
 *  contextInitialized() is the first method that gets called when the system is booted.
 *
 *
 */
public class StartupListener implements ServletContextListener {

	private static final ArahantLogger logger = new ArahantLogger(StartupListener.class);
	private static final int EVERY_DAY = 1000 * 60 * 60 * 24;
	private static final int EVERY_WEEK = EVERY_DAY * 7;
	private static final int EVERY_HOUR = 1000 * 60 * 60;

	//  This should probably be moved to a better class
	public static int DB_TYPE;
	public static final int DB_POSTGRESQL = 1;
	public static final int DB_MSSQL = 2;
	public static String databaseURL;
	//static TimerTask exportToCompuPayTask=new ExportToCompuPay();
	private static final TimerTask emailTask = new NotifyTimesheetEntryNeeded();
	private static final TimerTask expiredObjectDeleteTask = new DeleteExpiredObjects();
	private static final TimerTask wmCoNotifyBiling = new WmCoNotifyLateBilling();
	private static final TimerTask hrInvoicing = new HRInvoicing();
	private static final TimerTask overtimeLogout = new CheckOvertimeLogouts();
	private static final TimerTask cobraGuard = new CobraGuard();
	private static final TimerTask adpInterface = new ADPInterfaceDown();
	private static final TimerTask autoTimeLogger = new AutoLogTime();
//	private static CheckRunawayServices checkRunaway = new CheckRunawayServices();
	private static final java.util.Timer timesheetReminder = new Timer(true);
	private static final java.util.Timer benefitDeleteReminder = new Timer(true);
	private static final java.util.Timer wmcoNotifyBilling = new Timer(true);
	private static final java.util.Timer hrInvoicingTimer = new Timer(true);
	private static final java.util.Timer overtimeLogoutTimer = new Timer(true);
	private static final java.util.Timer checkRunawayServicesTimer = new Timer(true);
	private static final java.util.Timer cobraGuardTimer = new Timer(true);
	private static final java.util.Timer adpInterfaceTimer = new Timer(true);
	private static final java.util.Timer autoTimeLoggerTimer = new Timer(true);
	private static final java.util.Timer evaluationEmailNotificationsTimer = new Timer(true);
	private static final TimerTask evaluationEmailNotificationsTask = new EvaluationEmailNotifications();
	private Server GWTServer;
	private Cron cronJobs;
	private boolean externalFileRootDefined;

	//static java.util.Timer exportToCompuPay=new Timer(true);

	@Override
	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void contextInitialized(final ServletContextEvent contextEvent) {
		/*
		 * try { //org.apache.catalina.jsp_classpath =
		 * /Applications/NetBeans/apache-tomcat-6.0.14/work/Catalina/localhost/Arahant
		 * Enumeration
		 * e=contextEvent.getServletContext().getInitParameterNames(); while
		 * (e.hasMoreElements()) logger.info(e.nextElement().toString()+" =
		 * "+contextEvent.getServletContext().getInitParameter((String)
		 * e.nextElement())); } catch(Throwable t) { t.printStackTrace(); }
		 */
		try {

			//		AspectXmlLoader.deployXML(this.getClass().getClassLoader().getResource("jboss-aop.xml"));

			String appname;
			boolean multipleCompanySupport = false;
			//String appname=ArahantSession.systemName();
			try {
				appname = contextEvent.getServletContext().getContextPath();
			} catch (Throwable t) {
				appname = ArahantSession.systemName();
			}
			if (appname.startsWith("/") || appname.startsWith("\\"))
				appname = appname.substring(1);
            if (appname.isEmpty())
                appname = "/";

            logger.info("********** Web application at " + appname + " is starting up ");
			logger.info("********** Internal path is " + contextEvent.getServletContext().getRealPath("/"));

			String path = FileSystemUtils.setWorkingDirectory(contextEvent.getServletContext().getRealPath("/"));
			MainServlet.setApplicationPath(path);  // coordinate with the Kiss library

			// For the Kiss library.  Makes sure reports get put in the correct directory.
			MainServlet.setRootPath(contextEvent.getServletContext().getRealPath("/"));

			try {
				logger.info("Looking for hibernate config file.");
				InputStream hcfg = this.getClass().getClassLoader().getResourceAsStream("hibernate.cfg.xml");

				Document doc = DOMUtils.createDocument(hcfg);
				hcfg.close();
				databaseURL = DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.url\"]");


				if (databaseURL == null || "".equals(databaseURL.trim())) {
					logger.info("Database name not found in config file");
					return;
				}
				logger.info("Attempting to open database at " + databaseURL);

				Class.forName(DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.driver_class\"]"));

				Properties props = new Properties();
				props.put("user", DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.username\"]"));
				props.put("password", DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.password\"]"));


				Connection con = DriverManager.getConnection(databaseURL, props);
				con.setAutoCommit(false);

				String dbname = con.getMetaData().getDatabaseProductName();
				if ("PostgreSQL".equals(dbname))
					DB_TYPE = DB_POSTGRESQL;
				else if ("Microsoft SQL Server".equals(dbname))
					DB_TYPE = DB_MSSQL;
				else {
					logger.info("Database type could not be determined.");
					return;
				}

				Statement st = con.createStatement();

				ResultSet rs = st.executeQuery("select count(*) from company_detail");

				rs.next();

				int companyCount = rs.getInt(1);

				rs.close();
				st.close();

				if (companyCount > 1) {
					multipleCompanySupport = true;
					logger.info("Using Multiple Company Support");
				}

				logger.info("Successfully connected to database.");

				{
					//  This property must be set or the system will not run properly.
					//  This property tells the system where external storage is located.
					org.kissweb.database.Connection db = new org.kissweb.database.Connection(con);
					Record rec = db.fetchOne("select * from property where prop_name = ?", StandardProperty.EXTERNAL_FILE_ROOT);
					if (rec == null || rec.getString("prop_value") == null || rec.getString("prop_value").isEmpty()) {
						logger.error("*** System property " + StandardProperty.EXTERNAL_FILE_ROOT + " not set. ***");
						externalFileRootDefined = false;
					} else {
						String epath = rec.getString("prop_value");
						if (!(new File(epath).exists())) {
							logger.error("*** External file path " + epath + " does not exist. ***");
							externalFileRootDefined = false;
						} else
							externalFileRootDefined = true;
					}
				}

				int num = runSQLUpdateScripts(con);  // old repository changes
				num += runUpdateScripts(con);	// new repository changes

				con.close();
				if (num > 0) {
					/*
					      The system needs to be restarted because I've found that Hibernate
					      sometimes gets confused when a schema change takes place after the
					      database is opened.  Restarting assures there are no changes.
					 */
					logger.info("Schema has changed.  Please restart tomcat.");
					System.exit(1);
				}

			} catch (Throwable e) {
				logger.error(e);
				System.exit(-1);
			}

			//  The following must occur before the ArahantSession is initialized
			try {
				new Database(null);  // needed because lisp accesses this class so it must be linked in
				ABCL.init();  //  Initialize Lisp.  FileSystemUtils.setWorkingDirectory() must be called first and
				//  File system (ArahantSession class) should be initialized prior
			} catch (Throwable e) {
				logger.error("Error initializing Lisp.", e);
			}
			ArahantSession.multipleCompanySupport = multipleCompanySupport;  //  used to execute static initializer in ArahantSession 
			HibernateSessionUtil hsu = ArahantSession.getHSU();
            
            Timesheet.setupDefaultTimeType();

			cronJobs = new Cron(FileSystemUtils.getSourcePath() + "timedTasks/crontab",
					StartupListener::getConnection,
					StartupListener::success,
					StartupListener::failure);

			//Here is where you add the hooks in that are run off of ArahantBeanInterceptor
			//They implement IInterceptorHook interface, and are for specific tasks on add,edit,deletes to the database
			//EDIT -- STAY AWAY FROM INTERCEPTORS!  They are very erratic
//			if(BProperty.getBoolean("DRCMessaging"))
//			{
//				ArahantBeanInterceptor.addHook(new com.arahant.interceptor.DRCMessage());
//				logger.info("Added DRC Messaging Hook into listener");
//			}

			//ArahantBeanInterceptor.addHook(new ArahantHistory());

			//This is to keep Arahant Session static initializer from going off too soon
			{
				//	ArahantSession.init();

				ArahantSession.waitUntilReady();
				//Thread.sleep(10000);  //Testing
				hsu.setCurrentPersonToArahant();

				logger.info("checking properties.");

				//make sure all standard properties are loaded
				for (String name : Property.STANDARD_PROPERTIES) {
					Property p = hsu.get(Property.class, name);
					if (p == null) {
						BProperty bp = new BProperty();
						bp.setName(name);
						bp.setValue("");
						if (name.equals(StandardProperty.COMBO_MAX))
							bp.setValue("20");
						if (name.equals(StandardProperty.SEARCH_MAX))
							bp.setValue("100");
						bp.setDescription(name);
						bp.insert();
					} else {
						if (name.equals(StandardProperty.COMBO_MAX))
							if (p.getPropValue() == null || p.getPropValue().trim().equals("")) {
								p.setPropValue("20");
								hsu.saveOrUpdate(p);
							}
						if (name.equals(StandardProperty.SEARCH_MAX))
							if (p.getPropValue() == null || p.getPropValue().trim().equals("")) {
								p.setPropValue("100");
								hsu.saveOrUpdate(p);
							}
					}
				}

				logger.info("Scheduling benefit maintenance tasks.");
				Calendar tonight = DateUtils.getNow();
				//	logger.info("Check point .1");
				tonight.add(Calendar.DAY_OF_YEAR, 1);
				tonight.set(Calendar.HOUR_OF_DAY, 1);
				tonight.set(Calendar.MINUTE, 0);
				tonight.set(Calendar.SECOND, 0);
				//logger.info("Check point .2");

				benefitDeleteReminder.scheduleAtFixedRate(expiredObjectDeleteTask, tonight.getTime(), EVERY_DAY);

				if (BProperty.getBoolean("RunHRInvoicing")) {
					logger.info("Scheduling HR invoicing");
					tonight.set(Calendar.HOUR_OF_DAY, 3);
					hrInvoicingTimer.scheduleAtFixedRate(hrInvoicing, tonight.getTime(), EVERY_DAY);
				}

				if (BProperty.getBoolean("RunCobraGuardInterface")) {
					tonight.set(Calendar.HOUR_OF_DAY, 3);
					cobraGuardTimer.scheduleAtFixedRate(cobraGuard, tonight.getTime(), EVERY_DAY);
					logger.info("Scheduling Cobra Guard for " + DateUtils.getDateAndTimeFormatted(tonight.getTime()));
				}

				{
					tonight.set(Calendar.HOUR_OF_DAY, 1);
					adpInterfaceTimer.scheduleAtFixedRate(adpInterface, tonight.getTime(), EVERY_DAY);
					logger.info("Scheduling ADP Import for every weekday starting at " + DateUtils.getDateAndTimeFormatted(tonight.getTime()) + " (if enabled)");
				}

				//	logger.info("Check point 1");

				if (BProperty.getBoolean("SendTimesheetReminders")) {
					logger.info("Scheduling timesheet reminders");
					final Calendar nextDay = DateUtils.getNow();

					final int dayOfWeek = nextDay.get(Calendar.DAY_OF_WEEK);

					if (dayOfWeek < Calendar.FRIDAY)
						nextDay.add(Calendar.DAY_OF_YEAR, Calendar.FRIDAY - dayOfWeek);


					if (dayOfWeek > Calendar.FRIDAY)
						nextDay.add(Calendar.DAY_OF_YEAR, 8);

					nextDay.set(Calendar.HOUR_OF_DAY, 19);
					nextDay.set(Calendar.MINUTE, 0);
					nextDay.set(Calendar.SECOND, 0);


					timesheetReminder.scheduleAtFixedRate(emailTask, nextDay.getTime(), EVERY_WEEK);
				}
				//	logger.info("Check point 2");
				if (BProperty.getBoolean("DoWmCoNotifyLateBilling")) {
					logger.info("Scheduling Wm Co notify late billing.");
					Calendar cal = Calendar.getInstance();

					if (cal.get(Calendar.HOUR_OF_DAY) > 17)
						cal.add(Calendar.DAY_OF_YEAR, 1);

					cal.set(Calendar.HOUR_OF_DAY, 3);
					cal.set(Calendar.MINUTE, 0);
					wmcoNotifyBilling.scheduleAtFixedRate(wmCoNotifyBiling, cal.getTime(), EVERY_DAY);
				}


				if (BProperty.getBoolean("ForceOvertimeLogout")) {
					logger.info("Scheduling Overtime Logouts.");
					Calendar cal = Calendar.getInstance();

					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 0);
					overtimeLogoutTimer.scheduleAtFixedRate(overtimeLogout, cal.getTime(), EVERY_DAY);
				}


				if (BProperty.getBoolean("EvaluationEmailNotifications")) {
					logger.info("Scheduling Evaluation Email Notifications");
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, 1);
					cal.set(Calendar.HOUR_OF_DAY, 1);
					cal.set(Calendar.MINUTE, 0);
					evaluationEmailNotificationsTimer.scheduleAtFixedRate(evaluationEmailNotificationsTask, cal.getTime(), EVERY_DAY);
				}

				logger.info("Scheduling Auto Time Logger.");
				{
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 22);
					cal.set(Calendar.MINUTE, 0);
					autoTimeLoggerTimer.scheduleAtFixedRate(autoTimeLogger, cal.getTime(), EVERY_DAY);
				}

				logger.info("Scheduling EDI uploads");

				List<VendorCompany> vendorsToSchedule = hsu.createCriteriaNoCompanyFilter(VendorCompany.class).eq(VendorCompany.EDIACTIVATED, 'Y').list();
				for (VendorCompany vc : vendorsToSchedule) {
					BVendorCompany bvc = new BVendorCompany(vc);
					scheduleEdiTimerTask(bvc, bvc.getTimeToSend(), bvc.getEdiActivated().charAt(0));
				}

//				if (BProperty.getBoolean("PurgeImages"))
//					setupAutoDataPurge();

//					Calendar cal = Calendar.getInstance();
//
//					if (cal.get(Calendar.HOUR_OF_DAY) > 22)
//						cal.add(Calendar.DAY_OF_YEAR, 1);
//
//					cal.set(Calendar.HOUR_OF_DAY, 22);
//					cal.set(Calendar.MINUTE, 0);
//
//					sendEDI.scheduleAtFixedRate(sendWmCoEdiTask, cal.getTime(), EVERY_DAY);


				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         BCN           /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar bcnCal = Calendar.getInstance();
//
//					int bcnDay = Calendar.WEDNESDAY - bcnCal.get(Calendar.DAY_OF_WEEK); //send next wednesday
//
//					bcnCal.add(Calendar.DAY_OF_YEAR, bcnDay);
//
//					if (bcnDay < 0 || (bcnDay == 0 && bcnCal.get(Calendar.HOUR_OF_DAY) > 20))
//					{
//						bcnCal.add(Calendar.DAY_OF_YEAR, 7);
//					}
//
//					bcnCal.set(Calendar.HOUR_OF_DAY, 20);  //Send at 8:00 pm
//					bcnCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling BCN EDI for " + DateUtils.getDateAndTimeFormatted(bcnCal.getTime()));
//					sendBcnEdi.scheduleAtFixedRate(sendBcnEdiTask,  bcnCal.getTime(), EVERY_WEEK);

				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         HUMANA        /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar humanaCal = Calendar.getInstance();
//
//					int humanaDay = Calendar.MONDAY - humanaCal.get(Calendar.DAY_OF_WEEK); //send next monday
//
//					humanaCal.add(Calendar.DAY_OF_YEAR, humanaDay);
//
//					if (humanaDay < 0 || (humanaDay == 0 && humanaCal.get(Calendar.HOUR_OF_DAY) > 18))
//					{
//						humanaCal.add(Calendar.DAY_OF_YEAR, 7);
//					}
//
//					humanaCal.set(Calendar.HOUR_OF_DAY, 18);  //Send at 6:00 pm
//					humanaCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling Humana EDI for " + DateUtils.getDateAndTimeFormatted(humanaCal.getTime()));
//					sendHumanaEdi.scheduleAtFixedRate(sendHumanaEdiTask, humanaCal.getTime(), EVERY_WEEK);


				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         EBC           /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar ebcCal = Calendar.getInstance();
//					ebcCal.add(Calendar.DAY_OF_YEAR, 1);
//
//					ebcCal.set(Calendar.HOUR_OF_DAY, 20);  //Send at 8:00 pm
//					ebcCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling EBC EDI for " + DateUtils.getDateAndTimeFormatted(ebcCal.getTime()));
//					sendEbcEdi.scheduleAtFixedRate(sendEbcEdiTask, ebcCal.getTime(), EVERY_DAY);


				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         DELTA           /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar deltaCal = Calendar.getInstance();
//
//					if (deltaCal.get(Calendar.HOUR_OF_DAY) >= 20)
//					{
//						deltaCal.add(Calendar.DAY_OF_YEAR, 1);
//					}
//
//					deltaCal.set(Calendar.HOUR_OF_DAY, 20);  //Send at 6:00 pm
//					deltaCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling Delta Dental EDI for " + DateUtils.getDateAndTimeFormatted(deltaCal.getTime()));
//					sendDeltaDentalEdi.scheduleAtFixedRate(sendDeltaDentalEdiTask, deltaCal.getTime(), EVERY_DAY);


				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         EHIM           /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar ehimCal = Calendar.getInstance();
//					ebcCal.add(Calendar.DAY_OF_YEAR, 1);
//
//					ehimCal.set(Calendar.HOUR_OF_DAY, 6);  //Send at 8:00 pm
//					ehimCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling EHIM EDI for " + DateUtils.getDateAndTimeFormatted(ebcCal.getTime()));
//					sendEhimEdi.scheduleAtFixedRate(sendEhimEdiTask, ehimCal.getTime(), EVERY_DAY);


				///////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////         VSP           /////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////

//					Calendar vspCal = Calendar.getInstance();
//
//					if (vspCal.get(Calendar.HOUR_OF_DAY) >= 20)
//					{
//						vspCal.add(Calendar.DAY_OF_YEAR, 1);
//					}
//
//					vspCal.set(Calendar.HOUR_OF_DAY, 20);  //Send at 8:00 pm
//					vspCal.set(Calendar.MINUTE, 0);
//					logger.info("Scheduling VSP EDI for " + DateUtils.getDateAndTimeFormatted(vspCal.getTime()));
//					sendVspEdi.scheduleAtFixedRate(sendVspEdiTask, vspCal.getTime(), EVERY_DAY);

				logger.info("Scheduling Runaway Task Checker");

				checkRunawayServicesTimer.scheduleAtFixedRate(ArahantSession.runawayServiceChecker, new Date(), CheckRunawayServices.MINUTES * 60 * 1000);

				logger.info("Initializing email");
				hsu.commitTransaction();
				initializeEmail();

				ArahantSession.setUpTime();
				ArahantSession.setLocation(contextEvent.getServletContext().getRealPath("."));

				if (BProperty.getBoolean("Use Quickbooks"))
					logger.info("Using Quickbooks integration");


				hsu.commitTransaction();
				ArahantSession.clearSession();

				// the following code failed when starting up multiple instances of Arahant on the same server
//				GWTServer = new Server(2000);
//				GWTServer.listenSeperateThread();  //  start the socket listner for socket communications with GWT

				logger.info("Instance started.");
			}
		} catch (Throwable t) {
			logger.error(t);
		}

	}

	@Override
	public void contextDestroyed(final ServletContextEvent contextEvent) {
		String appname = ArahantSession.systemName();

		if (appname.startsWith("/") || appname.startsWith("\\"))
			appname = appname.substring(1);
		if (appname.isEmpty())
			appname = "/";

		logger.info("********** " + appname + " SHUTTING DOWN ***********************");

		cronJobs.cancel();
		if (GWTServer != null)
			GWTServer.close();
		emailTask.cancel();
		expiredObjectDeleteTask.cancel();
		//sendWmCoEdiTask.cancel();
		timesheetReminder.cancel();
		benefitDeleteReminder.cancel();
		HibernateUtil.close();
	}

	@SuppressWarnings("unchecked")
	private int runSQLUpdateScripts(Connection con) throws Exception {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select prop_value from property where prop_name='Database Version'");
		if (rs.next()) {
			String val = rs.getString(1);
			rs.close();
			if (val == null  ||  val.isEmpty()  ||  val.indexOf('-') > -1) {
				st.close();
				return 0;  // all the scripts from the old repository have already been run (or new DB)
			}
		} else {
			rs.close();
			st.close();
			return 0;  // no scripts run.  Must be new (and up-to-date) database
		}

		//check to see if need to upgrade db
		//what is my latest file number
		File dbups = new File(FileSystemUtils.getWorkingDirectory(), "WEB-INF/classes/com/arahant/db");
		File[] fl = dbups.listFiles(pathname -> {
			if (DB_TYPE == DB_POSTGRESQL)
				return pathname.getName().endsWith(".sql") && !pathname.getName().startsWith("M") &&
						!pathname.getName().startsWith("P")  &&  !pathname.getName().startsWith("D");
			else
				return pathname.getName().endsWith(".sql") && pathname.getName().startsWith("MS")  &&
						!pathname.getName().startsWith("MSU");
		});

		String prefix;
		if (DB_TYPE == DB_POSTGRESQL)
			prefix = "";
		else
			prefix = "MS";
		int start = prefix.length();

		int current = 0;
		rs = st.executeQuery("select prop_value from property where prop_name='Database Version'");
		if (rs.next())
			current = rs.getInt(1);
		else {
			int maxVersion = 0;

			for (File aFl : fl)
				try {
					String nm = aFl.getName().substring(start, aFl.getName().indexOf('.'));
					maxVersion = Math.max(Integer.parseInt(nm), maxVersion);
				} catch (Exception e) {
					//bad file, skip it
				}
			st.executeUpdate("insert into property (prop_name,prop_value,prop_desc) values ('Database Version','" + maxVersion + "','Database Update Number')");
			con.commit();
		}
		rs.close();

		ArrayList<Integer> lint = new ArrayList<>();

		if (current > 0)
			for (File file : fl)
				try {
					String nm = file.getName().substring(start, file.getName().indexOf('.'));
					int nmi = Integer.parseInt(nm);
					if (nmi > current)
						lint.add(nmi);
				} catch (Exception e) {
					//bad file, skip it
				}

		//sort upgrade list
		lint.sort((Comparator) (arg0, arg1) -> {
			Integer i0 = (Integer) arg0;
			Integer i1 = (Integer) arg1;

			return i0 - i1;
		});

		int num = 0;
		for (Integer i : lint) {
			runSqlFile(con, st, i, prefix);
			if (i == 15101)
				db_update_15101(con);
			st.executeUpdate("update property set prop_value='" + i + "' where prop_name='Database Version'");
			con.commit();
			num++;
		}
		updateEmployeeStatuses(current);
		st.close();
		return num;
	}

	/**
	 * Run SQL and GROOVY update scripts.
	 *
	 * @param con
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked"})
	private int runUpdateScripts(Connection con) throws Exception {
		//check to see if we need to upgrade db
		//what is my latest file number
		org.kissweb.database.Connection db = new org.kissweb.database.Connection(con);  // Kiss DB
		final File dbups = new File(FileSystemUtils.getWorkingDirectory(), "WEB-INF/classes/com/arahant/db");
		final File[] fl = dbups.listFiles(new java.io.FileFilter() {

			@Override
			public boolean accept(File pathname) {
				final String name = pathname.getName();
				if (DB_TYPE == DB_POSTGRESQL)
					return  name.startsWith("P_") && (name.endsWith(".groovy") || name.endsWith(".sql"));
				else
					return name.startsWith("M_") && (name.endsWith(".groovy") || name.endsWith(".sql"));
			}
		});
		if (fl == null  ||  fl.length == 0)
			return 0;
		final Statement st = con.createStatement();

		final String prefix;
		if (DB_TYPE == DB_POSTGRESQL)
			prefix = "P_";
		else
			prefix = "M_";
		final int start = prefix.length();
		
		int maxVersion = 0;

		for (File file : fl)
			try {
				int n = getFileNumber(file);
				maxVersion = Math.max(n, maxVersion);
			} catch (Exception e) {
				//bad file, skip it
			}

		int current;
		ResultSet rs = st.executeQuery("select prop_value from property where prop_name='Database Version'");
		if (rs.next()) {
			final String val = rs.getString(1);
			if (val == null  ||  val.isEmpty())
				current = 0;  //  signal no scripts needed
			else if (val.startsWith("2-"))
				current = Integer.parseInt(val.substring(2));
			else
				current = 1;  //  to force all scripts to run
		} else
			current = 0;  //  signal no scripts needed
		rs.close();

		final ArrayList<File> fl2 = new ArrayList<>();

		if (current > 0)
			for (File f : fl)
				try {
					int nmi = getFileNumber(f);
					if (nmi > current)
						fl2.add(f);
				} catch (Exception e) {
					//bad file, skip it
				}

		//sort upgrade list
		fl2.sort(Comparator.comparingInt(StartupListener::getFileNumber));

		// run the SQL and Groovy update scripts in order
		int num = 0;
		for (File f : fl2) {
			final String name = f.getName();
			final int idx = name.indexOf('.');
			if (idx > 0 && idx+1 < name.length()) {
				final String suffix = name.substring(idx+1).toLowerCase();
				int i = 0;
				if ("sql".equals(suffix)) {
					i = Integer.parseInt(name.substring(start, idx));
					runSqlFile(con, st, i, prefix);
					if (i == 13)
						db_update_2_13(con);
				} else if ("groovy".equals(suffix)) {
					if (!externalFileRootDefined)
						System.exit(-1);  //  must be defined
					i = Integer.parseInt(name.substring(2, idx));
					logger.info("Running DB Update " + f.getName());
					GroovyClass.invoke(false, f.getAbsolutePath(), "run", null, db);
				}
				if (i != 0) {
					st.executeUpdate("update property set prop_value='2-" + i + "' where prop_name='Database Version'");
					con.commit();
				}
				num++;
			}
		}

		// Update the system version number
		rs = st.executeQuery("select prop_value from property where prop_name='Database Version'");
		if (rs.next()) {
			final String val = rs.getString(1);
			if (val == null  ||  val.isEmpty()) {
				st.executeUpdate("insert into property (prop_name,prop_value,prop_desc) values ('Database Version','2-" + maxVersion + "','Database Update Number')");
				con.commit();
			}
		} else {
			st.executeUpdate("insert into property (prop_name,prop_value,prop_desc) values ('Database Version','2-" + maxVersion + "','Database Update Number')");
			con.commit();
		}
		rs.close();
		st.close();

		return num;
	}

	/**
	 * Get the file number.  The file number is the number located as part of the file's name.
	 * The file number should be the same as the subversion commit number of the file.
	 * Expected file names are:
	 *     P_NNNN.sql
	 *     M_NNNN.sql
	 *     P_NNNN.groovy
	 *     M_NNNN.groovy
	 */
	private static int getFileNumber(File f) {
		final String name = f.getName();
		final int idx = name.indexOf('.');
		if (idx > 0 && idx < name.length()+3)
			return Integer.parseInt(name.substring(2, idx));
		return 0;
	}
	
	/**
	 * Encrypt all the social security numbers.
	 * 
	 * @param con Connection
	 */
	private void db_update_2_13(Connection con) {
		Database db = new Database(con);
		com.arahant.sql.Statement read_stmt = db.createStatement();
		com.arahant.sql.Statement update_stmt = db.createStatement();
		
		logger.info("Encrypting social security numbers in the person table.");
		update_stmt.prepare("update person set ssn=:ssn where person_id=:personId");
		read_stmt.execute("select * from person");
		while (read_stmt.next()) {
			String ssn = read_stmt.getString("ssn");
			if (ssn != null  &&  ssn.length() > 8) {
				update_stmt.setString("personId", read_stmt.getString("person_id"));
				String ssn2 = null;
				try {
					ssn2 = Crypto.encryptTripleDES(Person.encKey(), ssn);
				} catch (Exception e) {
					logger.error("Error encrypting ssn on the person table during schema update", e);
				}
				update_stmt.setString("ssn", ssn2);
				update_stmt.execute();
			}
		}
		
		logger.info("Encrypting social security numbers in the person_h table.");
		update_stmt.prepare("update person_h set ssn=:ssn where history_id=:histId");
		read_stmt.execute("select * from person_h");
		while (read_stmt.next()) {
			String ssn = read_stmt.getString("ssn");
			if (ssn != null  &&  ssn.length() > 8) {
				update_stmt.setString("histId", read_stmt.getString("history_id"));
				String ssn2 = null;
				try {
					ssn2 = Crypto.encryptTripleDES(Person.encKey(), ssn);
				} catch (Exception e) {
					logger.error("Error encrypting ssn on the person_h table during schema update", e);
				}
				update_stmt.setString("ssn", ssn2);
				update_stmt.execute();
			}
		}
		
		logger.info("Encrypting social security numbers in the hr_employee_beneficiary table.");
		update_stmt.prepare("update hr_employee_beneficiary set ssn=:ssn where beneficiary_id=:personId");
		read_stmt.execute("select * from hr_employee_beneficiary");
		while (read_stmt.next()) {
			String ssn = read_stmt.getString("ssn");
			if (ssn != null  &&  ssn.length() > 8) {
				update_stmt.setString("personId", read_stmt.getString("beneficiary_id"));
				String ssn2 = null;
				try {
					ssn2 = Crypto.encryptTripleDES(Person.encKey(), ssn);
				} catch (Exception e) {
					logger.error("Error encrypting ssn on the hr_employee_beneficiary table during schema update", e);
				}
				update_stmt.setString("ssn", ssn2);
				update_stmt.execute();
			}
		}
		
		logger.info("Encrypting social security numbers in the hr_employee_beneficiary_h table.");
		update_stmt.prepare("update hr_employee_beneficiary_h set ssn=:ssn where history_id=:histId");
		read_stmt.execute("select * from hr_employee_beneficiary_h");
		while (read_stmt.next()) {
			String ssn = read_stmt.getString("ssn");
			if (ssn != null  &&  ssn.length() > 8) {
				update_stmt.setString("histId", read_stmt.getString("history_id"));
				String ssn2 = null;
				try {
					ssn2 = Crypto.encryptTripleDES(Person.encKey(), ssn);
				} catch (Exception e) {
					logger.error("Error encrypting ssn on the hr_employee_beneficiary_h table during schema update", e);
				}
				update_stmt.setString("ssn", ssn2);
				update_stmt.execute();
			}
		}

		read_stmt.close();
		update_stmt.close();
	}

	private void db_update_15101(Connection con) {
		Database db = new Database(con);
		com.arahant.sql.Statement read_stmt = db.createStatement();
		com.arahant.sql.Statement update_stmt = db.createStatement();

		boolean isWmCo = false;
		if (read_stmt.executeRead("select * from property where prop_name='WmCoEDI'")) {
			String val = read_stmt.getString("prop_value");
			if (val != null && val.equalsIgnoreCase("true"))
				isWmCo = true;
		}

		update_stmt.prepare("update hr_benefit_join set coverage_change_date=:ccd, original_coverage_date=:ocd, policy_start_date=:psd,"
				+ "coverage_start_date=:csd where benefit_join_id=:bjid");
		read_stmt.execute("select * from hr_benefit_join");
		while (read_stmt.next()) {
			int policy_start_date = read_stmt.getInt("policy_start_date");
			int coverage_start_date = read_stmt.getInt("coverage_start_date");
			String benefit_config_id = read_stmt.getString("benefit_config_id");

			update_stmt.setString("bjid", read_stmt.getString("benefit_join_id"));
			update_stmt.setInt("ccd", policy_start_date);
			update_stmt.setInt("ocd", coverage_start_date);

			if (isWmCo && benefit_config_id != null && benefit_config_id.length() > 10)
				if (benefit_config_id.equals("00001-0000000022") || //  Co-Pay
						benefit_config_id.equals("00001-0000000055") || //  Co-Pay
						benefit_config_id.equals("00001-0000000079") || //  Co-Pay
						benefit_config_id.equals("00001-0000000023") || //  Co-Pay
						benefit_config_id.equals("00001-0000000027") || //  Co-Pay
						benefit_config_id.equals("00001-0000000024") || //  Co-Pay
						benefit_config_id.equals("00001-0000000028") || //  Co-Pay
						benefit_config_id.equals("00001-0000000054") || //  Co-Pay
						benefit_config_id.equals("00001-0000000056") || //  Co-Pay
						benefit_config_id.equals("00001-0000000006") || //  Deductable
						benefit_config_id.equals("00001-0000000019") || //  Deductable
						benefit_config_id.equals("00001-0000000020") || //  Deductable
						benefit_config_id.equals("00001-0000000029") || //  Deductable
						benefit_config_id.equals("00001-0000000030") || //  Deductable
						benefit_config_id.equals("00001-0000000057") || //  Deductable
						benefit_config_id.equals("00001-0000000058") || //  Deductable
						benefit_config_id.equals("00001-0000000059") || //  Deductable
						benefit_config_id.equals("00001-0000000080") || //  Deductable
						benefit_config_id.equals("00001-0000000077") || //  Medicare Advantage
						benefit_config_id.equals("00001-0000000078") || //  Medicare Advantage
						benefit_config_id.equals("00001-0000000042") || //  Rx Only - Retiree
						benefit_config_id.equals("00001-0000000043")) { //  Rx Only - Retiree
					if (policy_start_date < 20110701)
						policy_start_date = 20110701;
					if (coverage_start_date < policy_start_date)
						coverage_start_date = policy_start_date;
				} else if (benefit_config_id.equals("00001-0000000005") || //  Dental Dental
						benefit_config_id.equals("00001-0000000021") || //  Dental Dental
						benefit_config_id.equals("00001-0000000060") || //  Dental Dental
						benefit_config_id.equals("00001-0000000061")) {   //  Dental Dental
					if (policy_start_date < 20080701)
						policy_start_date = 20080701;
					if (coverage_start_date < policy_start_date)
						coverage_start_date = policy_start_date;
				} else if (benefit_config_id.equals("00001-0000000032") || // Vision
						benefit_config_id.equals("00001-0000000033") || // Vision
						benefit_config_id.equals("00001-0000000034")) {   // Vision
					if (policy_start_date < 20110101)
						policy_start_date = 20110101;
					if (coverage_start_date < policy_start_date)
						coverage_start_date = policy_start_date;
				} else if (coverage_start_date < policy_start_date)
					coverage_start_date = policy_start_date;

			update_stmt.setInt("psd", policy_start_date);
			update_stmt.setInt("csd", coverage_start_date);
			update_stmt.execute();
		}
		read_stmt.close();
		update_stmt.close();
	}

	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	private void initializeEmail() {
		final HibernateSessionUtil hsu = new HibernateSessionUtil();

		try {
			hsu.beginTransaction();

			new Mail(BProperty.get(StandardProperty.EMAIL_HOST));

			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
		}
	}

	protected static void executeFile(final BufferedReader br) throws IOException {
		StringBuilder filecontents = new StringBuilder();

		final HibernateSessionUtil hsu = new HibernateSessionUtil();

		while (br.ready()) {
			final String line = br.readLine();
			if (line.trim().startsWith("--"))
				continue;
			filecontents.append(line);
		}

		final StringTokenizer stk = new StringTokenizer(filecontents.toString(), ";");

		while (stk.hasMoreTokens()) {
			final String sql = stk.nextToken();

			if (sql.startsWith("SET client_encoding") || sql.startsWith("CREATE PROCEDURAL LANGUAGE plpgsql"))
				continue;

			if (sql.trim().equals(""))
				continue;
//			 logger.debug("Executing "+sql);
			hsu.executeSQL(sql);
		}

		hsu.commitTransaction();
		hsu.close();
	}

	private void dropMSConstraints() {
		logger.info("Dropping MS Constraints");
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.commitTransaction();
			hsu.beginTransaction();

			Connection con = hsu.getConnection();

			String stmt = "declare @tbl nvarchar(128), @constraint nvarchar(128)\r\n"
					+ "DECLARE @sql nvarchar(255)\r\n"
					+ "declare cur cursor fast_forward for\r\n"
					+ "select distinct con.name, tbl.name from sysobjects con join sysobjects tbl on con.parent_obj=tbl.id where con.xtype in ('C', 'D', 'F','UQ')\r\n"
					+ "open cur\r\n"
					+ "fetch next from cur into   @constraint, @tbl\r\n"
					+ "while @@fetch_status <> -1\r\n"
					+ "begin\r\n"
					+ "select @sql = 'ALTER TABLE dbo.' + @tbl + ' DROP CONSTRAINT ' + @constraint\r\n"
					+ "exec sp_executesql @sql\r\n"
					+ "fetch next from cur into  @constraint, @tbl\r\n"
					+ "end\r\n"
					+ "close cur\r\n"
					+ "deallocate cur\r\n";

			PreparedStatement ps = con.prepareStatement(stmt);
			ps.execute();
			//     logger.info("Dropping MS Constraints 2");
			stmt = "declare @tbl nvarchar(128), @constraint nvarchar(128)\r\n"
					+ "DECLARE @sql nvarchar(255)\r\n"
					+ "declare cur cursor fast_forward for\r\n"
					+ "select distinct con.name, tbl.name from sysobjects con join sysobjects tbl on con.parent_obj=tbl.id where con.xtype in ('PK')\r\n"
					+ "open cur\r\n"
					+ "fetch next from cur into   @constraint, @tbl\r\n"
					+ "while @@fetch_status <> -1\r\n"
					+ "begin\r\n"
					+ "select @sql = 'ALTER TABLE dbo.' + @tbl + ' DROP CONSTRAINT ' + @constraint\r\n"
					+ "exec sp_executesql @sql\r\n"
					+ "fetch next from cur into  @constraint, @tbl\r\n"
					+ "end\r\n"
					+ "close cur\r\n"
					+ "deallocate cur\r\n";

			ps = con.prepareStatement(stmt);
			ps.execute();

			ps.close();

			hsu.commitTransaction();
			hsu.beginTransaction();

			//   logger.info("Dropping MS Constraints 3");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void runSqlFile(Connection con, Statement st, int fileCount, String prefix) {

		try {
			String filename = prefix + fileCount + ".sql";

			StringBuilder filecontents = new StringBuilder();

			File fyle = new File(new File(FileSystemUtils.getWorkingDirectory(), "WEB-INF/classes/com/arahant/db"), filename);

			if (!fyle.exists())
				return;

			logger.info("Running DB Update " + filename);

			if (DB_TYPE == DB_MSSQL)
				dropMSConstraints();

			BufferedReader br = new BufferedReader(new FileReader(fyle));

			String line;

			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("--"))
					continue;
				if (line.trim().equals("GO"))//for microsoft files
					line = ";";
				filecontents.append(line).append("\r\n");
			}

			StringTokenizer stk = new StringTokenizer(filecontents.toString(), ";");

			while (stk.hasMoreTokens()) {
				String sql = stk.nextToken();

				if (sql.startsWith("SET client_encoding") || sql.startsWith("CREATE PROCEDURAL LANGUAGE plpgsql"))
					continue;

				if (sql.startsWith("SET standard_conforming_strings") || sql.startsWith("ALTER PROCEDURAL LANGUAGE plpgsql"))
					continue;

				if (sql.trim().equals(""))
					continue;

				// System.out.println(sql);
				try {
					st.execute(sql);
					con.commit();
				} catch (Exception e) {
					//        System.out.println(e.getMessage());
					//          e.printStackTrace();
					logger.debug(e);
				}
			}
			br.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/*
	 * public static void main(String args[]) { try {
	 * ArahantSession.getHSU().beginTransaction(); Connection
	 * con=ArahantSession.getHSU().getConnection();
	 *
	 * String stmt="declare @tbl nvarchar(128), @constraint nvarchar(128)\r\n" +
	 * "DECLARE @sql nvarchar(255)\r\n" + "declare cur cursor fast_forward
	 * for\r\n" + "select distinct con.name, tbl.name from sysobjects con join
	 * sysobjects tbl on con.parent_obj=tbl.id where con.xtype in ('C', 'D',
	 * 'F','UQ')\r\n" + "open cur\r\n" + "fetch next from cur into @constraint,
	 * @tbl\r\n" + "while @@fetch_status <> -1\r\n" + "begin\r\n" + "select @sql
	 * = 'ALTER TABLE dbo.' + @tbl + ' DROP CONSTRAINT ' + @constraint\r\n" +
	 * "exec sp_executesql @sql\r\n" + "fetch next from cur into @constraint,
	 * @tbl\r\n" + "end\r\n" + "close cur\r\n" + "deallocate cur\r\n";
	 *
	 *
	 * declare cur cursor fast_forward for select distinct con.name, tbl.name
	 * from sysobjects con join sysobjects tbl on con.parent_obj=tbl.id where
	 * con.xtype in ('PK') open cur fetch next from cur into @constraint, @tbl
	 * while @@fetch_status <> -1 begin select @sql = 'ALTER TABLE dbo.' + @tbl
	 * + ' DROP CONSTRAINT ' + @constraint exec sp_executesql @sql fetch next
	 * from cur into @constraint, @tbl end close cur deallocate cur
	 *
	 *
	 * PreparedStatement ps=con.prepareStatement(stmt); ps.execute();
	 * ArahantSession.getHSU().commitTransaction(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 *
	 */
//	import java.net.URL;
//import java.security.CodeSource;
	/*
	 * Find out where a class on the classpath will be loaded from. Fully
	 * qualified classname goes on the command line.
	 */
	/**
	 * main
	 *
	 * @param qualifiedClassName name of fully qualified class to find, using dots, but no dot
	 * class. e.g. java.exe Where javax.mail.internet.MimeMessage
	 */
	private static URL findJarFile(String qualifiedClassName) {
		try {
			Class qc = Class.forName(qualifiedClassName);
			CodeSource source = qc.getProtectionDomain().getCodeSource();
			if (source != null)
				return source.getLocation();
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	static private final HashMap<String, Timer> ediTimerTasks = new HashMap<>();

	/**
	 * This causes the task to run every day at the specified time.  The task itself (SendEditTask.run()) determines if this is a day to run.
	 * 
	 * @param vendor
	 * @param timeToSend
	 * @param activated 
	 */
	public static void scheduleEdiTimerTask(BVendorCompany vendor, int timeToSend, char activated) {
		Timer ediTimer = ediTimerTasks.get(vendor.getId());

		//make sure we have a real vendor
		if (vendor.getBean() == null)
			throw new ArahantException("A valid vendor was not provided to schedule an EDI.");

		String owningCompanyName = "";
		if (vendor.getAssociatedCompany() != null)
			owningCompanyName = " (" + vendor.getAssociatedCompany().getName() + ")";

		SendEdiTask ediTask = new SendEdiTask(vendor.getInterfaceId(), vendor.getId());

		int timeHours = timeToSend / 100;
		int timeMinutes = timeToSend % 100;

		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.HOUR_OF_DAY) > timeHours  ||  cal.get(Calendar.HOUR_OF_DAY) == timeHours  &&  cal.get(Calendar.MINUTE) >= timeMinutes)
			cal.add(Calendar.DAY_OF_YEAR, 1);

		cal.set(Calendar.HOUR_OF_DAY, timeHours);
		cal.set(Calendar.MINUTE, timeMinutes);

		//if this vendor already has a timer..
		if (ediTimer != null) {
			//cancel current tasks and schedule new task
			ediTimer.cancel();
			ediTimer.purge();
			if (activated == 'Y') {
				ediTimer = new Timer(true);
				ediTimer.scheduleAtFixedRate(ediTask, cal.getTime(), EVERY_DAY);
				ediTimerTasks.put(vendor.getId(), ediTimer);
				logger.info("Scheduled " + vendor.getName() + " EDI for " + DateUtils.getDateAndTimeFormatted(cal.getTime()) + owningCompanyName);
			} else {
				ediTimerTasks.remove(vendor.getId());
				logger.info("Canceled " + vendor.getName() + " current scheduled EDI" + owningCompanyName);
			}

		} else if (activated == 'Y') {
			//create new timer and schedule new task
			ediTimer = new Timer(true);
			ediTimer.scheduleAtFixedRate(ediTask, cal.getTime(), EVERY_DAY);
			logger.info("Scheduled " + vendor.getName() + " EDI for " + DateUtils.getDateAndTimeFormatted(cal.getTime()) + owningCompanyName);
			ediTimerTasks.put(vendor.getId(), ediTimer);
		} else
			logger.info("No EDI scheduled for " + vendor.getName() + owningCompanyName);
	}

	private static void setupAutoDataPurge() {
		Timer timer = new Timer(true);
		TimerTask adp = new AutoDataPurge();
		Calendar dt = Calendar.getInstance();
		dt.add(Calendar.DAY_OF_YEAR, 1);
		dt.set(Calendar.HOUR_OF_DAY, 2);   //  2 AM
		dt.set(Calendar.MINUTE, 0);
		timer.scheduleAtFixedRate(adp, dt.getTime(), EVERY_DAY);
	}

	public static void main(String[] args) {
		ABCL.init();
		int current = 14000;

		updateEmployeeStatuses(current);
	}

	private static void updateEmployeeStatuses(int current) throws ArahantException {
		//If the database version is before the employee.status_id addition (14944)
		//need to move over statuses into the new field.
		//Don't worry about fresh database because there is nothing to move over anyway.
		//This will only run once per system.
//		if (current < 14944XX && current > 0) {  //The "14944" number is incorrect if they are passed that database update script but haven't run this commented code
//			int count = 0;
//			HibernateSessionUtil session = ArahantSession.getHSU();
//			session.dontAIIntegrate();
//			Session nativeSession = session.getSession();
//			HibernateScrollUtil<Employee> scr = session.createCriteriaNoCompanyFilter(Employee.class).scroll();
//			while (scr.next()) {
//				BEmployee be = new BEmployee(scr.get());
//				BHREmplStatusHistory bstat = be.getLastStatusHistory();
//				if (bstat != null) {
//					System.out.println(be.getNameFML());
//					be.setStatus(bstat.getHrEmployeeStatus());
//					be.setStatusEffectiveDate(bstat.getEffectiveDate());
//					nativeSession.update(be.getEmployee());
//					if (++count % 20 == 0) {
//						System.out.println("Updating Employee statuses: " + count);
//						nativeSession.flush();
//						nativeSession.clear();
//					}
//				}
//			}
//			if (count > 0) {
//				ArahantSession.getHSU().commitTransaction();
//			}
//		}
	}

	// For the cron system
	private static HibernateSessionUtil getConnection() {
		HibernateSessionUtil hsu = ArahantSession.openHSU(false);
		hsu.beginTransaction();
		return hsu;
	}

	private static void success(Object p) {
		HibernateSessionUtil hsu = (HibernateSessionUtil) p;
		hsu.commitTransaction();
		ArahantSession.clearSession();  // close hsu
	}

	private static void failure(Object p) {
		HibernateSessionUtil hsu = (HibernateSessionUtil) p;
		hsu.rollbackTransaction();
		ArahantSession.clearSession();  // close hsu
	}


}
