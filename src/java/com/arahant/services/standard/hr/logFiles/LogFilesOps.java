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


/**
 * 
 */
package com.arahant.services.standard.hr.logFiles;

import com.arahant.exports.EDI834EHIM;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrLogFilesOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class LogFilesOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(LogFilesOps.class);
	
	public LogFilesOps() {
		super();
	}
	
    @WebMethod()
	public GetLogFilesReturn getLogFiles(/*@WebParam(name = "in")*/final GetLogFilesInput in) {

		final GetLogFilesReturn ret = new GetLogFilesReturn();

		try
		{
			checkLogin(in);

			String dir = FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/Logfiles";

			File logDir = new File(dir);
			List<String> paths = getLogFiles(logDir);

			String[] retString = new String[paths.size()];

			int i = 0;
			for (String s : paths)
			{
				retString[i++] = s;
			}

			ret.setItem(retString);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	public static List<String> getLogFiles(File dir) {

		List<String> ls = new ArrayList<String>();

		File[] files = dir.listFiles();

		if(files != null)
		{
			for (File file : files) {
				if (file.getName().startsWith("."))
					continue;
				if (file.isFile()) {
					if (file.getName().endsWith("LOG.txt"))
					{
						ls.add(file.getPath().replace(FileSystemUtils.getWorkingDirectory().getAbsolutePath(), ""));
					}
					continue;
				}
				if (file.isDirectory())
					ls.addAll(getLogFiles(file));
			}
		}

		return ls;
	}
    @WebMethod()
	public ClearLogReturn clearLog(/*@WebParam(name = "in")*/final ClearLogInput in) {

		final ClearLogReturn ret = new ClearLogReturn();

		try
		{
			checkLogin(in);
			
			File logFile = new File(FileSystemUtils.getWorkingDirectory().getAbsoluteFile() + in.getPath());
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile,false));
			out.close();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	
	// <editor-fold defaultstate="collapsed" desc="comment">
	public static void main(String[] args) {

		if (false) {
			try {
				FileWriter logFile; //=new FileWriter(FileSystemUtils.makeAbsolutePath("EHIM_EDI_LOG.txt"), true);
				//File logFile = new File(FileSystemUtils.getWorkingDirectory() + File.separator + "EHIM_EDI_LOG.txt");
				boolean newLog = true;
				String logFileName = "EHIM_EDI_LOG.txt";
				BufferedWriter out;
				File newFile = new File(logFileName);

				System.out.println(newFile.getAbsolutePath());

				if (true) {
					if (!newFile.exists()) {
						newFile.createNewFile();
						logFile = new FileWriter(newFile);
					} else {
						logFile = new FileWriter(newFile, true);


					}
					out = new BufferedWriter(logFile);
					out.write("Logging EHIM EDI on " + DateUtils.getDateAndTimeFormatted(new Date()) + "\n\n");
					out.flush();

					out.close();
				}

			} catch (IOException ex) {
				Logger.getLogger(EDI834EHIM.class.getName()).log(Level.SEVERE, null, ex);
			}
		}


		//String f = "/home/brad/NetBeansProjects/ArahantNewLibs";

		//File servDir = new File(f); // + "/src/java/com/arahant");

//		String dir = "Logfiles";
//		boolean success = (new File(dir)).mkdir();
//
//		if (success)
//		{
//			System.out.println("Directory: " + dir + " created");
//		}
//		else
//		{
//			System.out.println("Not created");
//		}

		//String path = servDir.getAbsolutePath();

		//System.out.println("Path: " + path);

//		String dir = "/LogFiles";
//
//		File test = new File(dir);
//
//		System.out.println("test: " + test.getAbsolutePath());
//
//		System.out.println("f: " + f);
//
		//List<String> files = findOperations(servDir);

		File logDir = new File("Logfiles");
		List<String> paths = getLogFiles(logDir);

		for (String s : paths) {
			System.out.println(s);
		}
	}// </editor-fold>

}
