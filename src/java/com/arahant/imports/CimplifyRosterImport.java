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

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.beans.Person;
import com.arahant.beans.Property;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.WageType;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREEORace;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BHRNoteCategory;
import com.arahant.business.BHRPosition;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPersonNote;
import com.arahant.business.BProject;
import com.arahant.business.BProjectCategory;
import com.arahant.business.BProjectStatus;
import com.arahant.business.BProjectType;
import com.arahant.business.BProperty;
import com.arahant.business.BWageType;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class CimplifyRosterImport {
HibernateSessionUtil hsu = ArahantSession.getHSU();

    public void importEmployees(String groupsFilename, String idsFilename, String employeesFilename) {
		HashMap<String, String> employeeGroupMap = new HashMap<String, String>(600); //size of file is 365
		HashMap<String, String> employeeIdsMap = new HashMap<String, String>(600);

        try {

			//Save the group association info in a map first
            DelimitedFileReader gfr = new DelimitedFileReader(groupsFilename);
			gfr.nextLine();
			while (gfr.nextLine()) {

				employeeGroupMap.put(gfr.getString(6).trim(), gfr.getString(0).trim() + "-" + gfr.getString(1).trim());

			}

			//Save the employee id info in a map first
            DelimitedFileReader ifr = new DelimitedFileReader(idsFilename);
			ifr.nextLine();
			while (ifr.nextLine()) {

				employeeIdsMap.put(ifr.getString(4).trim()+ ":" + ifr.getString(3).trim(), ifr.getString(2).trim());

			}

            DelimitedFileReader efr = new DelimitedFileReader(employeesFilename);
            //skip header line
            efr.nextLine();

			Property securityProp = hsu.createCriteria(Property.class).eq(Property.NAME, StandardProperty.DEFAULT_SEC_GROUP).first();
			BProperty bSecurityProp = new BProperty();
			if(securityProp == null)
			{
				bSecurityProp.create();
				bSecurityProp.setName(StandardProperty.DEFAULT_SEC_GROUP);
				bSecurityProp.setValue("00000-0000000001");
				bSecurityProp.setDescription(StandardProperty.DEFAULT_SEC_GROUP);
				bSecurityProp.insert();
			}
			else
			{
				bSecurityProp = new BProperty(securityProp);
				bSecurityProp.setValue("00000-0000000001");
				bSecurityProp.update();
			}
			Property screenProp = hsu.createCriteria(Property.class).eq(Property.NAME, StandardProperty.DEFAULT_SCREEN_GROUP).first();
			BProperty bScreenProp = new BProperty();
			if(screenProp == null)
			{
				bScreenProp.create();
				bScreenProp.setName(StandardProperty.DEFAULT_SCREEN_GROUP);
				bScreenProp.setValue("00001-0000000158");
				bScreenProp.setDescription(StandardProperty.DEFAULT_SCREEN_GROUP);
				bScreenProp.insert();
			}
			else
			{
				bScreenProp = new BProperty(screenProp);
				bScreenProp.setValue("00001-0000000158");
				bScreenProp.update();
			}
			hsu.flush();
//			BProperty secGroup = new BProperty(StandardProperty.DEFAULT_SEC_GROUP);
//			secGroup.setValue("00000-0000000001");
//			secGroup.update();
//			BProperty scrGroup = new BProperty(StandardProperty.DEFAULT_SEC_GROUP);
//			scrGroup.setValue("00001-0000000158");
//			scrGroup.update();
			String maidenNoteCategoryId = BHRNoteCategory.findOrMake("Maiden Name");
			String suffixNoteCategoryId = BHRNoteCategory.findOrMake("Suffix");
			String activeStatusId = BHREmployeeStatus.findOrMake("Active", true);
			String inactiveStatusId = BHREmployeeStatus.findOrMake("Inactive", true);
			hsu.flush();

			int count = 0;
            while (efr.nextLine()) {
                if (++count % 50 == 0) {
                    //System.out.println(count);
                }
				String lname = efr.nextString().trim();
				String maidenName = efr.nextString().trim();
				String suffix = efr.nextString().trim();
				String fname = efr.nextString().trim();
				String mname = efr.nextString().trim();
				String nickname = efr.nextString().trim();
				String status = efr.nextString().trim();
				String location = efr.nextString().trim();
				String coCode = efr.nextString().trim();
				String site = efr.nextString().trim();
				String sex = efr.nextString().trim();
				String eeoCode = efr.nextString().trim();
			    String ssn = efr.nextString().trim();

				if (!StringUtils.isEmpty(ssn) && hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.SSN, ssn).exists())
				{
					System.out.println("The ssn " + ssn + " is already in the system!");
					continue;
				}

                int dob = convertDateStringToInt(efr.nextString().trim());
				String maritalStatus = efr.nextString().trim();
                String address = efr.nextString().trim();
                String city = efr.nextString().trim();
                String state = efr.nextString().trim();
                String zip = efr.nextString().trim();
                String phone = efr.nextString().trim();
				int hireDate = convertDateStringToInt(efr.nextString().trim());
				int rehireDate = convertDateStringToInt(efr.nextString().trim());
				int ftDate = convertDateStringToInt(efr.nextString().trim());
				boolean isDoctor = efr.nextString().equals("X");
				String title = efr.nextString().trim();
				String wageType = efr.nextString().trim();
				double hourlyAmount = convertDollarsToDouble(efr.nextString().trim());
				double salaryAmount = convertDollarsToDouble(efr.nextString().trim());
				int weeklyHours = convertToHours(efr.nextString().trim());
				int ptDate = convertDateStringToInt(efr.nextString().trim());
				int termDate = convertDateStringToInt(efr.nextString().trim());
				//boolean isCobra = efr.nextString().equals("X");
				//int benefitsEffectiveDate = convertDateStringToInt(efr.nextString());
				//double benefitSalary = convertDollarsToDouble(efr.nextString());


				BEmployee be = new BEmployee();
				be.create();
				be.setSsn(ssn);
				be.setJobTitle(title);
				be.setFirstName(fname);
				be.setLastName(lname);
				be.setMiddleName(mname);
				be.setNickName(nickname);
				be.setSex(sex);
				be.setDob(dob);
				be.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
				be.setEEORaceId(BHREEORace.findOrMake(eeoCode));
				be.setMaritalStatus(maritalStatus);
				
				be.setDob(dob);
				be.setStreet(address);
				be.setCity(city);
				be.setState(state);
				be.setZip(zip);
				be.setHomePhone(phone);
				be.makeLoginDefaults();

				String externalId = employeeIdsMap.get(ssn + ":" + coCode);

				if(!StringUtils.isEmpty(externalId))
				{
					be.setExtRef(externalId);
				}

				if(rehireDate > 0 && rehireDate > hireDate)
				{
					be.setWorkedForCompanyBefore('Y');
					be.setWorkedForCompanyWhen(DateUtils.getDateFormatted(hireDate));
				}

				be.insert(true);

				hsu.flush();


				//we dont have columns for these fields so just insert as a new note
				if(!StringUtils.isEmpty(suffix))
				{
					BPersonNote note = new BPersonNote();
					note.create();
					note.setNoteCategoryId(suffixNoteCategoryId);
					note.setNote(suffix);
					note.setPersonId(be.getPersonId());
					note.insert();
				}
				if(!StringUtils.isEmpty(maidenName))
				{
					BPersonNote note = new BPersonNote();
					note.create();
					note.setNoteCategoryId(maidenNoteCategoryId);
					note.setNote(suffix);
					note.setPersonId(be.getPersonId());
					note.insert();
				}
				
				short wagePeriod = wageType.equalsIgnoreCase("s")? WageType.PERIOD_SALARY : WageType.PERIOD_HOURLY;

				String wageTypeName = wageType.equalsIgnoreCase("s") ? "Salary" : "Hourly";
				String wageTypeId = BWageType.findOrMakeType(wageTypeName, wagePeriod, WageType.TYPE_REGULAR);
				if(title.length() > 30)
				{
					title = title.substring(0, 30);
				}
				String positionId = BHRPosition.findOrMake(title);
				be.setWageAndPosition(positionId, wageTypeId, wageType.equalsIgnoreCase("s")? salaryAmount : hourlyAmount, hireDate);

				//handle first status
				BHREmplStatusHistory statHist = new BHREmplStatusHistory();
				statHist.create();
				statHist.setEffectiveDate(hireDate);
				statHist.setEmployee(be.getEmployee());
				statHist.setHrEmployeeStatus(new BHREmployeeStatus(activeStatusId).getBean());
				statHist.setNotes("");
				statHist.insert();

				//were they termed?
				if(termDate > 0)
				{
					BHREmplStatusHistory termStatHist = new BHREmplStatusHistory();
					termStatHist.create();
					termStatHist.setEffectiveDate(termDate);
					termStatHist.setEmployee(be.getEmployee());
					termStatHist.setHrEmployeeStatus(new BHREmployeeStatus(inactiveStatusId).getBean());
					termStatHist.setNotes("");
					termStatHist.insert();
				}

				//...and rehired?
				if(rehireDate > 0 && rehireDate > hireDate)
				{
					BHREmplStatusHistory rehireStatHist = new BHREmplStatusHistory();
					rehireStatHist.create();
					rehireStatHist.setEffectiveDate(rehireDate);
					rehireStatHist.setEmployee(be.getEmployee());
					rehireStatHist.setHrEmployeeStatus(new BHREmployeeStatus(activeStatusId).getBean());
					rehireStatHist.setNotes("");
					rehireStatHist.insert();
				}

				String groupAssocationInfo = employeeGroupMap.get(ssn);
				String parentCode = "";
				String locationCode = "";
				String siteCode = "";
				String deptCode = "";
				if(StringUtils.isEmpty(groupAssocationInfo))
				{
					System.out.println("No group information for ssn " + ssn + " (" + fname + " " + lname + ")");
				}
				else
				{
					parentCode = groupAssocationInfo.split("-")[0];
					locationCode = groupAssocationInfo.split("-")[1];
					if(indexOfFirstNonDigit(locationCode) == -1)
					{
						siteCode = locationCode.substring(2,4);
						deptCode = locationCode.substring(4);
					}
				}

				//remove any existing org group associations so we can correctly associate them now
				hsu.delete(hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSONID, be.getPersonId()).list());
				

				OrgGroup og = hsu.createCriteria(OrgGroup.class)
						.eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE)
						.eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany())
						.eq(OrgGroup.NAME, coCode.trim())
						.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
						.eq(OrgGroupHierarchy.PARENT_ID, hsu.getCurrentCompany().getCompanyId())
						.first();
				BOrgGroup parentGroup = new BOrgGroup();
				if(og == null)
				{
					parentGroup.create();
					parentGroup.setCompanyId(hsu.getCurrentCompany().getCompanyId());
					parentGroup.setName(coCode);
					parentGroup.setExternalId(coCode);
					parentGroup.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
					parentGroup.insert();
					hsu.flush();
					//parentGroup.setParent(hsu.getCurrentCompany().getCompanyId());
					BOrgGroup company = new BOrgGroup(hsu.getCurrentCompany().getCompanyId());
					company.assignToThisGroup(parentGroup.getOrgGroupId());
					parentGroup.update();
					hsu.flush();
				}
				else
				{
					parentGroup = new BOrgGroup(og);
				}

				OrgGroup og2 = hsu.createCriteria(OrgGroup.class)
						.eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE)
						.eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany())
						.eq(OrgGroup.NAME, location.trim())
						.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
						.eq(OrgGroupHierarchy.PARENT_ID, parentGroup.getOrgGroupId())
						.first();
				BOrgGroup locationGroup = new BOrgGroup();
				if(og2 == null)
				{
					locationGroup.create();
					locationGroup.setCompanyId(hsu.getCurrentCompany().getCompanyId());
					locationGroup.setName(location);
					locationGroup.setExternalId(location.substring(0, 2));
					locationGroup.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
					locationGroup.insert();
					hsu.flush();
					createDefaultProjectForGroup(locationGroup);
					//locationGroup.setParent(parentGroup.getCompanyId());
					parentGroup.assignToThisGroup(locationGroup.getOrgGroupId());
					locationGroup.update();
					hsu.flush();
				}
				else
				{
					locationGroup = new BOrgGroup(og2);
				}

				//if they dont have valid codes for site and deptartment (00CORP for example)
				if(StringUtils.isEmpty(siteCode) || StringUtils.isEmpty(deptCode))
				{
					OrgGroup og3 = hsu.createCriteria(OrgGroup.class)
							.eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE)
							.eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany())
							.eq(OrgGroup.NAME, site.trim())
							.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
							.eq(OrgGroupHierarchy.PARENT_ID, locationGroup.getOrgGroupId())
							.first();
					BOrgGroup siteGroup = new BOrgGroup();
					if(og3 == null)
					{
						siteGroup.create();
						siteGroup.setCompanyId(hsu.getCurrentCompany().getCompanyId());
						siteGroup.setName(site);
						siteGroup.setExternalId(locationCode);
						siteGroup.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
						siteGroup.insert();
						createDefaultProjectForGroup(siteGroup);
						hsu.flush();
						//siteGroup.setParent(locationGroup.getCompanyId());
						locationGroup.assignToThisGroup(siteGroup.getOrgGroupId());
						siteGroup.update();
						hsu.flush();
					}
					else
					{
						siteGroup = new BOrgGroup(og3);
					}
					String[] personIds = new String[1];
					personIds[0] = be.getPersonId();
					siteGroup.assignPeopleToGroup(personIds);
					System.out.println("{" + count + "} Assigning ssn " + ssn + " (" + fname + " " + lname + ") to site group " + siteGroup.getName() + " - " + siteGroup.getExternalId());
				}
				else
				{
					OrgGroup og3 = hsu.createCriteria(OrgGroup.class)
							.eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE)
							.eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany())
							.eq(OrgGroup.NAME, site.trim())
							.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
							.eq(OrgGroupHierarchy.PARENT_ID, locationGroup.getOrgGroupId())
							.first();
					BOrgGroup siteGroup = new BOrgGroup();
					if(og3 == null)
					{
						siteGroup.create();
						siteGroup.setCompanyId(hsu.getCurrentCompany().getCompanyId());
						siteGroup.setName(site);
						siteGroup.setExternalId(siteCode);
						siteGroup.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
						siteGroup.insert();
						hsu.flush();
						//siteGroup.setParent(locationGroup.getCompanyId());
						createDefaultProjectForGroup(siteGroup);
						locationGroup.assignToThisGroup(siteGroup.getOrgGroupId());
						siteGroup.update();
						hsu.flush();
					}
					else
					{
						siteGroup = new BOrgGroup(og3);
					}

					OrgGroup og4 = hsu.createCriteria(OrgGroup.class)
							.eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE)
							.eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany())
							.eq(OrgGroup.NAME, deptCode)
							.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
							.eq(OrgGroupHierarchy.PARENT_ID, siteGroup.getOrgGroupId())
							.first();
					BOrgGroup deptGroup = new BOrgGroup();
					if(og4 == null)
					{
						deptGroup.create();
						deptGroup.setCompanyId(hsu.getCurrentCompany().getCompanyId());
						deptGroup.setName(deptCode);
						deptGroup.setExternalId(locationCode);
						deptGroup.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
						deptGroup.insert();
						hsu.flush();
						//deptGroup.setParent(siteGroup.getCompanyId());
						createDefaultProjectForGroup(deptGroup);
						siteGroup.assignToThisGroup(deptGroup.getOrgGroupId());
						deptGroup.update();
						hsu.flush();
					}
					else
					{
						deptGroup = new BOrgGroup(og4);
					}

					String[] personIds = new String[1];
					personIds[0] = be.getPersonId();
					deptGroup.assignPeopleToGroup(personIds);
					System.out.println("{" + count + "} Assigning ssn " + ssn + " (" + fname + " " + lname + ") to dept group " + deptGroup.getName() + " - " + deptGroup.getExternalId());
				}


				hsu.commitTransaction();
				hsu.beginTransaction();
			}
        } catch (IOException ex) {
            Logger.getLogger(BoxMakerImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BoxMakerImport.class.getName()).log(Level.SEVERE, null, ex);

        }
    }


	private int convertDateStringToInt(String dateString)
	{
		try {
			int ret = 0;
			int day = 0;
			int month = 0;
			int year = 0;
			String[] dateValues = dateString.split("/");
			if (dateValues != null && dateValues.length == 3) {
				day = Integer.parseInt(dateValues[1]);
				month = Integer.parseInt(dateValues[0]);
				year = Integer.parseInt(dateValues[2]);
			} else {
				return 0;
			}

			if (year < 100) {
				if (year < 12) {
					year += 2000;
				} else {
					year += 1900;
				}
			}

			ret = (year * 10000) + (month * 100) + (day);

			return ret;
		} catch (NumberFormatException numberFormatException) {
			return 0;
		}
	}

	private double convertDollarsToDouble(String dollarString)
	{
		try {
			dollarString = dollarString.replace("$", "");
			dollarString = dollarString.replace(",", "");
			if (!StringUtils.isEmpty(dollarString)) {
				return Double.parseDouble(dollarString);

			}
			return 0.0;
		} catch (NumberFormatException numberFormatException) {
			return 0.0;
		}
	}

	private int convertToHours(String hoursString)
	{
		try {
			return Integer.parseInt(hoursString);
		} catch (NumberFormatException numberFormatException) {
			return 0;
		}
	}

	private int indexOfFirstNonDigit(String homeDept) {

		for(int i = 0; i < homeDept.length(); i++)
		{
			if(!Character.isDigit(homeDept.charAt(i)))
			{
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		ArahantSession.getHSU().setCurrentPersonToArahant();
		new CimplifyRosterImport().importEmployees("/Users/arahant/Desktop/groupAssociations.csv","/Users/arahant/Desktop/employeeExternalIDs.csv","/Users/arahant/Desktop/employeeRoster.csv");
	}

	private void createDefaultProjectForGroup(BOrgGroup group)
	{
		BProject project = new BProject();
		project.create();
		project.setRequestingOrgGroupId(group.getOrgGroupId());

		project.setProjectStatusId(BProjectStatus.findOrMake("Default Active"));
		project.setProjectTypeId(BProjectType.findOrMake("Default Timeclock"));
		project.setProjectCategoryId(BProjectCategory.findOrMake("Default Timeclock"));
		String name = group.getName().length() > 20 ? group.getName().substring(0,20) : group.getName();
		project.setProjectName(name.length() < 4 ? group.getExternalId() : name + " Timeclock");
		project.setDescription("Clock in / Clock out project for " + group.getName());

		project.insert();
		hsu.flush();

		group.setDefaultProjectId(project.getProjectId());
	}
}
