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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.business.BCompany;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.standard.hr.eeo1Survey.NewSurveyInputEstablishment;
import com.arahant.utils.ArahantSession;  
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import org.kissweb.StringUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
 
/**
 *
 */
public class EEO1Export {
    static final String EEO_CATEGORY_ID_ADMINISTRATIVE_SUPPORT_WORKERS = "00000-0000000004";
    static final String EEO_CATEGORY_ID_CRAFT_WORKERS = "00000-0000000005";
    static final String EEO_CATEGORY_ID_EXECUTIVE_SENIOR_LEVEL_OFFICIALS_AND_MANAGERS = "00000-0000000009";
    static final String EEO_CATEGORY_ID_FIRST_MID_LEVEL_OFFICIALS_AND_MANAGERS = "00000-0000000000";
    static final String EEO_CATEGORY_ID_LABORERS_AND_HELPERS = "00000-0000000007";
    static final String EEO_CATEGORY_ID_OPERATIVES = "00000-0000000006";
    static final String EEO_CATEGORY_ID_PROFESSIONALS = "00000-0000000001";
    static final String EEO_CATEGORY_ID_SALES_WORKERS = "00000-0000000003";
    static final String EEO_CATEGORY_ID_SERVICE_WORKERS = "00000-0000000008";
    static final String EEO_CATEGORY_ID_TECHNICIANS = "00000-0000000002";
    static final String EEO_CATEGORY_IDS_IN_REPORT_ORDER[] = {
        EEO_CATEGORY_ID_EXECUTIVE_SENIOR_LEVEL_OFFICIALS_AND_MANAGERS,
        EEO_CATEGORY_ID_FIRST_MID_LEVEL_OFFICIALS_AND_MANAGERS,
        EEO_CATEGORY_ID_PROFESSIONALS,
        EEO_CATEGORY_ID_TECHNICIANS,
        EEO_CATEGORY_ID_SALES_WORKERS,
        EEO_CATEGORY_ID_ADMINISTRATIVE_SUPPORT_WORKERS,
        EEO_CATEGORY_ID_CRAFT_WORKERS,
        EEO_CATEGORY_ID_OPERATIVES,
        EEO_CATEGORY_ID_LABORERS_AND_HELPERS,
        EEO_CATEGORY_ID_SERVICE_WORKERS
    };

    static final String EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE = "00000-0000000004";
    static final String EEO_RACE_ID_ASIAN = "00000-0000000003";
    static final String EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN = "00000-0000000001";
    static final String EEO_RACE_ID_HISPANIC_OR_LATINO = "00000-0000000002";
    static final String EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER = "00000-0000000005";
    static final String EEO_RACE_ID_TWO_OR_MORE_RACE = "00000-0000000006";
    static final String EEO_RACE_ID_WHITE = "00000-0000000000";
    static final String EEO_RACE_IDS_ALL[] = {
        EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE,
        EEO_RACE_ID_ASIAN,
        EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN,
        EEO_RACE_ID_HISPANIC_OR_LATINO,
        EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER,
        EEO_RACE_ID_TWO_OR_MORE_RACE,
        EEO_RACE_ID_WHITE
    };

	private StringBuffer stringBuffer;
    private int startDate;
    private int finalDate;
    private boolean governmentContractor;
    private boolean commonOwnership;
    private String certifierTitle;
    private String certifierName;
    private String certifierPhone;
    private String certifierEmail;
    private ReportData consolidatedData;
    

