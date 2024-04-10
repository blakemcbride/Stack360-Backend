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

package com.arahant.imports.glaggs;

import com.arahant.beans.Agency;
import com.arahant.beans.Agent;
import com.arahant.beans.BenefitClass;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupAssociationId;
import com.arahant.beans.Person;
import com.arahant.beans.ScreenGroup;
import com.arahant.beans.SecurityGroup;
import com.arahant.beans.WageType;
import com.arahant.business.BBenefitClass;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BWageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import org.hibernate.ScrollableResults;

/**
 *
 */
public class GLAGSFixedImportBase {

	protected HibernateSessionUtil hsu=ArahantSession.getHSU();
	protected static final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	protected static final SimpleDateFormat sdfSlashes=new SimpleDateFormat("yyyy/MM/dd");


	private HashMap<String,HashMap<String,String>> categories=new HashMap<String, HashMap<String, String>>();
	private HashMap<String,HashMap<String,String>> benefits=new HashMap<String, HashMap<String, String>>();
	private HashMap<String,HashMap<String,String>> configs=new HashMap<String, HashMap<String, String>>();
	private HashMap<String,String> benWageTypes=new HashMap<String, String>();
	private HashMap<String,HashMap<String,String>> statuses=new HashMap<String, HashMap<String, String>>();
	private HashMap<String,HashMap<String,String>> benefitClasses=new HashMap<String, HashMap<String, String>>();
	protected HashMap<String,String> groupToCompany=new HashMap<String, String>();
	protected HashMap<String,String> ssnToPersonId=new HashMap<String, String>();
	protected HashMap<String,Agency> agencyToCompany=new HashMap<String, Agency>();
	protected HashMap<String,Agent> agentToPerson=new HashMap<String, Agent>();
	protected HashMap<String,Agency> agentToCompany=new HashMap<String, Agency>();
	protected HashMap<String,String> insuranceToCompany=new HashMap<String, String>();


	private HashSet<String> loginSet=new HashSet<String>();

	private static final HashSet<String> cancelIds=new HashSet<String>();

	static
	{
		String [] ids=new String[]{"23C785"};

		for (String x : ids)
			cancelIds.add(x);
	}

	protected boolean isDeadCompany(String id)
	{
		return cancelIds.contains(id);
	}

	protected boolean isCombineId(String id)
	{
		return !cancelIds.contains(id);
	}

