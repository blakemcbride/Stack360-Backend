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

package com.arahant.dynamic.services.standard.project.projectForm

import com.arahant.beans.ProjectForm
import com.arahant.business.BProject
import com.arahant.business.BProjectForm
import com.arahant.business.BProjectShift
import com.arahant.business.BProperty
import com.arahant.dynamic.services.standard.site.activeEmployeesReport.GetReport
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps
import com.arahant.utils.Image
import com.arahant.utils.DateUtils
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.PDF
import org.kissweb.StringUtils
import com.arahant.utils.dynamicwebservices.DataObjectMap
import org.apache.pdfbox.pdmodel.font.PDType1Font

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Author: Blake McBride
 * Date: 3/27/18
 */
class FormReport {

    public static void main(DataObjectMap input, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
    {
        File fyle = FileSystemUtils.createTempFile "FormReport-", ".pdf"
        PDF pdf = null;

        try {
            float scale = 0.14f
            def project_id = input.getString "projectId"
            def shiftId = input.getString "shiftId"
            def formTypeId = input.getString "formType"
            def entriesPerPage = input.getInt "entriesPerPage"
            def beginningDate = input.getInt "beginningDate"
            def endingDate = input.getInt "endingDate"
            def bproj = new BProject(project_id)
            def bshift = new BProjectShift(shiftId)

            pdf = new PDF(fyle.getAbsolutePath())

            pdf.newPage()

            pdf.setFont PDType1Font.COURIER, 14

            pdf.textOut 6, 32, "Project Form Report"

            pdf.drawRect 50, 210, 85, 400, 1, -1

            pdf.setFont PDType1Font.COURIER, 12

            pdf.textOut 9, 24, "Run date:  " + DateUtils.getDateTimeFormatted(DateUtils.today(), DateUtils.getTime(new Date()))

            pdf.textOut 11, 10, "Summary: " + bproj.getSummary()

            pdf.textOut 12, 10, "Type:  " + bproj.getProjectType().getDescription()

            def drtext = "Date range:  "
            if (beginningDate == 0  &&  endingDate == 0)
                drtext += "All"
            else if (beginningDate == 0)
                drtext += "Up through " + DateUtils.getDateFormatted(endingDate)
            else if (endingDate == 0)
                drtext += "Ending " + DateUtils.getDateFormatted(beginningDate)
            else
                drtext += DateUtils.getDateFormatted(beginningDate) + " - " + DateUtils.getDateFormatted(endingDate)

            pdf.textOut 13, 10, drtext

            ArrayList<ProjectForm> pforms = new ArrayList<ProjectForm>()
            bshift.getProjectForms().forEach {
                if (it.formType.formTypeId == formTypeId  &&
                        it.formDate >= beginningDate &&
                        (endingDate == 0 || it.formDate <= endingDate))
                    pforms.add it
            }

            pforms.sort { a, b -> a.formDate <=> b.formDate }

            if (entriesPerPage == 1) {

                if (!pforms.isEmpty()) {
                    String comment = makeLine pforms[0]
                    pdf.textOutpx 180, 30, line1(comment)
                    pdf.textOutpx 190, 30, line2(comment)
                    try {
                        imageOut2(pdf, 750, 30, 200, 500, pforms[0])
                    } catch (Exception e) {
                        //  can't print PDF files
                    }
                }

                for (int i = 1; i < pforms.size();) {
                    pdf.newPage()

                    String comment = makeLine pforms[i]
                    pdf.textOutpx 70, 30, line1(comment)
                    pdf.textOutpx 80, 30, line2(comment)
                    try {
                        imageOut2(pdf, 750, 30, 100, 500, pforms[i++])
                    } catch (Exception e) {
                        //  can't print PDF files
                    }

                }
            } else {
                if (!pforms.isEmpty()) {
                    String comment = makeLine pforms[0]
                    pdf.textOutpx 180, 30, line1(comment)
                    pdf.textOutpx 190, 30, line2(comment)
                    try {
                        imageOut2(pdf, 750, 30, 200, 500, pforms[0])
                    } catch (Exception e) {
                        //  can't print PDF files
                    }
                }

                for (int i = 1; i < pforms.size();) {
                    pdf.newPage()
                    String comment = makeLine pforms[i]
                    pdf.textOutpx 70, 30, line1(comment)
                    pdf.textOutpx 80, 30, line2(comment)
                    try {
                        imageOut2(pdf, 350, 30, 100, 500, pforms[i++])
                    } catch (Exception e) {
                        //  can't print PDF files
                    }

                    if (i < pforms.size()) {
                        comment = makeLine pforms[i]
                        pdf.textOutpx 470, 30, line1(comment)
                        pdf.textOutpx 480, 30, line2(comment)
                        try {
                            imageOut2(pdf, 750, 30, 500, 500, pforms[i++])
                        } catch (Exception e) {
                            //  can't print PDF files
                        }
                    }
                }
            }
           // pdf.grid()

            out.put "reportUrl", FileSystemUtils.getHTTPPath(fyle)
        } catch (Exception ex) {
            Logger.getLogger(GetReport.class.getName()).log(Level.SEVERE, null, ex)
        } finally {
            if (pdf != null)
                pdf.endDocument();
        }
    }

    private static String makeLine(ProjectForm pf) {
        String comment = pf.getComments()
        if (comment == null)
            comment = ""
        return StringUtils.rightStrip(comment) + " (" + pf.projectFormId.substring(10) + ")"
    }

    private static void imageOut2(PDF pdf, float ypos, float xpos, float ypos2, float xpos2, ProjectForm pf) {
        BProjectForm bpf = new BProjectForm(pf)
        byte[] image = ExternalFile.getBinary(ExternalFile.PROJECT_FORM_IMAGE, bpf.getProjectFormId(), bpf.getFileNameExtension());
        String ext = pf.getFileNameExtension().toLowerCase()
        if (ext != 'jpg' && ext != 'jpeg')
            image = Image.compressImage(ext, image, BProperty.getFloat("JPGCompressionFactor", 0.2f))
        pdf.imageOut(ypos, xpos, ypos2, xpos2, image);
    }

    private final static int MAX_STRING = 70;

    private static String line1(String s) {
        if (s == null)
            return ""
        if (s.length() <= MAX_STRING)
            return s
        int i
        for (i=MAX_STRING ; i >= 0  &&  s[i] != ' ' ; i--);
        return s.substring(0, i)
    }

    private static String line2(String s) {
        if (s == null  ||  s.length() <= MAX_STRING)
            return ""
        int i
        for (i=MAX_STRING ; i >= 0  &&  s[i] != ' ' ; i--);
        return s.substring(i+1)
    }


}
