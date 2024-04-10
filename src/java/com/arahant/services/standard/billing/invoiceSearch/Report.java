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

package com.arahant.services.standard.billing.invoiceSearch;

import com.arahant.business.BInvoice;
import com.arahant.business.BOrgGroup;
import org.kissweb.DateUtils;
import org.kissweb.Groff;
import org.kissweb.NumberFormat;
import org.kissweb.StringUtils;

import java.io.IOException;
import java.util.Random;

/**
 * Author: Blake McBride
 * Date: 12/17/21
 */
class Report {

    private static final boolean demoMode = false;

    String build(BInvoice [] invs, ReportInput in) throws IOException, InterruptedException {
        final int descLen = 40;
        final int clientLen = 40;
        double total = 0;
        int projectNo = 1, clientNo = 1, nInvoices = 0;
        Groff rpt = new Groff("InvoiceReport-", "Invoice Report", true);
        //rpt.dontDeleteGroffFile();
        if (demoMode)
            rpt.setRuntime("12/17/20XX 1:47 PM");
        report_title(rpt, in);
        start_table(rpt);
        for (BInvoice bi : invs) {
            if (demoMode && nInvoices > 20)
                break;
            ++nInvoices;
            String id = bi.getAccountingInvoiceId();
            if (id == null)
                id = "";
            else
                id = id.trim();
            rpt.column(id);
            if (demoMode)
                rpt.column(demoDate(bi.getDate()));
            else
                rpt.column(DateUtils.format4(bi.getDate()));
            String client = (new BOrgGroup(bi.getCustomerProphetId())).getName();
            if (client == null)
                client = "";
            client = client.trim();
            if (demoMode)
                client = "Client " + clientNo++;
            if (client.length() > clientLen)
                client = StringUtils.take(client, clientLen);
            rpt.column(client);
            double amt = bi.getInvoiceAmount();
            if (demoMode)
                amt = Math.random() * 100000.0;
            rpt.column(amt, "CD", 2);
            total += amt;
            String desc = bi.getDescription();
            if (demoMode)
                desc = "Project " + projectNo++;
            if (desc == null)
                desc = "";
            else if (desc.length() > descLen)
                desc = StringUtils.take(desc, descLen);
            desc = desc.replaceAll("\n", " ");
            desc = desc.replaceAll("\r", "");
            rpt.column(desc);
            if (demoMode)
                rpt.column(demoDate(DateUtils.addDays(bi.getDate(), 10)));
            else
                rpt.column(DateUtils.format4(DateUtils.toInt(bi.getExportDate())));
        }
        rpt.column(nInvoices, "");
        rpt.column(null);
        rpt.column("     Total");
        rpt.column(total, "CD", 2);

        //System.out.println(rpt.getGroffFileName());
        return rpt.process(0.5f);
    }

    private static String demoDate(int date) {
        String dt = DateUtils.format4(date);
        return StringUtils.drop(dt, -2) + "XX";
    }

    private static void report_title(Groff rpt, ReportInput in) {
        rpt.out(".SP");
        String clientId = in.getClientId();
        if (clientId != null && clientId.length() > 5)
            rpt.out("Client: " + (new BOrgGroup(clientId)).getName());
        else
            rpt.out("Client: All");
        int invBegDate = in.getInvoiceStartDate();
        int invEndDate = in.getInvoiceEndDate();
        if (invBegDate > 0 && invEndDate > 0)
            rpt.out("Invoice date range: " + DateUtils.format4(invBegDate) + " to " + DateUtils.format4(invEndDate));
        else if (invBegDate > 0)
            rpt.out("Invoice date range: " + DateUtils.format4(invBegDate) + " to current");
        else if (invEndDate > 0)
            rpt.out("Invoice date range: beginning to " + DateUtils.format4(invEndDate));
        else
            rpt.out("Invoice date range: All");

        String invId = in.getInvoiceId();
        if (invId != null  &&  !invId.isEmpty())
            switch (in.getInvoiceIdSearchType()) {
                case 2:
                    rpt.out("Invoice IDs starting with " + invId);
                    break;
                case 3:
                    rpt.out("Invoice IDs ending with " + invId);
                    break;
                case 4:
                    rpt.out("Invoice IDs containing " + invId);
                    break;
                case 5:
                    rpt.out("Invoice ID " + invId);
                    break;
            }

        double amt = in.getAmount();
        if (amt > .009) {
            switch (in.getAmountSearchType()) {
                case 11:
                    rpt.out("Invoices greater than " + NumberFormat.Format(amt, "DC", 0, 2));
                    break;
                case 12:
                    rpt.out("Invoices less than " + NumberFormat.Format(amt, "DC", 0, 2));
                    break;
                case 13:
                    rpt.out("Invoices equal to " + NumberFormat.Format(amt, "DC", 0, 2));
                    break;
                case 14:
                    rpt.out("Invoices not equal to " + NumberFormat.Format(amt, "DC", 0, 2));
                    break;
            }
        }
        switch (in.getInvoiceStatus()) {
            case 1:
                rpt.out("Invoices not yet transmitted to the accounting system");
                break;
            case 2:
                rpt.out("Invoices transmitted to the accounting system");
                break;
            case 0:
                break;
        }
        rpt.out(".SP 1");
    }

    private static void start_table(Groff rpt) {
        rpt.startTable("L C L R L R");
        rpt.column("Inv #");
        rpt.column("Date");
        rpt.column("Client");
        rpt.column("Amount");
        rpt.column("Description");
        rpt.column("Date Transmitted");
        rpt.endTitle();
    }

}