	protected void preLoadMaps()
	{
		hsu.noAutoFlush();

		ArahantSession.multipleCompanySupport=true;
		ArahantSession.setFastKeys(true);

		secGroup=hsu.get(SecurityGroup.class, "00000-0000000001"); //Hard coded for speed, makes assumptions about configuration
		screenGroup=hsu.get(ScreenGroup.class,"00001-0000000158");
/*
		for (ProphetLogin pl : hsu.createCriteriaNoCompanyFilter(ProphetLogin.class).list())
		{
			loginSet.add(pl.getUserLogin());
		}
*/
		//pre load some of the maps where it will speed things up


		ScrollableResults sr= hsu.createQuery("select og, e.extRef from Agent e join e."+Employee.ORGGROUPASSOCIATIONS+" oga" +
				" join oga."+OrgGroupAssociation.ORGGROUP+" og where og.name<>'Misc' ")
				.scroll();
	


		while (sr.next())
		{
			OrgGroup og=(OrgGroup)sr.get(0);
			String ref=sr.getString(1);

			if (isEmpty(ref))
				continue;

			if (og instanceof Agency)
			{
				agentToCompany.put(Integer.parseInt(ref)+"",(Agency) og);
			}
		}

		sr.close();
		
		System.out.println("Pre load statuses");
		for (HrEmployeeStatus stat  : hsu.createCriteriaNoCompanyFilter(HrEmployeeStatus.class).list())
		{
			if (statuses.get(stat.getOrgGroup().getOrgGroupId())==null)
				statuses.put(stat.getOrgGroup().getOrgGroupId(), new HashMap<String, String>());

			statuses.get(stat.getOrgGroup().getOrgGroupId()).put(stat.getName(), stat.getStatusId());
		}

		System.out.println("Pre load categories");
		for (HrBenefitCategory x  : hsu.createCriteriaNoCompanyFilter(HrBenefitCategory.class).list())
		{
			if (categories.get(x.getCompany().getOrgGroupId())==null)
				categories.put(x.getCompany().getOrgGroupId(), new HashMap<String, String>());

			categories.get(x.getCompany().getOrgGroupId()).put(x.getDescription(), x.getBenefitCatId());
		}

		System.out.println("Pre load benefits");
		for (HrBenefit x  : hsu.createCriteriaNoCompanyFilter(HrBenefit.class).list())
		{
			if (benefits.get(x.getHrBenefitCategory().getCompany().getOrgGroupId())==null)
				benefits.put(x.getHrBenefitCategory().getCompany().getOrgGroupId(), new HashMap<String, String>());

			benefits.get(x.getHrBenefitCategory().getCompany().getOrgGroupId()).put(x.getName(), x.getBenefitId());
		}

		System.out.println("Pre load benefit configs");
		for (HrBenefitConfig x  : hsu.createCriteriaNoCompanyFilter(HrBenefitConfig.class).list())
		{
			if (configs.get(x.getHrBenefit().getHrBenefitCategory().getCompany().getOrgGroupId())==null)
				configs.put(x.getHrBenefit().getHrBenefitCategory().getCompany().getOrgGroupId(), new HashMap<String, String>());

			configs.get(x.getHrBenefit().getHrBenefitCategory().getCompany().getOrgGroupId()).put(x.getName(), x.getBenefitConfigId());
		}

		System.out.println("Pre load benefit classes");
		for (BenefitClass x  : hsu.createCriteriaNoCompanyFilter(BenefitClass.class).list())
		{
			if (benefitClasses.get(x.getOrgGroup().getOrgGroupId())==null)
				benefitClasses.put(x.getOrgGroup().getOrgGroupId(), new HashMap<String, String>());

			benefitClasses.get(x.getOrgGroup().getOrgGroupId()).put(x.getName(), x.getBenefitClassId());
		}

		System.out.println("Pre load wage types");
		for (WageType x : hsu.createCriteriaNoCompanyFilter(WageType.class).eq(WageType.NAME, "Insurance").list())
		{
			benWageTypes.put(x.getOrgGroup().getOrgGroupId(), x.getWageTypeId());
		}

		int count=0;

		System.out.println("Pre load company details");
		for (CompanyDetail dt : hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).list())
		{
			if (++count%50==0)
				System.out.println("Loaded "+count+" companies");
			if (isCombineId(dt.getExternalId()))
				groupToCompany.put(dt.getExternalId(), dt.getOrgGroupId());
		}

		System.out.println("Pre load company benefit group ids");
		for (HrBenefit b : hsu.createCriteriaNoCompanyFilter(HrBenefit.class).list())
		{
			insuranceToCompany.put(b.getGroupId(), b.getHrBenefitCategory().getCompany().getOrgGroupId());
		}

		System.out.println("Pre load employees");
		for (Employee p : hsu.createCriteria(Employee.class).notIn(Employee.SSN, new String[]{"999-99-9999",""}).list())
		{
			ssnToPersonId.put(p.getUnencryptedSsn(), p.getPersonId());
		}

		System.out.println("Pre load agencies");
		for (Agency a : hsu.getAll(Agency.class))
		{
			agencyToCompany.put(a.getIdentifier(),a);
		}

		

		


