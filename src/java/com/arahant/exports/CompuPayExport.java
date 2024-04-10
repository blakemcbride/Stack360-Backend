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

import com.arahant.beans.ElectronicFundTransfer;
import com.arahant.beans.Employee;
import com.arahant.beans.Garnishment;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.IHrBenefitJoin;
import com.arahant.beans.WageType;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.List;


//TODO: change this to write to temporary files and then append on success that way partial files never get written
/**
 *
 * 
 */
public class CompuPayExport {
	private static final ArahantLogger logger=new ArahantLogger(CompuPayExport.class);
	private void exportDeductionsEmp(Employee emp, DelimitedFileWriter fw) throws Exception {

		String corp = emp.getOrgGroupAssociations().iterator().next().getOrgGroup().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getName();


		writeEFT(fw, "EFT1", emp, corp, (short) 1);
		writeEFT(fw, "EFT2", emp, corp, (short) 2);
		writeEFT(fw, "EFT3", emp, corp, (short) 3);
		
		List <HrBenefitConfig> l =ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.list();
		
		for (HrBenefitConfig bc : l)
		{
			if (bc.getHrBenefit().getInsuranceCode()==null || bc.getHrBenefit().getInsuranceCode().trim().equals(""))
				continue;
			
			writeBenefit(fw, bc.getBenefitConfigId(), bc.getHrBenefit().getInsuranceCode(), emp, corp);

		}
	
		writeGarnishments(fw, emp, corp);
	}

