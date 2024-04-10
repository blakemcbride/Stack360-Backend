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

import com.arahant.business.BPropertyKissOnly;
import org.kissweb.StringUtils;

import java.io.File;
import java.io.IOException;


public class FileSystemUtils {

	private static final ArahantLogger logger=new ArahantLogger(FileSystemUtils.class);
	private static final String TEMP = "temporary";  // "temp" doesn't work under tomcat under certain circumstances
	private static final String REPORTS = "reports";

	private static File workingDirectory;
	private static boolean runningUnderTomcat = false;
	private static boolean runningUnderIDE = false;

	/**
	 * Creates a FILE object representing a file in the temp directory.  The
	 * file is not actually created.  The object is set to auto-delete.
	 * This method also deletes old files in the temp directory (which is
	 * created if necessary).
	 *
	 * If name is null then just return pointer to dir
	 *
	 * @param name the name of the temp file to represent
	 * @return the FILE instance
	 */
	public static File createTempDirFile(String name) {
        File f, tdir = new File(getWorkingDirectory(), TEMP);
        tdir.mkdirs();
        if (name != null) {
            f = new File(tdir, name);
            f.deleteOnExit();
        } else
            f = tdir;

        deleteOldFiles(TEMP);

        return f;
    }

	/**
	 * Sets the root of the application.  Must be set during tomcat boot process.
	 * Must not be called if not running under tomcat.
	 *
	 * @param dirName
	 */
	public static String setWorkingDirectory(String dirName) {
		// hack needed because when run with jRebel dirName is wrong
		if ((new File(dirName + "/../build/web")).exists())
			dirName = dirName.substring(0, dirName.length()-4) + "build/web";

		try {
			workingDirectory=new File(dirName);
			workingDirectory.mkdirs();
			logger.info("Set working directory to " + workingDirectory.getAbsolutePath());
		} catch (final RuntimeException re) {
			workingDirectory=new File(".");
		}
		runningUnderTomcat = true;  // since this method would/should only be called by tomcat initialization
		String appPath = workingDirectory.getAbsolutePath();
		runningUnderIDE = (new File(appPath + "/../../src/java/com/arahant/beans/Person.java")).exists();
		return workingDirectory.getAbsolutePath();
	}

	/**
	 * This routine returns the root of the running application correctly under four environments:
	 *   Under tomcat
	 *   Under NetBeans / tomcat
	 *   Under NetBeans by running a main() not under tomcat
	 *   Running the app from the command line in a production environment
	 *
	 *   Under NetBeans the runtime system is under the 'build/web' directory
	 *
	 *   setWorkingDirectory MUST be called before getWorkingDirectory if operating under tomcat!
	 *
	 * @return representation of the root of the application
	 */
	public static File getWorkingDirectory() {
        File fyle = workingDirectory;
        if (fyle == null) {
            // must be running without tomcat
            fyle = new File(".");
            String appPath = fyle.getAbsolutePath();
            int len = appPath.length();
            if (len > 2 && appPath.charAt(len - 1) == '.' && (appPath.charAt(len - 2) == '/' || appPath.charAt(len - 2) == '\\'))
                appPath = StringUtils.drop(appPath, -2);
            if (runningUnderIDE = (new File(appPath + "/src/java/com/arahant/beans/Person.java")).exists())
                fyle = new File(appPath + "/build/web");
            else
                fyle = new File(appPath);
            workingDirectory = fyle;
        }
        return fyle;
    }

	public static String getSourcePath() {
		String appPath = getWorkingDirectory().getAbsolutePath();
		if (File.separator.equals("\\"))
			appPath = appPath.replace('\\', '/');
		if (runningUnderIDE) {
			String path = appPath;
			int index = path.lastIndexOf('/');
			if (index > 0)
				path = path.substring(0, index);
			index = path.lastIndexOf('/');
			if (index > 0)
				path = path.substring(0, index);
			return path + "/src/java/";
		} else
			return appPath + "/WEB-INF/classes/";
	}

	/**
	 * Return the absolute path of the root of the custom directory.
	 */
	public static String getCustomRoot() {
		String rootPath = FileSystemUtils.getSourcePath();
		rootPath = StringUtils.drop(rootPath, -1);  //  remove trailing slash
		String customRoot = BPropertyKissOnly.get(StandardProperty.CustomPath, "~");
		return customRoot.replace("~", rootPath);
	}

