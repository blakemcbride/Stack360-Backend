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

import com.arahant.beans.Holiday;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TotalCompensation extends ReportBase {

    public TotalCompensation() throws ArahantException {
        super("TtlCmpStmt", "", false, 225F, 30F);
        beneCatList = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.DESCRIPTION).list();
    }
    final private List<HrBenefitCategory> beneCatList;
    private double employerCost;

    public String build(int asOfDate) throws DocumentException, ArahantException {
        try {
            //ArahantSession.getHSU().setCurrentPerson(new BPerson("00001-0000387579").getPerson());
            BPerson bp = new BPerson(hsu.getCurrentPerson().getPersonId());
            ArahantSession.setCalcDate(asOfDate);

            bp.deleteExpiredBenefits();
            writeHeader(bp);
            writeEmployer(bp, asOfDate);
            writeEmployee(bp, asOfDate);
        } finally {
            close();
        }
        return getFilename();
    }

    final private double getAmountCovered(BHRBenefitJoin ebj) throws ArahantException {
        if (ebj.getAmountCovered() != 0)
            return ebj.getAmountCovered();

        // check if this is a policy benefit join
        if (ebj.isPolicyBenefitJoin())
            for (HrBenefitJoin dbj : ebj.getDependentBenefitJoins())
                if (dbj.getAmountCovered() != 0)
                    return dbj.getAmountCovered();

        return 0;
    }

    final static protected boolean dateInRange(int start, int end, int date) {
        return (start > 0 && start <= date) && (end == 0 || date <= end);
    }

    final protected void writeEmployer(BPerson emp, int date) throws DocumentException, ArahantException {

        double totalCost = 0;

        String alreadyPrinted = new String();
        HashMap<String, List<BenefitInfo>> categoryToBenefitInfo = new HashMap<String, List<BenefitInfo>>();
        List<BenefitInfo> uncategorized = new ArrayList<BenefitInfo>();

        BHRBenefitJoin[] ebjs = new BHRBenefitJoin[0];

        ebjs = new BEmployee(emp).getNonTimeRelatedBenefitJoins(date);

        for (BHRBenefitJoin ebj : ebjs) {
            if (alreadyPrinted.contains(ebj.getBenefitConfigName()))
                continue;
            else
                alreadyPrinted += ebj.getBenefitConfigName() + " ";
            if (ebj.getBenefitConfig() == null)
                continue;

            if (dateInRange(ebj.getCoverageStartDate(), ebj.getCoverageEndDate(), date))
                if (!isEmpty(ebj.getBenefitCategoryId())) {
                    List<BenefitInfo> beneInfo = categoryToBenefitInfo.get(ebj.getBenefitCategoryId());
                    if (beneInfo == null) {
                        beneInfo = new LinkedList<BenefitInfo>();
                        categoryToBenefitInfo.put(ebj.getBenefitCategoryId(), beneInfo);
                    }
                    beneInfo.add(new BenefitInfo(ebj.getBenefitName()/*.getBenefitConfig().getName()*/, MoneyUtils.formatMoney(ebj.getBenefitConfig().getEmployerCost()), getAmountCovered(ebj)));
                    totalCost += ebj.getBenefitConfig().getEmployerCost();

                } else {
                    uncategorized.add(new BenefitInfo(ebj.getBenefitName()/*.getBenefitConfig().getName()*/, MoneyUtils.formatMoney(ebj.getBenefitConfig().getEmployerCost()), getAmountCovered(ebj)));
                    totalCost += ebj.getBenefitConfig().getEmployerCost();
                }
        }

        BEmployee bemp = new BEmployee(emp);

        double salary = bemp.getAnnualRate();

        PdfPTable table = makeTable(new int[]{75, 25});


        List<OutputData> odList = new ArrayList<OutputData>(20);

        for (HrBenefitCategory beneCat : beneCatList) {
            List<BenefitInfo> beneList = categoryToBenefitInfo.get(beneCat.getBenefitCatId());
            if (beneList != null)
                for (BenefitInfo bene : beneList)
                    if (bene == null) {
                        //if (beneCat.getBenefitType()!=BHRBenefitCategory.VISION)
                        //	odList.add(new OutputData(beneCat.getDescription(),"No",""));
                    } else
                        //write out bene name and stuff
                        if (bene.coveredAmount == 0)
                            odList.add(new OutputData(bene.benefitName, bene.amount));
                        else
                            odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount));
        }

        //now list all the benefits the employee has that don't have a category
        for (BenefitInfo bene : uncategorized)
