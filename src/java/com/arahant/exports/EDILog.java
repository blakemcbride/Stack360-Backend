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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.exports;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import org.kissweb.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class EDILog {

	public static final String DIRECTORY = FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/Logfiles/";
	private File logFile;
	private BufferedWriter writer;
	private String fileName;

	public EDILog()
	{
	}

	public EDILog(String fileName) 
	{
		this.fileName = fileName;
		boolean success = (new File(DIRECTORY)).mkdir();
		if (success)
		{
			System.out.println("Directory: " + DIRECTORY + " created");
		}
		else
		{
			System.out.println("Directory already exists");
		}
		if(!StringUtils.isEmpty(fileName))
		{
			logFile = new File(DIRECTORY + fileName + "_LOG.txt");
		}
		else
		{
			throw new ArahantException("Tried to create a log file without a name.");
		}
		try {
			writer = new BufferedWriter(new FileWriter(logFile, true));
			write(DateUtils.getDateAndTimeFormatted(new Date()) + "\n\n");
		} catch (IOException ex) {
			Logger.getLogger(EDILog.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	public void setWriter(BufferedWriter out) {
		this.writer = out;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public void write(String s) throws IOException
	{
		writer.write(s + "\n");
	}

	public void flush() throws IOException
	{
		writer.flush();
	}

	public void close() throws IOException
	{
		writer.close();
	}
}
