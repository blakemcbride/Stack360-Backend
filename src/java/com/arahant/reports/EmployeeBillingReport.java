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

import com.arahant.beans.Employee;
import com.arahant.beans.Project;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Formatting;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EmployeeBillingReport extends ReportBase {

    public EmployeeBillingReport() {
        super("prEmployeerpt", "Employee Billing Report", true);
    }

    public String build(int startDate, int endDate) throws DocumentException {
        HibernateScrollUtil sumAndProjects;
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        List<Employee> employees = hsu.getAll(Employee.class);
        ArrayList<ReportDetail> reportDetails = new ArrayList<>();
        String reportPeriod = (DateUtils.dateFormat("MM/dd/yyyy", startDate) + " to " + DateUtils.dateFormat("MM/dd/yyyy", endDate));
        for (Employee employee : employees) {
            sumAndProjects = BTimesheet.getTimesheetsForEmployee(hsu, employee.getPersonId(), startDate, endDate);

            double billableHours = 0;
            double overEstimateHours = 0;
            double nonBillableHours = 0;
            double totalHours = 0;
            double percentageBillableHours = 0;
            double percentageOverEstimatedHours = 0;
            double percentageNonBillabedHours = 0;
            double revenue = 0;

            while (sumAndProjects.next()) {
                final Object[] eles = sumAndProjects.getObjects();
                final Double hours = (Double) eles[0];
                final Project project = (Project) eles[1];
                final Character billable = (Character) eles[2];

                BProject bproject = new BProject(project);
                //only do reports for companies being billed not Arahant or the current company
                if (!bproject.getRequestingCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId())) {
                    if (billable == 'Y') {
                        billableHours += hours;
                        revenue += hours * bproject.getDefaultBillingRate(null);
                        overEstimateHours += hours - bproject.getEstimateHours();
                    } else
                        nonBillableHours += hours;
                }
                totalHours += hours;
            }

            //calculate percentages after toatls
            if (billableHours > 0.0 && nonBillableHours > 0.0)
                percentageBillableHours = (billableHours / totalHours) * 100;
            if (overEstimateHours > 0.0)
                percentageOverEstimatedHours = (overEstimateHours / (billableHours + overEstimateHours)) * 100;
            if (totalHours > 0.0 && nonBillableHours > 0.0)
                percentageNonBillabedHours = (nonBillableHours / totalHours) * 100;

            ReportDetail reportDetail = new ReportDetail();
            reportDetail.setEmployee(employee.getFname() + " " + employee.getLname());
            reportDetail.setBillableHours(billableHours);
            reportDetail.setNonBillableHours(nonBillableHours);
            reportDetail.setOverEstimateHours(overEstimateHours);
            reportDetail.setPercentageBillableHours(percentageBillableHours);
            reportDetail.setPercentageNonBillabedHours(percentageNonBillabedHours);
            reportDetail.setPercentageOverEstimatedHours(percentageOverEstimatedHours);
            reportDetail.setTotalHours(totalHours);
            reportDetail.setRevenue(revenue);
            reportDetails.add(reportDetail);
        }
        printReport(reportDetails, reportPeriod);
        return getFilename();
    }

    private void printReport(List<ReportDetail> reportDetails, String reportPeriod) {
        Collections.sort(reportDetails);
        Collections.reverse(reportDetails);
        try {
            PdfPTable table;
            writeHeaderLine("Report Period:", reportPeriod);
            addHeaderLine();
            addColumnHeader("Employee", Element.ALIGN_LEFT, 15);
            addColumnHeader("Billable", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Over\nEstimated", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Non\nBillalbe", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Total\nHours", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Billable %", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Over\nEstimated %", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Non\nBillable %", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Revenue", Element.ALIGN_RIGHT, 16);
            table = makeTable(getColHeaderWidths());
            writeColHeaders(table);

            //print employee report
            boolean alternateRow = false;
            for (ReportDetail reportDetail : reportDetails) {
                alternateRow = !alternateRow;
                write(table, reportDetail.getEmployee(), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getOverEstimateHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getNonBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getTotalHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getPercentageBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getPercentageOverEstimatedHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getPercentageNonBillabedHours(), 0), alternateRow);
                writeRight(table, MoneyUtils.formatMoney(reportDetail.getRevenue()), alternateRow);
            }
            addTable(table);
        } catch (DocumentException ex) {
            Logger.getLogger(EmployeeBillingReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }

    private class ReportDetail implements Comparable<ReportDetail> {
        private String employee;
        private double billableHours;
        private double overEstimateHours;
        private double nonBillableHours;
        private double totalHours;
        private double percentageBillableHours;
        private double percentageOverEstimatedHours;
        private double percentageNonBillabedHours;
        private double revenue;

        public double getBillableHours() {
            return billableHours;
        }

        public void setBillableHours(double billableHours) {
            this.billableHours = billableHours;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public double getNonBillableHours() {
            return nonBillableHours;
        }

        public void setNonBillableHours(double nonBillableHours) {
            this.nonBillableHours = nonBillableHours;
        }

        public double getOverEstimateHours() {
            return overEstimateHours;
        }

        public void setOverEstimateHours(double overEstimateHours) {
            this.overEstimateHours = overEstimateHours;
        }

        public double getPercentageBillableHours() {
            return percentageBillableHours;
        }

        public void setPercentageBillableHours(double percentageBillableHours) {
            this.percentageBillableHours = percentageBillableHours;
        }

        public double getPercentageNonBillabedHours() {
            return percentageNonBillabedHours;
        }

        public void setPercentageNonBillabedHours(double percentageNonBillabedHours) {
            this.percentageNonBillabedHours = percentageNonBillabedHours;
        }

        public double getPercentageOverEstimatedHours() {
            return percentageOverEstimatedHours;
        }

        public void setPercentageOverEstimatedHours(double percentageOverEstimatedHours) {
            this.percentageOverEstimatedHours = percentageOverEstimatedHours;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }

        public double getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(double totalHours) {
            this.totalHours = totalHours;
        }

        @Override
        public int compareTo(ReportDetail reportField) {
            int ret = Double.compare(revenue, reportField.revenue);
            return ret;
        }
    }

    public static void main(String[] args) {
        try {
            //build(String clientCompanyId, String employeeId, String orgGroupId, String projectId, boolean isApproved, boolean isNotApproved, boolean isInvoiced)
            ArahantSession.getHSU().setCurrentPerson(new BPerson("00001-0000000001").getPerson());
            String fname = new EmployeeBillingReport().build(20170101, 20170630);
            System.out.println("Output file: " + fname);
            //new EmployeeBillingReport().test(new BPerson("00001-0000007298").getPerson());
        } catch (DocumentException ex) {
            Logger.getLogger(EmployeeBillingReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        //new EmployeeBillingReport().test(new BPerson("00001-0000007298").getPerson());
    }
}
