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


package com.arahant.imports.census;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class StandardMap extends ImportMap {

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
	private HashMap<String, String> medBenes = new HashMap<String, String>();
	private HashMap<String, String> medConfigs = new HashMap<String, String>();
	private static final int EMPID = 0;
	private static final int SSN = 1;
	private static final int FNAME = 2;
	private static final int MNAME = 3;
	private static final int LNAME = 4;
	private static final int HIRE = 5;
	private static final int STREET1 = 6;
	private static final int STREET2 = 7;
	private static final int CITY = 8;
	private static final int STATE = 9;
	private static final int ZIP = 10;
	private static final int DOB = 11;
	private static final int GENDER = 12;
	private static final int PAYTYPE = 13;
	private static final int PAY = 14;
	private static final int HOMEPHONE = 15;
	private static final int JOBTITLE = 16;
	private static final int BENEFITCLASS = 17;
	private static final int ORGGROUP = 18;
	private static final int MED1 = 19;
	private static final int MED1LVL = 20;
	private static final int MED1START = 21;
	private static final int MED2 = 22;
	private static final int MED2LVL = 23;
	private static final int MED2START = 24;
	private static final int DENTAL = 25;
	private static final int DENTALLVL = 26;
	private static final int DENTALSTART = 27;
	private static final int VISION = 28;
	private static final int VISIONLVL = 29;
	private static final int VISIONSTART = 30;
	private static final int LIFE = 31;
	private static final int LIFESTART = 32;
	private static final int LIFEAMT = 33;
	private static final int FLEX1 = 34;
	private static final int FLEX1START = 35;
	private static final int FLEX1AMT = 36;
	private static final int FLEX2 = 37;
	private static final int FLEX2START = 38;
	private static final int FLEX2AMT = 39;
	private static final int VOL1 = 40;
	private static final int VOL1START = 41;
	private static final int VOL1AMT = 42;
	private static final int VOL2 = 43;
	private static final int VOL2START = 44;
	private static final int VOL2AMT = 45;
	private static final int VOL3 = 46;
	private static final int VOL3START = 47;
	private static final int VOL3AMT = 48;
	private static final int VOL4 = 49;
	private static final int VOL4START = 50;
	private static final int VOL4AMT = 51;
	private static final int VOL5 = 52;
	private static final int VOL5START = 53;
	private static final int VOL5AMT = 54;
	private static final int VOL6 = 55;
	private static final int VOL6START = 56;
	private static final int VOL6AMT = 57;
	private static final int VOL7 = 58;
	private static final int VOL7START = 59;
	private static final int VOL7AMT = 60;
	private static final int VOL8 = 61;
	private static final int VOL8START = 62;
	private static final int VOL8AMT = 63;

	public StandardMap(String fname) {
		try {
			dFileName = fname;
			dfr = new DelimitedFileReader(dFileName);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(StandardMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public StandardMap() {
		try {
			dfr = new DelimitedFileReader(dFileName);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(StandardMap.class.getName()).log(Level.SEVERE, null, ex);
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
		BGlAccount wagesAccount = new BGlAccount(BGlAccount.findOrMake("Wages"));

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
		 * // create default project config if not present ProjectCategory
		 * projectCategoryBean =
		 * ArahantSession.getHSU().createCriteria(ProjectCategory.class).eq(ProjectCategory.CODE,
		 * "Misc").first(); if (projectCategoryBean == null) { BProjectCategory
		 * category = new BProjectCategory(); this.projectCategoryId =
		 * category.create(); category.setCode("Misc");
		 * category.setDescription("Catch-all Category for Projects");
		 * category.setScope(ProjectCategory.SCOPE_GLOBAL); category.insert(); }
		 * else { this.projectCategoryId =
		 * projectCategoryBean.getProjectCategoryId(); }
		 *
		 * ProjectStatus projectStatusBean =
		 * ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE,
		 * "Misc").first(); if (projectStatusBean == null) { BProjectStatus
		 * status = new BProjectStatus(); this.projectStatusId =
		 * status.create(); status.setActive('Y'); status.setCode("Misc");
		 * status.setDescription("Catch-all Status for Projects");
		 * status.insert(); } else { this.projectStatusId =
		 * projectStatusBean.getProjectStatusId(); }
		 *
		 * Route routeBean =
		 * ArahantSession.getHSU().createCriteria(Route.class).eq(Route.NAME,
		 * "Misc").first(); if (routeBean == null) { BProjectPhase phase = new
		 * BProjectPhase(); phase.create(); phase.setCategoryId(2); //
		 * development, which is 1 (setter subtracts 1) phase.setCode("Misc");
		 * phase.setDescription("Catch-all Phase for Route Stops");
		 * phase.insert();
		 *
		 * BRoute route = new BRoute(); route.create();
		 * route.setDescription("Catch-all Route for Projects");
		 * route.setName("Misc");
		 * route.setDefaultStatusId(this.projectStatusId); route.insert();
		 *
		 * BRouteStop routeStop = new BRouteStop(); routeStop.create();
		 * routeStop.setDescription("Single Route Stop");
		 * routeStop.setPhaseId(phase.getId());
		 * routeStop.setProjectStatusIds(new String[] {this.projectStatusId});
		 * routeStop.setRouteId(route.getRouteId()); routeStop.insert();
		 *
		 * route.setDefaultRouteStop(routeStop.getRouteStopId());
		 * route.update();
		 *
		 * BProjectType type = new BProjectType();
		 * this.projectTypeId=type.create(); type.setCode("Time Off Projects");
		 * type.setDescription("Projects for Time Off");
		 * type.setScope(ProjectType.SCOPE_GLOBAL); type.insert(); } else {
		 *
		 * ProjectType
		 * t=ArahantSession.getHSU().createCriteria(ProjectType.class).eq(ProjectStatus.CODE,
		 * "Time Off Projects").first();
		 *
		 * if (t==null) { BProjectType bt=new BProjectType();
		 * projectTypeId=bt.create(); bt.setCode("Time Off Projects");
		 * bt.setDescription("Time Off Projects");
		 * bt.setScope(ProjectType.SCOPE_INTERNAL); bt.insert(); } else
		 * this.projectTypeId = t.getProjectTypeId();
		 *
		 * }
		 *
		 *
		 * miscBenefits.clear();
		 * vacationBenefitId=createBenefitProject(miscCategory.getCategoryId(),
		 * "Vacation", "Vacation");
		 * sickBenefitId=createBenefitProject(miscCategory.getCategoryId(),
		 * "Sick", "Sick"); createBenefitProject(miscCategory.getCategoryId(),
		 * "Holiday", "Holiday");
		 *
		 */
		//create the benefits
		try {
			DelimitedFileReader bdfr = new DelimitedFileReader(dFileName);

			bdfr.nextLine();

			while (bdfr.nextLine()) {
				makeBenefit(bdfr.getString(MED1), bdfr.getString(MED1LVL), medicalCategory.getCategoryId());
				makeBenefit(bdfr.getString(MED2), bdfr.getString(MED2LVL), medical2Category.getCategoryId());

				makeBenefit(bdfr.getString(DENTAL), bdfr.getString(DENTALLVL), dentalCategory.getCategoryId());

				makeBenefit(bdfr.getString(VISION), bdfr.getString(VISIONLVL), visionCategory.getCategoryId());

				makeBenefit(bdfr.getString(LIFE), bdfr.getString(LIFE), lifeCategory.getCategoryId());

				makeBenefit(bdfr.getString(FLEX1), bdfr.getString(FLEX1), flexCategory.getCategoryId());
				makeBenefit(bdfr.getString(FLEX2), bdfr.getString(FLEX2), flexCategory.getCategoryId());

				for (int i = VOL1; i <= VOL8; i += 3)
					if (!bdfr.getString(i + 2).toLowerCase().contains("waiv")) {
						String categoryName = bdfr.getString(i).toLowerCase();
//						DecimalFormat format = new DecimalFormat("#,###,###.00");
						if (categoryName.contains("life"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryLifeCategory.getCategoryId());
//						else if(categoryName.contains("life"))
//							makeBenefit(bdfr.getString(i), bdfr.getString(i) + " $" + format.format(Float.valueOf(bdfr.getString(i+2))), voluntaryLifeCategory.getCategoryId());
						else if (categoryName.contains("&"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryADDCategory.getCategoryId());
						else if (categoryName.contains("short") || categoryName.contains("st"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntarySTDCategory.getCategoryId());
						else if (categoryName.contains("long") || categoryName.contains("lt"))
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryLTDCategory.getCategoryId());
						else
							makeBenefit(bdfr.getString(i), bdfr.getString(i), voluntaryCategory.getCategoryId());
					}
//				makeBenefit(bdfr.getString(VOL2), bdfr.getString(VOL2), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL3), bdfr.getString(VOL3), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL4), bdfr.getString(VOL4), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL5), bdfr.getString(VOL5), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL6), bdfr.getString(VOL6), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL7), bdfr.getString(VOL7), voluntaryCategory.getCategoryId());
//				makeBenefit(bdfr.getString(VOL8), bdfr.getString(VOL8), voluntaryCategory.getCategoryId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeBenefit(String medName, String level, String categoryId) {
		medName = medName.trim();
		level = level.trim();

		if (medName.equals(""))
			return;

		BHRBenefit ppoBenefit = new BHRBenefit(BHRBenefit.findOrMake(medName, categoryId, getBenefitWageTypeId()));
		medBenes.put(medName, ppoBenefit.getBenefitId());

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

		//Check to see if the config exists first.  If not create it.
		HrBenefitConfig config = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, level).eq(HrBenefitConfig.HR_BENEFIT, ppoBenefit.getBean()).first();

		if (config != null) {
			medConfigs.put(medName + "|" + level, config.getBenefitConfigId());
			return;
		}

		//if (!medConfigs.containsKey(medName+"|"+level))
		//{
		BHRBenefitConfig conf = new BHRBenefitConfig();
		conf.create();
		conf.setBenefitId(medBenes.get(medName));
		conf.setCoversEmployee(true);

		if (level.equalsIgnoreCase("EE+1")) {
			conf.setCoversEmployeeSpouseOrChildren(true);
			conf.setSpouseNonEmpOrChildren(true);
			conf.setMaxDependents(1);
		}

		if (level.equalsIgnoreCase("FAMILY")) {
			conf.setCoversEmployeeSpouseOrChildren(true);
			conf.setSpouseNonEmpOrChildren(true);
		}

		if (level.equalsIgnoreCase("EE+CHILDREN"))
			conf.setCoversChildren(true);

		if (level.equalsIgnoreCase("EE+CHILD")) {
			conf.setCoversChildren(true);
			conf.setMaxDependents(1);
		}

		if (level.equalsIgnoreCase("EE+SPOUSE")) {
			conf.setSpouseEmployee(true);
			conf.setSpouseNonEmployee(true);
			conf.setMaxDependents(1);
		}

		if (level.toLowerCase().contains("spouse life")) {
			conf.setSpouseEmployee(true);
			conf.setSpouseNonEmployee(true);
			conf.setMaxDependents(1);
		}

		if (level.toLowerCase().contains("child(ren) life"))
			conf.setCoversChildren(true);

		if (level.toLowerCase().contains("ad&d spouse")) {
			conf.setSpouseEmployee(true);
			conf.setSpouseNonEmployee(true);
			conf.setMaxDependents(1);
		}

		if (level.toLowerCase().contains("ad&d child")) {
			conf.setCoversChildren(true);
			conf.setMaxDependents(1);
		}

		conf.setName(level);
		conf.insert();

		medConfigs.put(medName + "|" + level, conf.getId());
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

	public void setScreenGroupId(String sgId) {
		screenGroupId = sgId;
	}

	public void setSecurityGroupId(String sgId) {
		securityGroupId = sgId;
	}

	public void setFileName(String fname) {
		dFileName = fname;
	}

	@Override
	public List<EmployeeBenefit> getStandardBenefits() {
		List<EmployeeBenefit> ebl = new ArrayList<EmployeeBenefit>(miscBenefits.size());
		for (String id : miscBenefits)
			ebl.add(new EmployeeBenefit(id, parseDate(dfr.getString(11)), changeReasonId));
		return ebl;
	}

	@Override
	public boolean nextRecord() {
		try {
			String currentId = getSSN();
			boolean ret = true;
			while (ret && getSSN().equals(currentId))
				ret = dfr.nextLine();
			return ret;
		} catch (Exception ex) {
			Logger.getLogger(StandardMap.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private String fixSSN(String val) {

		if (val == null)
			return null;

		if (val.length() == 11)
			return val;
		if (val.length() == 0)
			return null;
		while (val.length() < 9)
			val = '0' + val;

		if (val.length() > 9)
			val = val.substring(0, 9);


		//	System.out.println(val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5));
		return val.substring(0, 3) + "-" + val.substring(3, 5) + "-" + val.substring(5);
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

	private int getDate(int col) {
		try {
			//System.out.println(dfr.getString(col));
			return DateUtils.getDate(dfr.getString(col));
		} catch (Exception e) {
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
		List<EmployeeStatus> stats = new ArrayList<EmployeeStatus>();
		int hireDate = getDate(5);
		if (hireDate == 0)
			hireDate = DateUtils.now();
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
		List<EmployeeWageAndPosition> plist = new ArrayList<EmployeeWageAndPosition>();

		String wageId = unknownWageTypeId;
		char t = dfr.getString(PAYTYPE).length() > 0 ? dfr.getString(PAYTYPE).charAt(0) : 'H';
		double pay;
		if (t == 'H')
			wageId = hourlyWageTypeId;
		if (t == 'S')
			wageId = salaryWageTypeId;

		pay = !isEmpty(dfr.getString(PAY)) ? dfr.getDouble(PAY) : 0;

		int statusDate = getDate(HIRE);
		plist.add(new EmployeeWageAndPosition(wageId, pay, statusDate, unknownPositionId));
		return plist;
	}

	@Override
	public List<EmployeeOrgGroup> getOrgGroups() {
		List<EmployeeOrgGroup> o = new ArrayList<EmployeeOrgGroup>();
		o.add(new EmployeeOrgGroup(getCompanyId()));
		return o;
	}

	private void setBene(List<EmployeeBenefit> l, int ben, int lvl, int start) {
		if (isEmpty(dfr.getString(lvl)))
			return;

//		System.out.println("-----------"+dfr.getString(ben)+"|"+dfr.getString(lvl));

		String configId = medConfigs.get(dfr.getString(ben) + "|" + dfr.getString(lvl));
//		System.out.println(configId);
		l.add(new EmployeeBenefit(configId, getDate(start), changeReasonId));
	}

	private void setBene(List<EmployeeBenefit> l, int ben, int lvl, int start, int amt) {
		if (isEmpty(dfr.getString(lvl).trim()))
			return;

//		System.out.println("-----------"+dfr.getString(ben)+"|"+dfr.getString(lvl));

		String configId = medConfigs.get(dfr.getString(ben) + "|" + dfr.getString(lvl));
		double covered = 0;
		if (!isEmpty(dfr.getString(amt)))
			covered = dfr.getDouble(amt);
//		System.out.println("\"" + dfr.getString(ben) + "\": " + "Start "+start+" "+dfr.getString(start));
		l.add(new EmployeeBenefit(configId, getDate(start), covered, changeReasonId));
	}

	@Override
	public List<EmployeeBenefit> getBenefits() {
		List<EmployeeBenefit> ret = new ArrayList<EmployeeBenefit>();

		setBene(ret, MED1, MED1LVL, MED1START);
		setBene(ret, MED2, MED2LVL, MED2START);
		setBene(ret, DENTAL, DENTALLVL, DENTALSTART);
		setBene(ret, VISION, VISIONLVL, VISIONSTART);
		setBene(ret, LIFE, LIFE, LIFESTART);
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

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().dontAIIntegrate();

			StandardMap importMap = new StandardMap("/Users/sample2.csv");
			importMap.setScreenGroupId("00001-0000000158");
			importMap.setSecurityGroupId("00000-0000000001");
			DataImport.doEmployeeImport(importMap);

			ArahantSession.getHSU().commitTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(StandardMap.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public String getBenefitClass() {
		if (!isEmpty(dfr.getString(BENEFITCLASS).trim())) {
			BenefitClass bc = ArahantSession.getHSU().createCriteria(BenefitClass.class).eq(BenefitClass.NAME, dfr.getString(BENEFITCLASS)).first();

			if (bc == null) {
				BBenefitClass bbc = new BBenefitClass();
				bbc.create();
				bbc.setName(dfr.getString(BENEFITCLASS));
				bbc.setDescription(dfr.getString(BENEFITCLASS));
				bbc.insert();
				ArahantSession.getHSU().flush();

				return bbc.getId();
			} else
				return bc.getBenefitClassId();
		}
		return "";
	}

	@Override
	public String getOrgGroup() {
		if (!isEmpty(dfr.getString(ORGGROUP).trim())) {
			OrgGroup bc = ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE).eq(OrgGroup.NAME, dfr.getString(ORGGROUP)).first();

			if (bc == null) {
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
			} else
				return bc.getOrgGroupId();
		}
		return "";
	}
}