    public String export(int startDate, int finalDate, boolean governmentContractor, boolean commonOwnership, String certifierTitle, String certifierName, String certifierPhone, String certifierEmail, NewSurveyInputEstablishment[] establishments) {
        ReportData currentReportData;


        this.stringBuffer = new StringBuffer();
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.governmentContractor = governmentContractor;
        this.commonOwnership = commonOwnership;
        this.certifierTitle = certifierTitle;
        this.certifierName = certifierName;
        this.certifierPhone = certifierPhone;
        this.certifierEmail = certifierEmail;
        this.consolidatedData = new ReportData();
        this.consolidatedData.type = "2";


        if (establishments.length == 1) {
            // Single Establishment Company:
            // REPORT 1 - Single Establishment Company Report

            currentReportData = this.getReportData(establishments[0], establishments);
            currentReportData.type = "1";

            this.writeReport(currentReportData);
        } else {
            // Multiple Establishment Company:
            // REPORT 3 - HQ Report
            // REPORT 4 - Establishment Report(s) - 50 or more employees, not first time
            // REPORT 9 - (Establishment Report(s) - 50 or more employees, first time
            // REPORT 8 - Establishment Report - less than 50 employees
            // REPORT 2 - Consolidated Report

            for (NewSurveyInputEstablishment currentEstablishment : establishments) {
                currentReportData = this.getReportData(currentEstablishment, establishments);

                if (currentEstablishment.isHeadquarters()) {
                    currentReportData.type = "3";
                    this.writeReport(currentReportData);

                    // also fill in the consolidated data from headquarters data
                    this.consolidatedData.city = currentReportData.city;
                    this.consolidatedData.county = currentReportData.county;
                    this.consolidatedData.name = currentReportData.name;
                    this.consolidatedData.state = currentReportData.state;
                    this.consolidatedData.street = currentReportData.street;
                    this.consolidatedData.street2 = currentReportData.street2;
                    // per instructions from EEO-1 people, put company number as the unit number for consolidated report
                    this.consolidatedData.unitNumber = new BCompany(ArahantSession.getHSU().getCurrentCompany()).getEeo1CompanyId();
                    this.consolidatedData.zip = currentReportData.zip;
                } else if (currentReportData.getMaleAndFemaleCount() < 50) {
                    currentReportData.type = "8";
                    this.writeReport(currentReportData);
                } else if (currentEstablishment.getFiledLastYear()) {
                    currentReportData.type = "4";
                    this.writeReport(currentReportData);
                } else {
                    currentReportData.type = "9";
                    currentReportData.unitNumber = ""; // make sure we don't fill out a unit number for this report type
                    this.writeReport(currentReportData);
                }
            }

            writeReport(this.consolidatedData);
        }

        return this.stringBuffer.toString();
    }

    private ReportData getReportData(NewSurveyInputEstablishment currentEstablishment, NewSurveyInputEstablishment[] establishments) {
        ReportData data = new ReportData();
        BOrgGroup orgGroup = new BOrgGroup(currentEstablishment.getId());
        List<String> employeeIds = this.getActiveEmployeeIds(currentEstablishment, establishments);

        data.city = this.cleanForEEO1(orgGroup.getCity(), false);
        data.county = this.cleanForEEO1(orgGroup.getCounty(), false);
        data.filedLastYear = currentEstablishment.getFiledLastYear();
        data.name = this.cleanForEEO1(orgGroup.getName(), true);
        data.state = this.cleanForEEO1(orgGroup.getState(), false);
        data.street = this.cleanForEEO1(orgGroup.getStreet(), false);
        data.street2 = this.cleanForEEO1(orgGroup.getStreet2(), false);
        data.unitNumber = currentEstablishment.getUnitNumber();
        data.zip = this.cleanForEEO1(orgGroup.getZip(), false);

        try {
            // Now that we have the employee ids, get them grouped by EEO Category Id, EEO Race Id, Sex, Count

            String query =
                "SELECT emp.eeo_category_id, emp.eeo_race_id, per.sex, count (emp.person_id) " +
                "FROM employee emp " +
                "JOIN person per ON emp.person_id = per.person_id " +
                "JOIN hr_eeo_race race ON emp.eeo_race_id = race.eeo_id " +
                "JOIN hr_eeo_category category ON emp.eeo_category_id = category.eeo_category_id ";

            if (employeeIds.size() > 0) {
                StringBuffer employeeIdList = new StringBuffer();

                for (String employeeId : employeeIds) {
                    if (employeeIdList.length() > 0) {
                        employeeIdList.append(",");
                    }
                    employeeIdList.append("'");
                    employeeIdList.append(employeeId);
                    employeeIdList.append("'");
                }

                query += "WHERE emp.person_id IN (" + employeeIdList.toString() + ") ";
            }

            query += "GROUP BY emp.eeo_race_id, emp.eeo_category_id, per.sex";

            // spin through the grouped results and cache up data for this establishment and the consolidated numbers (totals)
            PreparedStatement ps = ArahantSession.getHSU().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String eeoCategoryId = rs.getString(1);
                String eeoRaceId = rs.getString(2);
                char sex = rs.getString(3).charAt(0);
                int count = rs.getInt(4);

                if (this.isCategoryAndRaceForEEO1(eeoCategoryId, eeoRaceId)) {
                    if (sex == 'M') {
                        data.getCategoryData(eeoCategoryId).getRaceData(eeoRaceId).maleCount = count;

                        this.consolidatedData.getCategoryData(eeoCategoryId).getRaceData(eeoRaceId).maleCount += count;
                    } else if (sex == 'F') {
                        data.getCategoryData(eeoCategoryId).getRaceData(eeoRaceId).femaleCount = count;

                        this.consolidatedData.getCategoryData(eeoCategoryId).getRaceData(eeoRaceId).femaleCount += count;
                    }
                }
            }

			rs.close();
			ps.close();
        } catch (Exception e) {
            throw new ArahantException(e);
        }

