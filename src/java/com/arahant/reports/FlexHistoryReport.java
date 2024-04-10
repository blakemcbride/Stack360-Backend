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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.OrgGroup;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 */
public class FlexHistoryReport extends ReportBase {

	public FlexHistoryReport() {
		super("BeneHis", "Benefit History");
	}

	
	private class ReportData implements Comparable<ReportData>
	{
		String lname;
		String fname;
		String mname;
		String ssn;
		double amount;
		String benefit;
		int policyStartDate;
		int policyEndDate;
		double monthlyCost;


		@Override
		public int hashCode() {
			int hash = 7;
			hash = 47 * hash + (this.lname != null ? this.lname.hashCode() : 0);
			hash = 47 * hash + (this.fname != null ? this.fname.hashCode() : 0);
			hash = 47 * hash + (this.mname != null ? this.mname.hashCode() : 0);
			hash = 47 * hash + (this.ssn != null ? this.ssn.hashCode() : 0);
			hash = 47 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
			hash = 47 * hash + (this.benefit != null ? this.benefit.hashCode() : 0);
			hash = 47 * hash + this.policyStartDate;
			hash = 47 * hash + this.policyEndDate;
			return hash;
		}
		
		@Override
		public boolean equals(Object o)
		{
			ReportData r=(ReportData) o;
			return lname.equals(r.lname) && fname.equals(r.fname)
					&& mname.equals(r.mname) && ssn.equals(r.ssn)
					&& benefit.equals(r.benefit) && policyStartDate==r.policyStartDate;
		}

		public int compareTo(ReportData o) {
			if (benefit.compareTo(o.benefit)!=0)
				return benefit.compareTo(o.benefit);
			
			if (lname.compareTo(o.lname)!=0)
				return lname.compareTo(o.lname);
			
			if (fname.compareTo(o.fname)!=0)
				return fname.compareTo(o.fname);
			
			if (mname.compareTo(o.mname)!=0)
				return mname.compareTo(o.mname);
			
			if (ssn.compareTo(o.ssn)!=0)
				return ssn.compareTo(o.ssn);
			
			return policyStartDate-o.policyStartDate;
			
		}
	}
	
	private List<String> loadData(Query query, List<ReportData> data)
	{
		List<String> ids=new LinkedList<String>();		
		
		HibernateScrollUtil scr=new HibernateScrollUtil(query.scroll());		

		while (scr.next()) {
			ReportData rd=new ReportData();

			rd.lname=scr.getString(0);
			rd.fname=scr.getString(1);
			rd.mname=scr.getString(2);
			rd.ssn=scr.getString(3);
			rd.amount=scr.getDouble(4);
			rd.benefit=scr.getString(5);
			rd.policyStartDate=scr.getInt(6);
			rd.policyEndDate=scr.getInt(7);
			rd.monthlyCost=scr.getDouble(10)/12;

			ids.add(scr.getString(8));

			if (!data.contains(rd))
				data.add(rd);
		}
		
		
		scr.close();
		
		return ids;
	}

	public String build(int startDate, int endDate, String statusId, String [] orgGroupIds, boolean groupOrgGroups, String[] benefitIds, boolean splitFlex) throws DocumentException {

        try {
			BHRBenefit b=new BHRBenefit(benefitIds[0]);
			doingFlex=(b.getBenefitCategory().getBenefitType()==HrBenefitCategory.FLEX_TYPE);
				
			writeHeaderLine("Employee Status",(isEmpty(statusId) ? "(any)" : hsu.get(HrEmployeeStatus.class, statusId).getName()));
			if (orgGroupIds.length==0)
	        	writeHeaderLine("Location","(any)");
	        else
	        {
	        	String ogNames="";
	        	for (OrgGroup og :hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).in(OrgGroup.ORGGROUPID, orgGroupIds).list())
					ogNames=ogNames+og.getName()+", ";
	        	
	        	if (!isEmpty(ogNames))
	        		ogNames=ogNames.substring(0,ogNames.length()-2);
	        	writeHeaderLine("Location", ogNames);
	        }
			writeHeaderLine("Begin Date",DateUtils.getDateFormatted(startDate));
			writeHeaderLine("End Date",DateUtils.getDateFormatted(endDate));
			
            addHeaderLine();
			
			
			// load list of requested org groups (locations)
			List<OrgGroup> ogList;
			if (orgGroupIds.length==0)
			{
				ogList=new ArrayList<OrgGroup>();
				for (BOrgGroup bg : BOrgGroup.getLocations())
					ogList.add(bg.getOrgGroup());
			}
			else
				ogList=hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).in(OrgGroup.ORGGROUPID, orgGroupIds).list();
		