	public static String getJavaClassPath() {
		return getWorkingDirectory().getAbsolutePath() + "/WEB-INF/classes/";
	}

	public static boolean isUnderIDE() {
		getWorkingDirectory();  //  make sure runningUnderIDE is set
		return runningUnderIDE;
	}

	public static boolean isUnderTomcat() {
		return runningUnderTomcat;
	}

	/**
	 * Takes a relative path and turns it into an absolute path relative to the
	 * application directory.
	 *
	 * @param path
	 * @return
	 */
	public static String makeAbsolutePath(String path) {
		String res = getWorkingDirectory().getPath();
		if (path == null  || path.isEmpty())
			return res;
		if (path.charAt(0) != '/'  &&  path.charAt(0) != '\\')
			res = res + '/';
		return res + path;
	}

	/**
	 * Create a File object for a new, randomly named, file with the specified
	 * prefix and suffix to the file name.
	 * <br><br>
	 * Files created by this routine are meant to be used internally only and not shared externally.
	 * <br><br>
	 * This method also deletes old files in the TEMP directory.
	 *
	 * @param prefix
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(final String prefix, String suffix) throws IOException {
		File dir = new File(getWorkingDirectory(), TEMP);
		dir.mkdir();
		if (suffix != null && !suffix.isEmpty() && suffix.charAt(0) != '.')
			suffix = '.' + suffix;
		final File f = File.createTempFile(prefix, suffix, dir);
		//f.deleteOnExit();
		deleteOldFiles(TEMP);
		return f;
	}

	/**
	 * Create a File object for a new, randomly named, file with the specified
	 * prefix and suffix to the file name.
	 * <br><br>
	 * Files created by this routine are meant to be shared externally with the front-end.  This includes
	 * files meant for reports or exports.
	 * <br><br>
	 * This method also deletes old files in the TEMP directory.
	 *
	 * @param prefix
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static File createReportFile(final String prefix, String suffix) throws IOException {
		File dir = new File(getWorkingDirectory(), REPORTS);
		dir.mkdir();
		if (suffix != null && !suffix.isEmpty() && suffix.charAt(0) != '.')
			suffix = '.' + suffix;
		final File f = File.createTempFile(prefix, suffix, dir);
		//f.deleteOnExit();
		deleteOldFiles(REPORTS);
		return f;
	}

	/**
	 * Takes a previously obtained File and returns a path the front-end should use to access the file.
	 */
	public static String getHTTPPath(File f) {
		String name1 = f.getName();
		f = f.getParentFile();
		if (f == null)
			return name1;
		String name2 = f.getName();
		return name2 + "/" + name1;
	}

	/**
	 * Takes a previously obtained file path and returns a path the front-end should use to access the file.
	 */
	public static String getHTTPPath(String fn) {
		return getHTTPPath(new File(fn));
	}

	/**
	 * Generates the absolute path from a given HTTP path.
	 *
	 * @param  path	the HTTP path to convert
	 * @return         	the absolute path generated from the HTTP path
	 */
	public static String httpPathToAbsolutePath(String path) {
		return FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/" + path;
	}

	public static File moveToTempFile(final File sourceFile, final String prefix, final String suffix) throws IOException {
		// use create temp file algorithm to get a temp file name, but delete that file and move the source file to it
		final File f = File.createTempFile(prefix, suffix, new File(getWorkingDirectory(), TEMP));
		f.delete();
		sourceFile.renameTo(f);

		deleteOldFiles(TEMP);

		return f;
	}

	private static void deleteOldFiles(String dir) {
		try {
			long nHoursAgo = new java.util.Date().getTime();
			long hoursAgo = 1L;

			nHoursAgo -= hoursAgo * (60L * 60L * 1000L);

			final File[] fyles = new File(getWorkingDirectory(), dir).listFiles();

			if (fyles != null)
				for (final File element : fyles)
					if (element.isFile() && element.lastModified() < nHoursAgo)
						element.delete();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String [] argv) {
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));
		File f1 = new File("src/java/header.html");
		String httpPath = getHTTPPath(f1);
		httpPath = getHTTPPath("src/java/header.html");
		httpPath = getHTTPPath("java/header.html");
		httpPath = getHTTPPath("header.html");
		int x = 0;
	}
}