//			write out bene name and stuff
            if (bene.coveredAmount == 0)
                odList.add(new OutputData(bene.benefitName, bene.amount));
            else
                odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount));

        writeColHeaderBold(table, "Benefit", Element.ALIGN_LEFT, 11F);
        writeColHeaderBold(table, "Employer Cost", Element.ALIGN_RIGHT, 11F);
        table.setHeaderRows(1);

        Collections.sort(odList);

        for (OutputData od : odList) {
            writeLeft(table, od.name, false);
            writeRight(table, od.hasIt, false);
        }

        if ((uncategorized.size() + beneCatList.size()) % 2 == 1) {
            writeLeft(table, "", false);
            writeLeft(table, "", false);
        }

        write(table, "Social Security ", false);
        write(table, MoneyUtils.formatMoney(0.062 * Math.min(salary, 106800)), false);
        totalCost += 0.062 * Math.min(salary, 106800);

        write(table, "Medicare ", false);
        write(table, MoneyUtils.formatMoney(salary * 0.0145), false);
        totalCost += salary * 0.0145;

        write(table, "Federal Unemployment ", false);
        write(table, MoneyUtils.formatMoney(0.062 * Math.min(salary, 7000)), false);
        totalCost += 0.062 * Math.min(salary, 7000);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, 1);

        Calendar e = Calendar.getInstance();
        e.set(Calendar.MONTH, Calendar.DECEMBER);
        e.set(Calendar.DAY_OF_MONTH, 31);

        List<HrBenefit> l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, emp.getPerson()).list();

        HashMap<HrBenefit, Double> res = new HashMap<HrBenefit, Double>();
        for (HrBenefit h : l) {
            BHRAccruedTimeOff[] bat = bemp.listTimeOff(h.getBenefitId(), DateUtils.getDate(c), DateUtils.getDate(e), 0);
            double total = 0;
            for (BHRAccruedTimeOff b : bat) {
                if (!isEmpty(b.getAccrualId()))
                    continue;

                if (b.getAccrualHours() < 0)
                    continue;

                total += b.getAccrualHours();
            }

            res.put(h, total);
        }

        for (HrBenefit b : res.keySet()) {
            double x = res.get(b);
            write(table, b.getName(), false);
            write(table, MoneyUtils.formatMoney(x * bemp.getHourlyRate()), false);
            totalCost += x * bemp.getHourlyRate();
        }

        List<Holiday> hl = hsu.createCriteria(Holiday.class).list();

        int holidays = hl.size();
        write(table, "Holidays (" + holidays + " paid)", false);
        write(table, MoneyUtils.formatMoney((holidays * 8) * bemp.getHourlyRate()), false);
        totalCost += (holidays * 8) * bemp.getHourlyRate();

        write(table, "Total Employer Cost", false);
        write(table, MoneyUtils.formatMoney(totalCost), false);

        addTable(table);

        employerCost = totalCost;

    }

    final protected void writeEmployee(BPerson emp, int date) throws DocumentException, ArahantException {

        double totalCost = 0;

        String alreadyPrinted = new String();
        HashMap<String, List<BenefitInfo>> categoryToBenefitInfo = new HashMap<String, List<BenefitInfo>>();
        List<BenefitInfo> uncategorized = new ArrayList<BenefitInfo>();

        BHRBenefitJoin[] ebjs = new BHRBenefitJoin[0];

        ebjs = new BEmployee(emp).getNonTimeRelatedBenefitJoins(date);

        for (BHRBenefitJoin ebj : ebjs) {
            if (alreadyPrinted.contains(ebj.getBenefitConfigName()))
                continue;
            else
                alreadyPrinted += ebj.getBenefitConfigName() + " ";
            if (ebj.getBenefitConfig() == null)
                continue;

            if (dateInRange(ebj.getCoverageStartDate(), ebj.getCoverageEndDate(), date))
                if (!isEmpty(ebj.getBenefitCategoryId())) {
                    List<BenefitInfo> beneInfo = categoryToBenefitInfo.get(ebj.getBenefitCategoryId());
                    if (beneInfo == null) {
                        beneInfo = new LinkedList<BenefitInfo>();
                        categoryToBenefitInfo.put(ebj.getBenefitCategoryId(), beneInfo);
                    }
                    beneInfo.add(new BenefitInfo(ebj.getBenefitName()/*.getBenefitConfig().getName()*/, MoneyUtils.formatMoney(ebj.getBenefitConfig().getEmployeeCost()), getAmountCovered(ebj)));
                    totalCost += ebj.getBenefitConfig().getEmployeeCost();

                } else {
                    uncategorized.add(new BenefitInfo(ebj.getBenefitName()/*.getBenefitConfig().getName()*/, MoneyUtils.formatMoney(ebj.getBenefitConfig().getEmployeeCost()), getAmountCovered(ebj)));
                    totalCost += ebj.getBenefitConfig().getEmployeeCost();
                }
        }

        BEmployee bemp = new BEmployee(emp);

        double salary = bemp.getAnnualRate();

        PdfPTable table = makeTable(new int[]{75, 25});

        List<OutputData> odList = new ArrayList<OutputData>(20);

        for (HrBenefitCategory beneCat : beneCatList) {
            List<BenefitInfo> beneList = categoryToBenefitInfo.get(beneCat.getBenefitCatId());
            if (beneList != null)
                for (BenefitInfo bene : beneList)
                    if (bene == null) {
                        //if (beneCat.getBenefitType()!=BHRBenefitCategory.VISION)
                        //	odList.add(new OutputData(beneCat.getDescription(),"No",""));
                    } else
                        //write out bene name and stuff
                        if (bene.coveredAmount == 0)
                            odList.add(new OutputData(bene.benefitName, bene.amount));
                        else
                            odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount));
        }

        //now list all the benefits the employee has that don't have a category
        for (BenefitInfo bene : uncategorized)
