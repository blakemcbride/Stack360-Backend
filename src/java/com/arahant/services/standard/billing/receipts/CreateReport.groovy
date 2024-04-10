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

package com.arahant.services.standard.billing.receipts

import com.arahant.business.BExpenseReceipt
import com.arahant.servlets.REST
import com.arahant.utils.*
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/20/18
 */
class CreateReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        PDF pdf = null

        try {

            File fyle = FileSystemUtils.createReportFile "ReceiptReport-", ".pdf"

            JSONArray ids = injson.getJSONArray("expense_receipt_ids")

            pdf = new PDF(fyle.getAbsolutePath())

            int len = ids.length()
            for (int i = 0; i < len; i++) {

                String id = ids.getString(i)
                BExpenseReceipt ber = new BExpenseReceipt(id)

                pdf.newPage()

                pdf.setFont PDType1Font.COURIER, 14

                pdf.textOut 6, 29, "Expense Receipt Report"

                pdf.drawRect 50, 205, 85, 400, 1, -1

                pdf.setFont PDType1Font.COURIER, 12

                pdf.textOut 6, 59, DateUtils.getDateTimeFormatted(DateUtils.today(), DateUtils.getTime(new Date()))

                int col = 5

                pdf.textOut 9, col, "Worker: " + ber.getPerson().getNameLFM()

                pdf.textOut 10, col, "Project:  " + ber.getProject().getDescription()

                pdf.textOut 11, col, "Expense account:  " + ber.getExpenseAccount().getDescription()

                String pm
                switch (ber.getPaymentMethod().toString()) {
                    case 'A': pm = 'Company Debt Card'; break;
                    case 'B': pm = 'Employee Comdata Card'; break;
                    case 'E': pm = 'Employee Personal Account'; break;
                    default: pm = '';
                }

                String approved = ber.getApproved().toString() == 'Y' ? 'Approved' : 'Not approved'

                pdf.textOut 12, col, "Receipt:  " + DateUtils.getDateFormatted(ber.getReceiptDate()) + Utils.Format(ber.getAmount(), "CD", 12, 2) + " (" + pm + ") - " + approved


                pdf.textOut 13, col, line1(ber.getBusinessPurpose())
                pdf.textOut 14, col, line2(ber.getBusinessPurpose())

                byte[] pic = ber.getPicture1()
                if (pic != null)
                    try {
                        pdf.imageOut(740, 30, 170, 580, pic)
                    } catch (Exception e) {
                        //  can't print PDF files
                    }

                pdf.textOut 63, col, "Uploaded: " + ber.getWhoUploaded().getNameLFM() + "(" +
                        DateUtils.dateFormat("MM/dd/yy HH:mm", ber.getWhenUploaded()) + ")"
                if (ber.getApproved().toString() == 'Y')
                    pdf.textOut 64, col, "Approved: " + ber.getWhoApproved().getNameLFM() + "(" +
                            DateUtils.dateFormat("MM/dd/yy HH:mm", ber.getWhenApproved()) + ")"

                //pdf.grid();

                hsu.clear()

            }
            outjson.put("url", FileSystemUtils.getHTTPPath(fyle))
        } finally {
            if (pdf != null)
                pdf.endDocument();
        }
    }

    private final static int MAX_STRING = 70

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