			// switch off if we are grouping org groups (locations)
			if (groupOrgGroups)	{
				processOrgGroups(splitFlex, startDate, endDate, statusId, ogList, benefitIds);
			} else {			
				// writing org groups one at a time
				for (final OrgGroup og: ogList) {
					
					List<OrgGroup> ogl=new ArrayList<OrgGroup>();
					ogl.add(og);
					writeCentered("For Location "+og.getName());
					
					processOrgGroups(splitFlex, startDate, endDate, statusId, ogl, benefitIds);
				}
			}
				
			
        } finally {
            close();

        }

        return getFilename();
    }

	private void processOrgGroups(boolean splitFlex, int startDate, int endDate, String statusId, List<OrgGroup> ogl, String [] benefitIds) throws DocumentException
	{
		if (splitFlex)
		{
			writeCentered("All enrolled in CoPay plan");

			writeData(getData(startDate, endDate, statusId, ogl,1, benefitIds),"CoPay ");

			blankLine();

			writeCentered("All enrolled in Deductible plan");

			writeData(getData(startDate, endDate, statusId, ogl,2, benefitIds),"Deductible ");

			
			writeLine("Total Amount : "+MoneyUtils.formatMoney(totalAmount));
		}
		else
			writeData(getData(startDate, endDate, statusId, ogl,0, benefitIds),"Total ");
		
		writeLine("Total Count : " +totalCount);
		writeLine("Total Paid : "+MoneyUtils.formatMoney(totalPaid));
	}
	
	
	private String getQueryBase(String table, int startDate, int endDate, String statusId, int medSwitch)
	{
		//already filtered by org group id's doesn't need employee company filter
		String q= "select distinct bj.payingPerson.lname, bj.payingPerson.fname," +
					"bj.payingPerson.mname, bj.payingPerson.ssn, bj.amountCovered," +
					"bj.hrBenefitConfig.hrBenefit.name, bj.policyStartDate, bj.policyEndDate," +
					"bj.benefitJoinId, bj.recordChangeDate, bj.amountPaid  "+
					"   from " + table +" bj join bj.payingPerson.orgGroupAssociations orga" +
					"  where " +
					"((bj.policyStartDate < "+startDate+" and bj.policyEndDate >= "+startDate+") " +
					"or " +
					"(bj.policyStartDate >= "+startDate+" and bj.policyStartDate <= "+endDate+")) " +
					" and bj.hrBenefitConfig.hrBenefit.benefitId in (:benefitIds) " +
					" and orga.orgGroup in (:orgGroups) ";
		
		if (!isEmpty(statusId))
				q+=" and bj.payingPerson.personId in (select emp.personId from Employee emp join emp.hrEmplStatusHistories hist where hist.effectiveDate = " +
                        " (select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + DateUtils.now() + ") \n" +
						"and hist.hrEmployeeStatus = :stat )";
		
		switch (medSwitch)
		{
			case 1: q+=" and bj.payingPerson.personId in (select bj2.payingPerson.personId from HrBenefitJoin bj2 where bj2.hrBenefitConfig.hrBenefit.benefitId='00001-0000000023')";
				break;
			case 2: q+=" and bj.payingPerson.personId not in (select bj2.payingPerson.personId from HrBenefitJoin bj2 where bj2.hrBenefitConfig.hrBenefit.benefitId='00001-0000000023')";
				break;
			
		}
		
		return q;
	}
	private List<ReportData> getData(int startDate, int endDate, String statusId, final List<OrgGroup> ogList, int type, String []benefitIds)
	{

			Query q=hsu.createQuery(getQueryBase("HrBenefitJoin",startDate,endDate,statusId,type));
			
			q.setParameterList("benefitIds", benefitIds);
			
			List <OrgGroup> allOrgsInGroup=new ArrayList<OrgGroup>();
			
			for (OrgGroup og : ogList)
				for (String id : new BOrgGroup(og).getAllOrgGroupsInHierarchy())
					allOrgsInGroup.add(hsu.get(OrgGroup.class, id));
		
			
			q.setParameterList("orgGroups", allOrgsInGroup);
		
			if (!isEmpty(statusId))
				q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
			
			
			List<ReportData> data=new LinkedList<ReportData>();
			
			List <String> ids=loadData(q, data);
			
			String query=getQueryBase("HrBenefitJoinH",startDate,endDate,statusId,type);
			
			//if I'm flex, check amount covered
			
			if (doingFlex)
				query+=" and bj.amountCovered!=0 ";
			
			q=hsu.createQuery(query+" order by bj.recordChangeDate ");

		//	q.setParameterList("ids", ids);
			q.setParameterList("orgGroups", allOrgsInGroup);
			q.setParameterList("benefitIds", benefitIds);
			
			if (!isEmpty(statusId))
				q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
			
			
			loadData(q, data);

			
			Collections.sort(data);
			
			
			return data;
	}
	
	
	
	
	private void writeData(List <ReportData> data, String section) throws DocumentException
	{
		

		int count=0;
		
		if (data.size()==0)
			return;

		PdfPTable table=makeTable(new int[]{100});
				
		write(table, data.get(0).benefit, false);
		
		addTable(table);
		
		table = writeColHeaders();
		
		boolean alternateRow = true;
		
		double amount=0;

		int benCount=0;
		double benAmount=0;
		double paid=0;

		String lastBenName=data.get(0).benefit;

		for (ReportData rd : data) {
			
			
			if (!rd.benefit.equals(lastBenName))
			{
				
				
				addTable(table);
				
				//write the totals
				writeLine (lastBenName+" count : "+benCount);
				if (doingFlex)
					writeLine (lastBenName+" amount : "+MoneyUtils.formatMoney(benAmount));
				
				benCount=0;
				benAmount=0;
			
				writeLine(rd.benefit);
				
				table=writeColHeaders();
				
				lastBenName=rd.benefit;
			}
			
			// toggle the alternate row
			alternateRow = !alternateRow;

			write(table, rd.lname+", "+rd.fname+" "+rd.mname, alternateRow);
			write(table, rd.ssn, alternateRow);
			write(table, DateUtils.getDateFormatted(rd.policyStartDate), alternateRow);
			write(table, MoneyUtils.formatMoney(rd.monthlyCost), alternateRow);
		//	write(table, DateUtils.getDateFormatted(rd.policyEndDate), alternateRow);
			if (doingFlex)
				write(table, MoneyUtils.formatMoney(rd.amount), alternateRow);

			benAmount+=rd.amount;
			benCount++;
			count++;
			amount+=rd.amount;
			paid+=rd.monthlyCost;
		}

		addTable(table);

		writeLine (lastBenName+" count : "+benCount);
		if (doingFlex)
			writeLine (lastBenName+" amount : "+MoneyUtils.formatMoney(benAmount));

		writeLine(section+"Count: " + count);
		if (doingFlex)
			writeLine(section+"Amount: " + MoneyUtils.formatMoney(amount));
		
		if (paid>0)
			writeLine(section+"Paid: "+MoneyUtils.formatMoney(paid));
		
		totalAmount+=amount;
		totalCount+=count;
		totalPaid+=paid;
	}
	
	double totalPaid=0;
	int totalCount=0;
	int totalAmount=0;
	boolean doingFlex=false;	
	private PdfPTable writeColHeaders() throws DocumentException
	{
		PdfPTable table;
		
		if (doingFlex)
			table= makeTable(new int[]{30, 27, 27, 19,17});
		else
			table= makeTable(new int[]{30, 25, 25, 20});
		writeColHeader(table, "Paying Name", Element.ALIGN_LEFT);
		writeColHeader(table, "Paying SSN", Element.ALIGN_LEFT);
		writeColHeader(table, "Policy Start Date", Element.ALIGN_LEFT);
		
	//	writeColHeader(table, "Policy End Date", Element.ALIGN_LEFT);
	//	writeColHeader(table, "Benefit Name", Element.ALIGN_LEFT);
		
		if (doingFlex)
			writeColHeader(table, "Amount", Element.ALIGN_RIGHT);
		
		writeColHeader(table, "Amount Paid", Element.ALIGN_LEFT);
		
		return table;
	}
	
    public static void main(String args[]) {
        try {
				List ids=ArahantSession.getHSU().createCriteria(HrBenefit.class)
					.selectFields(HrBenefit.BENEFITID)
					.joinTo(HrBenefit.BENEFIT_CATEGORY)
					.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
					.list();
				
				String []ar=new String[ids.size()];
				for (int loop=0;loop<ar.length;loop++)
					ar[loop]=ids.get(loop).toString();
				
            new FlexHistoryReport().build(20070101,20071231,""/*00001-0000000001"*/, new String[]{}, true, ar,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * 	
			Query q=hsu.createQuery("select distinct bj.payingPerson.lname, bj.payingPerson.fname," +
					"bj.payingPerson.mname, bj.payingPerson.ssn, bj.amountCovered," +
					"bj.hrBenefitConfig.hrBenefit.name, bj.policyStartDate, bj.policyEndDate," +
					"bj.benefitJoinId " +
					"   from HrBenefitJoin bj where " +
					"((bj.policyStartDate < "+startDate+" and bj.policyEndDate >= "+startDate+") " +
					"or " +
					"(bj.policyStartDate >= "+startDate+" and bj.policyStartDate <= "+endDate+")) " +
					" and bj.hrBenefitConfig.hrBenefit.hrBenefitCategory.benefitType="+BHRBenefitCategory.FLEX_TYPE );
			
			List<ReportData> data=new LinkedList<ReportData>();
			
			List <String> ids=loadData(q, data);
			
			q=hsu.createQuery("select distinct bj.payingPerson.lname, bj.payingPerson.fname," +
					"bj.payingPerson.mname, bj.payingPerson.ssn, bj.amountCovered," +
					"bj.hrBenefitConfig.hrBenefit.name, bj.policyStartDate, bj.policyEndDate," +
					"bj.benefitJoinId, bj.recordChangeDate " +
					"   from HrBenefitJoinH bj where " +
					"((bj.policyStartDate < "+startDate+" and bj.policyEndDate >= "+startDate+") " +
					"or " +
					"(bj.policyStartDate >= "+startDate+" and bj.policyStartDate <= "+endDate+")) " +
					" and bj.benefitJoinId not in (:ids) " +
					" and bj.recordChangeType='D' " +
					" and bj.hrBenefitConfig.hrBenefit.hrBenefitCategory.benefitType="+BHRBenefitCategory.FLEX_TYPE+
					" order by bj.recordChangeDate ");
 */
