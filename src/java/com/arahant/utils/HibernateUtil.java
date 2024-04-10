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


import com.arahant.interceptor.ArahantBeanInterceptor;
import com.arahant.utils.dynamicclassloader.ArahantClassLoader;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final ArahantLogger logger = new ArahantLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
	
	/*
	 * x4 is part of an effort to protect the system from being run past a certain date.
	 * We are trying to prevent end users from being able to de-compile and get around
	 * our time limit.
	 *
	 * For example, let's say the system is installed at a user's facility, and they are
	 * supposed to pay you a usage fee every month.  If they don't pay you, how can you
	 * stop them from using the system?  You need a timeout.  x4 supports this.
	 * One of the lisp files contains the timeout date.
	 *
	 * If x4 is changed the following class files should be deleted and re-built:
	 * 
	 * com.arahant.utils.HibernateUtil
	 * com.arahant.lisp.ABCL
	 * com.arahant.services.ServiceBase
	 */
	public static final boolean x4 = false;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			if (x4) {
				try {
					Class cls = new ArahantClassLoader().loadArahantClassFromFile(true, FileSystemUtils.getSourcePath() + "data3");
					Class[] ca = {};
					@SuppressWarnings("unchecked") 
					Method meth = cls.getMethod("createSession", ca);
					if (meth == null) {
						System.out.println("System initialization failure (101).");
						System.exit(0);
					}
					sessionFactory = (SessionFactory) meth.invoke(cls);
				} catch (Exception e) {
					//  The following error will occur if the java encryption policy files for the U.S. are not installed.  
					System.out.println("System initialization failure (102).");
					System.exit(0);
				}
			} else {
				try {
					// Create the SessionFactory from hibernate.cfg.xml
					//			final Configuration config=new Configuration().configure();
					//			config.setInterceptor( new ArahantBeanInterceptor() );
					//       sessionFactory = config.buildSessionFactory();
					//	config.set
					//	SettingsFactory factory=config.get
					final Configuration con = new Configuration();
					int nClasses = 0;
					con.setInterceptor(new ArahantBeanInterceptor());
					for (Class c : HibernateUtil.getClasses("com.arahant.beans"))
						for (Annotation annot : c.getAnnotations())
							if (annot.annotationType().equals(javax.persistence.Entity.class)) {
								con.addAnnotatedClass(c);
								nClasses++;
							}

                    String tmpDir = System.getProperty("java.io.tmpdir");
                    if (!(new File(tmpDir)).canWrite())
                        System.out.println("Error: do not have permission to write to " + tmpDir);
					con.configure();
					sessionFactory = con.buildSessionFactory();  //  This is the line that causes hibernate.cfg.xml to be read

				} catch (final Throwable ex) {
					// Make sure you log the exception, as it might be swallowed
					logger.error("Initial SessionFactory creation failed.", ex);
					throw new ExceptionInInitializerError(ex);
				}
			}
			
        return sessionFactory;
    }

    public static Class[] getClasses(String pckgname)
			throws ClassNotFoundException {
		ArrayList<Class> classes = new ArrayList<Class>();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}

			//May not want first '/' here
			String path =  pckgname.replace('.', '/');

			//System.out.println("Get classes looking for "+path);

			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			try {
				//			directory = new File(resource.getFile());
							directory = new File(resource.toURI().getSchemeSpecificPart());
			} catch (URISyntaxException ex) {
				logger.error("Error trying to access " + resource.getFile(), ex);
			}

		//	System.out.println("Directory is "+directory.getAbsolutePath());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (String file : files) {
				// we are only interested in .class files
				if (file.endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(pckgname + '.'
							+ file.substring(0, file.length() - 6)));
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " (" + directory.getAbsolutePath()
					+ ") does not appear to be a valid package");
		}
		Class[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

    /**
     * Close the hibernate session.  If this is not performed, the application will never end because hibernate threads
     * will still be running.
     */
	public static void close() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
	}

	public static void main(String [] argv) {
		getSessionFactory();
	}
}