        return data;
    }


    private void getChildren(List<String> orgid, HashSet<String> excludes, List<String> orgIds)
    {
        List <String> oglist=(List)ArahantSession.getHSU().createCriteria(OrgGroup.class)
            .selectFields(OrgGroup.ORGGROUPID)
            .notIn(OrgGroup.ORGGROUPID, excludes)
            .joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
            .in(OrgGroupHierarchy.PARENT_ID, orgid)
            .list();

        if (oglist.size()==0)
            return;
        orgIds.addAll(oglist);
        getChildren(oglist, excludes, orgIds);
    }

    private List<String> getActiveEmployeeIds(NewSurveyInputEstablishment currentEstablishment, NewSurveyInputEstablishment[] establishments) {

        // TODO fix this query
        // VERY TRiCKY!  Must find employees for OrgGroup where
        // a) employee has an active status that has some overlap in to the date range (this.startDate - this.finalDate)
        // b) employee was associated to this org group or a non-establishment sub org group during the data range (this.startDate - this.finalDate)
        // c) how are hierarchies handled ? e.g. current establishment includes users from sub org groups until a child that is one of the listed establishments is hit

        List<String> orgIds=new LinkedList<String>();
        orgIds.add(currentEstablishment.getId());

        HashSet<String> excludes=new HashSet<String>(establishments.length);
        for (NewSurveyInputEstablishment est : establishments)
            excludes.add(est.getId());

        getChildren(orgIds, excludes, orgIds);

        HibernateCriteriaUtil<Employee> hcu=ArahantSession.getHSU().createCriteria(Employee.class).selectFields(Employee.PERSONID);

        hcu.activeEmployee(this.startDate, this.finalDate);

        hcu.joinTo(Employee.ORGGROUPASSOCIATIONS)
              .dateSpanCompare(OrgGroupAssociation.STARTDATE, OrgGroupAssociation.FINALDATE, this.startDate, this.finalDate)
              .in(OrgGroupAssociation.ORG_GROUP_ID,orgIds);

        List<String> employeeIds = (List)hcu.list();

        return employeeIds;
    }

    private String cleanForEEO1(String str, boolean isName) {

        /* Please observe the following rules for all names and addresses:
         * Do not use periods or commas anywhere in name or address fields.
         * Do not begin a name with "The". "The" should be attached at the end of a name.
         * EXAMPLE: The Greatest Corporation would be submitted in an EEO-1 report as Greatest Corporation The.
         * Do not begin a name with a numeral. The first character must be alphabetic.
         */

        str = str.replace('.', ' ');
        str = str.replace(',', ' ');

        if (isName) {
            if (str.toLowerCase().startsWith("the ")) {
                str = str.substring(4, str.length());
                str += " The";
            }

            if (str.charAt(0) == '1') {
                str = "One" + str.substring(1, str.length());
            } else if (str.charAt(1) == '2') {
                str = "Two" + str.substring(1, str.length());
            } else if (str.charAt(1) == '3') {
                str = "Three" + str.substring(1, str.length());
            } else if (str.charAt(1) == '4') {
                str = "Four" + str.substring(1, str.length());
            } else if (str.charAt(1) == '5') {
                str = "Five" + str.substring(1, str.length());
            } else if (str.charAt(1) == '6') {
                str = "Six" + str.substring(1, str.length());
            } else if (str.charAt(1) == '7') {
                str = "Seven" + str.substring(1, str.length());
            } else if (str.charAt(1) == '8') {
                str = "Eight" + str.substring(1, str.length());
            } else if (str.charAt(1) == '9') {
                str = "Nine" + str.substring(1, str.length());
            } else if (str.charAt(1) == '0') {
                str = "Zero" + str.substring(1, str.length());
            }
        }

        return str;
    }

    private boolean isCategoryAndRaceForEEO1(String eeoCategoryId, String eeoRaceId) {
        boolean foundCategory = false;
        boolean foundRace = false;

        for (int idx = 0; idx < EEO1Export.EEO_CATEGORY_IDS_IN_REPORT_ORDER.length; idx++) {
            if (EEO1Export.EEO_CATEGORY_IDS_IN_REPORT_ORDER[idx].equals(eeoCategoryId)) {
                foundCategory = true;
                break;
            }
        }

        for (int idx = 0; idx < EEO1Export.EEO_RACE_IDS_ALL.length; idx++) {
            if (EEO1Export.EEO_RACE_IDS_ALL[idx].equals(eeoRaceId)) {
                foundRace = true;
                break;
            }
        }

        return foundCategory && foundRace;
    }

    private void writeReport(ReportData data) {
        ArrayList<String> errors = new ArrayList<String>();
        BCompany company = new BCompany(ArahantSession.getHSU().getCurrentCompany());
        Calendar calendar1 = DateUtils.getCalendar(this.startDate);
		Calendar calendar2 = DateUtils.getCalendar(this.finalDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");
		
        // do some error checking
        if (isEmpty(company.getEeo1CompanyId())) {
            errors.add("EEO-1 Company ID is not set on the company");
        }
        if (isEmpty(data.street)) {
            errors.add("Street (Address) is not set on " + data.name);
        }
        if (isEmpty(data.city)) {
            errors.add("City (Address) is not set on " + data.name);
        }
        if (isEmpty(data.state)) {
            errors.add("State (Address) is not set on " + data.name);
        }
        if (isEmpty(data.zip)) {
            errors.add("Zip (Address) is not set on " + data.name);
        }
        if (isEmpty(data.county)) {
            errors.add("County (Address) is not set on " + data.name);
        }
        if (isEmpty(company.getNaicsCode())) {
            errors.add("NAICS code is not set on the company");
        }
        if (errors.size() > 0) {
            throw new ArahantException("Unable to generate EEO-1 Report.\n\n" + StringUtils.join(errors.toArray(new String[errors.size()]), "\n"));
        }
        
        // passed error checks, write out this report
        
        if (this.stringBuffer.length() > 0) {
            this.stringBuffer.append("\n");
        }

        this.write(company.getEeo1CompanyId(), 7);
        this.write(data.type, 1);
        this.write(data.unitNumber, 7);
        this.write(data.name, 35);
        this.write(data.street, 34);
        this.write(data.street2, 25);
        this.write(data.city, 20);
        this.write(data.state, 2);
        this.write(data.zip, 5);
        this.write(data.filedLastYear?"1":"2", 1);
        this.write(this.consolidatedData.getMaleAndFemaleCount()>=100?"1":"2", 1);
        this.write(this.commonOwnership?"1":"2", 1);
        this.write(this.governmentContractor?"1":"2", 1);
        this.write(company.getDunBradstreet(), 9);
        this.write(data.county, 18);
        this.write(simpleDateFormat.format(calendar1.getTime()) + simpleDateFormat.format(calendar2.getTime()), 16);
        this.write(company.getNaicsCode(), 6);
        this.write(this.certifierTitle, 35);
		this.write(this.certifierName, 35);
		this.write(this.certifierPhone, 10);
		this.write(this.certifierEmail, 40);
        
        for (String eeoCategoryId : EEO1Export.EEO_CATEGORY_IDS_IN_REPORT_ORDER) {
            CategoryData categoryData = data.getCategoryData(eeoCategoryId);
            
            this.write(categoryData.getRaceData(EEO_RACE_ID_HISPANIC_OR_LATINO).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_HISPANIC_OR_LATINO).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_WHITE).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_ASIAN).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_TWO_OR_MORE_RACE).maleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_WHITE).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_ASIAN).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE).femaleCount + "", 6);
            this.write(categoryData.getRaceData(EEO_RACE_ID_TWO_OR_MORE_RACE).femaleCount + "", 6);
            this.write(categoryData.getMaleAndFemaleCount() + "", 7);
        }

        this.write(data.getMaleCount(EEO_RACE_ID_HISPANIC_OR_LATINO) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_HISPANIC_OR_LATINO) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_WHITE) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_ASIAN) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE) + "", 6);
        this.write(data.getMaleCount(EEO_RACE_ID_TWO_OR_MORE_RACE) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_WHITE) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_BLACK_OR_AFRICAN_AMERICAN) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_NATIVE_HAWAIIN_OR_OTHER_PACIFIC_ISLANDER) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_ASIAN) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_AMERICAN_INDIAN_OR_ALASKAN_NATIVE) + "", 6);
        this.write(data.getFemaleCount(EEO_RACE_ID_TWO_OR_MORE_RACE) + "", 6);
        this.write(data.getMaleAndFemaleCount() + "", 7);
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

	private void write(String str, int len) {
		if (str == null)
			str = "";

		if (str.length() > len) {
			str = str.substring(len);
        }

		while (str.length() < len) {
			str += " ";
        }

		this.stringBuffer.append(str);
	}

    private class ReportData {
        String type;
        String unitNumber;
        boolean filedLastYear;
        String name;
        String street;
        String street2;
        String city;
        String state;
        String zip;
        String county;
        HashMap<String, CategoryData> categories = new HashMap<String, CategoryData>();
        
        CategoryData getCategoryData(final String eeoCategoryId) {
            CategoryData categoryData = this.categories.get(eeoCategoryId);
            
            if (categoryData == null) {
                categoryData = new CategoryData();
                this.categories.put(eeoCategoryId, categoryData);
            }

            return categoryData;
        }

        private long getMaleAndFemaleCount() {
            long count = 0;

            for (String eeoCategoryId : this.categories.keySet()) {
                CategoryData categoryData = this.categories.get(eeoCategoryId);

                count += categoryData.getMaleAndFemaleCount();
            }

            return count;
        }

        private long getMaleCount(String eeoRaceId) {
            long count = 0;

            for (String eeoCategoryId : this.categories.keySet()) {
                CategoryData categoryData = this.categories.get(eeoCategoryId);

                count += categoryData.getRaceData(eeoRaceId).maleCount;
            }

            return count;
        }

        private long getFemaleCount(String eeoRaceId) {
            long count = 0;

            for (String eeoCategoryId : this.categories.keySet()) {
                CategoryData categoryData = this.categories.get(eeoCategoryId);

                count += categoryData.getRaceData(eeoRaceId).femaleCount;
            }

            return count;
        }
    }

    private class CategoryData {
        HashMap<String, RaceData> races = new HashMap<String, RaceData>();

        RaceData getRaceData(final String eeoRaceId) {
            RaceData raceData = this.races.get(eeoRaceId);

            if (raceData == null) {
                raceData = new RaceData();
                this.races.put(eeoRaceId, raceData);
            }

            return raceData;
        }

        private long getMaleAndFemaleCount() {
            long count = 0;

            for (String eeoRaceId : this.races.keySet()) {
                RaceData raceData = this.races.get(eeoRaceId);

                count += raceData.maleCount;
                count += raceData.femaleCount;
            }

            return count;
        }
    }


    private class RaceData {
        int maleCount;
        int femaleCount;
    }
	
	public static void main (String args[]) {
		ArahantSession.getHSU().setCurrentPersonToArahant();
		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class, "00001-0000000001"));

        NewSurveyInputEstablishment[] establishments = new NewSurveyInputEstablishment[1];
        establishments[0] = new NewSurveyInputEstablishment();
        establishments[0].setFiledLastYear(true);
        establishments[0].setHeadquarters(true);
        establishments[0].setId("00001-0000000001");
        establishments[0].setUnitNumber("U123456");

		EEO1Export eeo1 = new EEO1Export();
        String data = eeo1.export(20000101, 20100101, false, true, "Beer Inspector", "Homer Simpson", "555-5555", "homer@springfield.com", establishments);
        System.out.println(data);
	}
	
}