//			write out bene name and stuff
            if (bene.coveredAmount == 0)
                odList.add(new OutputData(bene.benefitName, bene.amount));
            else
                odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount));

        writeColHeaderBold(table, "Benefit", Element.ALIGN_LEFT, 11F);
        writeColHeaderBold(table, "Employee Cost", Element.ALIGN_RIGHT, 11F);
        table.setHeaderRows(1);

        Collections.sort(odList);

        for (OutputData od : odList) {
            writeLeft(table, od.name, false);
            writeRight(table, od.hasIt, false);
        }

        if ((uncategorized.size() + beneCatList.size()) % 2 == 1) {
            writeLeft(table, "", false);
            writeLeft(table, "", false);
        }

        write(table, "Social Security ", false);
        write(table, MoneyUtils.formatMoney(0.062 * Math.min(salary, 106800)), false);
        totalCost += 0.062 * Math.min(salary, 106800);

        write(table, "Medicare ", false);
        write(table, MoneyUtils.formatMoney(salary * 0.0145), false);
        totalCost += salary * 0.0145;

        write(table, "Federal Unemployment ", false);
        write(table, MoneyUtils.formatMoney(0), false);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, 1);

        Calendar e = Calendar.getInstance();
        e.set(Calendar.MONTH, Calendar.DECEMBER);
        e.set(Calendar.DAY_OF_MONTH, 31);

        List<HrBenefit> l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, emp.getPerson()).list();

        HashMap<HrBenefit, Double> res = new HashMap<HrBenefit, Double>();
        for (HrBenefit h : l) {
            BHRAccruedTimeOff[] bat = bemp.listTimeOff(h.getBenefitId(), DateUtils.getDate(c), DateUtils.getDate(e), 0);
            double total = 0;
            for (BHRAccruedTimeOff b : bat) {
                if (!isEmpty(b.getAccrualId()))
                    continue;

                if (b.getAccrualHours() < 0)
                    continue;

                total += b.getAccrualHours();
            }

            res.put(h, total);
        }

        for (HrBenefit b : res.keySet()) {
            double x = res.get(b);
            write(table, b.getName(), false);
            write(table, MoneyUtils.formatMoney(x * bemp.getHourlyRate()), false);
            totalCost += x * bemp.getHourlyRate();
        }

        List<Holiday> hl = hsu.createCriteria(Holiday.class).list();

        int holidays = hl.size();
        write(table, "Holidays (" + holidays + ")", false);
        write(table, MoneyUtils.formatMoney(0), false);

        write(table, "Total Employee Cost", false);
        write(table, MoneyUtils.formatMoney(totalCost), false);

        addTable(table);

        table = makeTable(new int[]{45, 20, 30, 5});

        table = makeTable(new int[]{45, 20, 30, 5});

        writeBoldLeft(table, "TOTAL SALARY & FRINGE BENEFITS", 10);
        writeBoldLeft(table, MoneyUtils.formatMoney((salary + employerCost) - totalCost), 10);
        write(table, "", false);
        write(table, "", false);

        addTable(table);

    }

    final protected class BenefitInfo {

        /**
         * @param benefitName2
         * @param startDate2
         */
        public BenefitInfo(String benefitName, String amount, double coveredAmount) {
            this.benefitName = benefitName;
            this.amount = amount;
            this.coveredAmount = coveredAmount;
        }
        public String benefitName;
        public String amount;
        public double coveredAmount;
    }

    final private class OutputData implements Comparable<OutputData> {

        /**
         * @param description
         * @param string
         * @param string2
         */
        public OutputData(String description, String has) {
            name = description;
            hasIt = has;
        }
        private String name = "";
        private String hasIt = "";

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(OutputData o) {
            if (name == null)
                return 1;
            if (o == null)
                return -1;
            if (o.name == null)
                o.name = "";
            return name.compareTo(o.name);
        }
    }

    /**
     * @throws DocumentException
     * @throws ArahantException
     *
     */
    final protected void writeHeader(BPerson emp) throws DocumentException, ArahantException {

        PdfPTable table = makeTable(new int[]{100});
        writeBoldCentered(table, "Total Compensation Statement", 16F);

        addTable(table);

        table = makeTable(new int[]{10, 50, 50, 10});
        write(table, "", false);
        writeLeft(table, emp.getNameFML(), false);
        writeRight(table, DateUtils.getDateSpelledOut(DateUtils.now()), false);
        write(table, "", false);
        write(table, "", false);
        writeLeft(table, emp.getCompanyName(), false);
        writeRight(table, "", false);
        write(table, "", false);
        write(table, "", false);

        addTable(table);


        blankLine();

        table = makeTable(new int[]{100});

        writeLeft(table, "", false);

        addTable(table);

        BEmployee bemp = new BEmployee(emp);

        double salary = bemp.getAnnualRate();

        table = makeTable(new int[]{35, 20, 30, 15});

        write(table, "CURRENT ANNUAL SALARY " + DateUtils.year(DateUtils.now()), false);
        write(table, MoneyUtils.formatMoney(salary), false);
        write(table, "", false);
        write(table, "", false);

        addTable(table);
    }

    public static void main(String args[]) {
        try {
            ArahantSession.setHSU(new HibernateSessionUtil());
            TotalCompensation ebj = new TotalCompensation();
            ebj.build(20090101);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