	private void exportDemogEmp(Employee emp, DelimitedFileWriter fw) throws Exception, ArahantException, Exception {
		
		String corp="";
		String unitCode="";
		String deptCode="";
		
		try
		{
			corp = emp.getOrgGroupAssociations().iterator().next().getOrgGroup().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getName();

			try
			{
				unitCode = emp.getOrgGroupAssociations().iterator().next().getOrgGroup().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getExternalId();
			}
			catch(Exception e)
			{
				//don't care
			}
			try
			{
				deptCode = emp.getOrgGroupAssociations().iterator().next().getOrgGroup().getExternalId();
			}
			catch(Exception e)
			{
				//don't care
			}

		}
		catch (Exception e)
		{
			logger.error(e);
			throw new ArahantWarning("Employee must be assigned to a valid org group for export - "+emp.getNameLFM());
		}
		
		writeField(fw, emp.getExtRef());

		writeField(fw, stripDashes(emp.getUnencryptedSsn()));	
		
		writeField(fw, corp);

		
		while (unitCode.length() < 4) {
			unitCode = "0" + unitCode;
		}
		writeField(fw, unitCode);

		while (deptCode.length() < 2) {
			deptCode = "0" + deptCode;
		}
		writeField(fw, deptCode);

		writeField(fw, emp.getNameLFM());

		BEmployee bemp = new BEmployee(emp);

		writeField(fw, bemp.getStreet());
		writeField(fw, bemp.getStreet2());
		writeField(fw, bemp.getCity());
		writeField(fw, bemp.getState());
		writeField(fw, bemp.getZip());

		writeField(fw, bemp.getHomePhone());

		//Active/Term/Hold (loa)
		String activeFlag = "A";
		//logger.info(bemp.getLastStatusHistory().getHrEmployeeStatus().getActive());
		if (bemp.getLastStatusHistory().getHrEmployeeStatus().getActive() == 'N') {
			activeFlag = "T";
		}

		if (bemp.getLastStatusHistory().getHrEmployeeStatus().getStatusId().equals("00001-0000000003")) {
			activeFlag = "I";
		}
		if (bemp.getLastStatusHistory().getHrEmployeeStatus().getStatusId().equals("00001-0000000004")) {
			activeFlag = "I";
		}
		writeField(fw, activeFlag);

		writeField(fw, (emp.getOvertimePay() == 'Y') ? "H" : "S");


		String race="";
//		logger.info(bemp.getEEORaceId());
		if (bemp.getEEORaceId().equals("00000-0000000000"))
			race="W";
		if (bemp.getEEORaceId().equals("00000-0000000001"))
			race="B";
		if (bemp.getEEORaceId().equals("00000-0000000002"))
			race="H";
		writeField(fw, race); //race not required ADS is 90% blank
		writeField(fw, bemp.getSex());

		writeField(fw, bemp.getPositionId().equals("00001-0000000001") ? "F" : "P");

		String w4status="S";
		if (bemp.getW4Status()=='M')
			w4status="M";
		writeField(fw, w4status);

		writeField(fw, emp.getPayPeriodsPerYear() + "");

		writeField(fw, emp.getExpectedHoursPerPeriod()); //hours worked per week for salary emps
		
		
		
		if (bemp.getCurrentSalaryType()==WageType.PERIOD_HOURLY)
			writeField(fw, com.arahant.utils.Formatting.formatNumber(bemp.getCurrentSalary()*80, 2));
		else
		{
			double sal=Double.parseDouble(com.arahant.utils.Formatting.formatNumber(bemp.getCurrentSalary()/26,2));
			while (sal*26<bemp.getCurrentSalary())
				sal+=.01;
			writeField(fw, com.arahant.utils.Formatting.formatNumber(sal, 2));
		}
		//rate 1
		
		//pay
		double pay;
		if (bemp.getCurrentSalaryType()==WageType.PERIOD_HOURLY)
			pay=bemp.getCurrentSalary();
		else
		{
			pay=bemp.getCurrentSalary()/2080;

			//logger.info(bemp.getCurrentSalary());
			pay=Double.parseDouble(com.arahant.utils.Formatting.formatNumber(pay, 2));
			while (pay*2080<bemp.getCurrentSalary())
				pay+=.01;
			
			//logger.info(com.arahant.utils.Formatting.formatNumber(pay*2080,2));
		}
		writeField(fw, com.arahant.utils.Formatting.formatNumber(pay, 2));
		writeField(fw, ""); //place holder
		writeField(fw, ""); //rate 2, not required
		writeField(fw, ""); //place holder
		writeField(fw, ""); //rate 3, not required
		writeField(fw, ""); //place holder
		writeField(fw, ""); //rate 4, not required
		writeField(fw, ""); //place holder
		writeField(fw, ""); //rate 5, not required
		writeField(fw, ""); //place holder
		writeField(fw, ""); //place holder
		if (bemp.getLastRaiseDate()!=bemp.getEmploymentDate())  //last raise date
			writeDate(fw, bemp.getLastRaiseDate());
		else
			writeField(fw, "");
		writeField(fw, ""); //place holder
		writeDate(fw, emp.getDob());
		writeDate(fw, bemp.getEmploymentDate());

		if (bemp.getRehireDate()>0)
			writeDate(fw, bemp.getRehireDate());
		else
			writeField(fw, "");
			
		
		if (bemp.getLastInactiveStatusHistory() != null) {
			int dt=bemp.getLastInactiveStatusHistory().getEffectiveDate();
			//if (dt!=0)
			//	dt=DateUtils.addDays(dt, 1);
			writeDate(fw, dt);
		} else {
			writeField(fw, "");
		}
		writeField(fw, ""); //Job class, they were all blank

		writeField(fw, ""); //date pay, not used
		fw.endRecord();
	}

