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


package com.arahant.imports;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */

public class GenericFileImport {

	private static final boolean COULD_ALREADY_BE_THERE=false;
	public static final String BENEFIT_NAME="Benefit Name";
	public static final String BENEFIT_COVERAGE="Benefit Coverage";
	public static final String SUBSCRIBER_SSN="Subscriber SSN";
	public static final String SUBSCRIBER_FULL_NAME="Subscriber Full Name";
	public static final String FILTER_COLUMN="Filter";
	public static final String ENROLL_SSN="Enrollee SSN";
	public static final String ENROLL_LNAME="Enrollee Last Name";
	public static final String ENROLL_FNAME="Enrollee First Name";
	public static final String ENROLL_MNAME="Enrollee Middle Name";
	public static final String ENROLL_RELATIONSHIP="Enrollee Relationship";
	public static final String ENROLL_DOB="Enrollee DOB";
	public static final String ENROLL_COVERAGE_START="Enrollee Coverage Start";
	public static final String ENROLL_COVERAGE_END="Enrollee Coverage End";
	public static final String ENROLL_CITY="Enrollee City";
	public static final String ENROLL_STREET1="Enrollee Street 1";
	public static final String ENROLL_STREET2="Enrollee Street 2";
	public static final String ENROLL_STATE="Enrollee State";
	public static final String ENROLL_ZIP="Enrollee Zip";

	public static final String COMPANY_REF="Company Reference";

	private static final String[]benefitColumns=new String[]{BENEFIT_NAME, BENEFIT_COVERAGE};

	private static final short MAX_BENEFITS=4;
	private static final short MAX_ENROLLEES=10;

	private static final String[]enrolleeColumns=new String[]{ENROLL_SSN,ENROLL_LNAME,ENROLL_FNAME,ENROLL_MNAME,
		ENROLL_RELATIONSHIP,ENROLL_DOB,ENROLL_COVERAGE_START,ENROLL_COVERAGE_END,ENROLL_CITY,ENROLL_STREET1,
		ENROLL_STREET2,ENROLL_STATE,ENROLL_ZIP};


	public static final String availableColumns[];

	static
	{
		int index=0;
		availableColumns=new String[benefitColumns.length*MAX_BENEFITS+enrolleeColumns.length*MAX_ENROLLEES+3];
		//availableColumns[index++]=FILTER_COLUMN;
		availableColumns[index++]=COMPANY_REF;
		availableColumns[index++]=SUBSCRIBER_SSN;
		availableColumns[index++]=SUBSCRIBER_FULL_NAME;


		for (int loop=0;loop<MAX_BENEFITS;loop++)
			for (int bloop=0;bloop<benefitColumns.length;bloop++)
				availableColumns[index++]=benefitColumns[bloop]+" "+(loop+1);
		
		for (int loop=0;loop<MAX_ENROLLEES;loop++)
			for (int eloop=0;eloop<enrolleeColumns.length;eloop++)
				availableColumns[index++]=enrolleeColumns[eloop]+" "+(loop+1);
	}



	public void importFile(String filename, ImportType type) throws Exception
	{
		ArahantSession.setFastKeys(true);
		ArahantSession.getHSU().noAutoFlush();
		
		switch (type.getFileFormat())
		{
			case ImportType.DELIMITED_FILE : importDelimited(filename, type);
				break;
			case ImportType.FIXED_LENGTH_FILE : importFixedLength(filename, type);
				break;
			default:
				throw new ArahantException("Unknown import file type found.");
		}
		ArahantSession.setFastKeys(false);

	}

	private ImportColumn getColumn(ImportFilter recordSet, String columnName) {
		for (ImportColumn c : recordSet.getColumns())
			if (c.getColumnName().equals(columnName))
				return c;
		return null;
		//throw new ArahantException("Unknown column name requested in importer '"+columnName+"'.");
	}

	private void buildSample() {

		BImportType ft=new BImportType();
		ft.create();
		ft.setImportName("Test");
		ft.setDelimiter('\t');
		ft.setQuote('"');
		ft.setType(ImportType.DELIMITED_FILE);
		ft.setImportProgramName("DRC Benefit Import");
		ft.setFilterStartPos(0);
		ft.setFilterEndPos(-1);
		ft.insert();


		BImportFilter bif=new BImportFilter();
		bif.create();
		bif.setImportFilterDesc("Record Set 1");
		bif.setImportFilterName("RS1");
		bif.setImportTypeId(ft.getImportFileTypeId());
		bif.setFilterValue("5");
		


		bif.addColumn(COMPANY_REF,1);
		bif.addColumn(SUBSCRIBER_SSN,3);
		bif.addColumn(ENROLL_LNAME+" 1",8);
		bif.addColumn(ENROLL_FNAME+" 1",6);
		bif.addColumn(ENROLL_MNAME+" 1",7);
		bif.addColumn(ENROLL_SSN+" 1",3);

		bif.addColumn(ENROLL_RELATIONSHIP+" 1",5);
	//	ft.addColumn(ENROLL_DOB,9);
	//	ft.addColumn(ENROLL_COVERAGE_START,13);
	//	ft.addColumn(ENROLL_COVERAGE_END,14);

		bif.addColumn(BENEFIT_NAME+" 1",16);
		bif.addColumn(BENEFIT_NAME+" 2",17);
		bif.addColumn(BENEFIT_NAME+" 3",18);
		bif.addColumn(BENEFIT_NAME+" 4",19);

		
		bif.insert();

	}




