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
package com.arahant.services.utils.imports;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProspectLog;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectLog;
import com.arahant.business.BSalesActivity;
import com.arahant.imports.SheakleyImport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SheaklyActivityImport {

    public static final String CALL = "00001-0000000001"; //;"00000-0000000005";0;"Dials";"Dials";1;0
//private String CALL = "00001-0000000002";"00000-0000000005";1;"QPAct";"Qual Prospect Act";2;0
//private String CALL = "00001-0000000003";"00000-0000000005";2;"Referrer Act";"Referrer Act";10;0
//private String CALL = "00001-0000000004";"00000-0000000005";3;"Referrals";"Referrals";10;0
//private String CALL = "00001-0000000005";"00000-0000000005";4;"Appt Sets";"Appointment Sets";10;0
    public static final String APPOINTMENT = "00001-0000000006";//"00000-0000000005";5;"Appts";"Appointments";25;0
    public static final String CLOSE = "00001-0000000007";//"00000-0000000005";6;"Deals";"Deals";80;0

    public static void doImport(String filename) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        hsu.dontAIIntegrate();
        hsu.setCurrentPersonToArahant();
        //ArahantSession.setFastKeys(true);
        Date date = new Date();
        DateFormat formatter;

        short seqNo = 5;
        int maxLength = 50;
        String prevData = "";
        String curData = "";
        int index = 0;
        try {
            DelimitedFileReader fr = new DelimitedFileReader(filename);
            //get Sales activity types

            //skip header line
            // for (int loop=0;loop<7101;loop++)
            //		fr.skipLine();

            int count = 0;
            while (fr.nextLine()) {


                String recorID = fr.nextString();
                String type = fr.nextString();
                String result = fr.nextString();
                String schedule = fr.nextString();
                String noteResult = fr.nextString();
                int activityDate = 0;
                int activityTime = 0;

                //get ORG_GROUP external id by the RECROD_ID
                OrgGroup org = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.EXTERNAL_REF, recorID).first();

                if (org != null) {
                    String[] notes = getNotes(noteResult);
                    //get the prospect info
                    BProspectCompany pc = new BProspectCompany(org.getOrgGroupId());

                    //if we have date convert it to integer
                    if (!schedule.equals("")) {
                        formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
                        try {
                            date = (Date) formatter.parse(schedule);
                        } catch (ParseException parseException) {
                            //just give today's date if error
                            date = new Date();
                        }
                        //     System.out.println("DATE read " + date.toString());
                        activityDate = DateUtils.getDate(date);
                        activityTime = DateUtils.getTime(date);
                    }

                    if (pc.getNextContactDate() < activityDate) {
                        pc.setNextContactDate(activityDate);
                        pc.update();
                    }
                    //map type
                    if (type.trim().toLowerCase().startsWith("call") || type.toLowerCase().startsWith("follow")) {
                        type = SheaklyActivityImport.CALL;
                    } else if (type.trim().toLowerCase().startsWith("appointment")) {
                        type = SheaklyActivityImport.APPOINTMENT;
                    } else if (type.trim().toLowerCase().startsWith("close")) {
                        type = SheaklyActivityImport.CLOSE;
                    } else {
                        //    System.out.println("WARNING: did not find mapping for " + type);
                        type = "00001-0000000008"; //NOT PROSPECT
                    }


                    //create the result first before saving to log
                    //for each notes, create results
                    for (String note : notes) {
                        //check the first few characters and see if it's a date
                        boolean isDate = isDateNote(note);
                        String dateToConvert = "";
                        int month = 0;
                        int year = 0;
                        if (isDate) {
                            dateToConvert = getDate(note);
                            String DATE_FORMAT = "MM";
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                            month = Integer.parseInt(sdf.format(date));
                            DATE_FORMAT = "yyyy";
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            year = cal.get(Calendar.YEAR);
                            month = cal.get(Calendar.MONDAY);
                            String[] dateFound = dateToConvert.split("/");
                            String noteMonth = dateFound[0];
                            String noteDay = "01";
                            if (dateFound.length > 1) {
                                noteDay = dateFound[1];
                            }
                            if (noteMonth.length() == 1) {
                                noteMonth = "0" + noteMonth;
                            }
                            if (noteDay.length() == 1) {
                                noteDay = "0" + noteDay;
                            }
                            //if (Integer.parseInt(dateFound[0]) > month){
                            activityDate = Integer.parseInt(year + noteMonth + noteDay);
                            //}

                        }

                        curData = recorID + activityDate + activityTime + "";
                        index++;
                        activityTime = activityTime + index;
                        if (!prevData.equals(curData)) {
                            prevData = curData;
                            //activityTime = activityTime + index;
                        }

                        //If the prospect log already exists, don't create it
                        if (!ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.ORG_GROUP, new BOrgGroup(pc.getOrgGroupId()).getOrgGroup()).eq(ProspectLog.CONTACT_DATE, activityDate).eq(ProspectLog.TIME, activityTime).exists()) {

                            //PROSPECT LOG is where the activities are being logged
                            BProspectLog bprospectLog = new BProspectLog();
                            bprospectLog.create();
                            bprospectLog.setContactDate(activityDate);
                            bprospectLog.setContactTime(activityTime);
                            bprospectLog.setContactText(note);
                            bprospectLog.setEntryDate(DateUtils.getDate(DateUtils.now()));
                            bprospectLog.setEmployee(pc.getSalesPerson().getEmployee());
                            bprospectLog.setOrgGroupId(pc.getOrgGroupId());
                            bprospectLog.setSalesActivity((new BSalesActivity(type)).getBean());
                            bprospectLog.setEmployees("");
                            //  System.out.println("REC ID " + recorID + " ORG = " + org.getName() + " Date " + activityDate + " Time " + activityTime);
                            bprospectLog.insert();
                            if (++count % 50 == 0) {
                                System.out.println(count);
                                hsu.commitTransaction();
                                hsu.beginTransaction();
                            }
                        }
                    }

                }

            }
            //hsu.commitTransaction();  //For Testing File Upload Servlet will commit in the app
        } catch (IOException ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SheaklyActivityImport.doImport("/home/brad/Desktop/Activities.csv");

    }

    public static boolean isDateNote(String note) {
        if (!note.equals("")) {
            char x = note.charAt(0);
            return Character.isDigit(x);
        }
        return false;
    }

    public static String getDate(String note) {
        String date = "";
        String dataDate = "";
        char[] chars = note.toCharArray();
        for (char x : chars) {
            if (Character.isDigit(x) || (x == '/')) {
                dataDate = dataDate + x;
            } else {
                //done;
                break;
            }
        }
        return dataDate;
    }

    public static String[] getNotes(String notes) {
        String re = "\n\n";
        String[] callResults = notes.split(re);
        return callResults;
    }
}
