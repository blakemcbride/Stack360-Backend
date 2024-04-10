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

import com.arahant.beans.BenefitClass;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.WageType;
import com.arahant.business.BBenefitClass;
import com.arahant.business.BGlAccount;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BHRPosition;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BWageType;
import com.arahant.imports.census.DataImport;
import com.arahant.imports.census.EmployeeBenefit;
import com.arahant.imports.census.EmployeeOrgGroup;
import com.arahant.imports.census.EmployeeOrgGroup;
import com.arahant.imports.census.EmployeeStatus;
import com.arahant.imports.census.EmployeeStatus;
import com.arahant.imports.census.EmployeeWageAndPosition;
import com.arahant.imports.census.ImportMap;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class StandardCASMap extends ImportMap {


	private String dFileName;
	private String securityGroupId;
	private String screenGroupId;

	protected DelimitedFileReader dfr;

	protected String unknownWageTypeId;
	protected String salaryWageTypeId;
	protected String hourlyWageTypeId;
	protected String unknownPositionId;
    protected String activeId;
	protected String partTimeId;
    protected String inactiveId;
    protected String termId;
    protected HashMap<String, HashMap<String, String>> benefitTypeMap;
    protected String fourOhOneKConfigId;
    protected String changeReasonId;

	HashMap<String,String> orgs=new HashMap<String, String>();
	HashMap<String,String> locs=new HashMap<String, String>();

	HashMap<String,String> medBenes=new HashMap<String, String>();
	HashMap<String,String> medConfigs=new HashMap<String, String>();
	HashMap<String,String> dentalConfigs=new HashMap<String, String>();


	HibernateSessionUtil hsu=ArahantSession.getHSU();


	private static final int EMPID = 0;
	private static final int SSN = EMPID + 1;
	private static final int FNAME = SSN + 1;
	private static final int MNAME = FNAME + 1;
	private static final int LNAME = MNAME + 1;
	private static final int HIRE = LNAME + 1;
	private static final int STREET1 = HIRE + 1;
	private static final int STREET2 = STREET1 + 1;
	private static final int CITY = STREET2 + 1;
	private static final int STATE = CITY + 1;
	private static final int ZIP = STATE + 1;
	private static final int DOB = ZIP + 1;
	private static final int GENDER = DOB + 1;
	private static final int PAYTYPE = GENDER + 1;
	private static final int PAY = PAYTYPE + 1;
	private static final int HOMEPHONE = PAY + 1;
	private static final int JOBTITLE = HOMEPHONE + 1;
	private static final int BENEFITCLASS = JOBTITLE + 1;
	private static final int ORGGROUP = BENEFITCLASS + 1;
	private static final int MMREFID = ORGGROUP + 1;
	private static final int RXREFID = MMREFID + 1;

	private static final int MED1 = RXREFID + 1;
	private static final int MED1LVL = MED1 + 1;
	private static final int MED1START = MED1LVL + 1;
	private static final int MED2 = MED1START + 1;
	private static final int MED2LVL = MED2 + 1;
	private static final int MED2START = MED2LVL + 1;
	private static final int DENTAL = MED2START + 1;
	private static final int DENTALLVL = DENTAL + 1;
	private static final int DENTALSTART = DENTALLVL + 1;
	private static final int VISION = DENTALSTART + 1;
	private static final int VISIONLVL = VISION + 1;
	private static final int VISIONSTART = VISIONLVL + 1;;

	private static final int LIFE = VISIONSTART + 1;
	private static final int LIFESTART = LIFE + 1;
	private static final int LIFEAMT = LIFESTART + 1;

	private static final int FLEX1 = LIFEAMT + 1;
	private static final int FLEX1START = FLEX1 + 1;
	private static final int FLEX1AMT = FLEX1START + 1;

	private static final int FLEX2 = FLEX1AMT + 1;
	private static final int FLEX2START = FLEX2 + 1;
	private static final int FLEX2AMT = FLEX2START + 1;

	private static final int VOL1 = FLEX2AMT + 1;
	private static final int VOL1START = VOL1 + 1;
	private static final int VOL1AMT = VOL1START + 1;

	private static final int VOL2 = VOL1AMT + 1;
	private static final int VOL2START = VOL2 + 1;
	private static final int VOL2AMT = VOL2START + 1;

	private static final int VOL3 = VOL2AMT + 1;
	private static final int VOL3START = VOL3 + 1;
	private static final int VOL3AMT = VOL3START + 1;

	private static final int VOL4 = VOL3AMT + 1;
	private static final int VOL4START = VOL4 + 1;
	private static final int VOL4AMT = VOL4START + 1;

	private static final int VOL5 = VOL4AMT + 1;
	private static final int VOL5START = VOL5 + 1;
	private static final int VOL5AMT = VOL5START + 1;

	private static final int VOL6 = VOL5AMT + 1;
	private static final int VOL6START = VOL6 + 1;
	private static final int VOL6AMT = VOL6START + 1;

	private static final int VOL7 = VOL6AMT + 1;
	private static final int VOL7START = VOL7 + 1;
	private static final int VOL7AMT = VOL7START + 1;

	private static final int VOL8 = VOL7AMT + 1;
	private static final int VOL8START = VOL8 + 1;
	private static final int VOL8AMT = VOL8START + 1;


	public StandardCASMap(String fname)
	{
		try {
			dFileName=fname;
			dfr = new DelimitedFileReader(dFileName);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(StandardCASMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public StandardCASMap()
	{
		try {
			dfr = new DelimitedFileReader(dFileName);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(StandardCASMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}



	@Override
	public void createNonImportSetups() {

        // create full time status if one does not already exist
        this.activeId = BHREmployeeStatus.findOrMake("Full Time", true);

        // create full time status
//        BHREmployeeStatus fullTime=new BHREmployeeStatus();
//		this.activeId = fullTime.create();
//		fullTime.setName("Full Time");
//		fullTime.setActiveFlag('Y');
//		fullTime.setDateType("S");
//		fullTime.setBenefitType('B');
//		fullTime.insert();

        // create part time status if one does not already exist
        this.partTimeId = BHREmployeeStatus.findOrMake("Part Time", true);

        // create part time status
//	BHREmployeeStatus parttime=new BHREmployeeStatus();
//		this.partTimeId = parttime.create();
//		parttime.setName("Part Time");
//		parttime.setActiveFlag('Y');
//		parttime.setDateType("S");
//		parttime.setBenefitType('B');
//		parttime.insert();

        //create inactive status if one does not already exist
        this.inactiveId = BHREmployeeStatus.findOrMake("Inactive", false);

        // create inactive status
//        BHREmployeeStatus partTime=new BHREmployeeStatus();
//		this.inactiveId = partTime.create();
//		partTime.setName("Inactive");
//		partTime.setActiveFlag('N');
//		partTime.setDateType("F");
//		partTime.setBenefitType('B');
//		partTime.insert();

        // create terminated status if one does not already exist
        this.termId = BHREmployeeStatus.findOrMake("Terminated", false);
        
		// create term status
//        BHREmployeeStatus term=new BHREmployeeStatus();
//		this.termId = term.create();
//		term.setName("Terminated");
//		term.setActiveFlag('N');
//		term.setDateType("F");
//		term.setBenefitType('B');
//		term.insert();


		//create wages gl account if one does not already exist
		BGlAccount wagesAccount=new BGlAccount(BGlAccount.findOrMake("Wages"));

		//creage wages gl account
//		BGlAccount wagesAccount=new BGlAccount();
//		wagesAccount.create();
//		wagesAccount.setAccountName("Wages");
//		wagesAccount.setDefaultFlag(false);
//		wagesAccount.setAccountType(24);
//		wagesAccount.setAccountNumber("");
//		wagesAccount.insert();


		//create unknown wage type if one does not already exist
		this.unknownWageTypeId = BWageType.findOrMake("Unknown", wagesAccount.getBean(), WageType.PERIOD_SALARY, WageType.TYPE_REGULAR);

		// create unknown wage type
//		BWageType unknownWageType = new BWageType();
//		this.unknownWageTypeId = unknownWageType.create();
//		unknownWageType.setIsDeduction(false);
//		unknownWageType.setName("Unknown");
//		unknownWageType.setPeriodType(WageType.PERIOD_SALARY);
//		unknownWageType.setType(WageType.TYPE_REGULAR);
//		unknownWageType.setExpenseAccount(wagesAccount.getBean());
//		unknownWageType.insert();

		//create salary wage type if one does not already exist
		this.salaryWageTypeId = BWageType.findOrMake("Salary", wagesAccount.getBean(), WageType.PERIOD_SALARY, WageType.TYPE_REGULAR);

//		BWageType salaryWageType = new BWageType();
//		this.salaryWageTypeId = salaryWageType.create();
//		salaryWageType.setIsDeduction(false);
//		salaryWageType.setName("Salary");
//		salaryWageType.setPeriodType(WageType.PERIOD_SALARY);
//		salaryWageType.setType(WageType.TYPE_REGULAR);
//		salaryWageType.setExpenseAccount(wagesAccount.getBean());
//		salaryWageType.insert();

		//create hourly wage type if one does not already exist
		this.hourlyWageTypeId = BWageType.findOrMake("Hourly", wagesAccount.getBean(), WageType.PERIOD_HOURLY, WageType.TYPE_REGULAR);

//		BWageType hourlyWageType = new BWageType();
//		this.hourlyWageTypeId = hourlyWageType.create();
//		hourlyWageType.setIsDeduction(false);
//		hourlyWageType.setName("Hourly");
//		hourlyWageType.setPeriodType(WageType.PERIOD_HOURLY);
//		hourlyWageType.setType(WageType.TYPE_REGULAR);
//		hourlyWageType.setExpenseAccount(wagesAccount.getBean());
//		hourlyWageType.insert();

		//create unknown position if one does not already exist
		this.unknownPositionId = BHRPosition.findOrMake("Unknown");

        // create unknown position
//        BHRPosition position = new BHRPosition();
//        this.unknownPositionId = position.create();
//        position.setName("Unknown");
//        position.insert();

		//create benefit categories if they do not already exist
		BHRBenefitCategory medicalCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Medical", false, true, HrBenefitCategory.HEALTH));
		BHRBenefitCategory medical2Category = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Medical2", false, true, HrBenefitCategory.HEALTH));

		// create benefit categories
//        BHRBenefitCategory medicalCategory = new BHRBenefitCategory();
//        medicalCategory.create();
//		  medicalCategory.setAllowsMultipleBenefits(false);
//        medicalCategory.setDescription("Medical");
//        medicalCategory.setRequiresDecline(true);
//        medicalCategory.setTypeId(HrBenefitCategory.HEALTH);
//        medicalCategory.insert();

		BHRBenefitCategory dentalCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Dental", false, true, HrBenefitCategory.DENTAL));

//        BHRBenefitCategory dentalCategory = new BHRBenefitCategory();
//        dentalCategory.create();
//        dentalCategory.setAllowsMultipleBenefits(false);
//        dentalCategory.setDescription("Dental");
//        dentalCategory.setRequiresDecline(true);
//        dentalCategory.setTypeId(HrBenefitCategory.DENTAL);
//        dentalCategory.insert();

		BHRBenefitCategory visionCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Vision", false, true, HrBenefitCategory.VISION));

//        BHRBenefitCategory visionCategory = new BHRBenefitCategory();
//        visionCategory.create();
//        visionCategory.setAllowsMultipleBenefits(false);
//        visionCategory.setDescription("Vision");
//        visionCategory.setRequiresDecline(true);
//        visionCategory.setTypeId(HrBenefitCategory.VISION);
//        visionCategory.insert();

		BHRBenefitCategory lifeCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Life", false, true, HrBenefitCategory.LIFE));
		
		BHRBenefitCategory miscCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Misc", true, false, HrBenefitCategory.MISC));

//        BHRBenefitCategory miscCategory = new BHRBenefitCategory();
//        miscCategory.create();
//        miscCategory.setAllowsMultipleBenefits(true);
//        miscCategory.setDescription("Misc");
//        miscCategory.setRequiresDecline(false);
//        miscCategory.setTypeId(HrBenefitCategory.MISC);
//        miscCategory.insert();

		BHRBenefitCategory flexCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Flex", true, false, HrBenefitCategory.FLEX_TYPE));

//        BHRBenefitCategory flexCategory = new BHRBenefitCategory();
//        flexCategory.create();
//        flexCategory.setAllowsMultipleBenefits(true);
//        flexCategory.setDescription("Flex");
//        flexCategory.setRequiresDecline(false);
//        flexCategory.setTypeId(HrBenefitCategory.FLEX_TYPE);
//        flexCategory.insert();

		BHRBenefitCategory voluntaryCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Voluntary", true, false, HrBenefitCategory.VOLUNTARY));

//		BHRBenefitCategory voluntaryCategory = new BHRBenefitCategory();
//        voluntaryCategory.create();
//        voluntaryCategory.setAllowsMultipleBenefits(true);
//        voluntaryCategory.setDescription("Voluntary");
//        voluntaryCategory.setRequiresDecline(false);
//        voluntaryCategory.setTypeId(HrBenefitCategory.VOLUNTARY);
//        voluntaryCategory.insert();

		BHRBenefitCategory voluntaryLifeCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Voluntary Life", true, false, HrBenefitCategory.VOLUNTARY));
		BHRBenefitCategory voluntaryADDCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Voluntary AD&D", true, false, HrBenefitCategory.VOLUNTARY));
		BHRBenefitCategory voluntarySTDCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Voluntary ST Disability", false, false, HrBenefitCategory.VOLUNTARY));
		BHRBenefitCategory voluntaryLTDCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Voluntary LT Disability", false, false, HrBenefitCategory.VOLUNTARY));

		 // create change reason id
        BHRBenefitChangeReason changeReason = BHRBenefitChangeReason.findOrMake("Initial Import");

        changeReason = BHRBenefitChangeReason.findOrMake("Internal Administration");
        this.changeReasonId = changeReason.getId();
/*
        // create default project config if not present
        ProjectCategory projectCategoryBean = ArahantSession.getHSU().createCriteria(ProjectCategory.class).eq(ProjectCategory.CODE, "Misc").first();
        if (projectCategoryBean == null) {
            BProjectCategory category = new BProjectCategory();
            this.projectCategoryId = category.create();
            category.setCode("Misc");
            category.setDescription("Catch-all Category for Projects");
            category.setScope(ProjectCategory.SCOPE_GLOBAL);
            category.insert();
        } else {
            this.projectCategoryId = projectCategoryBean.getProjectCategoryId();
        }

        ProjectStatus projectStatusBean = ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, "Misc").first();
        if (projectStatusBean == null) {
            BProjectStatus status = new BProjectStatus();
            this.projectStatusId = status.create();
            status.setActive('Y');
            status.setCode("Misc");
            status.setDescription("Catch-all Status for Projects");
            status.insert();
        } else {
            this.projectStatusId = projectStatusBean.getProjectStatusId();
        }

        Route routeBean = ArahantSession.getHSU().createCriteria(Route.class).eq(Route.NAME, "Misc").first();
        if (routeBean == null) {
            BProjectPhase phase = new BProjectPhase();
            phase.create();
            phase.setCategoryId(2); // development, which is 1 (setter subtracts 1)
            phase.setCode("Misc");
            phase.setDescription("Catch-all Phase for Route Stops");
            phase.insert();

            BRoute route = new BRoute();
            route.create();
            route.setDescription("Catch-all Route for Projects");
            route.setName("Misc");
            route.setDefaultStatusId(this.projectStatusId);
            route.insert();

            BRouteStop routeStop = new BRouteStop();
            routeStop.create();
            routeStop.setDescription("Single Route Stop");
            routeStop.setPhaseId(phase.getId());
            routeStop.setProjectStatusIds(new String[] {this.projectStatusId});
            routeStop.setRouteId(route.getRouteId());
            routeStop.insert();

            route.setDefaultRouteStop(routeStop.getRouteStopId());
            route.update();

            BProjectType type = new BProjectType();
            this.projectTypeId=type.create();
            type.setCode("Time Off Projects");
            type.setDescription("Projects for Time Off");
            type.setScope(ProjectType.SCOPE_GLOBAL);
            type.insert();
        } else {

			ProjectType t=ArahantSession.getHSU().createCriteria(ProjectType.class).eq(ProjectStatus.CODE, "Time Off Projects").first();
			
			if (t==null)
			{
				BProjectType bt=new BProjectType();
				projectTypeId=bt.create();
				bt.setCode("Time Off Projects");
				bt.setDescription("Time Off Projects");
				bt.setScope(ProjectType.SCOPE_INTERNAL);
				bt.insert();
			}
			else
				this.projectTypeId = t.getProjectTypeId();

        }


        miscBenefits.clear();
        vacationBenefitId=createBenefitProject(miscCategory.getCategoryId(), "Vacation", "Vacation");
        sickBenefitId=createBenefitProject(miscCategory.getCategoryId(), "Sick", "Sick");
        createBenefitProject(miscCategory.getCategoryId(), "Holiday", "Holiday");

*/
		//create the benefits
		try
		{
			DelimitedFileReader bdfr=new DelimitedFileReader(dFileName);

			bdfr.nextLine();

			while (bdfr.nextLine())
			{
				System.out.println("Processing benefits for... " + bdfr.getString(LNAME).trim() + ", " + bdfr.getString(FNAME).trim());
				
				makeBenefit(bdfr.getString(MED1), bdfr.getString(MED1LVL), medicalCategory.getCategoryId(), bdfr.getString(MMREFID));
				makeBenefit(bdfr.getString(MED2), bdfr.getString(MED2LVL), medical2Category.getCategoryId(), bdfr.getString(MMREFID));

				makeBenefit(bdfr.getString(DENTAL), bdfr.getString(DENTALLVL), dentalCategory.getCategoryId());

				makeBenefit(bdfr.getString(VISION), bdfr.getString(VISIONLVL), visionCategory.getCategoryId());

				makeBenefit(bdfr.getString(LIFE), bdfr.getString(LIFE), lifeCategory.getCategoryId());

				makeBenefit(bdfr.getString(FLEX1), bdfr.getString(FLEX1), flexCategory.getCategoryId());
				makeBenefit(bdfr.getString(FLEX2), bdfr.getString(FLEX2), flexCategory.getCategoryId());

				for(int i = VOL1; i <= VOL8; i+=3) {
					if(!bdfr.getString(i+2).toLowerCase().contains("waiv")) {
						String categoryName = bdfr.getString(i).toLowerCase();
//						DecimalFormat format = new DecimalFormat("#,###,###.00");
						if(categoryName.contains("life"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryLifeCategory.getCategoryId());
//						else if(categoryName.contains("life"))
//							makeBenefit(bdfr.getString(i), bdfr.getString(i) + " $" + format.format(Float.valueOf(bdfr.getString(i+2))), voluntaryLifeCategory.getCategoryId());
						else if(categoryName.contains("&"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryADDCategory.getCategoryId());
						else if(categoryName.contains("short") || categoryName.contains("st"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntarySTDCategory.getCategoryId());
						else if(categoryName.contains("long") || categoryName.contains("lt"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryLTDCategory.getCategoryId());
						else
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryCategory.getCategoryId());
					}
				}
//				makeBenefit(bdfr.getString(VOL2), bdfr.getString(VOL2), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL3), bdfr.getString(VOL3), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL4), bdfr.getString(VOL4), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL5), bdfr.getString(VOL5), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL6), bdfr.getString(VOL6), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL7), bdfr.getString(VOL7), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL8), bdfr.getString(VOL8), voluntaryCategory.getCategoryId());
//				System.out.println("\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void makeBenefit(String medName, String level, String categoryId) {
		makeBenefit(medName, level, categoryId, "");
	}

	private void makeBenefit(String medName, String level, String categoryId, String groupId)
	{
		medName = medName.trim();
		level = level.trim();

		if (medName.equals(""))
			return;

		boolean medical = new BHRBenefitCategory(categoryId).getDescription().contains("Medical");
		BHRBenefitCategory rxCategory = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Medical Rx", false, true, HrBenefitCategory.HEALTH));

		BHRBenefit ppoBenefit = new BHRBenefit(BHRBenefit.findOrMakeCASBenefit(medName, categoryId, getBenefitWageTypeId(), groupId));
		BHRBenefit rxBenefit = new BHRBenefit();
		medBenes.put(medName, ppoBenefit.getBenefitId());
		if(medical) {
			rxBenefit = new BHRBenefit(BHRBenefit.findOrMake(medName, rxCategory.getCategoryId(), getBenefitWageTypeId()));
			medBenes.put(medName.replaceAll("Blue Plan", "EHIM Plan"), rxBenefit.getBenefitId());
		}

//		if (!medBenes.containsKey(medName))
//		{
//			BHRBenefit ppoBenefit = new BHRBenefit();
//			ppoBenefit.create();
//			ppoBenefit.setBenefitCategoryId(categoryId);
//			ppoBenefit.setName(medName);
//			ppoBenefit.setWageTypeId(getBenefitWageTypeId());
//			ppoBenefit.insert();
//
//			medBenes.put(medName, ppoBenefit.getBenefitId());
//		}

		if (level.equals(""))
			return;

		HrBenefitConfig config = new HrBenefitConfig();
		//Check to see if the config exists first.  If not create it.
		if(medical) {
			config = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, level)
																							  .eq(HrBenefitConfig.HR_BENEFIT, ppoBenefit.getBean())
																							  .joinTo(HrBenefitConfig.HR_BENEFIT)
																							  .eq(HrBenefit.GROUPID, "MM-" + groupId).first();
		}
		else {
			config = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, level)
																							  .eq(HrBenefitConfig.HR_BENEFIT, ppoBenefit.getBean())
																							  .first();
		}


		HrBenefitConfig rxConfig = new HrBenefitConfig();
		if(medical)
			rxConfig = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, level).eq(HrBenefitConfig.HR_BENEFIT, rxBenefit.getBean()).first();

		if (config != null)
		{
			if(medical) {
				medConfigs.put(medName+"|"+level+"|"+groupId, config.getBenefitConfigId());
				if (rxConfig != null)
					medConfigs.put(medName.replaceAll("Blue Plan", "EHIM Plan")+"|"+level, rxConfig.getBenefitConfigId());
				return;
			}
			else {
				medConfigs.put(medName+"|"+level, config.getBenefitConfigId());
				return;
			}
		}

		//if (!medConfigs.containsKey(medName+"|"+level))
		//{
			BHRBenefitConfig conf = new BHRBenefitConfig();
			BHRBenefitConfig rxConf = new BHRBenefitConfig();
			conf.create();
			conf.setBenefitId(medBenes.get(medName));
			if(medical) {
				rxConf.create();
				rxConf.setBenefitId(medBenes.get(medName.replaceAll("Blue Plan", "EHIM Plan")));
				rxConf.setCoversEmployee(true);
			}

			conf.setCoversEmployee(true);

			if (level.equalsIgnoreCase("EE+1"))
			{
				conf.setCoversEmployeeSpouseOrChildren(true);
				conf.setSpouseNonEmpOrChildren(true);
				conf.setMaxDependents(1);
			}

			if (level.equalsIgnoreCase("FAMILY"))
			{
				conf.setCoversEmployeeSpouseOrChildren(true);
				conf.setSpouseNonEmpOrChildren(true);
			}

			if (level.equalsIgnoreCase("EE+CHILDREN"))
			{
				conf.setCoversChildren(true);
			}

			if (level.equalsIgnoreCase("EE+CHILD"))
			{
				conf.setCoversChildren(true);
				conf.setMaxDependents(1);
			}

			if (level.equalsIgnoreCase("EE+SPOUSE"))
			{
				conf.setSpouseEmployee(true);
				conf.setSpouseNonEmployee(true);
				conf.setMaxDependents(1);
			}

			if (level.toLowerCase().contains("spouse life"))
			{
				conf.setSpouseEmployee(true);
				conf.setSpouseNonEmployee(true);
				conf.setMaxDependents(1);
			}

			if (level.toLowerCase().contains("child(ren) life"))
			{
				conf.setCoversChildren(true);
			}

			if (level.toLowerCase().contains("ad&d spouse"))
			{
				conf.setSpouseEmployee(true);
				conf.setSpouseNonEmployee(true);
				conf.setMaxDependents(1);
			}

			if (level.toLowerCase().contains("ad&d child"))
			{
				conf.setCoversChildren(true);
				conf.setMaxDependents(1);
			}

			conf.setName(level);
			conf.insert();

			if(medical) {
				if (level.equalsIgnoreCase("EE+1"))
				{
					rxConf.setCoversEmployeeSpouseOrChildren(true);
					rxConf.setSpouseNonEmpOrChildren(true);
					rxConf.setMaxDependents(1);
				}

				if (level.equalsIgnoreCase("FAMILY"))
				{
					rxConf.setCoversEmployeeSpouseOrChildren(true);
					rxConf.setSpouseNonEmpOrChildren(true);
				}

				if (level.equalsIgnoreCase("EE+CHILDREN"))
				{
					rxConf.setCoversChildren(true);
				}

				if (level.equalsIgnoreCase("EE+CHILD"))
				{
					rxConf.setCoversChildren(true);
					rxConf.setMaxDependents(1);
				}

				if (level.equalsIgnoreCase("EE+SPOUSE"))
				{
					rxConf.setSpouseEmployee(true);
					rxConf.setSpouseNonEmployee(true);
					rxConf.setMaxDependents(1);
				}

				if (level.toLowerCase().contains("spouse life"))
				{
					rxConf.setSpouseEmployee(true);
					rxConf.setSpouseNonEmployee(true);
					rxConf.setMaxDependents(1);
				}

				if (level.toLowerCase().contains("child(ren) life"))
				{
					rxConf.setCoversChildren(true);
				}

				if (level.toLowerCase().contains("ad&d spouse"))
				{
					rxConf.setSpouseEmployee(true);
					rxConf.setSpouseNonEmployee(true);
					rxConf.setMaxDependents(1);
				}

				if (level.toLowerCase().contains("ad&d child"))
				{
					rxConf.setCoversChildren(true);
					rxConf.setMaxDependents(1);
				}

				rxConf.setName(level);
				rxConf.insert();
				medConfigs.put(medName.replaceAll("Blue Plan", "EHIM Plan")+"|"+level, rxConf.getId());
			}

			if(medical)
				medConfigs.put(medName+"|"+level+"|"+groupId, conf.getId());
			else
				medConfigs.put(medName+"|"+level, conf.getId());
		//}
	}

	@Override
	public String[] getCompanyOrgGroupNames() {
		return new String[0];
	}

	@Override
	public String getEmployeeRef() {
		return dfr.getString(EMPID);
	}

	@Override
	public String getScreenGroupId() {
		return screenGroupId;
	}

	@Override
	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setScreenGroupId(String sgId)
	{
		screenGroupId=sgId;
	}


	public void setSecurityGroupId(String sgId)
	{
		securityGroupId=sgId;
	}

	public void setFileName(String fname)
	{
		dFileName=fname;
	}



	@Override
	public List<EmployeeBenefit> getStandardBenefits() {
		List<EmployeeBenefit> ebl=new ArrayList<EmployeeBenefit>(miscBenefits.size());
		for (String id : miscBenefits)
			ebl.add(new EmployeeBenefit(id, parseDate(dfr.getString(11)), changeReasonId));
		return ebl;
	}

	@Override
	public boolean nextRecord() {
		try {
			String currentId = getSSN();
			boolean ret=true;
			while (ret && getSSN().equals(currentId)) {
				ret = dfr.nextLine();
			}
			return ret;
		} catch (Exception ex) {
			Logger.getLogger(StandardCASMap.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
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

		if (val.length()>9)
			val=val.substring(0,9);


	//	System.out.println(val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5));
		return val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5);
	}

	@Override
	public String getSSN() {
		return fixSSN(dfr.getString(SSN));
	}

	@Override
	public String getFirstName() {
		return dfr.getString(FNAME);
	}

	@Override
	public String getMiddleName() {
		return dfr.getString(MNAME);
	}

	@Override
	public String getLastName() {
//		System.out.println(dfr.getString(LNAME));
		return dfr.getString(LNAME);
	}

	@Override
	public String getSex() {
		return dfr.getString(GENDER);
	}

	@Override
	public int getDob() {
		return getDate(DOB);
	}

	private int getDate(int col)
	{
		try
		{
			//System.out.println(dfr.getString(col));
			return DateUtils.getDate(dfr.getString(col));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public String getHomePhone() {
		return dfr.getString(HOMEPHONE);
	}

	@Override
	public String getMobilePhone() {
		return "";
	}

	@Override
	public String getAddressLine1() {
		return dfr.getString(STREET1);
	}

	@Override
	public String getAddressLine2() {
		return dfr.getString(STREET2);
	}

	@Override
	public String getCity() {
		return dfr.getString(CITY);
	}

	@Override
	public String getStateProvince() {
		return dfr.getString(STATE);
	}

	@Override
	public String getZipPostalCode() {
		return dfr.getString(ZIP);
	}

	@Override
	public String getDriversLicenseNumber() {
		return "";
	}

	@Override
	public int getExpectedHoursPerPayPeriod() {
		return 40;
	}

	@Override
	public String getCountry() {
		return "US";
	}

	@Override
	public List<EmployeeStatus> getStatuses() {
		List<EmployeeStatus> stats=new ArrayList<EmployeeStatus>();
		int hireDate=getDate(5);
		if (hireDate==0)
			hireDate=DateUtils.now();
		stats.add(new EmployeeStatus(activeId, hireDate));
		return stats;

	}

	@Override
	public String getJobTitle() {
//		System.out.println("&&&"+dfr.getString(JOBTITLE));
		return dfr.getString(JOBTITLE);
	}

	@Override
	public List<EmployeeWageAndPosition> getWageAndPositions() {
		List<EmployeeWageAndPosition> plist=new ArrayList<EmployeeWageAndPosition>();

		String wageId=unknownWageTypeId;
		char t=dfr.getString(PAYTYPE).length()>0?dfr.getString(PAYTYPE).charAt(0):'H';
		double pay=0;
		if (t=='H')
		{
			wageId=hourlyWageTypeId;
		}
		if (t=='S')
		{
			wageId=salaryWageTypeId;
		}

		pay=!isEmpty(dfr.getString(PAY))?dfr.getDouble(PAY):0;

		int statusDate=getDate(HIRE);
		plist.add(new EmployeeWageAndPosition(wageId, pay, statusDate, unknownPositionId));
		return plist;
	}

	@Override
	public List<EmployeeOrgGroup> getOrgGroups() {
		List<EmployeeOrgGroup> o=new ArrayList<EmployeeOrgGroup>();
		o.add(new EmployeeOrgGroup(getCompanyId()));
		return o;
	}

	private void setBene(List<EmployeeBenefit> l, int ben, int lvl, int start)
	{
		if (isEmpty(dfr.getString(lvl)))
			return;
		boolean medical = dfr.getString(ben).contains("Blue Plan");

//		System.out.println("-----------"+dfr.getString(ben)+"|"+dfr.getString(lvl));
		
		String configId=medConfigs.get(dfr.getString(ben)+"|"+dfr.getString(lvl));
//		System.out.println(configId);
		l.add(new EmployeeBenefit(configId, getDate(start), changeReasonId));

		if(medical) {
			String rxConfigId=medConfigs.get(dfr.getString(ben).replaceAll("Blue Plan", "EHIM Plan")+"|"+dfr.getString(lvl));
//		System.out.println(configId);
			l.add(new EmployeeBenefit(rxConfigId, getDate(start), changeReasonId));
		}
	}

	private void setBene(List<EmployeeBenefit> l, int ben, int lvl, int start, String groupId)
	{
		if (isEmpty(dfr.getString(lvl)))
			return;
		boolean medical = dfr.getString(ben).contains("Blue Plan");

//		System.out.println("-----------"+dfr.getString(ben)+"|"+dfr.getString(lvl)+"|"+groupId);

		String configId=medConfigs.get(dfr.getString(ben)+"|"+dfr.getString(lvl)+"|"+groupId);
//		System.out.println(configId);
		l.add(new EmployeeBenefit(configId, getDate(start), changeReasonId));

		if(medical) {
			String rxConfigId=medConfigs.get(dfr.getString(ben).replaceAll("Blue Plan", "EHIM Plan")+"|"+dfr.getString(lvl));
//		System.out.println(configId);
			l.add(new EmployeeBenefit(rxConfigId, getDate(start), changeReasonId));
		}
	}

	private void setBene(List<EmployeeBenefit> l, int ben, int lvl, int start, int amt)
	{
		if (isEmpty(dfr.getString(lvl).trim()))
			return;

//		System.out.println("-----------"+dfr.getString(ben)+"|"+dfr.getString(lvl));

		String configId=medConfigs.get(dfr.getString(ben)+"|"+dfr.getString(lvl));
		double covered=0;
		if (!isEmpty(dfr.getString(amt)))
			covered=dfr.getDouble(amt);
//		System.out.println("\"" + dfr.getString(ben) + "\": " + "Start "+start+" "+dfr.getString(start));
		l.add(new EmployeeBenefit(configId, getDate(start), covered, changeReasonId));
	}
	
	@Override
	public List<EmployeeBenefit> getBenefits() {
		List<EmployeeBenefit> ret=new ArrayList<EmployeeBenefit>();

		setBene(ret, MED1, MED1LVL, MED1START, dfr.getString(MMREFID));
		setBene(ret, MED2, MED2LVL, MED2START, dfr.getString(MMREFID));
		setBene(ret, DENTAL, DENTALLVL, DENTALSTART);
		setBene(ret, VISION, VISIONLVL, VISIONSTART);
		setBene(ret, LIFE, LIFE, LIFESTART, LIFEAMT);
		setBene(ret, FLEX1, FLEX1, FLEX1START, FLEX1AMT);
		setBene(ret, FLEX2, FLEX2, FLEX2START, FLEX2AMT);

		setBene(ret, VOL1, VOL1, VOL1START, VOL1AMT);
		setBene(ret, VOL2, VOL2, VOL2START, VOL2AMT);
		setBene(ret, VOL3, VOL3, VOL3START, VOL3AMT);
		setBene(ret, VOL4, VOL4, VOL4START, VOL4AMT);
		setBene(ret, VOL5, VOL5, VOL5START, VOL5AMT);
		setBene(ret, VOL6, VOL6, VOL6START, VOL6AMT);
		setBene(ret, VOL7, VOL7, VOL7START, VOL7AMT);
		setBene(ret, VOL8, VOL8, VOL8START, VOL8AMT);

		return ret;
	}


	@Override
	public double getCurrentSickTotal() {
		return 0;
	}

	@Override
	public double getCurrentVacationTotal() {
		return 0;
	}

	@Override
	public String getLoginFileName() {
		return "logins.csv";
	}


	@Override
	public String getBenefitClass() {
		if(!isEmpty(dfr.getString(BENEFITCLASS).trim()))
		{
			BenefitClass bc = ArahantSession.getHSU().createCriteria(BenefitClass.class).joinTo(BenefitClass.BENEFITS)
																						.eq(HrBenefit.GROUPID, "MM-" + dfr.getString(BENEFITCLASS))
																						.first();

			if(bc == null)
			{
				BBenefitClass bbc = new BBenefitClass();
				bbc.create();
				bbc.setName(dfr.getString(BENEFITCLASS));
				bbc.setDescription(dfr.getString(BENEFITCLASS));
				bbc.insert();
				ArahantSession.getHSU().flush();
				System.out.println("New Benefit Class Created: " + dfr.getString(BENEFITCLASS));

				return bbc.getId();
			}
			else
			{
				return bc.getBenefitClassId();
			}
		}
		return "";

	}

	@Override
	public String getOrgGroup() {
		if(!isEmpty(dfr.getString(ORGGROUP).trim()))
		{
			OrgGroup bc = ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE).eq(OrgGroup.NAME, dfr.getString(ORGGROUP)).first();

			if(bc == null)
			{
				BOrgGroup bo = new BOrgGroup();
				bo.create();
				bo.setName(dfr.getString(ORGGROUP));
				bo.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
				bo.insert();
				ArahantSession.getHSU().flush();

				bo.setParent(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
				bo.update();
				ArahantSession.getHSU().flush();

				return bo.getId();
			}
			else
			{
				return bc.getOrgGroupId();
			}
		}
		return "";
	}

		/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		try {
			ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class, "00001-0000072632"));
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().dontAIIntegrate();

			StandardCASMap importMap=new StandardCASMap("/home/xichen/NetBeansProjects/Arahant/build/test/Adapted CAS Employee Import 20110412 Alb.csv");
			importMap.setScreenGroupId("00001-0000000260");
			importMap.setSecurityGroupId("00000-0000000001");
            DataImport.doEmployeeImport(importMap);

            ArahantSession.getHSU().commitTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(StandardCASMap.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}