	public String getValue(DelimitedFileReader dfr, ImportColumn col)
	{
		try {
			if (col.getDateFormat() == null || col.getDateFormat().trim().equals("")) {
				String x=dfr.getString(col.getStartPos()-1);
//				if (x.equals(col.getDiscriminatorValue()) && col.getDiscriminatorColumn()=='N')
//					x="";
			//	System.out.println(col.getColumnName()+"="+x);
				return x.trim();
			}

			SimpleDateFormat sdf = new SimpleDateFormat(col.getDateFormat());
			String date=dfr.getString(col.getStartPos()-1);
			if (isEmpty(date))
				return "0";
			if (date.trim().equals("NA"))
				return "0";
			return DateUtils.getDate(sdf.parse(date)) + "";
		} catch (ParseException ex) {
			Logger.getLogger(GenericFileImport.class.getName()).log(Level.SEVERE, null, ex);
			return "0";
		}
	}

	private void importDelimited(String filename, ImportType type) throws Exception {


		HibernateSessionUtil hsu=ArahantSession.getHSU();
		int count=0;
		HashMap<String,String> groupToCompany=new HashMap<String, String>();
		System.out.println("Pre load company details");
		for (CompanyDetail dt : hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).list())
		{
			if (++count%50==0)
				System.out.println("Loaded "+count+" companies");

		//	System.out.println("'"+dt.getExternalId()+"' = "+dt.getOrgGroupId());
			groupToCompany.put(dt.getExternalId(), dt.getOrgGroupId());
		}



