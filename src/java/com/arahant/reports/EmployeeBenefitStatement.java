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

import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.File;
import java.util.*;
import java.util.Collections;

/**
 *
 *
 * Created on Aug 12, 2007
 *
 */
public class EmployeeBenefitStatement extends ReportBase {

	/**
	 * @param reportFileNameStart
	 * @param title
	 * @throws ArahantException
	 */
	public EmployeeBenefitStatement() throws ArahantException {
		super("EmpBeneStmt", "", false, 225F, 30F);
		beneCatList = hsu.createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.DESCRIPTION).list();

		//	ArahantSession.getAI().watchAll();

		try {
			final File logof = BCompany.getReportLogo(null);

			logo = Image.getInstance(logof.getAbsolutePath());
			logo.scaleToFit(1000, 80);
			//logo.scaleToFit(10, 5);
			//logo.scalePercent(50);
		} catch (final Exception e) {
			logger.error(e);
		}

	}
	
	private List<HrBenefitCategory> beneCatList;
	private Image logo;

	private String headerText = "We have created this Benefit Statement so that you may have the opportunity to review the benefits "
			+ "that we currently have on record for you.  If you believe there is an error with the information that is "
			+ "on this form, please call or email the benefits department.";

	public String build(BPerson emp, boolean includeCredentials, int date) throws DocumentException, ArahantException {
		try {
			//    	ArahantSession.getAI().watchAll();
			emp.deleteExpiredBenefits();
			writeEmployee(emp, includeCredentials, date);
		} finally {
			close();
		}
		return getFilename();
	}

	private double getAmountCovered(BHRBenefitJoin ebj) throws ArahantException {
		if (ebj.getAmountCovered() != 0)
			return ebj.getAmountCovered();

		// check if this is a policy benefit join
		if (ebj.isPolicyBenefitJoin())
			for (HrBenefitJoin dbj : ebj.getDependentBenefitJoins())
				if (dbj.getAmountCovered() != 0)
					return dbj.getAmountCovered();

		return 0;
	}

	static protected boolean dateInRange(int start, int end, int date) {
		return (start > 0 && start <= date) && (end == 0 || date <= end);
	}

	protected void writeEmployee(BPerson emp, boolean includeCredentials, int date) throws DocumentException, ArahantException {
		writeHeader(emp.getNameFML(), emp.getStreet(), emp.getStreet2(), emp.getCity(), emp.getState(), emp.getZip());

		HashMap<String, List<BenefitInfo>> categoryToBenefitInfo = new HashMap<String, List<BenefitInfo>>();
		List<BenefitInfo> uncategorized = new ArrayList<BenefitInfo>();

		BHRBenefitJoin[] ebjs;

		//	ArahantSession.getAI().watchAll();
		if (emp.isEmployee())
			ebjs = new BEmployee(emp).getApprovedBenefitJoins(date);
		else
			ebjs = BHRBenefitJoin.makeArray(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, emp.getPerson()).eq(HrBenefitJoin.APPROVED, 'Y').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).list());

		// ArahantSession.runAI();
		// ArahantSession.AIEval("(facts)");
		if (ebjs.length > 0) {
			createEmployeeBody(ebjs, date, categoryToBenefitInfo, uncategorized);
			blankLine();
			writeLine("Current Benefits", 11F, ReportBase.STYLE_BOLD | ReportBase.STYLE_UNDERLINE);
			blankLine();
			writeBody(categoryToBenefitInfo, uncategorized);
			if (emp.isEmployee())
				createDependentBody(emp, date, 'Y');
		}
		
		
		if (emp.isEmployee())
			ebjs = new BEmployee(emp).getUnapprovedBenefitJoins(date);
		else
			ebjs = BHRBenefitJoin.makeArray(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, emp.getPerson()).eq(HrBenefitJoin.APPROVED, 'N').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).list());
		if (ebjs.length != 0) {
			categoryToBenefitInfo.clear();
			uncategorized.clear();
			blankLine();
			writeLine("New Benefit Elections", 11F, ReportBase.STYLE_BOLD | ReportBase.STYLE_UNDERLINE);
			blankLine();
			createEmployeeBody(ebjs, date, categoryToBenefitInfo, uncategorized);
			writeBody(categoryToBenefitInfo, uncategorized);
			if (emp.isEmployee())
				createDependentBody(emp, date, 'N');
		}
		writeEnding(emp.getUserLogin(), emp.getUserPassword(), includeCredentials);
	}
	
	private void createEmployeeBody(BHRBenefitJoin[] ebjs, int date, HashMap<String, List<BenefitInfo>> categoryToBenefitInfo, List<BenefitInfo> uncategorized) {
		for (BHRBenefitJoin ebj : ebjs) {
			if (ebj.getBenefitConfig() == null)
				continue;

			if (ebj.getBenefitConfig().getBenefitCategoryType() == HrBenefitCategory.MISC)
				continue;
			/*
			 * HrBenefitJoin hbj=ebj.getBean();
			 *
			 * hbj.getPolicyStartDate(); hbj.getPolicyEndDate();
			 * hbj.getPayingPersonId(); hbj.getCalculatedCost();
			 * hbj.getAmountPaid(); hbj.linkToEngine();
			 *
			 * HrBenefitJoin
			 * hblj=hsu.get(HrBenefitJoin.class,ebj.getBenefitJoinId()); ebj=new
			 * BHRBenefitJoin(hblj);
			 */

			boolean depHasIt = false;
			int depDate = 0;

			// check if this is a policy benefit join
			if (ebj.isPolicyBenefitJoin())
				// it is, have to check all the dependent benefit joins too
				for (HrBenefitJoin dbj : ebj.getDependentBenefitJoins())
					if (dateInRange(dbj.getCoverageStartDate(), dbj.getCoverageEndDate(), date)) {
						depHasIt = true;
						depDate = dbj.getCoverageStartDate();
						break;
					}

			if (dateInRange(ebj.getCoverageStartDate(), ebj.getCoverageEndDate(), date)) {

				String provider = "";

				if (!ebj.getCoveredPersonId().equals(ebj.getPayingPersonId()))
					provider = ebj.getPayingPerson().getPerson().getNameLFM();

				if (!isEmpty(ebj.getBenefitCategoryId())) {
					List<BenefitInfo> beneInfo = categoryToBenefitInfo.get(ebj.getBenefitCategoryId());
					if (beneInfo == null) {
						beneInfo = new LinkedList<BenefitInfo>();
						categoryToBenefitInfo.put(ebj.getBenefitCategoryId(), beneInfo);
					}
					beneInfo.add(new BenefitInfo(ebj.getBenefitName() + " : " + ebj.getBenefitConfig().getName(), ebj.getCoverageStartDate(), ebj.getCalculatedCost(), provider, getAmountCovered(ebj)));
				} else
					uncategorized.add(new BenefitInfo(ebj.getBenefitName() + " : " + ebj.getBenefitConfig().getName(), ebj.getCoverageStartDate(), ebj.getCalculatedCost(), provider, getAmountCovered(ebj)));
			} else if (depHasIt)
				if (!isEmpty(ebj.getBenefitCategoryId())) {
					List<BenefitInfo> beneInfo = categoryToBenefitInfo.get(ebj.getBenefitCategoryId());
					if (beneInfo == null) {
						beneInfo = new LinkedList<BenefitInfo>();
						categoryToBenefitInfo.put(ebj.getBenefitCategoryId(), beneInfo);
					}
					beneInfo.add(new BenefitInfo(ebj.getBenefitName() + " : " + ebj.getBenefitConfig().getName(), depDate, ebj.getCalculatedCost(), "", getAmountCovered(ebj)));
				} else
					uncategorized.add(new BenefitInfo(ebj.getBenefitName() + " : " + ebj.getBenefitConfig().getName(), depDate, "", "", getAmountCovered(ebj)));
		}
		
	}
	
	private void createDependentBody(BPerson emp, int date, char approved) {
		BEmployee bemp = new BEmployee(emp);

		//for my dependents, do they have medical via a dep relationship
		List<DepInfo> depList = new ArrayList<DepInfo>();

		for (BHREmplDependent dep : bemp.getDependents()) {
			if (!dep.isCurrentlyActive())
				continue;

			DepInfo depInfo = new DepInfo();
			depInfo.bday = dep.getPerson().getDob();
			depInfo.name = dep.getPerson().getNameFM();
			depInfo.lname = dep.getPerson().getLname();
			depList.add(depInfo);

			HibernateCriteriaUtil<HrBenefitJoin> dphcu = hsu.createCriteria(HrBenefitJoin.class);
			dphcu.eq(HrBenefitJoin.APPROVED, approved);
			dphcu.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, date);
			dphcu.joinTo(HrBenefitJoin.RELATIONSHIP).joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, dep.getPersonId());
			dphcu.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date);

			HibernateScrollUtil<HrBenefitJoin> dpScroll = dphcu.scroll();
			while (dpScroll.next()) {
				HrBenefitJoin dbj = dpScroll.get();
				if (dbj.getHrBenefitConfig() == null)
					continue;
				if (dbj.getHrBenefitConfig().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.HEALTH) {
					depInfo.medDate = dbj.getCoverageStartDate();
					if (!dbj.getPayingPerson().getPersonId().equals(emp.getPersonId()))
						depInfo.medProvided = dbj.getProviderInitials();
				}
				if (dbj.getHrBenefitConfig().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.DENTAL) {
					depInfo.dentDate = dbj.getCoverageStartDate();
					if (!dbj.getPayingPerson().getPersonId().equals(emp.getPersonId()))
						depInfo.dentProvided = dbj.getProviderInitials();
				}
				if (dbj.getHrBenefitConfig().getHrBenefitCategory().getBenefitType() == HrBenefitCategory.VISION) {
					depInfo.visDate = dbj.getCoverageStartDate();
					if (!dbj.getPayingPerson().getPersonId().equals(emp.getPersonId()))
						depInfo.visProvided = dbj.getProviderInitials();
				}
			}

			dpScroll.close();
		}
		if (!depList.isEmpty())
			try {
				writeDependentInfo(depList);
			} catch (Exception e) {

			}
	}

	private class BenefitInfo {

		/**
		 * @param benefitName2
		 * @param startDate2
		 */
		public BenefitInfo(String benefitName, int startDate, String amount, String providedBy, double coveredAmount) {
			this.startDate = startDate;
			this.benefitName = benefitName;
			this.amount = amount;
			this.providedBy = providedBy;
			this.coveredAmount = coveredAmount;
		}
		public int startDate;
		public String benefitName;
		public String amount;
		public String providedBy;
		public double coveredAmount;
	}

	private class DepInfo {

		String name = "";
		String lname = "";
		int medDate = 0;
		int dentDate = 0;
		int visDate = 0;
		int careDate = 0;
		int bday = 0;
		public String medProvided = "";
		public String dentProvided = "";
		public String visProvided = "";
		public String careProvided = "";

		@Override
		public String toString() {
			return name + " " + bday + " " + medDate + " " + dentDate + " " + visDate + " " + careDate;
		}
	}

	final private class OutputData implements Comparable<OutputData> {

		/**
		 * @param description
		 * @param string
		 * @param string2
		 */
		public OutputData(String description, String has, String startDate, String providedBy) {
			name = description;
			hasIt = has;
			date = startDate;
			this.providedBy = providedBy;
		}
		private String name = "", hasIt = "", date = "", providedBy = "";

		/*
		 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(OutputData o) {
			if (name == null)
				name = "";
			if (o == null)
				return -1;
			if (o.name == null)
				o.name = "";

			return name.compareTo(o.name);
		}
	}

	/**
	 * @throws DocumentException
	 *
	 */
	private void writeBody(HashMap<String, List<BenefitInfo>> categoryToBenefitInfo, List<BenefitInfo> uncategorized) throws DocumentException {

		PdfPTable table = makeTable(new int[]{49, 12, 14, 25});

		List<OutputData> odList = new ArrayList<OutputData>(20);

		for (HrBenefitCategory beneCat : beneCatList) {
			List<BenefitInfo> beneList = categoryToBenefitInfo.get(beneCat.getBenefitCatId());
			if (beneList != null)
				for (BenefitInfo bene : beneList)
					if (bene == null) {
						//if (beneCat.getBenefitType()!=BHRBenefitCategory.VISION)
						//	odList.add(new OutputData(beneCat.getDescription(),"No",""));
					} else //write out bene name and stuff
					if (bene.coveredAmount == 0)
						odList.add(new OutputData(bene.benefitName, bene.amount, DateUtils.getDateFormatted(bene.startDate), bene.providedBy));
					else
						odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount, DateUtils.getDateFormatted(bene.startDate), bene.providedBy));
		}


		//now list all the benefits the employee has that don't have a category
		for (BenefitInfo bene : uncategorized)
