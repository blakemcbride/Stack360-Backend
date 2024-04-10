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

import com.arahant.beans.HrBenefitProjectJoin;
import com.arahant.beans.HrBenefitProjectJoinId;
import com.arahant.beans.WageType;
import com.arahant.business.*;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public abstract class ImportMap {

	protected SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	protected HashMap<String, String> orgGroupIdsByName = new HashMap<String, String>();
	protected static HashSet<String> miscBenefits = new HashSet<String>();
	protected String projectStatusId;
	protected String projectCategoryId;
	protected String projectTypeId;
	private String benefitWageTypeId;

	public String getBenefitWageTypeId() {
		benefitWageTypeId = BWageType.findOrMake("Benefit", null, WageType.PERIOD_ONE_TIME, (short) WageType.TYPE_DEDUCTION);

//		if (benefitWageTypeId==null)
//		{
//			BWageType benefitWageType = new BWageType();
//			benefitWageTypeId = benefitWageType.create();
//			benefitWageType.setIsDeduction(false);
//			benefitWageType.setName("Benefit");
//			benefitWageType.setPeriodType(WageType.PERIOD_ONE_TIME);
//			benefitWageType.setType(WageType.TYPE_DEDUCTION);
//			benefitWageType.insert();
//		}
//
		return benefitWageTypeId;
	}

	/**
	 * Order: 1 - Called to initialize your import with the reader
	 */
	//public void setReader(DelimitedFileReader dfr) throws Exception;
	/**
	 * Order: 3 - Called to allow your import to create setups that will not
	 * come during processing of the import file. Often there are setups (like
	 * employee statuses) that may not be used by all employees in the import.
	 * It therefore does not make sense to use a lazy-create mode of just when
	 * it is encountered on an employee record. Those setups should be created
	 * here.
	 */
	public abstract void createNonImportSetups();

	public abstract String[] getCompanyOrgGroupNames();

	public List<Dependent> getDependents() {
		return new ArrayList<Dependent>();
	}

	public abstract String getEmployeeRef();

	public String getEarnedIncomeCreditStatus() {
		return " ";
	}

	public int getFederalExemptions() {
		return 0;
	}

	public double getFederalExtraWithhold() {
		return 0;
	}

	public String getMaritalStatus() {
		return " ";
	}

	public int getStateExemptions() {
		return 0;
	}

	public double getStateExtraWithhold() {
		return 0;
	}

	public abstract String getScreenGroupId();

	public abstract String getSecurityGroupId();

	public abstract List<EmployeeBenefit> getStandardBenefits();

	/**
	 * Order: 4 Called during employee import processing.
	 */
	public abstract boolean nextRecord();

	public abstract String getSSN();

	public abstract String getFirstName();

	public abstract String getMiddleName();

	public abstract String getLastName();

	public abstract String getSex();

	public abstract int getDob();

	public abstract String getHomePhone();

	public abstract String getMobilePhone();

	public abstract String getAddressLine1();

	public abstract String getAddressLine2();

	public abstract String getCity();

	public abstract String getStateProvince();

	public abstract String getZipPostalCode();

	public abstract String getDriversLicenseNumber();

	public abstract int getExpectedHoursPerPayPeriod();

	public abstract String getCountry();

	public abstract List<EmployeeStatus> getStatuses();

	public abstract String getJobTitle();

	public abstract List<EmployeeWageAndPosition> getWageAndPositions();

	public abstract List<EmployeeOrgGroup> getOrgGroups();

	public abstract List<EmployeeBenefit> getBenefits();
	//public abstract String getVacationBenefitId();
	//public abstract String getSickBenefitId();

	public abstract double getCurrentSickTotal();

	public abstract double getCurrentVacationTotal();

	public abstract String getLoginFileName();

	public abstract String getBenefitClass();

	public abstract String getOrgGroup();

	public void preProcess() {
		for (String name : getCompanyOrgGroupNames())
			createOrgGroup(name);

		createNonImportSetups();
	}

	public String getCompanyId() {

		return ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
	}

	public boolean skipFirst() {
		return true;
	}

	protected String createOrgGroup(String name) {
		BOrgGroup orgGroup = new BOrgGroup();
		String id = orgGroup.create();
		orgGroup.setName(name);
		orgGroup.setOrgGroupType(ArahantConstants.COMPANY_TYPE);
		orgGroup.insert();

		orgGroup.setParent(getCompanyId());

		this.orgGroupIdsByName.put(orgGroup.getName(), orgGroup.getOrgGroupId());
		return id;
	}

	protected int parseDate(String date) {
		try {
			return DateUtils.getDate(sdf.parse(date));
		} catch (ParseException ex) {
			Logger.getLogger(ImportMap.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}

	protected String createBenefitProject(String categoryId, String name, String ruleName) {
		String ret;
		BHRBenefit benefit = new BHRBenefit();
		ret = benefit.create();
		benefit.setCategoryId(categoryId);
		benefit.setName(name);
		benefit.setRuleName(ruleName);
		benefit.setTimeRelated(true);
		benefit.setPaid(true);
		benefit.setWageTypeId(getBenefitWageTypeId());
		benefit.insert();

		BHRBenefitConfig config = new BHRBenefitConfig();
		String conid = config.create();
		miscBenefits.add(conid);
		config.setBenefitId(benefit.getBenefitId());
		config.setName(name);
		config.setCoversEmployee(true);
		config.insert();
		ArahantSession.getHSU().flush();

		BProject project = new BProject();
		project.create();
		project.setDescription(name);
		project.setDetailDesc(name);
		project.setProjectStatusId(projectStatusId);
		project.setProjectCategoryId(projectCategoryId);
		project.setProjectTypeId(projectTypeId);
		project.setRequestingOrgGroupId(getCompanyId());
		project.insert();
//System.out.println("********"+config.getId());
		//	project.associateBenefitConfigIds(new String[]{conid});

		HrBenefitProjectJoinId bpjid = new HrBenefitProjectJoinId();
		bpjid.setBenefitConfigId(conid);
		bpjid.setProjectId(project.getProjectId());

		HrBenefitProjectJoin bpj = new HrBenefitProjectJoin();
		bpj.setId(bpjid);
		bpj.setHrBenefitConfig(config.getBean());
		bpj.setProject(project.getBean());

		ArahantSession.getHSU().saveOrUpdate(bpj);

		return ret;
	}
	protected String vacationBenefitId;

	public String getVacationBenefitId() {
		return vacationBenefitId;
	}
	protected String sickBenefitId;

	public String getSickBenefitId() {
		return sickBenefitId;
	}

	protected boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	protected class Name {

		public String fname;
		public String lname;
		public String mname = "";

		public Name(String name) {
			if (isEmpty(name))
				name = ". .";

			if (name.indexOf(' ') == -1) {
				fname = ".";
				lname = name;
			} else {
				lname = name.substring(0, name.indexOf(' ')).trim();
				fname = name.substring(name.indexOf(' ') + 1).trim();

				int spos = fname.indexOf(' ');
				if (spos != -1) {
					mname = fname.substring(spos + 1).trim();
					if (mname.length() > 20)
						mname = mname.substring(0, 20);
					fname = fname.substring(0, spos).trim();
				}
			}

			if (lname.endsWith(","))
				lname = lname.substring(0, lname.length() - 1);
			if (mname.endsWith(","))
				mname = mname.substring(0, mname.length() - 1);
		}
	}
}