		for (ImportFilter recordSet : type.getFilters())
		{

			char delim=type.getDelimChar();
			if (delim=='T')
				delim='\t';
			DelimitedFileReader dfr=new DelimitedFileReader(filename, delim, type.getQuoteChar());

			count=0;

			ImportColumn companyRefColumn=null;

			if (ArahantSession.multipleCompanySupport)
			{
				//get the current company
				companyRefColumn=hsu.createCriteria(ImportColumn.class)
					.eq(ImportColumn.COLUMN_NAME, COMPANY_REF)
					.joinTo(ImportColumn.IMPORT_FILTER)
					.eq(ImportFilter.IMPORT_TYPE, type)
					.first();
			}


			int bcount=0;
			//if multiple benefits are mapped, then it's a multibenefit file
			for (int loop=1;loop<=MAX_BENEFITS;loop++)
			{
				ImportColumn c=getColumn(recordSet, BENEFIT_NAME+" "+loop);//checking the main required column
				if (c!=null)
					bcount++;
			}
			boolean isMultiBenefitFile=bcount>1;


			int ecount=0;
			//if multiple benefits are mapped, then it's a multibenefit file
			for (int loop=1;loop<=MAX_ENROLLEES;loop++)
			{
				ImportColumn c=getColumn(recordSet, ENROLL_LNAME+" "+loop);//checking the main required column
				if (c!=null)
					ecount++;
			}
			boolean isMultiEnrolleeFile=ecount>1;


			while (dfr.nextLine())
			{

				if (++count%50==0)
				{
					System.out.println(count);
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				//check descriminator first
				if (type.getFilterStartPos()!=0)
				{
					//System.out.println(recordSet.getFilterValue()+"="+dfr.getString(type.getFilterStartPos()-1));
					if (!recordSet.getFilterValue().equals(dfr.getString(type.getFilterStartPos()-1)))
					{
						continue;
					}
				}


			//	System.out.println(count);

				if (companyRefColumn!=null)
				{
					String ref=dfr.getString(companyRefColumn.getStartPos()-1);
					try
					{
						int x=Integer.parseInt(ref);
						ref=x+"";
					}
					catch (Exception e)
					{
						//not using a numeric ref
						ref=dfr.getString(companyRefColumn.getStartPos()-1);
					}

					String companyId=groupToCompany.get(ref);

				//	System.out.println("ref is "+ref+" company id is "+companyId);

					if (!hsu.getCurrentCompany().getOrgGroupId().equals(companyId))
						hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));
				}

				//get the subscriber ssn
				String subscriberSSN=fixSSN(getValue(dfr, getColumn(recordSet,SUBSCRIBER_SSN)));

			//	System.out.println(subscriberSSN);

				//get all benefits
				HashSet<HashMap<String,String>> benefits=new HashSet<HashMap<String,String>>();

				for (int loop=1;loop<=MAX_BENEFITS;loop++)
				{
					HashMap<String, String> currentBenefit=new HashMap<String, String>();
					benefits.add(currentBenefit);
					for (int bloop=0;bloop<benefitColumns.length;bloop++)
					{
						ImportColumn c=getColumn(recordSet,benefitColumns[bloop]+" "+loop);
						if (c==null)
							continue;
						if (c.getStartPos()<0)
							continue;
						String val=getValue(dfr, c);
						String name=benefitColumns[bloop];
						currentBenefit.put(name, val);
					}
				}


				HashSet<HashMap<String,String>> enrollees=new HashSet<HashMap<String,String>>();
				for (int loop=1;loop<=MAX_ENROLLEES;loop++)
				{
					HashMap<String, String> currentEnrollee=new HashMap<String, String>();
					enrollees.add(currentEnrollee);
					for (int bloop=0;bloop<enrolleeColumns.length;bloop++)
					{
						ImportColumn c=getColumn(recordSet,enrolleeColumns[bloop]+" "+loop);
						if (c==null)
							continue;
					//	System.out.println(c.getColumnName()+' '+c.getStartPos());
						String val=getValue(dfr, c);
						String name=enrolleeColumns[bloop];

						if (name.equals(ENROLL_SSN))
							val=fixSSN(val);


						if (name.equals(ENROLL_RELATIONSHIP))
							val=fixRelationship(val);

					//	System.out.println(name+" "+val);
						currentEnrollee.put(name, val);
					}
				}


				


				//now I have a bunch of enrollees and benefits to put in
				//Benefits is the master file
				HashSet<String> usedBenefitIds=new HashSet<String>();
				HashSet<String> enrolledIds=new HashSet<String>();
				HashSet<ImportedEnrollee> enrolleeObjs=new HashSet<ImportedEnrollee>();

				for (HashMap<String, String> beneMap : benefits)
				{
					if (isEmpty(beneMap.get(BENEFIT_NAME))||"NA".equals(beneMap.get(BENEFIT_NAME)))
						continue;

					//if this benefit exists, change the record
					ImportedBenefit b=hsu.createCriteria(ImportedBenefit.class)
						.eq(ImportedBenefit.NAME, beneMap.get(BENEFIT_NAME))
						.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
						.first();

					BImportBenefit cb;
					if (b==null)
					{
						//make a new one
						cb=new BImportBenefit();
						cb.create();
						cb.setType(type);
						populateBenefit(cb,beneMap);
						cb.insert();
					}
					else
					{
						cb=new BImportBenefit(b);
						populateBenefit(cb,beneMap);
						cb.update();
					}

					usedBenefitIds.add(cb.getId());
					BImportedEnrollee sponsor=null;

				/*	for (HashMap<String,String> en : enrollees)
				{
					for (String k : en.keySet())
						System.out.println(k+"="+en.get(k));
				}
				 * */


					//find the EE
					for (HashMap<String,String> enrl : enrollees)
					{
					//	System.out.println(enrl.get(ENROLL_RELATIONSHIP));
					//	System.out.println(ImportedEnrollee.SSN);
					//	System.out.println(subscriberSSN+" "+enrl.get(ENROLL_SSN));
						if (!"EE".equals(enrl.get(ENROLL_RELATIONSHIP))) //just sponsor
							continue;
						
						if (isEmpty(enrl.get(ENROLL_SSN)))
							enrl.put(ENROLL_SSN, subscriberSSN);

						ImportedEnrollee e=hsu.get(ImportedEnrollee.class, enrl.get("ENROLLEE_ID"));

						//see if enrollee exists
						if (e==null)
							e=hsu.createCriteria(ImportedEnrollee.class)
								.eq(ImportedEnrollee.SSN, subscriberSSN)
								.eq(ImportedEnrollee.RELATIONSHIP, "EE")
								.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
								.first();

						if (e==null)
						{
							sponsor=new BImportedEnrollee();
							enrl.put("ENROLLEE_ID",sponsor.create());
							populateEnrollee(sponsor, enrl, cb, sponsor);
							sponsor.insert();
						}
						else
						{
							sponsor=new BImportedEnrollee(e);
							populateEnrollee(sponsor, enrl, cb, sponsor);
							sponsor.update();
						}

						enrolleeObjs.add(sponsor.getBean());

					}

					if (sponsor==null)
					{
					//	System.out.println(subscriberSSN);
						//find them from database
						ImportedEnrollee e=hsu.createCriteria(ImportedEnrollee.class)
							.eq(ImportedEnrollee.SSN, subscriberSSN)
							.eq(ImportedEnrollee.RELATIONSHIP, "EE")
							.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
							.first();
						if (e!=null)
							sponsor=new BImportedEnrollee(e);

					}
					//do the enrollees

					for (HashMap<String,String> enrl : enrollees)
					{
						
						if ("EE".equals(enrl.get(ENROLL_RELATIONSHIP))) //already did sponsor
							continue;

						if (isEmpty(enrl.get(ENROLL_FNAME)))
							continue; //must have at least first name


						ImportedEnrollee e=hsu.get(ImportedEnrollee.class, enrl.get("ENROLLEE_ID"));

						//see if enrollee exists
						if (e==null)
							e=hsu.createCriteria(ImportedEnrollee.class)
								.eq(ImportedEnrollee.SSN, enrl.get(ENROLL_SSN))
								.eq(ImportedEnrollee.LNAME, enrl.get(ENROLL_LNAME))
								.eq(ImportedEnrollee.FNAME, enrl.get(ENROLL_FNAME))
								.eq(ImportedEnrollee.MNAME, enrl.get(ENROLL_MNAME))
								.eq(ImportedEnrollee.RELATIONSHIP, enrl.get(ENROLL_RELATIONSHIP))
								.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
								//.eq(ImportedEnrollee.DOB, DateUtils.enrl.get(ENROLL_DOB)) parse date
								.first();

						BImportedEnrollee bie;

						if (e==null)
						{
							bie=new BImportedEnrollee();
							enrl.put("ENROLLEE_ID",bie.create());
							populateEnrollee(bie, enrl, cb, sponsor);
							bie.insert();
						}
						else
						{
							bie=new BImportedEnrollee(e);
							populateEnrollee(bie, enrl, cb, sponsor);
							bie.update();
						}

						enrolleeObjs.add(bie.getBean());
					}

					if (isMultiEnrolleeFile)
					{
						//delete enrollees not used for every benefit on this row
	
						ImportedBenefit ib=hsu.createCriteria(ImportedBenefit.class)
							.eq(ImportedBenefit.NAME, beneMap.get(BENEFIT_NAME))
							.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
							.joinTo(ImportedBenefit.BENEFIT_JOINS)
							.eq(ImportBenefitJoin.SPONSOR, sponsor.getBean())
							.first();

						if (ib!=null)
						{
							//find any enrollees for this benefit that I didn't do
							for (ImportedEnrollee ie : hsu.createCriteria(ImportedEnrollee.class)
								.joinTo(ImportedEnrollee.BENEFIT_JOINS)
								.eq(ImportBenefitJoin.BENEFIT, ib)
								.notIn(ImportedEnrollee.ID, enrolledIds)
								.list())
							new BImportedEnrollee(ie).unEnroll(new BImportBenefit(ib));
						}

					}

				}

				if (isMultiBenefitFile)
				{
					for (ImportedEnrollee e : enrolleeObjs)
					{

						//delete any benefits no longer associated to the subscriber in this type file
						for (ImportedBenefit ib : hsu.createCriteria(ImportedBenefit.class)
							.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
							.notIn(ImportedBenefit.ID, usedBenefitIds)
							.joinTo(ImportedBenefit.BENEFIT_JOINS)
							.eq(ImportBenefitJoin.ENROLLEE, e)
							.list())
							new BImportedEnrollee(e).unEnroll(new BImportBenefit(ib));
						
					}
				}

			}
		}
	}

	private HashMap<String,HashMap<String, ImportedBenefit>> benefitsMap=new HashMap<String, HashMap<String, ImportedBenefit>>();

	private HashSet<String> configSet=new HashSet<String>();

	private void importFixedLength(String filename, ImportType type) throws Exception {

		HibernateSessionUtil hsu=ArahantSession.getHSU();
		int count=0;
		HashMap<String,String> groupToCompany=new HashMap<String, String>();
		System.out.println("Pre load company details");
		for (CompanyDetail dt : hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).list())
		{
			if (++count%50==0)
				System.out.println("Loaded "+count+" companies");

	//		System.out.println("'"+dt.getExternalId()+"' = "+dt.getOrgGroupId());
			groupToCompany.put(dt.getExternalId(), dt.getOrgGroupId());
		}



		for (ImportFilter recordSet : type.getFilters())
		{

			BufferedReader fr=new BufferedReader(new FileReader(filename));

			count=0;

			ImportColumn companyRefColumn=null;

			if (ArahantSession.multipleCompanySupport)
			{
				//get the current company
				companyRefColumn=hsu.createCriteria(ImportColumn.class)
					.eq(ImportColumn.COLUMN_NAME, COMPANY_REF)
					.joinTo(ImportColumn.IMPORT_FILTER)
					.eq(ImportFilter.IMPORT_TYPE, type)
					.first();
			}


			int bcount=0;
			//if multiple benefits are mapped, then it's a multibenefit file
			for (int loop=1;loop<=MAX_BENEFITS;loop++)
			{
				ImportColumn c=getColumn(recordSet, BENEFIT_NAME+" "+loop);//checking the main required column
				if (c!=null)
					bcount++;
			}
			boolean isMultiBenefitFile=bcount>1;


			int ecount=0;
			//if multiple benefits are mapped, then it's a multibenefit file
			for (int loop=1;loop<=MAX_ENROLLEES;loop++)
			{
				ImportColumn c=getColumn(recordSet, ENROLL_LNAME+" "+loop);//checking the main required column
				if (c!=null)
					ecount++;
			}
			boolean isMultiEnrolleeFile=ecount>1;

			HashMap<String, ImportedEnrollee> ieMap=new HashMap<String, ImportedEnrollee>();


			HashMap<String, ImportedBenefit> defaultMap=new HashMap<String, ImportedBenefit>();
			HashSet<String> usedBenefitIds=new HashSet<String>();
			HashSet<String> enrolledIds=new HashSet<String>();
			HashSet<ImportedEnrollee> enrolleeObjs=new HashSet<ImportedEnrollee>();


			String line=null;
			while ((line=fr.readLine())!=null)
			{

				if (++count%50==0)
				{
					System.out.println(count);
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				//check descriminator first
				if (type.getFilterStartPos()!=0)
				{
					if (!recordSet.getFilterValue().equals(line.substring(type.getFilterStartPos()-1, type.getFilterEndPos())))
					{
						continue;
					}
				}


				//System.out.println(count);
				HashMap<String,ImportedBenefit> benefitMap;
				if (companyRefColumn!=null)
				{
					String ref=getValue(line,companyRefColumn);
					try
					{
						int x=Integer.parseInt(ref);
						ref=x+"";
					}
					catch (Exception e)
					{
						//not using a numeric ref
						ref=getValue(line,companyRefColumn);
					}

					String companyId=groupToCompany.get(ref);

			//		System.out.println("ref is "+ref+" company id is "+companyId+" for "+getValue(line,getColumn(recordSet, SUBSCRIBER_FULL_NAME)));

					if (!hsu.getCurrentCompany().getOrgGroupId().equals(companyId))
						hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));
					benefitMap=benefitsMap.get(companyId);
					if (benefitMap==null)
					{
						benefitMap=new HashMap<String, ImportedBenefit>();
						benefitsMap.put(companyId, benefitMap);
					}

				}
				else
					benefitMap=defaultMap;

// test code
//			if (!hsu.getCurrentCompany().getOrgGroupId().equals("00001-0000067403"))
//					continue;

				//get the subscriber ssn
				String subscriberSSN=fixSSN(getValue(line, getColumn(recordSet,SUBSCRIBER_SSN)));

				Name subscriberName=null;
				if (null!=getColumn(recordSet, SUBSCRIBER_FULL_NAME))
				{
					subscriberName=new Name(getValue(line,getColumn(recordSet, SUBSCRIBER_FULL_NAME)));
				}

			//	System.out.println(subscriberSSN);

				//get all benefits
				HashSet<HashMap<String,String>> benefits=new HashSet<HashMap<String,String>>();
//System.out.println("Load benefit data");
				for (int loop=1;loop<=MAX_BENEFITS;loop++)
				{
					HashMap<String, String> currentBenefit=new HashMap<String, String>();
					benefits.add(currentBenefit);
					for (int bloop=0;bloop<benefitColumns.length;bloop++)
					{
						ImportColumn c=getColumn(recordSet,benefitColumns[bloop]+" "+loop);
						if (c==null)
							continue;
						String val=getValue(line, c);
						String name=benefitColumns[bloop];
						currentBenefit.put(name, val);
					}
				}

				//Check to see if these benefits are set up in the system
				for (HashMap<String,String> bene : benefits)
				{
					String name=bene.get(BENEFIT_NAME);

					if (isEmpty(name))
						continue;

					String conCode=bene.get(BENEFIT_COVERAGE);

					if (configSet.contains(hsu.getCurrentCompany().getOrgGroupId()+name+conCode))
						continue;

					String configName=name;

					if ("0100".equals(conCode))
						configName+=" Single";
					else
						if ("0200".equals(conCode))
							configName+=" Double";
						else
							if ("0300".equals(conCode))
								configName+=" Family";
							else
								configName+=" "+conCode;


					//look for hr benefit with name
					HrBenefitConfig conf=hsu.createCriteria(HrBenefitConfig.class)
							.eq(HrBenefitConfig.INSURANCE_CODE,name+conCode)
							.first();
					
					if (conf==null)
					{

						HrBenefit b=hsu.createCriteria(HrBenefit.class)
							.eq(HrBenefit.INSURANCE_CODE, name)
							.first();

						if (b==null)
						{
							BHRBenefit bb=new BHRBenefit();
							bb.create();
							bb.setName(name);
							bb.setInsuranceCode(name);
							WageType wt=hsu.createCriteria(WageType.class).eq(WageType.NAME,"Insurance").first();
							if (wt==null)
							{
								BWageType bwt=new BWageType();
								bwt.create();
								bwt.setName("Insurance");
								bwt.setIsDeduction(true);
								bwt.setType(999);
								bwt.setPeriodType(WageType.PERIOD_ONE_TIME);
								bwt.insert();
								wt=bwt.getBean();
							}
							bb.setWageType(wt);
							HrBenefitCategory cat=hsu.createCriteria(HrBenefitCategory.class)
									.eq(HrBenefitCategory.DESCRIPTION, "Import Use Only")
									.first();
							if (cat==null)
							{
								BHRBenefitCategory bbcat=new BHRBenefitCategory();
								bbcat.create();
								bbcat.setDescription("Import Use Only");
								bbcat.setTypeId(HrBenefitCategory.HEALTH);
								bbcat.setAllowsMultipleBenefits(true);
								bbcat.setRequiresDecline(false);
								bbcat.insert();
								cat=bbcat.getBean();
							}
							bb.setBenefitCategoryId(cat.getBenefitCatId());
							bb.insert();
						//	hsu.flush();
							b=bb.getBean();
						}

						BHRBenefitConfig bcon=new BHRBenefitConfig();
						bcon.create();
						bcon.setName(configName);
						bcon.setBenefitId(b.getBenefitId());
						bcon.setInsuranceCode(name+conCode);
						bcon.insert();
					}

					bene.put(BENEFIT_NAME, name+conCode);
					configSet.add(hsu.getCurrentCompany().getOrgGroupId()+name+conCode);
				}

//System.out.println("Load enrollee data");
				HashSet<HashMap<String,String>> enrollees=new HashSet<HashMap<String,String>>();

				for (int loop=1;loop<=MAX_ENROLLEES;loop++)
				{
					HashMap<String, String> currentEnrollee=new HashMap<String, String>();
					enrollees.add(currentEnrollee);
					if (loop==1 && subscriberName!=null)
					{
						currentEnrollee.put(ENROLL_LNAME, subscriberName.lname);
						currentEnrollee.put(ENROLL_FNAME, subscriberName.fname);
						currentEnrollee.put(ENROLL_MNAME, subscriberName.mname);
					}

					for (int bloop=0;bloop<enrolleeColumns.length;bloop++)
					{

						ImportColumn c=getColumn(recordSet,enrolleeColumns[bloop]+" "+loop);
						if (c==null)
							continue;
					//	System.out.println(c.getColumnName()+' '+c.getStartPos());
						String val=getValue(line, c);
						String name=enrolleeColumns[bloop];

						if (name.equals(ENROLL_SSN))
							val=fixSSN(val);

						if (name.equals(ENROLL_RELATIONSHIP))
							val=fixRelationship(val);
//System.out.println(name+" "+loop+" = "+val);
						
					//	System.out.println(name+" "+val);
						currentEnrollee.put(name, val);
					}
				}

				
				for (HashMap<String,String> enrl : enrollees)
				{
					//	System.out.println(ImportedEnrollee.SSN);
					//	System.out.println(subscriberSSN+" "+enrl.get(ENROLL_SSN));
						if (enrl.get(ENROLL_RELATIONSHIP)==null) //just sponsor
							enrl.put(ENROLL_RELATIONSHIP,"EE");
				}

				//now I have a bunch of enrollees and benefits to put in
				//Benefits is the master file

				usedBenefitIds.clear();
				enrolledIds.clear();
				enrolleeObjs.clear();
				ieMap.clear();

				for (HashMap<String, String> beneMap : benefits)
				{
					if (isEmpty(beneMap.get(BENEFIT_NAME))||"NA".equals(beneMap.get(BENEFIT_NAME)))
						continue;

//System.out.println("Looking for benefit "+beneMap.get(BENEFIT_NAME));

					ImportedBenefit b=benefitMap.get(beneMap.get(BENEFIT_NAME));

					//if this benefit exists, change the record
					if (b==null&&COULD_ALREADY_BE_THERE)
						b=hsu.createCriteria(ImportedBenefit.class)
							.eq(ImportedBenefit.NAME, beneMap.get(BENEFIT_NAME))
							.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
							.first();

//System.out.println("query done");
					BImportBenefit cb;
					if (b==null)
					{
						//make a new one
						cb=new BImportBenefit();
						cb.create();
						cb.setType(type);
						populateBenefit(cb,beneMap);
						cb.insert();
					}
					else
					{
						cb=new BImportBenefit(b);
						populateBenefit(cb,beneMap);
						cb.update();
					}

					benefitMap.put(beneMap.get(BENEFIT_NAME), cb.getBean());

					usedBenefitIds.add(cb.getId());
					BImportedEnrollee sponsor=null;

				/*	for (HashMap<String,String> en : enrollees)
				{
					for (String k : en.keySet())
						System.out.println(k+"="+en.get(k));
				}
				 * */

//System.out.println("find ee");
					//find the EE
					for (HashMap<String,String> enrl : enrollees)
					{
//System.out.println("checking enrollee");
					//	System.out.println(subscriberSSN+" "+enrl.get(ENROLL_SSN));
						if (!"EE".equals(enrl.get(ENROLL_RELATIONSHIP))) //just sponsor
							continue;

						if (isEmpty(enrl.get(ENROLL_SSN)))
							enrl.put(ENROLL_SSN, subscriberSSN);
						
						ImportedEnrollee e=null;

						e=ieMap.get(enrl.get("ENROLLEE_ID"));

						if (e==null&&COULD_ALREADY_BE_THERE)
							e=hsu.get(ImportedEnrollee.class, enrl.get("ENROLLEE_ID"));

						//see if enrollee exists
						if (e==null&&COULD_ALREADY_BE_THERE)
							e=hsu.createCriteria(ImportedEnrollee.class)
								.eq(ImportedEnrollee.SSN, enrl.get(ENROLL_SSN))
								.eq(ImportedEnrollee.LNAME, enrl.get(ENROLL_LNAME))
								.eq(ImportedEnrollee.FNAME, enrl.get(ENROLL_FNAME))
								.eq(ImportedEnrollee.MNAME, enrl.get(ENROLL_MNAME))
								.eq(ImportedEnrollee.RELATIONSHIP, enrl.get(ENROLL_RELATIONSHIP))
								.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
								//.eq(ImportedEnrollee.DOB, DateUtils.enrl.get(ENROLL_DOB)) parse date
								.first();

						if (e==null)
						{
							sponsor=new BImportedEnrollee();
							enrl.put("ENROLLEE_ID",sponsor.create());
							populateEnrollee(sponsor, enrl, cb, sponsor);
							sponsor.insert();
						}
						else
						{
							sponsor=new BImportedEnrollee(e);
							populateEnrollee(sponsor, enrl, cb, sponsor);
							sponsor.update();
						}

						ieMap.put(sponsor.getEnrolleeId(), sponsor.getBean());
						enrolleeObjs.add(sponsor.getBean());

					}
//System.out.println("sponsor db check");
					if (sponsor==null)
					{
					//	System.out.println(subscriberSSN);
						//find them from database
						ImportedEnrollee e=hsu.createCriteria(ImportedEnrollee.class)
							.eq(ImportedEnrollee.SSN, subscriberSSN)
							.eq(ImportedEnrollee.RELATIONSHIP, "EE")
							.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
							.first();
						if (e!=null)
							sponsor=new BImportedEnrollee(e);

					}

					//do the enrollees
//System.out.println("do enrollees");
					for (HashMap<String,String> enrl : enrollees)
					{

//System.out.println("Enroll process");
						if ("EE".equals(enrl.get(ENROLL_RELATIONSHIP))) //just sponsor
							continue;

						if (isEmpty(enrl.get(ENROLL_FNAME)))
							continue; //must have at least first name


						ImportedEnrollee e=null;

						e=ieMap.get(enrl.get("ENROLLEE_ID"));

						if (e==null&&COULD_ALREADY_BE_THERE)
							e=hsu.get(ImportedEnrollee.class, enrl.get("ENROLLEE_ID"));

						//see if enrollee exists
						if (e==null&&COULD_ALREADY_BE_THERE)
							e=hsu.createCriteria(ImportedEnrollee.class)
								.eq(ImportedEnrollee.SSN, enrl.get(ENROLL_SSN))
								.eq(ImportedEnrollee.LNAME, enrl.get(ENROLL_LNAME))
								.eq(ImportedEnrollee.FNAME, enrl.get(ENROLL_FNAME))
								.eq(ImportedEnrollee.MNAME, enrl.get(ENROLL_MNAME))
								.eq(ImportedEnrollee.RELATIONSHIP, enrl.get(ENROLL_RELATIONSHIP))
								.eq(ImportedEnrollee.IMPORT_FILE_TYPE, type)
								//.eq(ImportedEnrollee.DOB, DateUtils.enrl.get(ENROLL_DOB)) parse date
								.first();

						BImportedEnrollee bie;

						if (e==null)
						{
							bie=new BImportedEnrollee();
							enrl.put("ENROLLEE_ID",bie.create());
							populateEnrollee(bie, enrl, cb, sponsor);
							bie.insert();
						}
						else
						{
							bie=new BImportedEnrollee(e);
							populateEnrollee(bie, enrl, cb, sponsor);
							bie.update();
							enrl.put("ENROLLEE_ID",bie.getEnrolleeId());
						}

						ieMap.put(bie.getEnrolleeId(), bie.getBean());
						enrolleeObjs.add(bie.getBean());
					}
//System.out.println("multi enroll file check");
					if (isMultiEnrolleeFile&&COULD_ALREADY_BE_THERE)
					{
						//delete enrollees not used for every benefit on this row

						hsu.createCriteria(ImportBenefitJoin.class)
							.notIn(ImportedEnrollee.ID, enrolledIds)
							.eq(ImportBenefitJoin.SPONSOR, sponsor.getBean())
							.eq(ImportedBenefit.NAME, beneMap.get(BENEFIT_NAME))
							.delete();

						/*
						ImportedBenefit ib=hsu.createCriteria(ImportedBenefit.class)
							.eq(ImportedBenefit.NAME, beneMap.get(BENEFIT_NAME))
							.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
							.joinTo(ImportedBenefit.BENEFIT_JOINS)
							.eq(ImportBenefitJoin.SPONSOR, sponsor.getBean())
							.first();

						if (ib!=null)
						{
							//find any enrollees for this benefit that I didn't do
							for (ImportedEnrollee ie : hsu.createCriteria(ImportedEnrollee.class)
								.joinTo(ImportedEnrollee.BENEFIT_JOINS)
								.eq(ImportBenefitJoin.BENEFIT, ib)
								.notIn(ImportedEnrollee.ID, enrolledIds)
								.list())
							new BImportedEnrollee(ie).unEnroll(new BImportBenefit(ib));
						}
						*/
					}
//System.out.println("multi bene file check");
					if (isMultiBenefitFile&&COULD_ALREADY_BE_THERE)
					{

							HibernateCriteriaUtil hcu=hsu.createCriteria(ImportBenefitJoin.class);
							hcu.eq(ImportBenefitJoin.SPONSOR, sponsor.getBean())
								.joinTo(ImportBenefitJoin.BENEFIT)
								.notIn(ImportedBenefit.ID, usedBenefitIds)
								.eq(ImportedBenefit.IMPORT_FILE_TYPE, type);
							hcu.joinTo(ImportBenefitJoin.ENROLLEE)
								.in(ImportedEnrollee.ID, enrolledIds);
							hcu.delete();
	/*
	 * 					for (ImportedEnrollee e : enrolleeObjs)
						{
							//delete any benefits no longer associated to the subscriber in this type file
							for (ImportedBenefit ib : hsu.createCriteria(ImportedBenefit.class)
								.eq(ImportedBenefit.IMPORT_FILE_TYPE, type)
								.notIn(ImportedBenefit.ID, usedBenefitIds)
								.joinTo(ImportedBenefit.BENEFIT_JOINS)
								.eq(ImportBenefitJoin.ENROLLEE, e)
								.list())
								new BImportedEnrollee(e).unEnroll(new BImportBenefit(ib));
	
						}
					*/
					}
				}

				

			}
		}


	}

	private boolean isEmpty(String s) {
		return s==null || s.trim().equals("");
	}


	
	private void populateBenefit(BImportBenefit cb, HashMap<String, String> beneMap) {
		cb.setBenefitName(beneMap.get(BENEFIT_NAME));
	}

	private void populateEnrollee(BImportedEnrollee bie, HashMap<String, String> enrl, BImportBenefit cb, BImportedEnrollee sponsor) {

	//	System.out.println("Enroll ssn is "+enrl.get(ENROLL_SSN));
		bie.setSSN(fixSSN(enrl.get(ENROLL_SSN)));

		String lname=enrl.get(ENROLL_LNAME);
		if (isEmpty(lname))
			lname=sponsor.getLname();
		bie.setLname(lname);
		bie.setFname(enrl.get(ENROLL_FNAME));
		bie.setMname(enrl.get(ENROLL_MNAME));
		bie.setRelationship(enrl.get(ENROLL_RELATIONSHIP));
		if (!isEmpty(enrl.get(ENROLL_DOB)))
			bie.setDOB(Integer.parseInt(enrl.get(ENROLL_DOB)));

		int startDate=0;
		if (!isEmpty(enrl.get(ENROLL_COVERAGE_START)))
			startDate=Integer.parseInt(enrl.get(ENROLL_COVERAGE_START));
		int endDate=0;
		if (!isEmpty(enrl.get(ENROLL_COVERAGE_END)))
			endDate=Integer.parseInt(enrl.get(ENROLL_COVERAGE_END));
		bie.setStreet1(enrl.get(ENROLL_STREET1));
		bie.setStreet2(enrl.get(ENROLL_STREET2));
		bie.setCity(enrl.get(ENROLL_CITY));
		bie.setState(enrl.get(ENROLL_STATE));
		bie.setZip(enrl.get(ENROLL_ZIP));
		bie.setImportType(cb.getImportType());
		bie.enrollIn(cb, startDate, endDate, sponsor);
	}


	protected class Name
	{
		String fname;
		String lname;
		String mname="";

		public Name(String name)
		{
//System.out.println("Name is "+name);
			if (isEmpty(name))
				name=". .";

			if (name.indexOf(' ')==-1)
			{
				fname=".";
				lname=name;
			}
			else
			{
				lname=name.substring(0, name.indexOf(' ')).trim();
				fname=name.substring(name.indexOf(' ')+1).trim();

				int spos=fname.indexOf(' ');
				if (spos!=-1)
				{
					mname=fname.substring(spos+1).trim();
					if (mname.length()>20)
						mname=mname.substring(0,20);
					fname=fname.substring(0,spos).trim();
				}
			}

			if (lname.endsWith(","))
				lname=lname.substring(0,lname.length()-1);
			if (mname.endsWith(","))
				mname=mname.substring(0,mname.length()-1);
		}
	}
	public static void main(String args[])
	{  
		try
		{
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().dontAIIntegrate();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			ArahantSession.multipleCompanySupport=true;

		//	new GenericFileImport().buildSample();
			new GenericFileImport().importFile("/Users/Arahant/GLAGGS/eddi_sample/DRCMB255.csv", ArahantSession.getHSU().get(ImportType.class,"00001-0000000011"));
			new GenericFileImport().importFile("/Users/Arahant/GLAGGS/eddi_sample/MEMB256.txt", ArahantSession.getHSU().get(ImportType.class,"00001-0000000012"));

			ArahantSession.getHSU().commitTransaction();

		}
		catch (Exception e)
		{
			ArahantSession.getHSU().rollbackTransaction();
			e.printStackTrace();
		}
	}

	private String getValue(String line, ImportColumn col) {
		try {
			if (col.getDateFormat() == null || col.getDateFormat().trim().equals("")) {
				String x=line.substring(col.getStartPos()-1, col.getLastPos());
//				if (x.equals(col.getDiscriminatorValue()) && col.getDiscriminatorColumn()=='N')
//					x="";
				return x.trim();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(col.getDateFormat());
			String date=line.substring(col.getStartPos()-1, col.getLastPos());
			if (isEmpty(date))
				return "0";
			return DateUtils.getDate(sdf.parse(date)) + "";
		} catch (ParseException ex) {
			Logger.getLogger(GenericFileImport.class.getName()).log(Level.SEVERE, null, ex);
			return "";
		}
	}


	private String fixSSN(String val) {

		if (val==null)
			return val;

		if (val.length()==11)
			return val;
		if (val.length()==0)
			return null;
		while (val.length()<9)
			val='0'+val;

	//	System.out.println(val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5));
		return val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5);
	}

	private String fixRelationship(String val) {
		val=val.trim();
		if (val.equals("M"))
			val="EE";
		if (val.equals("W")||val.equals("H"))
			val="SP";
		if (val.equals("S")||val.equals("D"))
			val="CH";

		if (val.equals("3")||val.equals("4"))
			val="SP";
		if (val.equals("1")||val.equals("2")||val.equals("8")||val.equals("9"))
			val="CH";
//		System.out.println("RELATIONSHIP IS "+val);

		return val;
	}


}
