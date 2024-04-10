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

package com.arahant.reports;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitProjectJoin;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.Timesheet;
import com.arahant.business.BProject;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Formatting;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class PayrollReport extends ReportBase {


	public PayrollReport() throws ArahantException {
        super("PayRpt", "Payroll Time Report", true);
    }

    public String build(int start, int end) throws DocumentException {

        try {

            PdfPTable table;

			writeHeaderLine("From", DateUtils.getDateFormatted(start));
			writeHeaderLine("To",DateUtils.getDateFormatted(end));
            addHeaderLine();

            HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteria(Timesheet.class)
				.dateBetween(Timesheet.WORKDATE, start, end)
//				.in(Timesheet.STATE, new char[]{ArahantConstants.TIMESHEET_APPROVED,ArahantConstants.TIMESHEET_INVOICED})
				.joinTo(Timesheet.PERSON)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.orderBy(Person.MNAME);

            HibernateScrollUtil<Timesheet> scr = hcu.scroll();

            int count = 0;

			List<HrBenefit> benefits=hsu.createCriteria(HrBenefit.class)
					.list();

			List<Project> p=hsu.createCriteria(Project.class)
				.joinTo(Project.HRBENEFITPROJECTJOINS)
				.joinTo(HrBenefitProjectJoin.HR_BENEFIT_CONFIG)
				.in(HrBenefitConfig.HR_BENEFIT,benefits)
				.orderBy(HrBenefit.NAME)
				.list();


			int maxNameLen=0;

			int ratio[]=new int[p.size()+2];
			int rc=0;

			ratio[rc++]=6;
			maxNameLen+=6;

			for (Project pro : p)
			{
				String name=new BProject(pro).getBenefitName();
				int nmsize=name.length();
				if (nmsize<5)
					nmsize=5;
				ratio[rc++]=nmsize;
				maxNameLen+=nmsize;
			}

			ratio[rc++]=6;
			maxNameLen+=6;

			int []sizes=new int[p.size()+4];
			sizes[0]=15;
			sizes[1]=12;

			for (int loop=2;loop<sizes.length;loop++)
				sizes[loop]=(int)(70*(ratio[loop-2]/((double)maxNameLen)));

            table = makeTable(sizes);

            writeColHeader(table, "Name", Element.ALIGN_LEFT);
            writeColHeader(table, "SSN", Element.ALIGN_LEFT);
			writeColHeader(table, "Regular", Element.ALIGN_RIGHT);


			for (Project proj : p)
				writeColHeader(table, new BProject(proj).getBenefitName(), Element.ALIGN_RIGHT);

			writeColHeader(table, "Total", Element.ALIGN_RIGHT);

            boolean alternateRow = true;

			String ssn="";
			String name="";
			double total=0;
			double standards=0;

			HashMap<String,Double> projectCounts=new HashMap<String,Double>();

            while (scr.next()) {
                count++;

				Timesheet ts=scr.get();
                // toggle the alternate row
                

				if (!ssn.equals(ts.getPerson().getUnencryptedSsn()))
				{
					alternateRow = !alternateRow;
					if (!isEmpty(name))
					{
						write(table, name, alternateRow);
						write(table, ssn, alternateRow);
						writeRight(table, Formatting.formatNumber(standards,2), alternateRow);

						for (Project pr : p)
						{
							String nam=new BProject(pr).getBenefitName();
							if (projectCounts.containsKey(nam))
								writeRight(table, Formatting.formatNumber(projectCounts.get(nam).doubleValue(),2), alternateRow);
							else
								writeRight(table, Formatting.formatNumber(0.0,2), alternateRow);

						}
						writeRight(table, Formatting.formatNumber(total,2), alternateRow);
					}
					total=0;
					ssn=scr.get().getPerson().getUnencryptedSsn();
					name=scr.get().getPerson().getNameLFM();
					standards=0;
					projectCounts.clear();
				}


				String beneName=new BProject(ts.getProjectShift().getProject()).getBenefitName();

				if (!isEmpty(beneName))
				{

					Double pt=projectCounts.get(beneName);
					if (pt==null)
						pt=0.0;
					projectCounts.put(beneName, pt+ts.getTotalHours());
				}
				else
					standards+=ts.getTotalHours();

				total+=ts.getTotalHours();

            }

			write(table, name, alternateRow);
			write(table, ssn, alternateRow);
			writeRight(table, Formatting.formatNumber(standards,2), alternateRow);

			for (Project pr : p)
			{
				String nam=new BProject(pr).getBenefitName();
				if (projectCounts.containsKey(nam))
					writeRight(table, Formatting.formatNumber(projectCounts.get(nam),2), alternateRow);
				else
					writeRight(table, Formatting.formatNumber(0.0,2), alternateRow);

			}
			writeRight(table, Formatting.formatNumber(total,2), alternateRow);

            scr.close();
            addTable(table);



        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			ArahantSession.multipleCompanySupport=true;
			ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class,"00001-0000000176"));
            new PayrollReport().build(20090101,20100101);
			ArahantSession.getHSU().rollbackTransaction();
        } catch (Exception e) {
            e.printStackTrace();
			ArahantSession.getHSU().rollbackTransaction();
        }
    }
}
