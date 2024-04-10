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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.beans.Project;
import com.arahant.beans.ProjectShift;
import com.arahant.beans.Timesheet;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * Arahant
 */
public class ReportBilling {

    public static void main(String[] args) {
        String clientCompanyId = "";
        String employeeId = "";
        String orgGroupId = "";
        String projectId = "";

        boolean isApproved = true;
        boolean isNotApproved = true;
        boolean isInvoiced = false;

        new ReportBilling().build(clientCompanyId, employeeId, orgGroupId, projectId, isApproved, isNotApproved, isInvoiced);
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

        public int compareTo(ReportDetail reportField) {
            int ret= client.compareTo(reportField.client);
            
            if (ret!=0)
                return ret;
            
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

        /**getEstimate
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

    public void build(String clientCompanyId, String employeeId, String orgGroupId, String projectId,
            boolean isApproved, boolean isNotApproved, boolean isInvoiced) {

        HibernateScrollUtil sumAndProjects = new HibernateScrollUtil(null);
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        hsu.setCurrentPerson(new BPerson("00000-0000000000").getPerson());
        sumAndProjects = BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(),
                clientCompanyId, projectId,
                0, 0, isApproved, isNotApproved, isInvoiced, -1);


        ArrayList<ReportDetail> reportDetails = new ArrayList();

        while (sumAndProjects.next()) {
            final Object[] eles = sumAndProjects.getObjects();
            final Double hours = (Double) eles[0];
            final Project p = (Project) eles[1];
            final Character c = (Character) eles[2];

            BProject bp = new BProject(p);
            BOrgGroup og = new BOrgGroup(p.getRequestingOrgGroup());
System.out.println("Hours " + hours);
            ReportDetail rd = new ReportDetail();
            rd.setClient(og.getName());
            rd.setId(p.getProjectName());
            rd.setSummary(p.getDescription());
            rd.setEstimate(p.getEstimateHours());
            rd.setInvoiced(bp.getInvoicedHours());
            rd.setRemaining(p.getEstimateHours() - bp.getInvoicedHours());
            float currentHours = hsu.createCriteria(Timesheet.class)
                    .joinTo(Timesheet.PROJECTSHIFT)
                    .eq(ProjectShift.PROJECT, bp.getBean())
                    .sum(Timesheet.TOTALHOURS)
                    .floatVal();
            rd.setCurrent(currentHours);
            double remain = p.getEstimateHours() - bp.getInvoicedHours();
            rd.setBillable(remain - currentHours);
            rd.setBillableRate(bp.getCalculatedBillingRate(null));
            System.out.println("Proj billable " + p.getBillingRate());
            System.out.println("Calculate bill " + rd.getBillableRate());
            System.out.println("\n\n");
            reportDetails.add(rd);
        }
        sumAndProjects.close();
        printReport(reportDetails);
    }

    private void printReport(ArrayList<ReportDetail> reportDetails) {
        Collections.sort(reportDetails);


        int size = reportDetails.size();
        double totalRemaining = 0;
        double totalCurrent = 0;
        double totalBillable = 0;
        String prevClient = "";

        for (int i = 0; i < size; i++) {
            ReportDetail rd = reportDetails.get(i);
            if (prevClient.equalsIgnoreCase("")) {
                System.out.println("Client " + rd.getClient());
            }
            System.out.println("Project ID " + rd.getId());
            System.out.println("Summary " + rd.getSummary());
            System.out.println("Estimated " + rd.getEstimate());
            System.out.println("Invoiced " + rd.getInvoiced());
            System.out.println("Remaining " + rd.getRemaining());
            System.out.println("Current " + rd.getCurrent());
            double totalBill = rd.getBillable() * rd.getBillableRate();
            System.out.println("Billable " + totalBill);
            System.out.println("==========");

            totalBillable += totalBill;
            totalCurrent += rd.getCurrent();
            totalRemaining += rd.getRemaining();

            //check if the next client is the same
            if (i < size - 1) {
                ReportDetail rdNext = reportDetails.get(i + 1);
                if (!rdNext.getClient().equalsIgnoreCase(rd.getClient())) {
                    //print total
                    System.out.println("\n");
                    System.out.println("-----Total--------");
                    System.out.println("Total remaining " + totalRemaining);
                    System.out.println("Total current " + totalCurrent);
                    System.out.println("Total billable " +  MoneyUtils.formatMoney(totalBillable));
                    System.out.println("\n");
                    totalRemaining = 0;
                    totalCurrent = 0;
                    totalBillable = 0;
                    prevClient = "";
                } else {
                    prevClient = rd.getClient();
                }
            }

        }
    }
}
