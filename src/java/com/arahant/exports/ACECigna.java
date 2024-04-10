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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.InsuranceLocationCode;
import com.arahant.beans.Person;
import com.arahant.beans.PersonH;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ACECigna {

	private BufferedWriter bw;

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

	private HibernateSessionUtil hsu=ArahantSession.getHSU();

	private Date lastExport;



	public void export(String filename, String vendorId, Date lastExport) throws IOException
	{
		bw=new BufferedWriter(new FileWriter(filename));

		this.lastExport=lastExport;
		//find all records

		HibernateScrollUtil <HrBenefitJoin> scr=hsu.createCriteria(HrBenefitJoin.class)
	//		.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
			.orderBy(HrBenefitJoin.PAYING_PERSON_ID)
			.orderBy(HrBenefitJoin.COVERED_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_PROVIDER)
			.eq(VendorCompany.ORGGROUPID, vendorId)
			.scroll(); //get cigna insurance

		HashSet<String> doneIds=new HashSet<String>();

		int count=0;
		while (scr.next())
		{
			HrBenefitJoin bj=scr.get();
			if (doneIds.contains(bj.getPayingPersonId()+bj.getCoveredPersonId()))
				continue;

			if (++count%50==0)
				System.out.println(count);

			doneIds.add(bj.getPayingPersonId()+bj.getCoveredPersonId());

			BPerson bp=new BPerson(bj.getPayingPerson());

			if (bj.getPayingPersonId().equals(bj.getCoveredPersonId()))
				writeEmployee(bp.getBEmployee(), new BHRBenefitJoin(bj));
			else
				writeDependent(new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId()), new BHRBenefitJoin(bj));
		}

		scr.close();
	}
	
	public void writeEmployee(BEmployee bemp, BHRBenefitJoin bj) throws IOException
	{
		write("01");//record id
		
		write("S"); //class of key S for SSN
		write(formatSSN(bemp.getSsn()), 9); //primary key
		write(" "); //filler
		
		write(bemp.getLastName(),30);
		write(bemp.getFirstName(),15);
		write(bemp.getMiddleName(),1);
		write("",3);
		
		writeDate(bemp.getDob());
		write(bemp.getSex());
		write(formatSSN(bemp.getSsn()), 9);
		
		write("S");
		write("",10); //TODO cobra survivor link
		
		
		write(formatPhone(bemp.getHomePhone()),10);
		write(formatPhone(bemp.getWorkPhone()),10);
		write("",4);
		
		write(bemp.getStreet(),30);
		write("",30);
		write(bemp.getCity(), 20);
		write(bemp.getState(), 2);
		write(bemp.getZip(), 9);
		write(bemp.getWorkAddress().getZip(),9);
		
		write(bemp.hasSpouse()?"M":"S");
		write("EE");

		String lastStatusName=bemp.getLastStatusName().toLowerCase();

		if (lastStatusName.indexOf("deceased")==-1)
		{
			write("N"); //TODO: can I get this? Deceased
			writeDate(0); //date of death
		}
		else
		{
			write("Y");
			writeDate(bemp.getLastStatusDate());
		}
		write(bemp.getHandicap()?"H":" "); //disabled or handicapped
		boolean retired=lastStatusName.indexOf("retire")!=-1;
		write(retired?"Y":"N");
		writeDate(retired?bemp.getLastStatusDate():0);

		write("S"); //prior key type
		//get the prior ssn
		PersonH p=hsu.createCriteria(PersonH.class)
			.lt(PersonH.HISTORY_DATE, lastExport)
			.eq(PersonH.PERSONID, bemp.getPersonId())
			.orderByDesc(PersonH.HISTORY_DATE)
			.first();
		String ssn=bemp.getSsn();
		if (p!=null)
			ssn=p.getSsn();
		write(formatSSN(ssn),10); //prior key value

		write("00000000",8); //salary
		write("",8); //last salary change date
		write("M"); //salary pay schedule

		writeDate(bemp.getHireDate());
		write("",8); //years of service start date
		write(bemp.getPersonalEmail(),50);
		write(bemp.getExtRef(),9);
		write(" ");//filler

	

		write("".equals(bj.getOtherInsurance().trim())?"N":"Y");
		write("",12);

		//Other carrier data
		for (int loop=0;loop<3;loop++)
		{
			write("",1);
			write("",2);
			write("",1);
			write("",8);
			write("",8);
			write("",1);
			write("",2);
			write("",15);
		}


		write(retired&&bemp.getAgeAsOf(DateUtils.now())>=65?"ERPDP":"",5); //pdp eligible

		//TODO: subsidy stuff
		write("",1);
		writeDate(0);//subsidy effective
		writeDate(0);//subsidy termination
		write("",2); //subsidy reason codes


		write("",9); //cbirt rate line code
		write ("",99); //filler

		writeMedical(bemp, bj);

		write("\r\n");
	}

	private String formatSSN(String ssn)
	{
		ssn=ssn.replaceAll("-", "");
		if (ssn.equals("999999999"))
			return "";

		return ssn;
	}


	public void writeDependent(BHREmplDependent dep, BHRBenefitJoin bj) throws IOException
	{
		write("02");//record id

		BEmployee bemp=new BEmployee(dep.getEmployee());

		write("S"); //class of key S for SSN
		write(formatSSN(bemp.getSsn()), 9); //primary key
		write(" "); //filler

		write(dep.getLastName(),30);
		write(dep.getFirstName(),15);
		write(dep.getMiddleName(),1);
		write("",3);

		writeDate(dep.getDob());
		write(dep.getSex());
		write(formatSSN(dep.getSsn()), 9);

		write("",136); //filler
		if (dep.getRelationship()=='C')
			write("CH");
		else
			if (dep.getRelationship()=='S')
				write("SP");
			else
				if (dep.getRelationship()=='O' && dep.getOtherRelationshipDescription().toLowerCase().indexOf("partner")!=-1)
					write("SP");
				else
				{
					System.out.println(dep.getEmployee().getNameLFM()+" "+dep.getNameLFM()+" "+dep.getOtherRelationshipDescription());
					write("AD");
				}


		write("N"); //Deceased
		writeDate(0);//deceased date

		write(dep.getHandicap()?"H":" ");

		write("N"); //retired
		writeDate(0); //retired date

		//get the prior ssn
		PersonH p=hsu.createCriteria(PersonH.class)
			.lt(PersonH.HISTORY_DATE, lastExport)
			.eq(PersonH.PERSONID, dep.getPersonId())
			.orderByDesc(PersonH.HISTORY_DATE)
			.first();
		String ssn=dep.getSsn();
		if (p!=null)
			ssn=p.getSsn();


		write("S"); //prior key type
		write(formatSSN(ssn),10); //prior key value


		writeDate(dep.getStudentVerificationDate());
		write(dep.getStudent()?"F":" ");


		write("",24); //filler
		write(dep.getPersonalEmail(),50);
		write("",10); //filler

		//COB data
		write("".equals(bj.getOtherInsurance().trim())?"N":"Y");
		write("",12);

		//Other carrier data
		for (int loop=0;loop<3;loop++)
		{
			write("",1);
			write("",2);
			write("",1);
			write("",8);
			write("",8);
			write("",1);
			write("",2);
			write("",15);
		}


		write("",5); //pdp eligible

		//TODO: subsidy stuff
		write("",1);
		writeDate(0);//subsidy effective
		writeDate(0);//subsidy termination
		write("",2); //subsidy reason codes


		write("",9); //cbirt rate line code
		write ("",99); //filler

		writeMedical(bemp, bj);

		write("\r\n");
	}

	private String formatPhone(String phone) {
		return phone.replaceAll("-", "").replace('(', '.').replace(')', '.').replaceAll(".", "");
	}
	private void write(String x) throws IOException {
		bw.write(x);
	}

	private void write(String x, int size) throws IOException {
		if (x==null)
			x="";
		x=x.trim();
		while (x.length()<size)
			x=x+" ";
		bw.write(x.substring(0,size));
	}

	private void writeDate(int dob) throws IOException {
		if (dob==0)
			write("",8);
		else
			write(sdf.format(DateUtils.getDate(dob)));
	}

	public void writeMedical(BEmployee bemp, BHRBenefitJoin bj) throws IOException
	{
		String acct=bj.getBean().getHrBenefitConfig().getHrBenefit().getProvider().getAccountNumber();
	//	System.out.println(acct);
		write(acct,7);
	/*	AIProperty prop=new AIProperty("CignaGroup");
		prop.setPerson(bemp.getPersonId());
		prop.loadValue();
		code=prop.getValue()
	 */
		String code="";
		InsuranceLocationCode ilc=hsu.createCriteria(InsuranceLocationCode.class)
			.eq(InsuranceLocationCode.BENEFIT_CLASS, bemp.getEmployee().getBenefitClass())
			.eq(InsuranceLocationCode.EMPLOYEE_STATUS, bemp.getLastStatusHistory().getHrEmployeeStatus())
			.eq(InsuranceLocationCode.BENEFIT, bj.getBenefitConfig().getBean().getHrBenefit())
			.first();

		if (ilc!=null)
			code=ilc.getInsLocCode();

                //System.out.println("Cobra ? " + bj.getBenefitJoinId() + " " + bj.getUsingCOBRA());
                //BHRBenefitJoin jj = new BHRBenefitJoin("00001-0000000006");
                        //System.out.println("Cobra2 ? " + bj.getPayingPersonId() + " " + jj.getUsingCOBRA());
                if (bj.getUsingCOBRA()){
                    code = "COBRA";
                }

		if ("".equals(code))
		{
                        System.out.println("CODE ***************** Employee " + bemp.getNameFML() + bemp.getPersonId());
                      
			if (bemp.getEmployee().getBenefitClass()==null)
				System.out.println("\t+++++" + bemp.getState()+" "+bemp.getCity());
			else
			{
				System.out.println("\t======" + bemp.getEmployee().getBenefitClass().getName()+"|"+bemp.getLastStatusName()+"|"+bj.getBenefitConfig().getBenefitName());
				System.out.println("\t======" +bemp.getEmployee().getBenefitClass().getBenefitClassId()+"|"+bemp.getLastStatusName()+"|"+bj.getBenefitConfig().getBenefitName());
			}
		}


		write(code,6);

		String configCode=bj.getBenefitConfig().getAdditionalInfo();

		if ("".equals(configCode))
		{
                        System.out.println("CONFIG CODE ***************** Employee " + bemp.getNameFML() + bemp.getPersonId());

			if (bemp.getEmployee().getBenefitClass()==null)
				System.out.println("\t+++++" + bemp.getState()+" "+bemp.getCity());
			else
			{
				System.out.println("\t======" + bemp.getEmployee().getBenefitClass().getName()+"|"+bemp.getLastStatusName()+"|"+bj.getBenefitConfig().getBenefitName());
				System.out.println("\t======" +bemp.getEmployee().getBenefitClass().getBenefitClassId()+"|"+bemp.getLastStatusName()+"|"+bj.getBenefitConfig().getBenefitName());
			}
		}


		if (bemp.getState().equals("CA")||bemp.getState().equals("NC"))
			configCode+="N";

		write(configCode,5);
		writeDate(Math.max(bj.getCoverageStartDate(),bj.getPolicyStartDate()));
		writeDate(bj.getCoverageEndDate());

		if (bemp.getPersonId().equals(bj.getCoveredPersonId()))
		{
			if (bj.getCoverageStartDate()>0 && bj.getCoverageEndDate()<=DateUtils.now())
				write("Y");
			else
				write("N");

			boolean spouse=false;
			boolean child=false;
			boolean other=false;

			List<HrBenefitJoin> depJoins=bj.getActiveDependentBenefitJoins();
			//HrEmplDependent spouse=bemp.getActiveSpouse(DateUtils.now());
			for (HrBenefitJoin dbj : bj.getActiveDependentBenefitJoins())
			{
				HrEmplDependent relat=dbj.getRelationship();
				if (relat==null)
				{
					
					HibernateCriteriaUtil<HrEmplDependent> hcu=
							hsu.createCriteria(HrEmplDependent.class);
					hcu.joinTo(HrEmplDependent.EMPLOYEE).eq(Employee.PERSONID, dbj.getPayingPersonId());
					hcu.joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, dbj.getCoveredPersonId());
					relat=hcu.first();

					if (relat==null)
						System.out.println(dbj.getPayingPersonId()+"  "+dbj.getCoveredPersonId());
				}
				if (relat==null)
					continue;
				if (relat.getRelationshipType()==HrEmplDependent.TYPE_SPOUSE)
					spouse=true;
				if (relat.getRelationshipType()==HrEmplDependent.TYPE_CHILD)
					child=true;
				if (relat.getRelationshipType()==HrEmplDependent.TYPE_OTHER)
					other=true;

			}

			write(spouse?"Y":"N");
			write(child?"Y":"N");
			write(other?"Y":"N");

			if (depJoins.size()==0)
				write("1");
			if (depJoins.size()==1)
				write("2");
			if (depJoins.size()>1)
				write("N");


		}
		else
		{
			write("",5);
		}

		if (bj.getUsingCOBRA())
		{
			writeDate(bj.getPolicyStartDate());
			writeDate(bj.getPolicyEndDate());
		}
		else
		{
			writeDate(0);
			writeDate(0);
		}

		writeDate(0); //date deductible amount becomes active
		write("",6); //dollar amount of deductible


		write("",3); //HMO code
		write("",5); //network ID
		write("",10); //PCP ID

		write("N",1); //established patient indicator
		writeDate(0); //PCP effective date
		writeDate(0); //pre existing condition end date
		write("",46); //filler
	}

	public static void main(String[] args)
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			ACECigna ace = new ACECigna();

			//ace.loadFloridaData();

			ace.export("testAce.txt", "00001-0000000042",new Date());

			ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(ACECigna.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void loadFloridaData() throws Exception
	{
		HibernateScrollUtil<Employee> scr=hsu.createCriteria(Employee.class)
			.scroll();

		while (scr.next())
		{
			BEmployee bemp=new BEmployee(scr.get());
			String code = getPayrollCode(bemp.getExtRef().trim()).trim();
			//System.out.println("code is "+code);
			if (code.equalsIgnoreCase("fd4")) {
				bemp.setBenefitClassId("00001-0000000008");
				bemp.update();
			}
			if (code.equalsIgnoreCase("rms"))
			{
				bemp.setBenefitClassId("00001-0000000009");

				bemp.update();
			}
		}
	}


	public String getPayrollCode(String empid) throws Exception {
		if (empid.trim().equals("")) {
			return "";
		}
		if (empid.startsWith("\"")) {
			empid = empid.substring(1);
		}
		if (empid.endsWith("\"")) {
			empid = empid.substring(0, empid.length() - 1);
		}

		DelimitedFileReader dfr = new DelimitedFileReader("/Users/Arahant/FrankCrystal/payrollcompanyimport3.csv", '\t', '\"');

		String ret = "";
		while (dfr.nextLine()) {
			String id = dfr.getString(0).substring(1).trim();

			String v = "";

			for (int loop = 0; loop < id.length(); loop++) {
				if (id.charAt(loop) >= '0' && id.charAt(loop) <= '9') {
					v += id.charAt(loop);
				}
			}

			id = v;
			//	System.out.println(id.length());
			int i1 = Integer.parseInt(id);
			int i2 = Integer.parseInt(empid.trim());

			if (i1 == i2) {
				ret = stripWackyStuff(dfr.getString(1).trim());
				break;
			}
		}

		if ("".equals(ret)) {
			System.out.println("did not find " + empid.trim());
		}
		return ret;
	}

	public String stripWackyStuff(String x) {
		String v = "";
		for (int loop = 0; loop < x.length(); loop++) {
			if (x.charAt(loop) != '\"') {
				v += x.charAt(loop);
				v = v.trim();
			}
		}

		return v;
	}
}
/*
  update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Miramar');
  update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Miami');
 update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Southwest Ranches');
 update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Tampa');
 update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='West Palm Beach');
 update employee set benefit_class_id ='00001-0000000002' where person_id in (select person_join from address where state='MD');
update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='North Palm Beach');
 update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Haverhill');
 update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Jupiter');
 update employee set benefit_class_id ='00001-0000000001' where person_id in (select person_join from address where state='PA');
 update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Coral Springs');
update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Deerfield Beach');
update employee set benefit_class_id ='00001-0000000008' where person_id in (select person_join from address where city='Coral Gables');
 
  update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Tequesta');
 update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Debary');
 update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Boca Raton');

  update employee set benefit_class_id ='00001-0000000002' where person_id in (select person_join from address where state='IL');

 //added by Kalvin
 update employee set benefit_class_id ='00001-0000000002' where person_id in (select person_join from address where city='East Hampton');
update employee set benefit_class_id ='00001-0000000009' where person_id in (select person_join from address where city='Moorestown');
update employee set benefit_class_id ='00001-0000000001' where person_id in (select person_join from address where city='Katy');
update employee set benefit_class_id ='00001-0000000002' where person_id in (select person_id from person where lname='Mckeon');
 *
 *
 */