//			write out bene name and stuff
			if (bene.coveredAmount == 0)
				odList.add(new OutputData(bene.benefitName, bene.amount, DateUtils.getDateFormatted(bene.startDate), bene.providedBy));
			else
				odList.add(new OutputData(bene.benefitName + " " + MoneyUtils.formatMoney(bene.coveredAmount), bene.amount, DateUtils.getDateFormatted(bene.startDate), bene.providedBy));

		writeColHeaderBold(table, "Benefit : Coverage", 11F);
		writeColHeaderBold(table, "Cost PPP", Element.ALIGN_RIGHT, 11F);
		writeColHeaderBold(table, "Effective", 11F); // normally right align, but since all are dates of same width, use centered for spacing
		writeColHeaderBold(table, "Policy Owner", 11F);
		table.setHeaderRows(1);

		Collections.sort(odList);

		for (OutputData od : odList) {
			writeLeft(table, od.name, false);
			writeRight(table, od.hasIt, false);
			writeCenteredNoBorder(table, od.date, false, 1);
			writeLeft(table, od.providedBy, false);
		}

		if ((uncategorized.size() + beneCatList.size()) % 2 == 1) {
			writeLeft(table, "", false);
			writeLeft(table, "", false);
			writeLeft(table, "", false);
			writeLeft(table, "", false);
		}

		addTable(table);

		blankLine();
	}

	/*
	 * final protected PdfPTable writeDependentHeader() throws DocumentException
	 * { PdfPTable table=makeTable(new int[]{100}); writeBoldCentered(table,
	 * "Dependent Benefits and Effective Dates",12F); addTable(table);
	 * table=makeTable(new int[]{24,14,3,14,3,14,3,14,3,14,3});
	 * writeCenteredNoBorder(table, ""); writeCenteredNoBorder(table, "Date of
	 * Birth"); writeCenteredNoBorder(table, "Medical");
	 * writeCenteredNoBorder(table, ""); writeCenteredNoBorder(table, "Dental");
	 * writeCenteredNoBorder(table, ""); writeCenteredNoBorder(table, "Vision");
	 * writeCenteredNoBorder(table, ""); writeCenteredNoBorder(table, "Long Term
	 * Care"); writeCenteredNoBorder(table, ""); table.setHeaderRows(1); return
	 * table; }
	 */
	private void writeDependentInfo(List<DepInfo> depList) throws DocumentException {

		boolean showVision = false;

		for (DepInfo depInfo : depList)
			if (depInfo.visDate != 0)
				showVision = true;
		PdfPTable table = makeTable(new int[]{100});

		writeColHeaderBold(table, "Dependent Benefits and Effective Dates", 11F);

		addTable(table);

		int[] cols = new int[]{21, 14, 14, 5, 14, 5, 14, 5, 14, 5};

		table = makeTable(cols);


		writeCenteredNoBorder(table, "");
		writeCenteredNoBorder(table, "Date of Birth");
		writeCenteredNoBorder(table, "Medical");
		writeCenteredNoBorder(table, "");
		writeCenteredNoBorder(table, "Dental");
		writeCenteredNoBorder(table, "");

		if (showVision) {
			writeCenteredNoBorder(table, "Vision");
			writeCenteredNoBorder(table, "");
		} else {
			writeCenteredNoBorder(table, "");
			writeCenteredNoBorder(table, "");
		}
		/*
		 * if (showLTC) { writeCenteredNoBorder(table, "Long Term Care");
		 * writeCenteredNoBorder(table, ""); } else
		 */ {
			writeCenteredNoBorder(table, "");
			writeCenteredNoBorder(table, "");
		}

		table.setHeaderRows(1);


		for (DepInfo depInfo : depList) {
			/*
			 * if (depInfo.careDate == 0 && depInfo.dentDate == 0 &&
			 * depInfo.medDate == 0 && depInfo.visDate == 0) { continue; }
			 */
			//logger.info(depInfo);
			writeLeft(table, depInfo.lname, false);
			writeCenteredNoBorder(table, DateUtils.getDateFormatted(depInfo.bday));
			writeCenteredNoBorder(table, depInfo.medDate == 0 ? "No" : DateUtils.getDateFormatted(depInfo.medDate));
			writeCenteredNoBorder(table, depInfo.medProvided);
			writeCenteredNoBorder(table, depInfo.dentDate == 0 ? "No" : DateUtils.getDateFormatted(depInfo.dentDate));
			writeCenteredNoBorder(table, depInfo.dentProvided);
			if (showVision) {
				writeCenteredNoBorder(table, depInfo.visDate == 0 ? "No" : DateUtils.getDateFormatted(depInfo.visDate));
				writeCenteredNoBorder(table, depInfo.visProvided);
			} else {
				writeCenteredNoBorder(table, "");
				writeCenteredNoBorder(table, "");
			}
			//        if (showLTC) {
			//          writeCenteredNoBorder(table, depInfo.careDate == 0 ? "No" : DateUtils.getDateFormatted(depInfo.careDate));
			//          writeCenteredNoBorder(table, depInfo.careProvided);
			//     } else {
			{
				writeCenteredNoBorder(table, "");
				writeCenteredNoBorder(table, "");
			}
			writeLeft(table, depInfo.name, false);
			for (int loop = 1; loop < cols.length; loop++)
				writeCenteredNoBorder(table, "");
		}
		endDependentInfo(table);
	}

	private void endDependentInfo(PdfPTable table) throws DocumentException {
		addTable(table);
	}

	/**
	 * @throws DocumentException
	 * @throws ArahantException
	 *
	 */
	private void writeHeader(String displayName, String street, String street2, String city, String state, String zip) throws DocumentException, ArahantException {

		PdfPTable table = makeTable(new int[]{30, 70});
		//write(table,"",false);
		writeImage(table, logo, true);
		write(table, "", false);
		addTable(table);
		table = makeTable(new int[]{100});
		//  writeImage(table, wmco, true);
//		writeBoldCentered(table, BProperty.get("Announcement"), 16F);
		writeBoldCentered(table, hsu.getCurrentCompany().getName(), 16F);
		writeBoldCentered(table, "Employee Benefit Statement", 16F);
		//  writeImage(table, wmschool, true);

		addTable(table);

		table = makeTable(new int[]{10, 50, 50, 10});
		write(table, "", false);
		writeLeft(table, displayName, false);
		writeRight(table, DateUtils.getDateSpelledOut(DateUtils.now()), false);
		write(table, "", false);
		write(table, "", false);
		writeLeft(table, street, false);
		writeRight(table, "", false);
		write(table, "", false);
		if (!isEmpty(street2)) {
			write(table, "", false);
			writeLeft(table, street2, false);
			writeRight(table, "", false);
			write(table, "", false);
		}
		write(table, "", false);
		String cityLine = isEmpty(city) ? "" : (city + ", ");
		cityLine += state + "  " + zip;
		writeLeft(table, cityLine, false);
		writeRight(table, "", false);
		write(table, "", false);

		addTable(table);

		blankLine();

		table = makeTable(new int[]{100});

		writeHeaderText(table);

		writeLeft(table, "", false);

		//	writeBoldCentered(table, "Employee Benefits and Effective Dates",12F);

		addTable(table);
	}

	protected void writeHeaderText(PdfPTable table) {
		writeLeft(table, headerText, false);
	}

	private void writeEnding(String userLogin, String userPassword, boolean includeCredentials) throws DocumentException {
		String message;

		try {
			message = "Your login is : " + userLogin + ".  Your password is " + Crypto.decryptTripleDES(userPassword) + ".";
		} catch (Exception e) {
			// RKK - decrypt throws exception now, so to keep same behavior for non-encrypted passwords, do this
			message = "Your login is : " + userLogin + ".  Your password is " + userPassword + ".";
		}

		blankLine();
		if (includeCredentials) {
			PdfPTable table = makeTable(new int[]{100});

			write(table, message);
			write(table, "By logging on to you company web site with this login and password, you may review and change your benefits.");
			addTable(table);
		}
	}

	protected void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public static void main(String args[]) {
		try {
			ArahantSession.setHSU(new HibernateSessionUtil());
			EmployeeBenefitStatement ebj = new EmployeeBenefitStatement();
			ebj.build(new BEmployee("00001-0000387508"), true, DateUtils.now());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