	private void exportTaxesEmp(DelimitedFileWriter fw, Employee emp) throws Exception, Exception, ArahantException {

		writeField(fw, ""); //left blank in taxes file
		writeField(fw, stripDashes(emp.getUnencryptedSsn()));

		String corp = emp.getOrgGroupAssociations().iterator().next().getOrgGroup().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId().getName();

		writeField(fw, corp);


		writeField(fw, ""); //auto pay flag, can be blank
		writeField(fw, emp.getExtRef()); //clock number
		writeField(fw, ""); //check sequence, not required
		BEmployee bemp = new BEmployee(emp);

		writeField(fw, bemp.hasPension() ? "Y" : "N"); //pension indicator
		String w4status="S";
		if (bemp.getW4Status()=='M')
			w4status="M";
		writeField(fw, w4status);

		writeField(fw, bemp.getTaxState()); //state tax code
		writeField(fw, emp.getLocalTaxCode()); // local tax code
		writeField(fw, (emp.getEarnedIncomeCreditStatus() + "").trim()); //earned income credit
		//Employee's Earned Income Credit Status:  'I', 'J', 'W', ''  (Individual, Joint,
		//Married Filing w/o Spouse, Does Not Qualify)
		writeFit(fw, emp.getAddFederalIncomeTax(), emp.getAddFederalIncomeTaxType()); //additional FIT
		writeFit(fw, emp.getAddStateIncomeTax(), emp.getAddStateIncomeTaxType()); //additional SIT
		writeFit(fw, emp.getAddLocalIncomeTax(), emp.getAddLocalIncomeTaxType()); //additional LIT
		writeFit(fw, emp.getAddStateDisabilityTax(), emp.getAddStateDisabilityTaxType()); //additional SDI
		writeField(fw, emp.getNumberFederalExemptions() + ""); //federal exemptions
		writeField(fw, emp.getNumberStateExemptions() + ""); //state exemptions
		writeField(fw, bemp.getUnemploymentState()); //state unemployment
		writeField(fw, bemp.getPayrollBankCode()); // payroll bank or group, 4 char required
		writeField(fw, ""); //filler
		writeField(fw, emp.getFederalExtraWithhold()); //fed extra withhold
		writeField(fw, emp.getStateExtraWithhold()); //state extrawithold
		writeField(fw, ""); //filler
		writeField(fw, ""); //pay effective date, not used
		fw.endRecord();
	}

	private String stripDashes(String s)
	{
		return s.replaceAll("-", "");
	}
	
	
	private String getFilePath()
	{
		String filePath=BProperty.get("CompuPayDirectory");
	//TODO: testing filepath
	//	filePath="./";
		if (filePath==null || filePath.trim().equals(""))
			return "";
		
		if (filePath.endsWith(File.separator))
			return filePath;
		
		return filePath+File.separator;
		
	}
	
