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
package com.arahant.imports;

import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */

/////////////////////////////////////////////////////////////////////
////  NOT FINISHED /////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
public class SheakleyImport {

    HibernateSessionUtil hsu;

    public void importActivities(String filename) {
        try {
            DelimitedFileReader fr = new DelimitedFileReader(filename);
            //skip header line
            fr.nextLine();
            int count = 0;
            while (fr.nextLine()) {
                if (++count % 50 == 0) {
                    System.out.println(count);
                    hsu.commitTransaction();
                    hsu.beginTransaction();
                }
                int recorID = fr.nextInt();
                String type = fr.nextString();
                String result = fr.nextString();
                String schedule = fr.nextString();
                String noteResult = fr.nextString();


            }
        } catch (IOException ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void importOpportunities(String filename) {
        try {
            DelimitedFileReader fr = new DelimitedFileReader(filename);
            //skip header line
            fr.nextLine();

            int count = 0;
            while (fr.nextLine()) {


                if (++count % 50 == 0) {
                    System.out.println(count);
                    hsu.commitTransaction();
                    hsu.beginTransaction();
                }

                int recordId = fr.nextInt();
                String oppTitle = fr.nextString();
                String compName = fr.nextString();
                String compPhone = fr.nextString();
                String salesRep = fr.nextString();
                String oppStatus = fr.nextString();

                String CPA = fr.nextString();
                String payFName = fr.nextString();
                String payLName = fr.nextString();
                String payTitle = fr.nextString();
                String payPhone = fr.nextString();
                String payEmail = fr.nextString();
                String payFax = fr.nextString();
                String payFrequency = fr.nextString();
                String payNumberOfEmployees = fr.nextString();

                String compFName = fr.nextString();
                String compLName = fr.nextString();
                String compAddress = fr.nextString();
                String compCity = fr.nextString();
                String compState = fr.nextString();
                String compzip = fr.nextString();
                String CPAFirm = fr.nextString();
                String CPAContact = fr.nextString();
                String CPAPhone = fr.nextString();
                String CPAEmail = fr.nextString();
                String oppNotes = fr.nextString();


            }

        } catch (IOException ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SheakleyImport.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String args[]) {
        SheakleyImport imp = new SheakleyImport();
        try {

            imp.hsu = ArahantSession.getHSU();
            imp.hsu.dontAIIntegrate();
            imp.hsu.setCurrentPersonToArahant();

            imp.importOpportunities("Opportunities.csv");
            //imp.importEarnings("/Users/Arahant/girlscouts/moredata/earnings.csv");
            imp.hsu.commitTransaction();
        } catch (Exception ex) {
            imp.hsu.rollbackTransaction();
            ex.printStackTrace();
            Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
