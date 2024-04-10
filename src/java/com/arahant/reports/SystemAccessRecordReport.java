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


package com.arahant.reports;

import com.arahant.beans.Screen;
import com.arahant.beans.SystemAccessRecord;
import com.arahant.business.BPerson;
import com.arahant.business.BScreen;
import com.itextpdf.text.DocumentException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.arahant.business.BSystemAccessRecord;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Arahant
 */
public class SystemAccessRecordReport extends ReportBase{

    public SystemAccessRecordReport() {
        super("SystemAccessRecordReport", "System Access Record Report");
    }

    @SuppressWarnings("empty-statement")
    public String Build(String[] personIds, int startingDate, int endingDate, boolean includeUser){
        HibernateSessionUtil HSU  = ArahantSession.getHSU();  

        try {
            PdfPTable table = makeTable(new int[]{20, 15, 70});
            writeHeaderLine("System Access Records: ", ArahantSession.getHSU().getCurrentCompany().getName());

            // if the person didn't select any people, just the "Include Yourself" button.
            if (personIds == null){
                personIds = new String[1];
                String id = (ArahantSession.getHSU().getArahantPersonId());
                List<String> lst = new ArrayList<String>();
                lst.add(id);
                personIds = lst.toArray(personIds);
            }

            // if the person selected other people and also the "Include Yourself" button.
            else if (includeUser){
                try{
                    List<String> personIdsList = new ArrayList<String>();
                    for (int loop = 0; loop < personIds.length; loop++){
                        personIdsList.add(personIds[loop]);
                    }
                    String yourId = (ArahantSession.getHSU().getArahantPersonId());
                    if (yourId != null)
                        personIdsList.add(yourId);
                    personIds = personIdsList.toArray(personIds);
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            // loops through the array and removes accidental duplicates
            /*List<String> originalList = new ArrayList<String>();
            for (int loop = 0; loop < personIds.length; loop++){
                originalList.add(personIds[loop]);
            }
            List<String> cleanList = new ArrayList<String>();
            for (int loop = 0; loop < originalList.size(); loop++){
                if (!cleanList.contains(originalList.get(loop)))
                    cleanList.add(originalList.get(loop)); System.out.println(originalList.get(loop));
            }
            System.out.println(personIds.length);
            personIds = cleanList.toArray(personIds);
            System.out.println(personIds.length);*/
            // loops through each employee in the input array, going in reverse-order to achieve proper alphabetic sequence
            for (int loop = personIds.length-1; loop >= 0; loop--){

                BPerson bpers = new BPerson(personIds[loop]);

                BSystemAccessRecord bsar = new BSystemAccessRecord();
                writeAlign(table, bpers.getNameLFM(), Element.ALIGN_LEFT, true, 3, 18);
                writeAlign(table, " ", Element.ALIGN_CENTER, false, 3);
                
                List<SystemAccessRecord> accessRecordList = bsar.getRecordsForPersonId(personIds[loop], startingDate, endingDate);
                
                if (accessRecordList.size() > 0){
                
                    writeColHeader(table, "Date", 1);
                    write(table, " ");
                    writeColHeader(table, "Screen Viewed", 1);
                    String previousScreenName = "-";

                    for (SystemAccessRecord sar : accessRecordList){

                        try{
                            String fileName = sar.getClassName();
                            fileName = fileName.trim();
                            fileName = fileName.replaceAll(".services.", "/app/screen/");
                            fileName = fileName.replace('.', '/');
                            fileName = fileName.substring(0, fileName.length()-3);
                            fileName += "Screen.swf";

                            HibernateCriteriaUtil screenCrit = ArahantSession.getHSU().createCriteria(Screen.class);
                            screenCrit.eq(Screen.FILENAME, fileName);
                            List<Screen> screens = screenCrit.list();
                            String screenName = screens.get(0).getName();

                            if (!previousScreenName.equals(screenName)){
                                String date = DateUtils.getDateTimeFormatted(sar.getEntryDate());
                                writeAlign(table, date, Element.ALIGN_LEFT, false, 1);
                                write(table, "");
                                writeAlign(table, screenName, Element.ALIGN_LEFT, false, 1);
                                //writeAlign(table, " ", Element.ALIGN_CENTER, false, 3);
                            }
                            previousScreenName = screenName;
                        }
                        catch(Exception ex){
                            System.out.println("1; " + ex);
                        }
                    }
                }
                else{
                    writeAlign(table, "No data saved for this person", Element.ALIGN_RIGHT, false, 3);
                    
                }
                writeAlign(table, "", Element.ALIGN_RIGHT, false, 3);
            }
            addTable(table);
            close();
        } 
        catch (DocumentException ex) {
            Logger.getLogger(SystemAccessRecordReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return getFilename();
    }
    public static void main(String[]args){
        SystemAccessRecordReport sarp = new SystemAccessRecordReport();
        String[] s = {"00000-0000000000", "00000-0000000000"};
        sarp.Build(s, 0,  0, true);
    }
}