	public void exportDemog()
	{
		try {
			HibernateSessionUtil hsu=ArahantSession.getHSU();
			
			DelimitedFileWriter fw = new DelimitedFileWriter(getFilePath()+"Demog.txt",false, 40);

			HibernateScrollUtil<Employee> empScr = hsu.createCriteria(Employee.class)
					.ne(Employee.MARITAL_STATUS, ' ')
					.orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME).scroll();

			while (empScr.next()) {

				exportDemogEmp(empScr.get(), fw);
			}

			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ArahantException(ex);
		}
	}

	
	public void exportTaxes()
	{
		try {
			HibernateSessionUtil hsu=ArahantSession.getHSU();
			DelimitedFileWriter fw = new DelimitedFileWriter(getFilePath()+"Taxes.txt",false,24);


			HibernateScrollUtil<Employee> empScr = hsu.createCriteria(Employee.class)
					.ne(Employee.MARITAL_STATUS, ' ')
					.orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME).scroll();

			while (empScr.next()) {
				
				exportTaxesEmp(fw, empScr.get());
			}

			fw.close();
		} catch (Exception ex) {
			throw new ArahantException(ex);
		}
	}
	
	public void writeFit(DelimitedFileWriter fw, float taxAmt, char taxType) throws Exception
	{
		if (taxAmt>0 && taxType=='A')
				writeField(fw,taxAmt);
			else
				if (taxType=='F')
					writeField(fw,-taxAmt);
				else
					if (taxType=='N')
						writeField(fw,"-.01");
					else
						if (taxType=='P')
							writeField(fw,taxAmt/100+"");
						else
							writeField(fw,""); //additional FIT
	}
	
	
	public void exportEmployee(BEmployee bemp)			
	{
		try
		{
			if (bemp.getMaritalStatus()==null || bemp.getMaritalStatus().trim().equals(""))
				throw new ArahantWarning("Employee's marital status must not be unknown for export - "+bemp.getNameLFM()+".");
			
			DelimitedFileWriter fw = new DelimitedFileWriter(getFilePath()+"Demog.txt",true, 40);
			exportDemogEmp(bemp.getEmployee(), fw);
			fw.close();
			
			fw=new DelimitedFileWriter(getFilePath()+"Deduct.txt",true,11);
			exportDeductionsEmp(bemp.getEmployee(), fw);
			fw.close();
			
			fw = new DelimitedFileWriter(getFilePath()+"Taxes.txt",true,24);
			exportTaxesEmp(fw, bemp.getEmployee());
			fw.close();
			
		}
		catch (ArahantWarning aw)
		{
			throw aw;
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ArahantException("Failed to write export to payroll system.",e);
		}
	}
	public void exportDeductions()
	{
		try {

			HibernateSessionUtil hsu=ArahantSession.getHSU();
			DelimitedFileWriter fw=new DelimitedFileWriter(getFilePath()+"Deduct.txt", false, 11);
			//FileWriter fw = new FileWriter("Deduct.txt");
			
			HibernateScrollUtil<Employee> empScr = hsu.createCriteria(Employee.class)
					.ne(Employee.MARITAL_STATUS, ' ')
					.orderBy(Employee.LNAME)
					.orderBy(Employee.FNAME)
					.orderBy(Employee.MNAME)
					.scroll();

			while (empScr.next()) {
				
				exportDeductionsEmp(empScr.get(), fw);
				
			}

			fw.close();
		} catch (Exception ex) {
			throw new ArahantException(ex);
		}
	}
	private void writeBenefit(DelimitedFileWriter fw, String id, String code, Employee emp, String corp) throws Exception
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		
				
		HrBenefitJoin bj=hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.PAYING_PERSON, emp)
			.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, id)
			.orderByDesc(HrBenefitJoin.POLICY_START_DATE)
			.first();
		
		if (bj==null)
		{
			writeExpiredBenefit(fw,id,code,emp,corp);
			return;
		}
		
		// this needs to check to make sure they don't have another newer one in another config with the same code
		if (hasNewer(code, bj))
			return;
				
		
		writeField(fw,""); //left blank in deductions file

		writeField(fw,stripDashes(emp.getUnencryptedSsn()));


		writeField(fw,corp);


		writeField(fw,code);//Deduction code

		BEmployee bemp=new BEmployee(bj.getPayingPersonId());
		
	//	HrBenefit b=bj.getHrBenefitConfig().getHrBenefit();
	//	logger.info(b.getName()+" "+b.getEmployeeIsProvider()+" "+bj.getAmountPaidType());

		if (bj.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider()=='N')
		{
			writeField(fw,0);//Deduction Percent Amount	
			writeField(fw,bj.getHrBenefitConfig().deprecatedGetEmployeeCost()/bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		}
		else
			if (bj.getAmountPaidType()=='P')
			{
				writeField(fw,bj.getAmountPaid());//Deduction Percent Amount
				writeField(fw,0);//Deduction Flat Amount
			}
			else
			{
				writeField(fw,0);//Deduction Percent Amount
				writeField(fw,bj.getAmountPaid()/bemp.getPayPeriodsPerYear());//Deduction Flat Amount
			}
		
		if (bj.getPolicyStartDate()>=bj.getPolicyEndDate() && bj.getPolicyEndDate()!=0)
		{
			writeDate(fw, 0);
			writeDate(fw, 0);
		}
		else
		{
			writeDate(fw,bj.getPolicyEndDate());//Deduction Expiration Date
			writeDate(fw,adjustStartDate(bj.getPolicyStartDate()));//Deduction Effective Date	
		}

		writeField(fw,"");
		writeField(fw,"");
		writeField(fw,"");
	
		fw.endRecord();

	}
	
	private int adjustStartDate(int date)
	{
		//if the date is a start of the month, return last day of period
		if (date<20081214)
			return date;
		
		//TODO: come up with algorithm for this
/*		Calendar cal=DateUtils.getCalendar(20081214);
		
		while (true)
		{
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			if (date<=DateUtils.getDate(cal))
			{
				cal.add(Calendar.DAY_OF_YEAR, -5);
				return DateUtils.getDate(cal);
			}
		}
*/		
		/*
		 * 5/1/09 the date in Compupay should be 4/27/09 (the last pay ending date for the first check of the month)
6/1/09 the date in Compupay should be 5/27/09 (the last pay ending date for the first check of the month)
7/1/09 the date in Compupay should be 6/26/09 (the last pay ending date for the first check of the month)
8/1/09 the date in Compupay should be 8/1/09
9/109 the date in Compupay should be 9/1/09
10/01/09 the date in Compupay should be 10/01/09
11/01/09 the date in Compupay should be 11/01/09
12/01/09 the date in Compupay should be 12/01/09
1/1/10 the date in Compupay should be 1/1/10
		 */
		if (date==20090501)
			return 20090427;
		if (date==20090601)
			return 20090527;
		if (date==20090701)
			return 20090626;
	
		return date;
		
	}
	
	private void writeGarnishments(DelimitedFileWriter fw, Employee emp, String corp) throws Exception
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		
		
		for (Garnishment bj : hsu.createCriteria(Garnishment.class)
			.eq(Garnishment.EMPLOYEE, emp)
			.orderBy(Garnishment.PRIORITY).list())
		{

			writeField(fw,""); //left blank in deductions file

			writeField(fw,stripDashes(emp.getUnencryptedSsn()));

			writeField(fw,corp);

			writeField(fw,bj.getGarnishmentType().getWageType().getPayrollInterfaceCode());//Deduction code
			if (bj.getDeductionPercentage()>.001)
			{
				writeField(fw,bj.getDeductionPercentage());//Deduction Percent Amount
				writeField(fw,0);//Deduction Flat Amount
			}
			else
			{
				writeField(fw,0);//Deduction Percent Amount
				writeField(fw,bj.getDeductionAmount());//Deduction Flat Amount
			}

			writeDate(fw,bj.getEndDate());//Deduction Expiration Date
			writeDate(fw,bj.getStartDate());//Deduction Effective Date	


			writeField(fw,"");
			writeField(fw,"");
			writeField(fw,"");

			fw.endRecord();
		}

	}
	
	private void writeEFT(DelimitedFileWriter fw, String code, Employee emp, String corp, short seq) throws Exception {
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		
		ElectronicFundTransfer bj=hsu.createCriteria(ElectronicFundTransfer.class)
			.eq(ElectronicFundTransfer.PERSON, emp)
			.eq(ElectronicFundTransfer.SEQNO, seq)
			.first();
		
		if (bj==null)
			return;
		
		writeField(fw,""); //left blank in deductions file

		writeField(fw,stripDashes(emp.getUnencryptedSsn()));


		writeField(fw,corp);


		writeField(fw,code);//Deduction code
		if (bj.getAmountType()=='P')
		{
			writeField(fw,bj.getAmount());//Deduction Percent Amount
			writeField(fw,0);//Deduction Flat Amount
		}
		else
		{
			writeField(fw,0);//Deduction Percent Amount
			writeField(fw,bj.getAmount());//Deduction Flat Amount
		}
		
		BEmployee bemp=new BEmployee(emp);

		writeField(fw,"");//Deduction Expiration Date
		writeDate(fw,bemp.getEmploymentDate());//Deduction Effective Date	
		
	/*	
		writeField(fw,"264171270"); //dd transit routing number
		writeField(fw,"1000000000"); //dd account
		writeField(fw,"C"); //dd - checking/savings
	*/			

		writeField(fw,bj.getBankRoute()); //dd transit routing number
		writeField(fw,bj.getBankAccount()); //dd account
		writeField(fw,bj.getAccountType()+""); //dd - checking/savings

		fw.endRecord();
	}

	public static void main (String args[])
	{
		
		ArahantSession.getHSU().setCurrentPersonToArahant();
		CompuPayExport exp=new CompuPayExport();
		exp.exportDemog();
		exp.exportTaxes();
		exp.exportDeductions();
		
		//logger.info(exp.adjustStartDate(20090901));
	}

	private boolean hasNewer(String code, IHrBenefitJoin bj)
	{
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
			.gt(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate())
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.eq(HrBenefit.INSURANCE_CODE, code)
			.exists();			
	}
	
	private void writeExpiredBenefit(DelimitedFileWriter fw, String id, String code, Employee emp, String corp) throws Exception {
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		
		HrBenefitJoinH bj=hsu.createCriteria(HrBenefitJoinH.class)
			.eq(HrBenefitJoinH.PAYING_PERSON, emp)
			.eq(HrBenefitJoinH.HR_BENEFIT_CONFIG_ID, id)
			.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE,'D')
			.orderByDesc(HrBenefitJoinH.POLICY_START_DATE)
			.first();
		

		if (bj==null)
			return;
		
		//this needs to check to make sure they don't have another newer one in another config with the same code
		if (hasNewer(code, bj))
			return;
		
		
		writeField(fw,""); //left blank in deductions file

		writeField(fw,stripDashes(emp.getUnencryptedSsn()));


		writeField(fw,corp);


		writeField(fw,code);//Deduction code
		
		BEmployee bemp=new BEmployee(bj.getPayingPersonId());

		if (bj.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider()=='N')
		{
			writeField(fw,0);//Deduction Percent Amount	
			writeField(fw,bj.getHrBenefitConfig().deprecatedGetEmployeeCost()/bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		}
		else
			if (bj.getAmountPaidType()=='P')
			{
				writeField(fw,bj.getAmountPaid());//Deduction Percent Amount
				writeField(fw,0);//Deduction Flat Amount
			}
			else
			{
				writeField(fw,0);//Deduction Percent Amount
				writeField(fw,bj.getAmountPaid()/bemp.getPayPeriodsPerYear());//Deduction Flat Amount
			}
		
		
		System.out.println(bj.getPolicyStartDate());
		if (bj.getPolicyStartDate()>=bj.getPolicyEndDate() && bj.getPolicyEndDate()!=0)
		{
			writeDate(fw, 0);
			writeDate(fw, 0);
		}
		else
		{
			writeDate(fw,bj.getPolicyEndDate());//Deduction Expiration Date
			writeDate(fw,bj.getPolicyStartDate());//Deduction Effective Date	
		}

		writeField(fw,"");
		writeField(fw,"");
		writeField(fw,"");
	
		fw.endRecord();
	}

	
	//We passed certification before the quotes were taken out
	//Have to keep them just in case for now
	public void writeField(DelimitedFileWriter fw, String x) throws Exception {
        fw.writeFieldUpperCase(x);
    }

    public void writeField(DelimitedFileWriter fw, double x) throws Exception {
        x *= 100;
        x = Math.round(x);
        x /= 100;
        fw.writeFieldUpperCase(x + "");
    }

    public void writeField(DelimitedFileWriter fw, int x) throws Exception {
        fw.writeFieldUpperCase(x + "");
    }
	
	private static final String dateFmt = "yyyy/MM/dd";
	
	public void writeDate(DelimitedFileWriter fw, int x) throws Exception {
        fw.writeFieldUpperCase(DateUtils.dateFormat(dateFmt, x));
    }
}
