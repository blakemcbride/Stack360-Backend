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


package com.arahant.services.standard.hr.w4;

import com.arahant.beans.Address;
import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FillW4 extends PDFBase {

    /**
     * @param args
     */
    private String W4_PDF = "w4J3.pdf";

    public FillW4() {
    }

    
    public String fillPDFForm(Employee employee, boolean allowEdit,String serverURL) {
        try {
            PdfStamper stamp = stampIt(W4_PDF, allowEdit);
            AcroFields pdfFormFields = stamp.getAcroFields();

            fillW4(pdfFormFields, employee, serverURL);

            if (!allowEdit) {
                stamp.setFormFlattening(true);
            }

            stamp.close();
            return this.getFilename();
        } catch (DocumentException ex) {
            Logger.getLogger(FillW4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FillW4.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private void fillW4(AcroFields pdfFormFields, Employee employee,String serverURL) {
        try {

            //SET these fields in the PDF
            //we'll need it later in the servlet
            String url = "http://"+serverURL.trim()+"W4PDFFormServlet";
            System.out.println("URL " + url);
            pdfFormFields.setField("fld_url", url);
            pdfFormFields.setField("fld_employeeId", employee.getPersonId());
            //DO NOT set the user/password.  The servlet will use the Arahant user to upload the form
            //pdfFormFields.setField("fld_user", employee.getProphetLogin().getUserLogin());
            //pdfFormFields.setField("fld_password", employee.getProphetLogin().getUserPassword());
            //pdfFormFields.setField("fld_formTypeId", "00001-0000000001");//hardcoded right now

            pdfFormFields.setField("fld_firstName", employee.getFname());
            pdfFormFields.setField("fld_lastName", employee.getLname());
            String SSN = employee.getUnencryptedSsn();
            if (SSN.length() > 0) {
                pdfFormFields.setField("fld_ssn1", SSN.substring(0, 3));
                pdfFormFields.setField("fld_ssn2", SSN.substring(4, 6));
                pdfFormFields.setField("fld_ssn3", SSN.substring(7));
            }

            // ' The form's checkboxes
            if (employee.getMaritalStatus()=='S'){
                pdfFormFields.setField("fld_single", "Yes");// marital status, single
            } else if (employee.getMaritalStatus()=='M'){
                pdfFormFields.setField("fld_married", "Yes");// married
            } else if (employee.getMaritalStatus()=='H'){
                pdfFormFields.setField("fld_married2", "Yes");// married with higher rate of withholding
            }

            if (employee.getW4exempt()=='Y'){
                pdfFormFields.setField("fld_exempt", "Yes");
            }

            if (employee.getFederalExtraWithhold() > 0){
                pdfFormFields.setField("fld_additionalAmount", employee.getFederalExtraWithhold() + "");
            }

            if (employee.getW4nameDiffers()=='Y'){
                 pdfFormFields.setField("fld_lastNameDifferent", "Yes");
            }
            pdfFormFields.setField("fld_exemptions", employee.getNumberFederalExemptions() + "");

            String CSZ = "";
            String streetAddress = "";

            BPerson person = new BPerson(employee.getPersonId());
            Address address = person.getHomeAddress();
            if (address != null) {
                streetAddress = address.getStreet() + " " + address.getStreet2();
                CSZ = address.getCity() + ", " + address.getState() + " " + address.getZip();
            }

            pdfFormFields.setField("fld_street1", streetAddress);
            pdfFormFields.setField("fld_city", address.getCity());
            pdfFormFields.setField("fld_state", address.getState());
            pdfFormFields.setField("fld_zip", address.getZip());
            pdfFormFields.setField("f1_18(0)", "");
            pdfFormFields.setField("f1_19(0)", "");
            pdfFormFields.setField("f1_20(0)", "");
            pdfFormFields.setField("f1_21(0)", "");
            pdfFormFields.setField("f1_22(0)", "");
//====
//            pdfFormFields.setField("fld_exemptions", "9");
//            pdfFormFields.setField("f1_17(0)", "10");
//            pdfFormFields.setField("fld_exempt", "EXEMPT");
//            pdfFormFields.setField("f1_19(0)", "Arahant, 130 9th Ave S #110  Franklin, TN  ");
//            pdfFormFields.setField("f1_20(0)", "Arahant123");
//            pdfFormFields.setField("f1_21(0)", "AB");
//            pdfFormFields.setField("f1_22(0)", "4321");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BEmployee emp = new BEmployee("00001-0000000001");
        BPerson person = new BPerson("00001-0000000001");
        ArahantSession.getHSU().setCurrentPerson(person.getPerson());
        FillW4 pdf = new FillW4();
        pdf.setFilename("/Forms/kpaul.pdf");
       // pdf.fillPDFForm(emp.getEmployee(), true);
        pdf.showFields();
    }


}
