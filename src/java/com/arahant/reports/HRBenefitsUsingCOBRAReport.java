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
 * Created on Jan 11, 2008
 * 
 */
package com.arahant.reports;
import com.arahant.beans.*;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 *
 * Created on Jan 11, 2008
 *
 */
public class HRBenefitsUsingCOBRAReport extends ReportBase {

	public HRBenefitsUsingCOBRAReport() throws ArahantException {
		super("BenUseCobra", "COBRA Benefits", true);
	}
	
	public String build(int policyStartDateFrom, int policyStartDateTo, boolean includeCoverageDetail, boolean limitToNoPolicyEndDate) throws DocumentException, ArahantException
	{		
		try {
			PdfPTable table = null; 
			boolean alternateRow = true;
			boolean samePerson;
			String personId = "";
			Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			for(OrgGroupAssociation o : oga)
				ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
			
			this.writeHeader(policyStartDateFrom, policyStartDateTo, includeCoverageDetail, limitToNoPolicyEndDate);
			
			HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.USING_COBRA, 'Y')
				.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON);

			if (policyStartDateFrom > 0)
				hcu.ge(HrBenefitJoin.POLICY_START_DATE, policyStartDateFrom);
			if (policyStartDateTo > 0)
				hcu.le(HrBenefitJoin.POLICY_START_DATE, policyStartDateTo);
			if (limitToNoPolicyEndDate)
				hcu.eq(HrBenefitJoin.POLICY_END_DATE, 0);

			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
				hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
			else
				hcu.joinTo(HrBenefitJoin.PAYING_PERSON).eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany()).orderBy(Person.LNAME).orderBy(Person.FNAME);
			
			hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).orderBy(HrBenefit.NAME);
			
			HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();
			while (scr.next()) {
				// see if we need to start a new policy table
				if (table == null || includeCoverageDetail) {
					table = startPolicyTable(includeCoverageDetail);
					alternateRow = true;
				}
				
				// toggle the alternate row
				alternateRow = !alternateRow;
				
				// check if we should skip printing the same person ssn/name
				samePerson = personId.equals(scr.get().getPayingPersonId());
				personId = scr.get().getPayingPersonId();
				
				// write policy table
				if (!samePerson || !includeCoverageDetail) {
					writeRight(table, samePerson ? "" : scr.get().getPayingPerson().getUnencryptedSsn(), alternateRow);
					writeRight(table, "", alternateRow);
					write(table, samePerson ? "" : scr.get().getPayingPerson().getNameLFM(), alternateRow);
					if (!includeCoverageDetail)
						write(table, scr.get().getHrBenefitConfig().getHrBenefit().getName(), alternateRow);
					writeRight(table, DateUtils.getDateFormatted(scr.get().getPolicyStartDate()), alternateRow);
					writeRight(table, "", alternateRow);
					writeRight(table, DateUtils.getDateFormatted(scr.get().getPolicyEndDate()), alternateRow);
				}
				
				// check if we need to write out coverage detail
				if (includeCoverageDetail){
					if (!samePerson)
						addTable(table);
					
					this.writeCoverageTable(scr.get());
				}
			}
			scr.close();
			if (!includeCoverageDetail && table != null) {
				addTable(table);
			}
        } finally {
        	close();
	         
        }
		
        return getFilename();
	}
	
	protected void writeHeader(int policyStartDateFrom, int policyStartDateTo, boolean includeCoverageDetail, boolean limitToNoPolicyEndDate) throws DocumentException {
		writeHeaderLine("Policy Start Date From", policyStartDateFrom == 0 ? "(none)" : DateUtils.getDateFormatted(policyStartDateFrom));
		writeHeaderLine("Policy Start Date To", policyStartDateTo == 0 ? "(none)" : DateUtils.getDateFormatted(policyStartDateTo));
		writeHeaderLine("Limit to Benefits without a Policy End Date", limitToNoPolicyEndDate ? "Yes" : "No");
		writeHeaderLine("Include Benefit Coverage Detail", includeCoverageDetail ? "Yes" : "No");
		
		addHeaderLine();
	}
	
	protected PdfPTable startPolicyTable(boolean includeCoverageDetail) throws DocumentException {
		PdfPTable table;
		
		if (includeCoverageDetail)
			table = makeTable(new int[] { 17, 1, 50, 17, 1, 14 });
		else
			table = makeTable(new int[] { 17, 1, 25, 25, 17, 1, 14 });
			
		writeColHeader(table, "Policy Holder SSN", Element.ALIGN_RIGHT);
		writeColHeader(table, "");
		writeColHeader(table, "Policy Holder Name", Element.ALIGN_LEFT);
		if (!includeCoverageDetail)
			writeColHeader(table, "Benefit", Element.ALIGN_LEFT);
        writeColHeader(table, "Policy Start Date", Element.ALIGN_RIGHT);
        writeColHeader(table, "");
        writeColHeader(table, "Policy End Date", Element.ALIGN_RIGHT);
        
        return table;
	}
	
	protected void writeCoverageTable(HrBenefitJoin hrbj) throws DocumentException, ArahantException {
		PdfPTable table = makeTable(new int[] { 6, 11, 1, 39, 18, 10, 1, 14 });
		boolean alternateRow = true;
		
		write(table, "");
		writeLeft(table, "Coverage Detail for Benefit '" + hrbj.getHrBenefitConfig().getHrBenefit().getName() + "'", false, 7);
		
		write(table, "");
		writeColHeader(table, "SSN", Element.ALIGN_RIGHT);
		writeColHeader(table, "");
		writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Relationship", Element.ALIGN_LEFT);
        writeColHeader(table, "Start Date", Element.ALIGN_RIGHT);
        writeColHeader(table, "");
        writeColHeader(table, "End Date", Element.ALIGN_RIGHT);
        
        BHRBenefitJoin bebj = new BHRBenefitJoin(hrbj);
        BHRBenefitJoin[] bbjs = bebj.getPolicyAndDependentBenefitJoins(false);
        
        for (BHRBenefitJoin bbj : bbjs) {		
			// toggle the alternate row
			alternateRow = !alternateRow;
			
			write(table, "", false);
	        writeRight(table, bbj.getCoveredSsn(), alternateRow);
	        write(table, "", alternateRow);
	        write(table, bbj.getCoveredNameLFM(), alternateRow);
	        write(table, bbj.getRelationshipText(), alternateRow);
	        writeRight(table, DateUtils.getDateFormatted(bbj.getCoverageStartDate()), alternateRow);
	        write(table, "", alternateRow);
	        writeRight(table, DateUtils.getDateFormatted(bbj.getCoverageEndDate()), alternateRow);
        }        
        
        addTable(table);
	}
	
	public static void main(String[] args) {
		try {
			new HRBenefitsUsingCOBRAReport().build(0, 0, true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

	
