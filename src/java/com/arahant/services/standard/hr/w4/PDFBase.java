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
package com.arahant.services.standard.hr.w4;

import com.arahant.utils.FileSystemUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class PDFBase {

    private String filename;
    private String PDF_OutputDirectory = "reports";
    public String PDF_FORM_TEMPLATE_DIR = "/Forms/";
    public PdfReader reader = null;

    public PDFBase() {
    }

    public PdfStamper stampIt(String pdfInputFilename, boolean allowEdit) {
        try {
            reader = null;
            reader = new PdfReader(PDF_FORM_TEMPLATE_DIR + pdfInputFilename);

            //create the directory to put the filled in PDF files
            File reportDir = new File(FileSystemUtils.getWorkingDirectory(), PDF_OutputDirectory);
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }

            //create the output PDF
            File outputPDF = File.createTempFile(pdfInputFilename, ".pdf", reportDir);
            this.setFilename(outputPDF.getName());

            if (!allowEdit) {
                PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputPDF));
                return stamp;
            } else {
                PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputPDF), '\0', true);
                return stamp;
            }

        } catch (DocumentException ex) {
            Logger.getLogger(PDFBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void changeFormActoinURL(AcroFields form, String newURL, String buttonName) {
        //not working
        Map<String,Item> fields = form.getFields();
        AcroFields.Item field = (AcroFields.Item) fields.get(buttonName);
        PRIndirectReference ref = (PRIndirectReference) field.getWidgetRef(0);
        PdfDictionary object = (PdfDictionary) reader.getPdfObject(ref.getNumber());
        PRIndirectReference action = (PRIndirectReference) object.get(PdfName.A);
        PdfDictionary objectAction = (PdfDictionary) reader.getPdfObject(action.getNumber());
        PdfDictionary file = (PdfDictionary) objectAction.get(PdfName.F);
        file.put(PdfName.F, new PdfString(newURL));

    }

    public void showFields() {
        try {
            reader = null;
            reader = new PdfReader(this.filename);
            AcroFields pdfFormFields = reader.getAcroFields();

            Iterator iterator = pdfFormFields.getFields().keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                System.out.println("Field " + key + " value = " + pdfFormFields.getField(key));
            }
        } catch (IOException ex) {
            System.out.println("You need to set the correct filename.");
            Logger.getLogger(FillW4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return PDF_OutputDirectory + "/" + filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
