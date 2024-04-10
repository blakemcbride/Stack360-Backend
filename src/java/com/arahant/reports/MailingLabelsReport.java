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

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

public class MailingLabelsReport// extends ReportBase 
{

	public MailingLabelsReport() throws ArahantException {
		//super("labels","", false, 0);
	}


	/*
	 *
	 * String query="select distinct emp.fname, emp.lname, addr.street,
	 * addr.street2, addr.city, addr.state, addr.zip, emp.personId from Employee
	 * emp left join emp.prophetLogin log " + " left join emp.addresses addr
	 * join emp.hrEmplStatusHistories hist "+ " join
	 * emp."+Employee.ORGGROUPASSOCIATIONS+" ogas ";
	 *
	 * if (benefitIds.length>0) query+=" join emp."
	 * +Employee.HR_BENEFIT_JOINS_WHERE_PAYING+" bj "; query+="where \n" ;
	 * query+=" ogas."+OrgGroupAssociation.ORGGROUP+"."+OrgGroup.ORGGROUPID+" in
	 * (:ids) and " ; if (benefitIds.length>0)
	 * query+="(bj."+HrBenefitJoin.POLICY_END_DATE+"=0 or
	 * bj."+HrBenefitJoin.POLICY_END_DATE+" <= "+DateUtils.now()+") and ";
	 *
	 * if (benefitIds.length>0) {
	 * query+="bj."+HrBenefitJoin.HR_BENEFIT_CONFIG+"."+HrBenefitConfig.HR_BENEFIT+"."+HrBenefit.BENEFITID+"
	 * in ("; for (int loop=0;loop<benefitIds.length;loop++)
	 * query+="'"+benefitIds[loop]+"',";
	 *
	 * query=query.substring(0,query.length()-1);
	 *
	 * query+=	") and "; }
	 *
	 * String statusClause=	" hist.hrEmployeeStatus.statusId in (";
	 *
	 * for (int loop=0;loop<statusIds.length;loop++)
	 * statusClause+="'"+statusIds[loop]+"',";
	 *
	 * statusClause=statusClause.substring(0,statusClause.length()-1);
	 *
	 * statusClause+=	") and hist.effectiveDate = \n"+ "(select
	 * max(effectiveDate) from HrEmplStatusHistory hist2 where
	 * hist2.employee=emp and hist2.effectiveDate<="+DateUtils.now()+") \n "+ "
	 * and ((select count(*) from HrEmplStatusHistory hist2 where
	 * hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " + " or
	 * hist.hrEmployeeStatus.dateType='S' ) ";
	 *
	 * query+=statusClause+ " and (addr.addressType=2 or addr.addressType is
	 * null) " + " order by addr.zip, emp.lname, emp.fname";
	 *
	 * Query q=ArahantSession.getHSU().createQuery(query);
	 * q.setParameterList("ids", orgIds);
	 *
	 * HibernateScrollUtil sr=new HibernateScrollUtil(q.scroll());
	 */
	public String build(final String statusIds[], final String[] benefitIds, String[] orgs, boolean includeId) throws DocumentException, ArahantException {

		String fileName = "";

		try {

			File fyle = FileSystemUtils.createTempFile("mailing", ".csv");
			fileName = FileSystemUtils.getHTTPPath(fyle);

			Set<String> orgIds = new HashSet<String>();

			for (String og : orgs)
				orgIds.addAll(new BOrgGroup(og).getAllOrgGroupsInHierarchy());

			if (orgIds.isEmpty())
				orgIds.add("");

			HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class);
			hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds);

			if (benefitIds.length > 0)
				hcu.joinTo(Employee.HR_BENEFIT_JOINS_WHERE_PAYING).isNull(HrBenefitJoin.RELATIONSHIP) //had to add this to get rid of duplicates
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).in(HrBenefit.BENEFITID, benefitIds);

			hcu.employeeCurrentStatusIn(statusIds, DateUtils.now());

			hcu.orderBy(Employee.SORT_ORDER).orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME);

			HibernateScrollUtil<Employee> sr = hcu.scroll();

			BufferedWriter bw = new BufferedWriter(new FileWriter(fyle));

			String empId = "";

			while (sr.next()) {
				BEmployee emp = new BEmployee(sr.get());

				if (emp.getPersonId().equals(empId))
					continue;

				bw.write(fix(emp.getFirstName()) + "," + fix(emp.getLastName()) + "," + fix(emp.getStreet()) + ","
						+ fix(emp.getStreet2()) + "," + fix(emp.getCity()) + "," + fix(emp.getState()) + "," + fix(emp.getZip()));
				if (includeId)
					bw.write("," + fix(emp.getPersonId()));

				bw.newLine();

				empId = emp.getPersonId();

			}
			bw.close();

			sr.close();


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//  close();
		}

		return fileName;
	}

	private String fix(String x) {
		return "\"" + x.replaceAll("\"", "") + "\"";
	}
}