		System.out.println("Pre load done");
	}



	public void makeLoginDefaults(BEmployee bemp) throws ArahantException
	{
   /*     String middleSubstring = isEmpty(bemp.getMiddleName()) ? "" : bemp.getMiddleName().substring(0,1);
		String loginName=(bemp.getFirstName().substring(0,1)+middleSubstring+bemp.getLastName()).toLowerCase();

		if (loginName.length()>18)
			loginName=loginName.substring(0,18);

		final Random rand=new Random();
		int val=rand.nextInt(99);


		while (loginSet.contains(loginName+val))
		{
			val=rand.nextInt(99);
		}

		loginSet.add(loginName+val);

		bemp.setUserLogin(loginName+val);

		//we are going to use a five digit number per Blake
		bemp.setUserPassword(String.format("%05d", new Object[]{rand.nextInt(99999)}));
		bemp.setCanLogin('Y');


		bemp.setSecurityGroup(secGroup);
		bemp.setScreenGroup(screenGroup);
	* */
	}

	private SecurityGroup secGroup;
	private ScreenGroup screenGroup;


	private HashMap<String,HashMap<String,HrEmployeeStatus>> stats=new HashMap<String, HashMap<String, HrEmployeeStatus>>();



	protected HrEmployeeStatus getStatus(String companyId, String statusName)
	{
		if (stats.get(companyId)==null)
			stats.put(companyId, new HashMap<String, HrEmployeeStatus>());

		HrEmployeeStatus ret=stats.get(companyId).get(statusName);

		if (ret==null)
		{
			ret=hsu.get(HrEmployeeStatus.class, getStatusId(companyId, statusName));
			stats.get(companyId).put(statusName, ret);
		}
		return ret;
	}

	protected String getStatusId(String companyId, String statusName)
	{

		if (statuses.get(companyId)==null)
			statuses.put(companyId, new HashMap<String, String>());
		String statusId=statuses.get(companyId).get(statusName);

		if (statusId==null)
		{
			statusId=hsu.createCriteria(HrEmployeeStatus.class)
				.selectFields(HrEmployeeStatus.STATUSID)
				.eq(HrEmployeeStatus.NAME, statusName)
				.joinTo(HrEmployeeStatus.ORG_GROUP)
				.eq(OrgGroup.ORGGROUPID, companyId)
				.stringVal();
		}
		if (isEmpty(statusId))
		{
			BHREmployeeStatus stat=new BHREmployeeStatus();
			statusId=stat.create();
			stat.setActiveFlag('Y');
			stat.setBenefitType('B');
			stat.setName(statusName);
			stat.setDateType("S");

			if (stat.getName().equals("COBRA"))
			{
				stat.setActiveFlag('N');
				stat.setBenefitType('C');
				stat.setName(statusName);
				stat.setDateType("S");

			}

			if (stat.getName().equals("RETIRED"))
			{
				stat.setActiveFlag('N');
				stat.setBenefitType('B');
				stat.setName(statusName);
				stat.setDateType("F");

			}

			stat.setOrgGroupId(companyId);

			stat.insert();

			statuses.get(companyId).put(statusName, statusId);
		}
		return statusId;

	}


	protected String getBenefitClassId(String companyId, String className)
	{
		if (benefitClasses.get(companyId)==null)
				benefitClasses.put(companyId, new HashMap<String, String>());

		String benefitClassId=benefitClasses.get(companyId).get(className);
		if (benefitClassId==null)
			benefitClassId=hsu.createCriteria(BenefitClass.class)
				.selectFields(BenefitClass.BENEFIT_CLASS_ID)
				.eq(BenefitClass.NAME,className)
				.stringVal();

		if (isEmpty(benefitClassId))
		{
			BBenefitClass cls=new BBenefitClass();
			benefitClasses.get(companyId).put(className, benefitClassId=cls.create());
			cls.setName(className);
			cls.setDescription(className);
			cls.insert();
		}
		return benefitClassId;
	}
	protected class Name
	{
		String fname;
		String lname;
		String mname="";

		public Name(String name)
		{
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
	protected boolean isEmpty(String x)
	{
		return x==null || x.trim().equals("");
	}

	private String getWageTypeId(String companyId)
	{

		String wageTypeId=benWageTypes.get(companyId);

/*		if (wageTypeId==null)
		{
			wageTypeId=hsu.createCriteria(WageType.class)
				.addReturnField(WageType.ID)
				.eq(WageType.NAME, "Insurance")
				.stringVal();
		}
*/		if (isEmpty(wageTypeId))
		{
			BWageType wt=new BWageType();
			benWageTypes.put(companyId, wageTypeId=wt.create());
			wt.setName("Insurance");
			wt.setPeriodType(WageType.PERIOD_ONE_TIME);
			wt.setType(WageType.TYPE_DEDUCTION);
			wt.setIsDeduction(true);
			wt.insert();
		}

		return wageTypeId;
	}

	protected String getBenefitId(String companyId, String benefitName, String categoryId, boolean cobra, String companyRef)
	{

		if (benefits.get(companyId)==null)
			benefits.put(companyId, new HashMap<String, String>());

		String benefitId=benefits.get(companyId).get(benefitName);

/*		if (benefitId==null)
		{
			benefitId=hsu.createCriteria(HrBenefit.class)
				.addReturnField(HrBenefit.BENEFITID)
				.eq(HrBenefit.NAME,benefitName)
				.stringVal();
		}
*/
		if (isEmpty(benefitId))
		{
			BHRBenefit bene=new BHRBenefit();
			benefits.get(companyId).put(benefitName, benefitId=bene.create());
			bene.setBenefitCategoryId(categoryId);
			bene.setCoveredUnderCOBRA(cobra);
			bene.setEmployeeChoosesAmount(false);
			bene.setGroupId(companyRef);
			bene.setName(benefitName);
			bene.setWageTypeId(getWageTypeId(companyId));
			bene.insert();
		}

		return benefitId;
	}

	protected String getBenefitConfigId(String companyId, String configName)
	{

		if (configs.get(companyId)==null)
			configs.put(companyId, new HashMap<String, String>());

		String configId=configs.get(companyId).get(configName);

		return configId;
	}
	protected String getBenefitConfigId(String companyId, String configName, String benefitId, String desc, String benefitClassId)
	{

		if (desc.length()>60)
			desc=desc.substring(0,60);

		if (configs.get(companyId)==null)
			configs.put(companyId, new HashMap<String, String>());

		String configId=configs.get(companyId).get(configName);

		if (configId==null)
		{

			HrBenefitConfig conf=null; //TODO: for testing
			/*hsu.createCriteria(HrBenefitConfig.class)
					.eq(HrBenefitConfig.NAME, configName)
					.first();
*/
			if (conf==null)
			{
				BHRBenefitConfig config=new BHRBenefitConfig();
				config.create();
				config.setName(configName);
				config.setAdditionalInfo(desc);
				if (!isEmpty(benefitClassId))
					config.setBenefitClasses(new String[]{benefitClassId});
				config.setBenefitId(benefitId);
				config.setCoversEmployee(true);
				if (configName.indexOf("Family")!=-1 || configName.indexOf("Code")!=-1)
				{
					config.setCoversEmployeeSpouseOrChildren(true);
					config.setSpouseNonEmpOrChildren(true);
				}
				if (configName.indexOf("Double")!=-1)
				{
					config.setCoversEmployeeSpouseOrChildren(true);
					config.setSpouseNonEmpOrChildren(true);
					config.setMaxChildren(1);
				}
				config.insert();
				configId=config.getBenefitConfigId();
			}
			else
			{
				BHRBenefitConfig config=new BHRBenefitConfig(conf);
				config.setName(configName);
				config.setAdditionalInfo(desc);
				config.setBenefitId(benefitId);
				if (!isEmpty(benefitClassId))
					config.setBenefitClasses(new String[]{benefitClassId});

				config.setCoversEmployee(true);
				if (configName.indexOf("Family")!=-1 || configName.indexOf("Code")!=-1)
				{
					config.setCoversEmployeeSpouseOrChildren(true);
					config.setSpouseNonEmpOrChildren(true);
				}
				if (configName.indexOf("Double")!=-1)
				{
					config.setCoversEmployeeSpouseOrChildren(true);
					config.setSpouseNonEmpOrChildren(true);
					config.setMaxChildren(1);
				}
				config.update();
				configId=config.getBenefitConfigId();
			}
			
			configs.get(companyId).put(configName, configId);
		}
		return configId;
	}

	protected String getCategoryId(String companyId,String categoryName)
	{
		if (categories.get(companyId)==null)
			categories.put(companyId, new HashMap<String, String>());
		String categoryId=categories.get(companyId).get(categoryName);

/*		if (categoryId==null)
		{
			categoryId=hsu.createCriteria(HrBenefitCategory.class)
				.addReturnField(HrBenefitCategory.BENEFIT_CATEGORY_ID)
				.eq(HrBenefitCategory.DESCRIPTION,categoryName)
				.stringVal();
		}
*/		if (isEmpty(categoryId))
		{
			BHRBenefitCategory cat=new BHRBenefitCategory();
			categories.get(companyId).put(categoryName, categoryId=cat.create());
			cat.setAllowsMultipleBenefits(true);
			cat.setRequiresDecline(false);
			cat.setTypeId(HrBenefitCategory.HEALTH);
			cat.setDescription(categoryName);
			cat.insert();
		}

		return categoryId;
	}

	protected void makeBenefitJoinIfNotFound(Person paying, Person covered, String config, int start, int end, HrEmplDependent dep)
	{
		if (config==null)
		{
			System.out.println("config not found");
			return;
		}
		//have I made this join already?

		HrBenefitJoin bj=null;//TODO: for import
		/*hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, config)
			.eq(HrBenefitJoin.PAYING_PERSON, paying)
			.eq(HrBenefitJoin.COVERED_PERSON, covered)
			.first();
*/
		try
		{
			if (bj==null)
			{
				BHRBenefitJoin bsj=new BHRBenefitJoin();
				bsj.create();
				bsj.setPayingPerson(paying);
				bsj.setCoveredPerson(covered);
				bsj.setPolicyStartDate(start);
				bsj.setCoverageStartDate(start);
				bsj.setPolicyEndDate(end);
				bsj.setCoverageEndDate(end);
				bsj.setBenefitConfigId(config);
				bsj.setBenefitApproved(true);
				bsj.setRelationship(dep);
				bsj.insert(true);
			}
		}
		catch (Exception e)
		{
			return;
		}

	}


	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary, Person person) throws ArahantException {

		if (isEmpty(orgGroupId))
			throw new ArahantWarning("An org group is required");

		OrgGroupAssociation oga=new OrgGroupAssociation();
			final OrgGroupAssociationId ogi=new OrgGroupAssociationId();
			ogi.setOrgGroupId(orgGroupId);
			ogi.setPersonId(person.getPersonId());
			oga.setId(ogi);
			oga.setPerson(person);
			oga.setOrgGroup(hsu.get(OrgGroup.class, orgGroupId));
			oga.setOrgGroupType(ArahantConstants.COMPANY_TYPE);

		oga.setPrimaryIndicator(isPrimary?'Y':'N');
		oga.setStartDate(0);
        oga.setFinalDate(0);
        
		hsu.insert(oga);
	}

	protected HrEmplDependent  makeRelationship(Person person, Employee emp, String relType)
	{
		HrEmplDependent bean=new HrEmplDependent();
		bean.generateId();
		bean.setDependentId(person.getPersonId());
		bean.setEmployee(emp);
		bean.setPerson(person);
		bean.setRelationship("");
		bean.setRelationshipType(relType.charAt(0));
		hsu.insert(bean);

		return bean;
	}

	protected String fixGroupName(String name)
	{
		name=name.trim();
		
		String [] st={"DRCC/","DRC/","GRCC/","GDCC"};
		for (String x : st)
			if (name.startsWith(x))
				name=name.substring(x.length());

		name=name.trim();
		if (name.startsWith("/"))
			name=name.substring(1);
		name=name.trim();
		if (name.startsWith("-"))
			name=name.substring(1);

		name=name.trim();
		return name;
	}

	protected String fixSSN (String ssn)
	{
		if (isEmpty(ssn))
			return null;
		if (ssn.indexOf('-')==-1)
			ssn=ssn.substring(0,3)+"-"+ssn.substring(3,5)+"-"+ssn.substring(5);

		return ssn;
	}
}
