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

import com.arahant.beans.*;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.StringType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Blake McBride
 * Date: 6/24/17
 */
public class EmployeePerformanceReport extends ReportBase {

    public EmployeePerformanceReport() {
        super("prEmployeePerrpt", "Employee Performance Report", true);
    }

    public String build(int startDate, int endDate) throws DocumentException {
        HibernateScrollUtil<?> sumAndProjects;
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        List<Employee> employees = hsu.getAll(Employee.class);
        List<EmployeePerformanceReport.ReportDetail> reportDetails = new ArrayList<>();
        String reportPeriod;
        int employeeNumber = 1;
        if (demoMode)
            reportPeriod = "8/01/20xx to 8/31/20xx";
        else
            reportPeriod = (DateUtils.dateFormat("MM/dd/yyyy", startDate) + " to " + DateUtils.dateFormat("MM/dd/yyyy", endDate));
        for (Employee employee : employees) {
            sumAndProjects = BTimesheet.getTimesheetsForEmployee(hsu, employee.getPersonId(), startDate, endDate);

            double billableHours = 0;
            double nonBillableHours = 0;
            double totalHours = 0;
            double percentageBillableHours = 0;
            double percentageNonBillabedHours = 0;

            while (sumAndProjects.next()) {
                final Object[] eles = sumAndProjects.getObjects();
                final Double hours = (Double) eles[0];
                final Project project = (Project) eles[1];
                final Character billable = (Character) eles[2];

                BProject bproject = new BProject(project);
                //only do reports for companies being billed not Arahant or the current company
                if (!bproject.getRequestingCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId())) {
                    if (billable == 'Y')
                        billableHours += hours;
                    else
                        nonBillableHours += hours;
                }
                totalHours += hours;
            }

            //calculate percentages after totals
            if (billableHours > 0.0 && nonBillableHours > 0.0)
                percentageBillableHours = (billableHours / totalHours) * 100;
            if (totalHours > 0.0 && nonBillableHours > 0.0)
                percentageNonBillabedHours = (nonBillableHours / totalHours) * 100;

            EmployeePerformanceReport.ReportDetail reportDetail = new ReportDetail();
            if (demoMode)
                reportDetail.setEmployee("Employee " + employeeNumber++);
            else
                reportDetail.setEmployee(employee.getFname() + " " + employee.getLname());
            reportDetail.setBillableHours(billableHours);
            reportDetail.setNonBillableHours(nonBillableHours);
            reportDetail.setPercentageBillableHours(percentageBillableHours);
            reportDetail.setPercentageNonBillabedHours(percentageNonBillabedHours);
            reportDetail.setTotalHours(totalHours);
            reportDetail.setRevenue(getTotalRevenue(employee.getPersonId(), startDate, endDate));
            reportDetail.setWages(getTotalSalary(hsu, employee, startDate, endDate));
            reportDetails.add(reportDetail);
        }
        printReport(reportDetails, reportPeriod);
        return getFilename();
    }

    private void printReport(List<EmployeePerformanceReport.ReportDetail> reportDetails, String reportPeriod) {
        Collections.sort(reportDetails);
        Collections.reverse(reportDetails);
        try {
            PdfPTable table;
            writeHeaderLine("Report Period:", reportPeriod);
            addHeaderLine();
            addColumnHeader("Employee", Element.ALIGN_LEFT, 15);
            addColumnHeader("Billable\nHours", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Non\nBillalbe\nHours", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Total\nHours", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Billable\nPercent", Element.ALIGN_RIGHT, 6);
            addColumnHeader("Non\nBillable\nPercent", Element.ALIGN_RIGHT, 6);
            addColumnHeader("Revenue\nGenerated", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Wages\nPaid", Element.ALIGN_RIGHT, 8);
            addColumnHeader("Net\nIncome", Element.ALIGN_RIGHT, 8);
            table = makeTable(getColHeaderWidths());
            writeColHeaders(table);

            //print employee report
            boolean alternateRow = false;
            for (EmployeePerformanceReport.ReportDetail reportDetail : reportDetails) {
                alternateRow = !alternateRow;
                write(table, reportDetail.getEmployee(), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getNonBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getTotalHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getPercentageBillableHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getPercentageNonBillabedHours(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getRevenue(), 0), alternateRow);
                writeRight(table, Formatting.formatNumberWithCommas(reportDetail.getWages(), 0), alternateRow);
                writeRight(table, Utils.Format(reportDetail.getRevenue()-reportDetail.getWages(), "CP", 11, 0), alternateRow);
            }
            addTable(table);
        } catch (DocumentException ex) {
            Logger.getLogger(EmployeePerformanceReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }

    private static class ReportDetail implements Comparable<EmployeePerformanceReport.ReportDetail> {
        private String employee;
        private double billableHours;
        private double nonBillableHours;
        private double totalHours;
        private double percentageBillableHours;
        private double percentageNonBillabedHours;
        private double revenue;
        private double wages;
        private double netIncome;

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

        public double getWages() {
            return wages;
        }

        public void setWages(double wages) {
            this.wages = wages;
            this.netIncome = revenue - wages;
        }

        public double getNetIncome() {
            return netIncome;
        }

        @Override
        public int compareTo(EmployeePerformanceReport.ReportDetail reportField) {
            int ret = Double.compare(netIncome, reportField.netIncome);
            return ret;
        }
    }

    private double getTotalSalary(HibernateSessionUtil hsu, Employee emp, int beginning_date, int ending_date) {
        HrWage wage = hsu.createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, emp)
                .le(HrWage.EFFECTIVEDATE, DateUtils.today())
                .orderByDesc(HrWage.EFFECTIVEDATE).first();
        if (wage == null)
            return 0;
        switch (wage.getWageType().getPeriodType()) {
            case 2:  //  wages
                return (wage.getWageAmount() / 360) * DateUtils.getDaysBetween(ending_date, beginning_date);
            case 1:  // hourly
                List<?> recs = hsu.getSession().createSQLQuery("select sum(total_hours) from timesheet " +
                        "where person_id=:person_id and beginning_date>=:bdate and beginning_date<=:edate")
                        .addScalar("sum", DoubleType.INSTANCE)
                        .setParameter("person_id", emp.getPersonId())
                        .setParameter("bdate", beginning_date)
                        .setParameter("edate", ending_date)
                        .list();
                if (recs.size() > 0) {
                    Double total_hours = (Double) recs.get(0);
                    if (total_hours != null)
                        return total_hours * wage.getWageAmount();
                }
            default:
                return 0;
        }
    }

    private double getTotalRevenue(String person_id, int beginning_date, int ending_date) {
        double person_total = 0;
        List<?> recs = hsu.getSession().createSQLQuery("select distinct li.invoice_line_item_id, li.adj_hours, li.adj_rate\n" +
                "from timesheet t\n" +
                "join invoice_line_item li\n" +
                "  on li.invoice_line_item_id = t.invoice_line_item_id\n" +
                "where t.person_id = :person_id\n" +
                "and t.beginning_date >= :beginning_date\n" +
                "and t.beginning_date <= :ending_date\n" +
                "order by li.invoice_line_item_id")
                .addScalar("invoice_line_item_id", StringType.INSTANCE)
                .addScalar("adj_hours", FloatType.INSTANCE)
                .addScalar("adj_rate", DoubleType.INSTANCE)
                .setParameter("person_id", person_id)
                .setParameter("beginning_date", beginning_date)
                .setParameter("ending_date", ending_date)
                .list();
        for (Object orec : recs) {
            Object [] rec = (Object []) orec;
            String invoice_line_item_id = (String) rec[0];
            float adj_hours = (float) rec[1];
            double adj_rate = (double) rec[2];
            double line_total = adj_hours * adj_rate;
            double total_hours = 0;
            double person_hours = 0;
            List<?> recs2 = hsu.getSession().createSQLQuery("select t.person_id, t.total_hours from timesheet t " +
                    "join invoice_line_item li " +
                    "  on li.invoice_line_item_id = t.invoice_line_item_id " +
                    "where t.invoice_line_item_id = :invoice_line_item_id")
                    .setParameter("invoice_line_item_id", invoice_line_item_id, StringType.INSTANCE)
                    .list();
            for (Object orec2 : recs2) {
                Object [] rec2 = (Object[]) orec2;
                String r_person_id = (String) rec2[0];
                Double r_total_hours = (Double) rec2[1];
                total_hours += r_total_hours;
                if (r_person_id.equals(person_id))
                    person_hours += r_total_hours;
            }
            person_total += (person_hours / total_hours) * line_total;
        }
        return person_total;
    }

    public static void main(String[] args) {
        try {
            HibernateSessionUtil hsu = ArahantSession.getHSU();
            hsu.setCurrentPerson(new BPerson("00001-0000000001").getPerson());
            String fname = new EmployeePerformanceReport().build(20170101, 20170630);
            System.out.println("Output file: " + fname);
        } catch (DocumentException ex) {
            Logger.getLogger(EmployeePerformanceReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
