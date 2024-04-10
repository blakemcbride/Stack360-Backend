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


package com.arahant.reports;

import com.arahant.beans.AIProperty;
import com.arahant.beans.Employee;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

public class ServiceListReport extends ReportBase {

	public ServiceListReport() {
		super("service", "Service List");
	}

	public String build(int month) {
		try {
			PdfPTable table = makeTable(new int[] {12, 14, 33, 41});

			String monthFormatted = "";
			if (month == 0) {
				monthFormatted = "January";
			} else if (month == 1) {
				monthFormatted = "February";
			} else if (month == 2) {
				monthFormatted = "March";
			} else if (month == 3) {
				monthFormatted = "April";
			} else if (month == 4) {
				monthFormatted = "May";
			} else if (month == 5) {
				monthFormatted = "June";
			} else if (month == 6) {
				monthFormatted = "July";
			} else if (month == 7) {
				monthFormatted = "August";
			} else if (month == 8) {
				monthFormatted = "September";
			} else if (month == 9) {
				monthFormatted = "October";
			} else if (month == 10) {
				monthFormatted = "November";
			} else if (month == 11) {
				monthFormatted = "December";
			}
			if (!isEmpty(monthFormatted)) {
				writeHeaderLine("Month", monthFormatted);
			}
			
			addHeaderLine();

			writeColHeader(table, "Seniority", Element.ALIGN_RIGHT);
			writeColHeader(table, "Date", Element.ALIGN_RIGHT);
			writeColHeader(table, "Name", Element.ALIGN_LEFT);
			writeColHeader(table, "Title", Element.ALIGN_LEFT);
			table.setHeaderRows(1);
			

			String query="select distinct emp, h.effectiveDate from Employee emp " +
					" join emp.hrEmplStatusHistories h" +
					" join emp."+Employee.ORGGROUPASSOCIATIONS+" oga " +
					" join emp.hrEmplStatusHistories h3 "; 
			
			
			String where="where h.effectiveDate=(select min(h2.effectiveDate) from HrEmplStatusHistory h2 where h2.employee=emp" +
					" and (h2.effectiveDate>(select max(h5.effectiveDate) from HrEmplStatusHistory h5 where h5.employee=emp" +
					" and h5.hrEmployeeStatus.active='N')" +
					" or null=(select max(h6.effectiveDate) from HrEmplStatusHistory h6 where h6.employee=emp and h6.hrEmployeeStatus.active='N' ))) " +
					"and h3.effectiveDate=(select max(h4.effectiveDate) from HrEmplStatusHistory h4 where h4.employee=emp) " +
					"and h3.hrEmployeeStatus.active='Y' " +
					"  and oga."+OrgGroupAssociation.ORGGROUP+"."+OrgGroup.OWNINGCOMPANY+"."+OrgGroup.ORGGROUPID+"='"+hsu.getCurrentCompany().getOrgGroupId()+"' ";
			
			if (month>-1)
			{
				where+=" and (";
			
				int yr=DateUtils.now()/10000;
				int d1=yr*10000+100;
				int d2=yr*10000+199;

				d1+=month*100;
				d2+=month*100;
				for (int loop=0;loop<100;loop++)
				{
					
					where+="(h.effectiveDate > "+d1+" and h.effectiveDate < "+d2+") or ";
					d1-=10000;
					d2-=10000;
				}
				where=where.substring(0, where.length()-3);
				where+=")";
			}
			List<String> orgs=AIProperty.getList("RestrictedOrgGroups");
		
			Set<String> orgSet=new HashSet<String>(orgs);
			if (orgs.size()>0)
			{
				orgSet.addAll(orgs);

				for(String o : orgs)
					orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());

				query+=" join emp."+Person.ORGGROUPASSOCIATIONS+" oga ";
	
				where+=" and oga."+OrgGroupAssociation.ORG_GROUP_ID+" in (:orgGroups) ";
				
			}
			
			
			query+=where+" order by h.effectiveDate ";
			
			Query q=hsu.createQuery(query);
					
			if (orgSet.size()>0)
				q.setParameterList("orgGroups", orgSet);
			
			ScrollableResults employees=q.scroll();
			
		 
			boolean alternateRow = false;
			Calendar toCalendar = Calendar.getInstance();
			
			// if we specified a month, we will determine years of service against last day of specified month
			if (month >= 0 && month <= 12) {
				toCalendar.set(Calendar.MONDAY, month);
				toCalendar.set(Calendar.DAY_OF_MONTH, toCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
			
			while (employees.next()) {
				BEmployee bEmployee = new BEmployee((Employee)employees.get(0));
				int employeeHireDate = (Integer)employees.get(1); // TODO 4 - what about breaks in service ?
				int yearsOfService = (int)DateUtils.getYearsBetween(employeeHireDate, DateUtils.getDate(toCalendar));
				String seniority = yearsOfService + "y";
				
				if (month < 0 || month > 12) {
					// also include months in seniority - showing how much seniority as of today
					Calendar fromCalendar = DateUtils.getCalendar(employeeHireDate);
					fromCalendar.add(Calendar.YEAR, yearsOfService);
					
					int monthsOfService = (int)DateUtils.getMonthsBetween(DateUtils.getDate(fromCalendar), DateUtils.getDate(toCalendar));
					seniority += ", " + monthsOfService + "m";
				}
						
				super.writeRight(table, seniority, alternateRow);
				super.writeRight(table, DateUtils.getDateFormatted(employeeHireDate), alternateRow);
				super.write(table, bEmployee.getNameLFM(), alternateRow);
				super.write(table, bEmployee.getJobTitle(), alternateRow);
				
				alternateRow = !alternateRow;
			}
			
			employees.close();

			addTable(table);
		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();
		}

		return getFilename();
	}

	public static void main(String args[]) {
		try {
			//ArahantSession.getHSU().setCurrentPersonToArahant();
			ArahantSession.getHSU().setCurrentPerson(ArahantSession.getHSU().get(Employee.class, "00001-0000002745"));
			
			new ServiceListReport().build(7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
