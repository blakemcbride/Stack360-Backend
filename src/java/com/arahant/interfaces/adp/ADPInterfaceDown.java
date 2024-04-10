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
 *
 * Author: Blake McBride
 * Date: 5/2020
 */

package com.arahant.interfaces.adp;

import com.arahant.beans.Person;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BApplication;
import com.arahant.business.BChangeLog;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.timertasks.TimerTaskBase;
import com.arahant.utils.*;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kissweb.JsonPath;
import org.kissweb.StringUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import org.kissweb.DateUtils;

import java.sql.SQLException;
import java.util.*;

public class ADPInterfaceDown extends TimerTaskBase {

    private static final ArahantLogger logger = new ArahantLogger(ADPInterfaceDown.class);
    private static boolean running = false;
    private static int totalWorkersReceived = 0;
    private static int totalNewWorkers = 0;
    private static int totalUpdatedWorkers = 0;
    private static int skippedOfficeWorkers = 0;
    private static int hiredApplicants = 0;
    private static Date lastCompletionDate = null;
    private static int nItterations = 0;


    public static void test() throws Exception {
        GetAccessToken.getAccessToken();
        //   System.out.println("access_token = " + Globals.access_token);
    }

    /**
     * runs the interface asynchronously
     * Returns true if it started and false if it was already running.
     *
     * @return
     */
    public static boolean runAsync() {
        if (running) {
            logger.error("Attempt to run ADP Import when already running");
            return false;
        } else {
            new Thread(() -> {
                try {
                    running = true;
                    nItterations = hiredApplicants = skippedOfficeWorkers = totalUpdatedWorkers = totalNewWorkers = totalWorkersReceived = 0;
                    HibernateSessionUtil hsu = ArahantSession.openHSU(false);
                    hsu.setCurrentPersonToArahant();
                    hsu.beginTransaction();
                    ADPInterfaceDown.run2();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lastCompletionDate = new Date();
                    ArahantSession.clearSession();
                    running = false;
                }
            }).start();
            return true;
        }
    }

