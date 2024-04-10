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


/**
 *
 */
package com.arahant.utils;

import org.kissweb.DelimitedFileReader;
import org.kissweb.DelimitedFileWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CIGNAConfig {

	/*Cigna file */
	public static int BENEFIT_COLUMN = 21;
    public static int SSN_COLUMN = 2;
    public static int LNAME_COLUMN = 5;
    public static int FNAME_COLUMN = 6;
	public static int RELATIONSHIP_COLUMN=9;


/* Extract files
    public static int BENEFIT_COLUMN = 20;
    public static int SSN_COLUMN = 4;
    public static int LNAME_COLUMN = 1;
    public static int FNAME_COLUMN = 2;
	public static int RELATIONSHIP_COLUMN=8;
*/
    private void run () throws Exception {
        DelimitedFileReader reader = new DelimitedFileReader("/Users/fcc/CIGNA_FCC.csv");
        DelimitedFileWriter writer = new DelimitedFileWriter("/Users/fcc/CIGNA_LEVEL_FCC.csv");

        reader.nextLine();

        int depCount = 0;
        String oldSsn = "";
        String oldBene = "";
        String oldLname = "", oldFname = "";
        boolean spouse = false;

        while (reader.nextLine()) {
            String ssn = reader.getString(SSN_COLUMN);
            String fname = reader.getString(FNAME_COLUMN);
            String lname = reader.getString(LNAME_COLUMN);
            String benefit = reader.getString(BENEFIT_COLUMN);

            if (benefit.equals("No"))
                continue;

            if (ssn.equals(oldSsn) && !benefit.trim().equals("")) {
                depCount++;

                String relationship = reader.getString(RELATIONSHIP_COLUMN);
                System.out.println(relationship);
                if (relationship.trim().equals("SP") || relationship.equals("Spouse") || relationship.contains("Partner"))
                    spouse = true;
            } else {
                if (!"".equals(oldSsn)) {
                    writer.writeField(oldSsn);
                    writer.writeField(oldFname);
                    writer.writeField(oldLname);

                    if (!"".equals(oldBene)) {
                        writer.writeField(oldBene);

                        if (depCount == 0)
                            writer.writeField("Employee Only");
                        if (depCount == 1 && spouse)
                            writer.writeField("Employee + Spouse");
                        if (depCount == 1 && !spouse)
                            writer.writeField("Employee + Child");
                        if (depCount > 1 && spouse)
                            writer.writeField("Employee + Family");
                        if (depCount > 1 && !spouse)
                            writer.writeField("Employee + Children");
                    } else {
                        writer.writeField("NONE");
                        writer.writeField("");
                    }
                    writer.endRecord();
                }

                oldSsn = ssn;
                oldFname = fname;
                oldLname = lname;
                oldBene = benefit;
                depCount = 0;
                spouse = false;

                String relationship = reader.getString(RELATIONSHIP_COLUMN);
                System.out.println(relationship);
                if (relationship.trim().equals("SP") || relationship.equals("Spouse") || relationship.contains("Partner"))
                    spouse = true;

            }
        }

        //write out last guy
        writer.writeField(oldSsn);
        writer.writeField(oldFname);
        writer.writeField(oldLname);
        writer.writeField(oldBene);

        if (depCount == 0)
            writer.writeField("Employee Only");
        if (depCount == 1)
            writer.writeField("Employee + 1");
        if (depCount > 1)
            writer.writeField("Employee + Family");

        writer.endRecord();

        writer.close();
    }

    public static void main(String[] args) {
        try {
            new CIGNAConfig().run();
        } catch (Exception ex) {
            Logger.getLogger(CIGNAConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
