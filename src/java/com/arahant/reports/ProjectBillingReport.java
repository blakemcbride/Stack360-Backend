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
 *
 */
package com.arahant.reports;

import com.arahant.beans.Project;
import com.arahant.business.BClientCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Formatting;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectBillingReport extends ReportBase {

    private boolean checkOtherConditions(Double currentHours, BProject bp) {
        //this was taken from Blake's diagram on 6/23/2010
        //Blake,you asked me to put in these comments
        
        //if there is billable hours then show it
        if (currentHours > 0.0) {
            return true;
        }

        //check if client is active, if not, don't show
        System.out.println("ORG " + bp.getRequestingGroupId());
        BClientCompany client = new BClientCompany(bp.getRequestingGroupId());
        if (client.getClientStatus().getActive() == 'N') {
            return false;
        }

        //has the project been completed (not active)
        //1=active, 0=not active, 2=other
        if (bp.getProjectStatus().getActive() == 0) {
            return false;
        }

        //is this an ongoing project?
        if (bp.getProjectStatus().getActive() == 2) {
            //does it have estimates?
            System.out.println("Estimate hours " + bp.getEstimateHours());
            if (bp.getEstimateHours() > 0.0) {
                return true;
            }
        }

        return false;

    }

    private class ReportDetail implements Comparable<ReportDetail> {

        private String client;
        private String id;
        private String summary;
        private double estimate;
        private double invoiced;
        private double remaining;
        private double current;
        private double billable;
        private double billableRate;

        @Override
        public int compareTo(ReportDetail reportField) {
            int ret = client.compareTo(reportField.client);

            if (ret != 0) {
                return ret;
            }

            return id.compareTo(reportField.id);

        }

        /**
         * @return the client
         */
        public String getClient() {
            return client;
        }

        /**
         * @param client the client to set
         */
        public void setClient(String client) {
            this.client = client;
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the summary
         */
        public String getSummary() {
            return summary;
        }

        /**
         * @param summary the summary to set
         */
        public void setSummary(String summary) {
            this.summary = summary;
        }

        /**
         * @return the estimate
         */
        public double getEstimate() {
            return estimate;
        }

        /**
         * @param estimate the estimate to set
         */
        public void setEstimate(double estimate) {
            this.estimate = estimate;
        }

        /**
         * @return the invoiced
         */
        public double getInvoiced() {
            return invoiced;
        }

        /**
         * @param invoiced the invoiced to set
         */
        public void setInvoiced(double invoiced) {
            this.invoiced = invoiced;
        }

        /**
         * @return the remaining
         */
        public double getRemaining() {
            return remaining;
        }

        /**
         * @param remaining the remaining to set
         */
        public void setRemaining(double remaining) {
            this.remaining = remaining;
        }

        /**
         * @return the current
         */
        public double getCurrent() {
            return current;
        }

        /**
         * @param current the current to set
         */
        public void setCurrent(double current) {
            this.current = current;
        }

        /**
         * @return the billable
         */
        public double getBillable() {
            return billable;
        }

        /**
         * @param billable the billable to set
         */
        public void setBillable(double billable) {
            this.billable = billable;
        }

        /**
         * @return the billableRate
         */
        public double getBillableRate() {
            return billableRate;
        }

        /**
         * @param billableRate the billableRate to set
         */
        public void setBillableRate(double billableRate) {
            this.billableRate = billableRate;
        }
    }

    public ProjectBillingReport() {
        super("prbillrpt", "Project Billing Report", true);
    }

    public String build(String clientCompanyId, String employeeId, String orgGroupId, String projectId, boolean isApproved,
            boolean isNotApproved, boolean isInvoiced) throws DocumentException {

        HibernateScrollUtil sumAndProjects = new HibernateScrollUtil(null);
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        sumAndProjects = BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(),
                clientCompanyId, projectId, 0, 0, isApproved, isNotApproved, isInvoiced, -1);

        ArrayList<ReportDetail> reportDetails = new ArrayList();

        while (sumAndProjects.next()) {
            final Object[] eles = sumAndProjects.getObjects();
            final Double hours = (Double) eles[0];
            final Project p = (Project) eles[1];
            final Character cBillable = (Character) eles[2];

            BProject bp = new BProject(p);
            BOrgGroup og = new BOrgGroup(p.getRequestingOrgGroup());

            ReportDetail rd = new ReportDetail();
            
            //only show projects with remaining estimates or non-billed
            if (cBillable == 'Y') {
                rd.setClient(og.getName());
                rd.setId(p.getProjectName());
                rd.setSummary(p.getDescription());
                rd.setEstimate(p.getEstimateHours());
                rd.setInvoiced(bp.getInvoicedHours());
                rd.setRemaining(p.getEstimateHours() - bp.getInvoicedHours());
                Double currentHours = hours; //hsu.createCriteria(Timesheet.class).eq(Timesheet.PROJECT, bp.getBean()).eq(Timesheet.BILLABLE, 'Y').ne(Timesheet.STATE, 'I').sum(Timesheet.TOTALHOURS).floatVal();
                rd.setCurrent(currentHours);
                double remain = p.getEstimateHours() - bp.getInvoicedHours();

                //If remaining < 0 set it to 0
                if (remain < 0) {
                    rd.setRemaining(0);
                }

                if (rd.getEstimate() == 0) {
                    rd.setBillable(currentHours);
                } else if (currentHours > remain) {
                    rd.setBillable(remain);
                } else {
                    rd.setBillable(currentHours);
                }
                //if <0 set to 0
                if (rd.getBillable() < 0) {
                    rd.setBillable(0);
                }
                rd.setBillableRate(bp.getDefaultBillingRate(null));

                //check other conditions before showing
                if (checkOtherConditions(currentHours, bp)) {
                    reportDetails.add(rd);
                }

            }
        }

        try {

            PdfPTable table;

            if (!isEmpty(projectId)) {
                BProject bpp = new BProject(projectId);
                writeHeaderLine("Project", bpp.getProjectName());
            }

            if (!isEmpty(clientCompanyId)) {
                BOrgGroup bo = new BOrgGroup(clientCompanyId);
                writeHeaderLine("Client", bo.getCompanyName());
            }

            if (!isEmpty(employeeId)) {
                BEmployee be = new BEmployee(employeeId);
                writeHeaderLine("Employee", be.getNameFML());
            }

            if (!isEmpty(orgGroupId)) {
                BOrgGroup bo = new BOrgGroup(orgGroupId);
                writeHeaderLine("Company", bo.getCompanyName());
            }

            if (isApproved || isNotApproved || isInvoiced) {
                String showLine = (isApproved ? "Approved, " : "");
                showLine += (isNotApproved ? "NotApproved, " : "");
                showLine += (isInvoiced ? "Invoiced, " : "");

                if (showLine.length() > 2) {
                    showLine = showLine.substring(0, showLine.trim().length() - 1);
                }

                writeHeaderLine("Timesheet Entry Status", showLine, 0);
            }

            addHeaderLine();

            addColumnHeader("Client", Element.ALIGN_LEFT, 15);
            addColumnHeader("ID", Element.ALIGN_LEFT, 5);
            addColumnHeader("Project Summary", Element.ALIGN_LEFT, 20);
            addColumnHeader("Estimated", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Invoiced", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Hours Remaining", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Current", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Billable", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Total", Element.ALIGN_RIGHT, 12);

            table = makeTable(getColHeaderWidths());

            writeColHeaders(table);

            printReport(reportDetails, table);


        } finally {
            close();
        }

        return getFilename();
    }

    private void printReport(ArrayList<ReportDetail> reportDetails, PdfPTable table) {
        Collections.sort(reportDetails);

        int size = reportDetails.size();
        double totalRemaining = 0;
        double totalCurrent = 0;
        double totalBillable = 0;
        double totalAmountBilledTotal = 0;

        double grandTotalRemaining = 0;
        double grandTotalCurrent = 0;
        double grandTotalBillable = 0;
        double grandTotalAmountBilledTotal = 0;

        String prevClient = "";

        for (int i = 0; i < size; i++) {
            ReportDetail rd = reportDetails.get(i);

            if (prevClient.equalsIgnoreCase("")) {
                write(table, rd.getClient(), false);
            } else {
                write(table, "", false);
            }

            double totalBill = rd.getBillable();
            write(table, rd.getId().trim(), false);
            write(table, rd.getSummary(), false);
            writeRight(table, Formatting.formatNumberWithCommas(rd.getEstimate(), 0) + "", false);
            writeRight(table, Formatting.formatNumberWithCommas(rd.getInvoiced(), 0) + "", false);


            writeRight(table, Formatting.formatNumberWithCommas(rd.getRemaining(), 0) + "", false);
            writeRight(table, Formatting.formatNumberWithCommas(rd.getCurrent(), 0) + "", false);
            writeRight(table, Formatting.formatNumberWithCommas(rd.getBillable(), 0) + "", false);
            double totalAmountBilled = rd.getBillableRate() * totalBill;

            writeRight(table, MoneyUtils.formatMoney(totalAmountBilled), false);

            totalBillable += totalBill;
            totalCurrent += rd.getCurrent();
            totalRemaining += rd.getRemaining();
            totalAmountBilledTotal += totalAmountBilled;

            grandTotalBillable += totalBill;
            grandTotalCurrent += rd.getCurrent();
            grandTotalRemaining += rd.getRemaining();
            grandTotalAmountBilledTotal += totalAmountBilled;

            //check if the next client is the same
            if (i < size - 1) {
                ReportDetail rdNext = reportDetails.get(i + 1);
                if (!rdNext.getClient().equalsIgnoreCase(rd.getClient())) {

                    write(table, "", true);
                    write(table, "", true);
                    write(table, "", true);
                    write(table, "", true);
                    writeRight(table, "Totals", true);
                    writeRight(table, Formatting.formatNumberWithCommas(totalRemaining, 0) + "", true);
                    writeRight(table, Formatting.formatNumberWithCommas(totalCurrent, 0) + "", true);
                    writeRight(table, Formatting.formatNumberWithCommas(totalBillable, 0) + "", true);
                    writeRight(table, MoneyUtils.formatMoney(totalAmountBilledTotal) + "", true);
                    totalRemaining = 0;
                    totalCurrent = 0;
                    totalBillable = 0;
                    totalAmountBilledTotal = 0;
                    prevClient = "";
                } else {
                    prevClient = rd.getClient();
                }
            }
        }

        //print the last total
        write(table, "", true);
        write(table, "", true);
        write(table, "", true);
        write(table, "", true);
        writeRight(table, "Totals", true);
        writeRight(table, Formatting.formatNumberWithCommas(totalRemaining, 0) + "", true);
        writeRight(table, Formatting.formatNumberWithCommas(totalCurrent, 0) + "", true);
        writeRight(table, Formatting.formatNumberWithCommas(totalBillable, 0) + "", true);
        writeRight(table, MoneyUtils.formatMoney(totalAmountBilledTotal) + "", true);



        try {
            addTable(table);
            table = makeTable(new int[]{56, 8, 8, 8, 12});
            //print GRAND total

            writeRight(table, " Grand Totals", true);
            writeRight(table, Formatting.formatNumberWithCommas(grandTotalRemaining, 0) + "", true);
            writeRight(table, Formatting.formatNumberWithCommas(grandTotalCurrent, 0) + "", true);
            writeRight(table, Formatting.formatNumberWithCommas(grandTotalBillable, 0) + "", true);
            writeRight(table, MoneyUtils.formatMoney(grandTotalAmountBilledTotal) + "", true);
            addTable(table);
        } catch (DocumentException ex) {
            Logger.getLogger(ProjectBillingReport.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static void main(String[] args) {
        //build(String clientCompanyId, String employeeId, String orgGroupId, String projectId, boolean isApproved, boolean isNotApproved, boolean isInvoiced)
        ArahantSession.getHSU().setCurrentPerson(new BPerson("00001-0000000000").getPerson());
        try {
            new ProjectBillingReport().build("", "", "", "", true, true, false);
        } catch (DocumentException ex) {
            Logger.getLogger(ProjectBillingReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
