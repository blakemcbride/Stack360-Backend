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
 * Created on Nov 14, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.beans.LoginLog;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




/**
 * 
 *
 * Created on Nov 14, 2007
 *
 */
public class LoginReport extends ReportBase {


	public LoginReport() throws ArahantException {
		super("LogRpt","Login List");
	}

	/**
	 * @param includeLoginAttemptStatus
	 * @param includeLoginName
	 * @param includeName
	 * @param loginAttemptDateFrom
	 * @param loginAttemptDateTo
	 * @param showFailedLoginAttempts
	 * @param showSuccessfulLoginAttempts
	 * @return
	 * @throws DocumentException 
	 */
	public String build(boolean includeLoginAttemptStatus, boolean includeLoginName, boolean includeName, int loginAttemptDateFrom, int loginAttemptDateTo, boolean showFailedLoginAttempts, boolean showSuccessfulLoginAttempts) throws DocumentException {
		
		 try {

	        	// write out the parts of our report
			 	addHeaderLine();
			 
			 	PdfPTable table=createTable(1, new boolean[]{includeLoginAttemptStatus, includeLoginName, includeName});
			 	
			 	HibernateCriteriaUtil<LoginLog>hcu=ArahantSession.getHSU().createCriteria(LoginLog.class);
			 	
			 	hcu.selectFields(LoginLog.PERSONID, LoginLog.LOGIN_NAME, LoginLog.SUCCESSFUL, LoginLog.DATE);
			 	
	
				if (loginAttemptDateFrom!=0)
					hcu.ge(LoginLog.DATE, DateUtils.getDate(loginAttemptDateFrom));
			 	
				if (loginAttemptDateTo!=0) {
					Date edate = DateUtils.getDate(loginAttemptDateTo);
					Calendar cdate = new GregorianCalendar();
					cdate.setTime(edate);
					cdate.add(Calendar.DAY_OF_YEAR, 1);
					hcu.lt(LoginLog.DATE, new Date(cdate.getTimeInMillis()));
				}
			 	
			 	hcu.orderByDesc(LoginLog.DATE);
			 	
			 	if (!showFailedLoginAttempts)
			 		hcu.ne(LoginLog.SUCCESSFUL, 'N');
			 	
			 	if (!showSuccessfulLoginAttempts)
			 		hcu.ne(LoginLog.SUCCESSFUL, 'Y');
			 	
			 	writeColHeader(table, "Date", Element.ALIGN_LEFT);
			 	if (includeLoginName)
			 		writeColHeader(table, "Login Name", Element.ALIGN_LEFT);
			 	if (includeName)
			 		writeColHeader(table, "Name", Element.ALIGN_LEFT);
			 	if (includeLoginAttemptStatus)
			 		writeColHeader(table, "Attempt Status", Element.ALIGN_LEFT);
			 	
			 	table.setHeaderRows(1);
			 	
			 	HibernateScrollUtil hscroll=hcu.scroll();
			 	int count=0;
			 	
			 	boolean altcolor=false;
			 	while (hscroll.next())
			 	{
			 		Person p=ArahantSession.getHSU().get(Person.class,hscroll.getString(0));
			 		write(table, DateUtils.getDateTimeFormatted(hscroll.getDate(3)),altcolor);
			 		if (includeLoginName)
			 			write(table, hscroll.getString(1),altcolor);
			 		if (includeName)
			 			if (p!=null)
			 				write(table, p.getNameLFM(),altcolor);
			 			else
			 				write(table, "",altcolor);
			 		if (includeLoginAttemptStatus)
			 			write(table, hscroll.getChar(2)=='Y'?"Successful":"Failed",altcolor);
			 		count++;
			 		altcolor=!altcolor;
			 	}
			 	hscroll.next();
                                
			 	addTable(table);
			 	
			 	table=makeTable(new int[]{100});
			 	
			 	write(table,"Total: "+count);
			 	
			 	addTable(table);

	        } finally {
	        	close();
		         
	        }
	        
	        return getFilename();
	}
	
	
}

	