    /**
     * Run synchronously.  Used for the timer task.
     *
     * @throws Exception
     */
    @Override
    public void execute() throws Exception {
        if (!BProperty.getBoolean("RunADPImport"))
            return;  //  not enabled
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY)
            return;  // don't run on the weekend
        int hour = c.get(Calendar.HOUR_OF_DAY);
        /*
        if (hour < 6  ||  hour > 18)
            return;  //  don't run after hours
         */
        if (running)
            return; // don't run if already running
        try {
            running = true;
            nItterations = hiredApplicants = skippedOfficeWorkers = totalUpdatedWorkers = totalNewWorkers = totalWorkersReceived = 0;
            ADPInterfaceDown.run2();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lastCompletionDate = new Date();
            running = false;
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static Date getLastCompletionDate() {
        return lastCompletionDate;
    }

    public static int getTotalWorkersReceived() {
        return totalWorkersReceived;
    }

    public static int getTotalNewWorkers() {
        return totalNewWorkers;
    }

    public static int getTotalUpdatedWorkers() {
        return totalUpdatedWorkers;
    }

    public static int getSkippedOfficeWorkers() {
        return skippedOfficeWorkers;
    }

    public static int getHiredApplicants() {
        return hiredApplicants;
    }

    private static void run2() {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Connection db = KissConnection.get();
        Command cmd = db.newCommand();
        List<Record> recs;
        int x;  // for setting breakpoints

        logger.setLevel(Level.ALL);

        logger.info("ADP import startup");

        hsu.setCurrentPersonToArahant();
        try {
            // The following line gives the wrong company if there are multiple companies and
            // this interface is being run automatically.  It has no way to know what the company is.
            final String orgGroupId = hsu.getCurrentCompany().getCompanyId();
            /*
            Record rec = db.fetchOne("select wage_type_id from wage_type where org_group_id=? and period_type=1 and " +
                    "wage_type=1 and first_active_date <= ? and (last_active_date = 0 OR last_active_date > ?)",
                    orgGroupId, DateUtils.today(), DateUtils.today());
            if (rec == null) {
                logger.error("Can't find applicable hourly wage type");
                return;
            }
            String wageTypeId = rec.getString("wage_type_id");
             */
            Record rec;
            String wageTypeId = BProperty.getIDWithCheck(StandardProperty.NEW_EMPLOYEE_WAGE_TYPE);

            HashMap<String, String> positionMap = getPositionMap(db, orgGroupId);

            HashMap<String, Status> statusMap = getStatusMap(db, orgGroupId);

            //dfw = new DelimitedFileWriter("/tmp/ADP-output.csv");
            HashSet<String> set = new HashSet<>();
            GetAccessToken.getAccessToken();  // this must be called first and only once!
            if (Globals.access_token == null) {
                logger.error("Aborting interface due to null access_token");
                return;
            }
            for (int page = 1; true; page++) {
                JSONObject jo = Request.makeRequest("https://api.adp.com/hr/v2/workers", Globals.GET, page);
                if (jo == null) {
                    //logger.error("jo is null");
                    break;
                }
                JSONArray workers = jo.getJSONArray("workers");
                if (workers == null) {
                    logger.error("workers is null; here is jo : " + jo.toString());
                    break;
                }
                int nWorkers = workers.length();
                totalWorkersReceived += nWorkers;
                for (int i = 0; i < nWorkers; i++) {
                    JSONObject worker = workers.getJSONObject(i);
                    JSONObject person = worker.getJSONObject("person");
                    JSONArray workAssignments = worker.getJSONArray("workAssignments");
                    String associateOID = worker.getString("associateOID");

                    JSONArray governmentIDs = person.getJSONArray("governmentIDs");
                    int n = governmentIDs.length();
                    String SSN = null;
                    for (int j = 0; j < n; j++) {
                        JSONObject obj = governmentIDs.getJSONObject(j);
                        JSONObject nameCode = obj.getJSONObject("nameCode");
                        String codeValue = nameCode.getString("codeValue");
                        if ("SSN".equals(codeValue)) {
                            SSN = obj.getString("idValue");
                            break;
                        }
                    }

                    // Do some data validation
                    if (set.contains(associateOID)) {
                        logger.error("Duplicate associateOID " + associateOID);
                        continue;
                    } else
                        set.add(associateOID);
                    if (associateOID.length() != 16) {
                        logger.error("associateOID " + associateOID + " length not 16");
                        continue;
                    }
                    if (SSN == null) {
                        logger.error("associateID " + associateOID + " is missing SSN");
                        continue;
                    }

                    JSONObject legalName = person.getJSONObject("legalName");
                    String fname = legalName.getString("givenName");
                    String lname = legalName.getString("familyName1");
                    String mname = legalName.getString("middleName");
                    String sex = JsonPath.getString(person, "genderCode.codeValue");
                    String dob = person.getString("birthDate");
                    int idob;
                    if (dob != null && !dob.isEmpty())
                        idob = DateUtils.parse(dob);
                    else
                        idob = 0;

                    Record employeeRec = db.fetchOne("select * from employee where adp_id=?", associateOID);
                    Record personRec;
                    String personId;
                    boolean newEmployee = false;
                    boolean newPerson = false;
                    String encryptedSSN = Person.encryptSsn(SSN);
                    if (employeeRec == null) {
                        // employee not found via associateOID, try accessing person via SSN
                        recs = db.fetchAll("select * from person where ssn=? or ssn=?", encryptedSSN, SSN);
                        if (recs.size() > 1) {
                            logger.error("More than one person has SSN " + SSN);
                            continue;
                        }
                        if (recs.isEmpty()) {
                            //  person never seen before (no SSN or associateOID match)
                            newEmployee = true;
                            newPerson = true;
                            personRec = db.newRecord("person");
                            employeeRec = db.newRecord("employee");
                            personId = IDGenerator.generate("person", "person_id");
                            personRec.set("person_id", personId);
                            personRec.set("ssn", encryptedSSN);
                            employeeRec.set("person_id", personId);
                            employeeRec.set("adp_id", associateOID);

                            personRec.setDateTime("record_change_date", new Date());
                            personRec.set("org_group_type", 1);
                            personRec.set("record_change_type", "N");
                            personRec.set("record_person_id", ArahantSession.getCurrentPerson().getPersonId());
                        } else {
                            //  person found via SSN, no associateOID match, employee or applicant?
                            personRec = recs.get(0);
                            personId = personRec.getString("person_id");
                            employeeRec = db.fetchOne("select * from employee where person_id=?", personId);
                            if (employeeRec == null) {
                                // not employee, perhaps they're an applicant
                                Record applicant = db.fetchOne("select * from applicant where person_id=?", personId);
                                if (applicant != null) {
                                    // Yes.  Is applicant!
                                    hiredApplicants++;
                                    newEmployee = true;
                                    employeeRec = db.newRecord("employee");
                                    employeeRec.set("person_id", personId);
                                    employeeRec.set("adp_id", associateOID);

                                    employeeRec.set("eeo_race_id", applicant.getString("eeo_race_id"));

                                    personRec.setDateTime("record_change_date", new Date());
                                    personRec.set("org_group_type", 1);

                                    // update applicant status
                                    applicant.set("applicant_status_id", BProperty.getIDWithCheck(StandardProperty.APPLICANT_STATUS_HIRED));
                                    applicant.update();
                                    Record aarec = db.fetchOne("select * from applicant_application where person_id = ? order by offer_last_generated desc", personId);
                                    if (aarec != null) {
                                        aarec.set("phase", 4); // hired
                                        BApplication.updateApplicationStatusId(aarec, (short) 4);
                                        aarec.update();
                                        BChangeLog.personLog(personId, "Applicant promoted to employee via ADP");
                                    }
                                } else {
                                    logger.error("person ID " + personRec.getString("person_id") + " is missing a matching employee or applicant record");
                                    continue;
                                }
                            } else {
                                // Is this a new hire of an ex-employee?
                                Record srec = db.fetchOne("select es.active " +
                                        "from hr_empl_status_history sh " +
                                        "join hr_employee_status es " +
                                        "  on sh.status_id = es.status_id " +
                                        "where sh.employee_id = ? " +
                                        "order by sh.effective_date desc", personId);
                                if (srec != null && !srec.getString("active").equals("A")) {
                                    // re-hire
                                    Record applicant = db.fetchOne("select * from applicant where person_id=?", personId);
                                    // update applicant status
                                    if (applicant != null) {
                                        applicant.set("applicant_status_id", BProperty.getIDWithCheck(StandardProperty.APPLICANT_STATUS_HIRED));
                                        applicant.update();
                                        Record aarec = db.fetchOne("select * from applicant_application where person_id = ? order by offer_last_generated desc", personId);
                                        if (aarec != null) {
                                            aarec.set("phase", 4); // hired
                                            BApplication.updateApplicationStatusId(aarec, (short) 4);
                                            aarec.update();
                                            BChangeLog.personLog(personId, "Applicant re-hired via ADP");
                                        }
                                    }
                                }
                            }
                            employeeRec.set("adp_id", associateOID);
                            //employeeRec.update();
                        }
                    } else {
                        // employee found via associateOID
                        personId = employeeRec.getString("person_id");
                        personRec = db.fetchOne("select * from person where person_id=?", personId);

                        Record ssnRec = db.fetchOne("select * from person where ssn=? or ssn=?", encryptedSSN, SSN);
                        if (ssnRec != null) {
                            String ssnPersonId = ssnRec.getString("person_id");
                            if (!ssnPersonId.equals(personId)) {
                                // duplicate SSN situation
                                logger.error("Person incoming from ADP (" + fname + " " + lname + ") has a duplicate SSN with someone already in the system (" + ssnRec.getString("fname") + " " + ssnRec.getString("lname") + ")");
                                logger.error("SSN = " + SSN + "; person_id1 = " + ssnPersonId + "; person_id2 = " + personId);
                                continue;
                            }
                        }

                        personRec.set("ssn", encryptedSSN);
                    }

                    // At this point we have employeeRec, personRec, adp_id, and SSN

                    JSONObject legalAddress = person.getJSONObject("legalAddress");
                    String street1;
                    String street2;
                    String city;
                    String state;
                    String zip;
                    String county;
                    if (legalAddress != null) {
                        street1 = legalAddress.getString("lineOne");
                        street2 = legalAddress.getString("lineTwo");
                        city = legalAddress.getString("cityName");
                        state = JsonPath.getString(legalAddress, "countrySubdivisionLevel1.codeValue");
                        zip = legalAddress.getString("postalCode");
                        county = JsonPath.getString(legalAddress, "countrySubdivisionLevel2.codeValue");
                    } else {
                        street1 = null;
                        street2 = null;
                        city = null;
                        state = null;
                        zip = null;
                        county = null;
                        logger.error("legalAddress missing for SSN " + SSN);
                    }

                    JSONObject communication = person.getJSONObject("communication");
                    String mobile = null, email = null, home = null;
                    if (communication != null) {
                        JSONArray mobiles = communication.getJSONArray("mobiles");
                        if (mobiles != null && mobiles.length() > 0) {
                            JSONObject m1 = mobiles.getJSONObject(0);
                            String num = m1.getString("dialNumber");
                            if (num != null)
                                mobile = m1.getString("areaDialing") + "-" + StringUtils.take(num, 3) + "-" + StringUtils.drop(num, 3);
                        }
                        JSONArray landlines = communication.getJSONArray("landlines");
                        if (landlines != null && landlines.length() > 0) {
                            JSONObject m1 = landlines.getJSONObject(0);
                            String num = m1.getString("dialNumber");
                            if (num != null)
                                home = m1.getString("areaDialing") + "-" + StringUtils.take(num, 3) + "-" + StringUtils.drop(num, 3);
                        }
                        JSONArray emails = communication.getJSONArray("emails");
                        if (emails != null && emails.length() > 0) {
                            JSONObject m1 = emails.getJSONObject(0);
                            email = m1.getString("emailUri");
                        }
                    } else {
                        logger.error("communication missing for SSN " + SSN);
                    }

                    String workerId = null;
                    String position = null;
                    String employeeStatus = null;
                    int effectiveDate = 0;
                    int statusEffectiveDate = 0;
                    int hireDate;
                    int terminationDate;
                    String workerTypeCode = null;
                    String positionType = null;
                    double wageAmount = 0;

                    if (workAssignments.length() != 1) {
                        logger.error("There are " + workAssignments.length() + " work assignments for " + fname + " " + lname);
                        continue;
                    }
                    JSONObject workAssignment = workAssignments.getJSONObject(0);

                    String str = workAssignment.getString("hireDate");
                    hireDate = str == null || str.isEmpty() ? 0 : DateUtils.parse(str);
                    if (hireDate == 0) {
                        logger.error(fname + " " + lname + " is missing their hire date");
                        continue;
                    }
                    str = workAssignment.getString("terminationDate");
                    terminationDate = str == null || str.isEmpty() ? 0 : DateUtils.parse(str);

                    JSONArray homeOrganizationalUnits = workAssignment.getJSONArray("homeOrganizationalUnits");
                    if (homeOrganizationalUnits != null && homeOrganizationalUnits.length() > 0) {
                        JSONObject homeOrganizationalUnit = homeOrganizationalUnits.getJSONObject(0);
                        JSONObject nameCode = homeOrganizationalUnit.getJSONObject("nameCode");
                        if (nameCode != null) {
                            positionType = nameCode.getString("shortName");
                            if ("Office".equals(positionType)) {
                                        /*
                                        dfw.writeField(lname);
                                        dfw.writeField(fname);
                                        dfw.writeField(mname);
                                        dfw.endRecord();
                                         */
                                skippedOfficeWorkers++;
                                //logger.info("Skipping office worker " + fname + " " + lname);
                                continue;  //  don't sync office personnel
                            }
                        }
                    }

                    if (newEmployee)
                        logger.info("Employee " + fname + " " + lname + " " + SSN + " not found.  Adding new employee.");

                    workerId = workAssignment.getString("payrollFileNumber");
                    position = workAssignment.getString("jobTitle");
                    JSONObject workerTypeCodeObj = workAssignment.getJSONObject("workerTypeCode");
                    if (workerTypeCodeObj != null) {
                        workerTypeCode = workerTypeCodeObj.getString("codeValue");  //  Always P=Part time
                    }

                    JSONObject baseRemuneration = workAssignment.getJSONObject("baseRemuneration");
                    if (baseRemuneration != null) {
                        String effectiveDateStr = baseRemuneration.getString("effectiveDate");
                        effectiveDate = DateUtils.parse(effectiveDateStr);
                        JSONObject hourlyRateAmount = baseRemuneration.getJSONObject("hourlyRateAmount");
                        if (hourlyRateAmount != null) {
                            wageAmount = hourlyRateAmount.getDouble("amountValue");
                        }
                    }

                    JSONObject assignmentStatus = workAssignment.getJSONObject("assignmentStatus");
                    if (assignmentStatus != null) {
                        JSONObject statusCode = assignmentStatus.getJSONObject("statusCode");
                        if (statusCode != null) {
                            String shortName = statusCode.getString("shortName");
                            String longName = statusCode.getString("longName");
                            employeeStatus = shortName;
                        }
                        String statusEffectiveDateStr = assignmentStatus.getString("effectiveDate");
                        statusEffectiveDate = DateUtils.parse(statusEffectiveDateStr);
                    }


                    if (effectiveDate == 0)
                        effectiveDate = DateUtils.today();

                    setStringField(personRec, "fname", fname, 30);
                    setStringField(personRec, "mname", mname, 20);
                    setStringField(personRec, "lname", lname, 30);
                    setStringField(personRec, "personal_email", email, 60);
                    personRec.set("sex", "F".equals(sex) ? "F" : "M");
                    personRec.set("dob", idob);
                    setStringField(employeeRec, "ext_ref", workerId, 11);

                    if (newEmployee) {
                        personRec.set("company_id", orgGroupId);
                        if (newPerson)
                            personRec.addRecord();
                        else
                            personRec.update();
                        employeeRec.addRecord();
                        BEmployee.applyLabels(employeeRec.getString("person_id"));
                        totalNewWorkers++;
                    } else {
                        try {
                            personRec.update();  //  error here - duplicate SSN
                        } catch (Exception e) {
                            logger.error("SSN = " + SSN + " \"" + Person.encryptSsn(SSN) + "\"");
                            continue;
                        }
                        employeeRec.update();
                        totalUpdatedWorkers++;
                    }

                    Record addressRec = db.fetchOne("select * from address where person_join=? and address_type=2 and record_type='R'", personId);
                    boolean newAddresRecord = false;
                    if (addressRec == null) {
                        newAddresRecord = true;
                        addressRec = db.newRecord("address");
                        addressRec.set("address_id", IDGenerator.generate("address", "address_id"));
                        addressRec.set("person_join", personId);
                        addressRec.set("address_type", 2);
                    }
                    setStringField(addressRec, "street", street1, 60);
                    setStringField(addressRec, "street2", street2, 60);
                    setStringField(addressRec, "city", city, 60);
                    setStringField(addressRec, "state", state, 25);
                    setStringField(addressRec, "zip", zip, 10);
                    setStringField(addressRec, "county", county, 30);
                    if (newAddresRecord) {
                        logger.info("New street address for " + fname + " " + lname + " (" + SSN + ") is " + street1);
                        addressRec.addRecord();
                    } else
                        addressRec.update();

                    Record phoneRec = db.fetchOne("select * from phone where person_join=? and phone_type=2 and record_type='R'", personId);
                    boolean newPhoneRecord = false;
                    if (phoneRec == null) {
                        newPhoneRecord = true;
                        phoneRec = db.newRecord("phone");
                        phoneRec.set("phone_id", IDGenerator.generate("phone", "phone_id"));
                        phoneRec.set("person_join", personId);
                        phoneRec.set("phone_type", 2);
                    }
                    home = Formatting.formatPhoneNumber(home);
                    if (home != null && !home.isEmpty()) {
                        setStringField(phoneRec, "phone_number", home, 20);
                        if (newPhoneRecord)
                            phoneRec.addRecord();
                        else
                            phoneRec.update();
                    } else if (!newPhoneRecord)
                        phoneRec.delete();

                    phoneRec = db.fetchOne("select * from phone where person_join=? and phone_type=3 and record_type='R'", personId);
                    newPhoneRecord = false;
                    if (phoneRec == null) {
                        newPhoneRecord = true;
                        phoneRec = db.newRecord("phone");
                        phoneRec.set("phone_id", IDGenerator.generate("phone", "phone_id"));
                        phoneRec.set("person_join", personId);
                        phoneRec.set("phone_type", 3);
                    }
                    mobile = Formatting.formatPhoneNumber(mobile);
                    if (mobile != null && !mobile.isEmpty()) {
                        setStringField(phoneRec, "phone_number", mobile, 20);
                        if (newPhoneRecord)
                            phoneRec.addRecord();
                        else
                            phoneRec.update();
                    } else if (!newPhoneRecord)
                        phoneRec.delete();

                    String positionId = positionMap.get(position.toUpperCase());
                    if (positionId == null) {
                        logger.error(fname + " " + lname + " has an unmapped position (" + position + ")");
                        continue;
                    }

                    recs = db.fetchAll("select * from hr_wage where employee_id=? and effective_date >= ? and " +
                                    "effective_date <= ? order by effective_date",
                            personId, DateUtils.addDays(effectiveDate, -14), DateUtils.addDays(effectiveDate, 14));
                    if (recs.isEmpty()) {
                        rec = db.newRecord("hr_wage");
                        rec.set("wage_id", IDGenerator.generate("hr_wage", "wage_id"));
                        rec.set("employee_id", personId);
                        rec.set("wage_type_id", wageTypeId);
                        rec.set("wage_amount", wageAmount);
                        rec.set("effective_date", effectiveDate);
                        rec.set("position_id", positionId);
                        rec.set("notes", "From ADP");
                        rec.addRecord();
                    } else if (recs.size() == 1) {
                        rec = recs.get(0);
                        rec.set("wage_type_id", wageTypeId);
                        rec.set("wage_amount", wageAmount);
                        rec.set("effective_date", effectiveDate);
                        rec.set("position_id", positionId);
                        rec.update();
                    } else {
                        boolean found = false;
                        for (Record r : recs) {
                            if (r.getInt("effective_date") == effectiveDate) {
                                r.set("wage_type_id", wageTypeId);
                                r.set("wage_amount", wageAmount);
                                r.set("position_id", positionId);
                                r.update();
                                found = true;
                            } else
                                r.delete();
                        }
                        if (!found) {
                            rec = db.newRecord("hr_wage");
                            rec.set("wage_id", IDGenerator.generate("hr_wage", "wage_id"));
                            rec.set("employee_id", personId);
                            rec.set("wage_type_id", wageTypeId);
                            rec.set("wage_amount", wageAmount);
                            rec.set("effective_date", effectiveDate);
                            rec.set("position_id", positionId);
                            rec.set("notes", "From ADP");
                            rec.addRecord();
                        }
                    }
                    db.execute("delete from hr_wage where employee_id=? and effective_date > ?",
                            personId, effectiveDate);

                    Status status = statusMap.get(employeeStatus.toUpperCase());
                    Status hireStatus = statusMap.get("ACTIVE");
                    boolean employeeTerminated = status.active.equals("N");

                    if (employeeTerminated) {
                        // Terminated
                        if (terminationDate == 0) {
                            logger.error(fname + " " + lname + " has been terminated but has no termination date");
                            continue;
                        }
                    } else {
                        // Working
                        if (terminationDate != 0) {
                            logger.error(fname + " " + lname + " is active but has termination date");
                            continue;
                        }
                    }

                    // save the hire date first
                    recs = db.fetchAll("select * from hr_empl_status_history where employee_id=? and " +
                                    "effective_date > ? and effective_date < ?",
                            personId, DateUtils.addDays(hireDate, -14), DateUtils.addDays(hireDate, 14));
                    if (recs.isEmpty()) {
                        rec = db.newRecord("hr_empl_status_history");
                        rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                        rec.set("employee_id", personId);
                        rec.set("effective_date", hireDate);
                        rec.set("status_id", hireStatus.status_id);
                        rec.set("notes", "From ADP");
                        rec.setDateTime("record_change_date", new Date());
                        rec.addRecord();
                    } else if (recs.size() == 1) {
                        rec = recs.get(0);
                        if (rec.getString("status_id").equals(status.status_id) || hireDate == rec.getInt("effective_date")) {
                            rec.set("effective_date", hireDate);
                            rec.set("status_id", hireStatus.status_id);
                            rec.set("notes", "From ADP");
                            rec.update();
                        } else {
                            rec = db.newRecord("hr_empl_status_history");
                            rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                            rec.set("employee_id", personId);
                            rec.set("effective_date", hireDate);
                            rec.set("status_id", hireStatus.status_id);
                            rec.set("notes", "From ADP");
                            rec.setDateTime("record_change_date", new Date());
                            rec.addRecord();
                        }
                    } else {
                        boolean found = false;
                        for (Record r : recs) {
                            if (r.getInt("effective_date") == hireDate) {
                                r.set("effective_date", hireDate);
                                r.set("status_id", hireStatus.status_id);
                                r.set("notes", "From ADP");
                                r.update();
                                found = true;
                            }
                        }
                        if (!found) {
                            rec = db.newRecord("hr_empl_status_history");
                            rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                            rec.set("employee_id", personId);
                            rec.set("effective_date", hireDate);
                            rec.set("status_id", hireStatus.status_id);
                            rec.set("notes", "From ADP");
                            rec.setDateTime("record_change_date", new Date());
                            rec.addRecord();
                        }
                        if (!employeeTerminated)
                            db.execute("delete from hr_empl_status_history where employee_id=? and effective_date > ?", personId, hireDate);
                    }

                    // handle LEAVE
                    if (!employeeTerminated && status.name.equals("LEAVE") && statusEffectiveDate != 0) {
                        rec = db.fetchOne("select * from hr_empl_status_history where employee_id=? and effective_date=?", personId, statusEffectiveDate);
                        if (rec == null) {
                            rec = db.newRecord("hr_empl_status_history");
                            rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                            rec.set("employee_id", personId);
                            rec.set("effective_date", statusEffectiveDate);
                            rec.set("status_id", status.status_id);
                            rec.set("notes", "From ADP");
                            rec.setDateTime("record_change_date", new Date());
                            rec.addRecord();
                        } else {
                            if (!status.status_id.equals(rec.getString("status_id"))) {
                                rec.set("status_id", status.status_id);
                                rec.set("notes", "From ADP");
                                rec.setDateTime("record_change_date", new Date());
                                rec.update();
                            }
                        }
                    }

                    //  handle terminated
                    if (employeeTerminated) {
                        cmd.execute("delete from project_employee_join where person_id=?", personId);
                        recs = db.fetchAll("select * from hr_empl_status_history where employee_id=? and " +
                                        "effective_date > ? and effective_date < ?",
                                personId, DateUtils.addDays(terminationDate, -7), DateUtils.addDays(terminationDate, 7));
                        if (recs.isEmpty()) {
                            rec = db.newRecord("hr_empl_status_history");
                            rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                            rec.set("employee_id", personId);
                            rec.set("effective_date", terminationDate);
                            rec.set("status_id", status.status_id);
                            rec.set("notes", "From ADP");
                            rec.setDateTime("record_change_date", new Date());
                            rec.addRecord();
                        } else if (recs.size() == 1) {
                            rec = recs.get(0);
                            if (rec.getString("status_id").equals(status.status_id) || terminationDate == rec.getInt("effective_date")) {
                                rec.set("effective_date", terminationDate);
                                rec.set("status_id", status.status_id);
                                rec.set("notes", "From ADP");
                                rec.update();
                            } else {
                                rec = db.newRecord("hr_empl_status_history");
                                rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                                rec.set("employee_id", personId);
                                rec.set("effective_date", terminationDate);
                                rec.set("status_id", status.status_id);
                                rec.set("notes", "From ADP");
                                rec.setDateTime("record_change_date", new Date());
                                rec.addRecord();
                            }
                        } else {
                            boolean found = false;
                            for (Record r : recs) {
                                if (r.getInt("effective_date") == terminationDate) {
                                    r.set("effective_date", terminationDate);
                                    r.set("status_id", status.status_id);
                                    r.set("notes", "From ADP");
                                    r.update();
                                    found = true;
                                }
                            }
                            if (!found) {
                                rec = db.newRecord("hr_empl_status_history");
                                rec.set("status_hist_id", IDGenerator.generate("hr_empl_status_history", "status_hist_id"));
                                rec.set("employee_id", personId);
                                rec.set("effective_date", terminationDate);
                                rec.set("status_id", status.status_id);
                                rec.set("notes", "From ADP");
                                rec.setDateTime("record_change_date", new Date());
                                rec.addRecord();
                            }
                        }
                        db.execute("delete from hr_empl_status_history where employee_id=? and effective_date > ?", personId, terminationDate);
                    }
                    hsu.commitTransaction();
                    hsu.beginTransaction();
                }
                    /*
                    System.out.println("----------------");
                    System.out.println(++nItterations);
                    System.out.println("totalWorkersReceived = " + totalWorkersReceived);
                    System.out.println("totalNewWorkers = " + totalNewWorkers);
                    System.out.println("totalUpdatedWorkers = " + totalUpdatedWorkers);
                    System.out.println("skippedOfficeWorkers = " + skippedOfficeWorkers);
                    System.out.println("hiredApplicants = " + hiredApplicants);
                     */
            }
            /*
            System.out.println("----------------");
            System.out.println(++nItterations);
            System.out.println("totalWorkersReceived = " + totalWorkersReceived);
            System.out.println("totalNewWorkers = " + totalNewWorkers);
            System.out.println("totalUpdatedWorkers = " + totalUpdatedWorkers);
            System.out.println("skippedOfficeWorkers = " + skippedOfficeWorkers);
            System.out.println("hiredApplicants = " + hiredApplicants);
             */
        } catch (Exception e) {
            logger.error(e);
        } finally {
            //dfw.close();
        }
        x = 2;
        logger.info("Total number of workers imported from ADP = " + totalWorkersReceived);
    }

    /**
     * Make sure string is not too big for the database column.
     */
    private static void setStringField(Record rec, String fld, String val, int maxLen) {
        if (val != null  &&  val.length() > maxLen)
            val = StringUtils.take(val, maxLen);
        rec.set(fld, val);
    }

    private static HashMap<String, String> getPositionMap(Connection db, String orgGroupId) throws Exception {
        List<Record> recs = db.fetchAll("select position_id, position_name from hr_position where org_group_id=? and first_active_date < ? and " +
                        "(last_active_date = 0 or last_active_date > ?)",
                orgGroupId, DateUtils.today(), DateUtils.today());
        HashMap<String, String> positionMap = new HashMap<>();
        recs.forEach(pos -> {
            try {
                positionMap.put(pos.getString("position_name").toUpperCase(), pos.getString("position_id"));
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        });
        return positionMap;
    }

    private static HashMap<String, Status> getStatusMap(Connection db, String orgGroupId) throws Exception {
        List<Record> recs = db.fetchAll("select status_id, name, active from hr_employee_status where org_group_id=? and " +
                        "(last_active_date = 0 or last_active_date >= ?)",
                orgGroupId, DateUtils.today());
        HashMap<String, Status> statusMap = new HashMap<>();
        recs.forEach(stat -> {
            try {
                Status s = new Status();
                s.status_id = stat.getString("status_id");
                s.name = stat.getString("name").toUpperCase();
                s.active = stat.getString("active");
                statusMap.put(s.name, s);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return statusMap;
    }

    private static int numberOfWorkers() throws Exception {
        JSONObject obj = Request.makeRequest("https://api.adp.com/hr/v2/workers?count=true&$top=1", Globals.GET, 0);
        JSONObject meta = obj.getJSONObject("meta");
        return meta.getInt("totalNumber");
    }

    private void updateStatus() {

    }

    private static class Status {
        String status_id;
        String name;
        String active;
    }

}

