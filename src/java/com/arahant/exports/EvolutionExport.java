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


package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;
//import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

//TODO: change this to write to temporary files and then append on success that way partial files never get written

public class EvolutionExport {

	private static final ArahantLogger logger = new ArahantLogger(EvolutionExport.class);

	private void exportDeductionsEmp(Employee emp, DelimitedFileWriter fw) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (HrBenefitConfig bc : hsu.getAll(HrBenefitConfig.class)) {
			String code = bc.getHrBenefit().getWageType().getPayrollInterfaceCode();
			if (code == null || code.trim().equals(""))
				continue;

			writeBenefit(fw, bc.getBenefitConfigId(), code, emp);
		}

		for (ElectronicFundTransfer eft : hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, emp).orderBy(ElectronicFundTransfer.SEQNO).list())
			writeEFT(fw, emp, eft);

		writeGarnishments(fw, emp);
	}

	private void exportDemogEmp(Employee emp, DelimitedFileWriter fw) throws Exception {
		BEmployee bemp = new BEmployee(emp);
		writeField(fw, bemp.getExtRef());
		writeField(fw, bemp.getSsn());
		writeField(fw, bemp.getFirstName());
		writeField(fw, bemp.getMiddleName());
		writeField(fw, bemp.getLastName());
		writeField(fw, bemp.getStreet());
		writeField(fw, bemp.getStreet2());
		writeField(fw, bemp.getCity());
		writeField(fw, bemp.getState());
		writeField(fw, bemp.getZip());
		writeField(fw, bemp.getWorkPhone());
		writeField(fw, bemp.getLastStatusName());
		writeField(fw, "");//original hire date - blank in import
		writeDate(fw, bemp.getHireDate());

		if (DateUtils.addDays(bemp.getHireDate(), 2) == bemp.getTermDate() && bemp.getTermDate() < 20090909)
			writeDate(fw, 0);
		else
			writeDate(fw, DateUtils.addDays(bemp.getTermDate(), -1));
		writeDate(fw, bemp.getDob());
		writeField(fw, bemp.getSex());
		if (bemp.getEEORaceId().equals("00000-0000000000"))
			writeField(fw, "C");
		else
			writeField(fw, "P");//ethnicity unknown
		writeField(fw, "N"); //position status - all N in import
		writeField(fw, "S"); //pay frequency - all S in import
		if (bemp.getExpectedHoursPerPayPeriod() != 0)
			writeField(fw, bemp.getExpectedHoursPerPayPeriod()); // needs to change to float
		else
			writeField(fw, "");
		writeField(fw, bemp.getMaritalStatus());
		if (bemp.getWageType() == WageType.PERIOD_SALARY) {
			writeField(fw, bemp.getWageAmount() / bemp.getPayPeriodsPerYear());
			writeField(fw, "0");//use salary
		} else {
			writeField(fw, "0");
			writeField(fw, bemp.getWageAmount());//use salary
		}

		writeField(fw, ""); //time clock all blank in import
		writeField(fw, bemp.getTaxState());
		writeField(fw, ""); //tax code - all blank in import
		writeField(fw, " ".equals(bemp.getEarnedIncomeCreditStatus()) ? "N" : "Y");
		if (bemp.getAddFederalIncomeTaxType().equals(Employee.TYPE_ADDITIONAL_AMOUNT + "")) {
			writeField(fw, "Additional Amount");
			writeField(fw, bemp.getAddFederalIncomeTaxAmount());
		} else {
			writeField(fw, "None");
			writeField(fw, "");
		}

		writeField(fw, "N"); //Tax Override Type State all N in import
		writeField(fw, ""); //Tax Override Value State all blank in import
		writeField(fw, bemp.getFederalExemptions());
		writeField(fw, bemp.getStateExemptions());
		writeField(fw, "TN-SUI"); //tax name

		writeField(fw, bemp.getOrgGroupRef());
		writeField(fw, bemp.getWorkersCompCode());
		writeField(fw, bemp.getJobTitle());
		writeField(fw, bemp.getEEOCategory());

		fw.endRecord();
	}

	private String getFilePath() {
		String filePath = BProperty.get("EvolutionExportDirectory");
		// testing filepath
		//	filePath="/Users/Arahant/girlscouts/test";
		if (filePath == null || filePath.trim().equals(""))
			return "";

		if (filePath.endsWith(File.separator))
			return filePath;

		return filePath + File.separator;
	}

	public String exportDemog(List<String> empIds) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			String filename = getFilePath() + "Demog" + DateUtils.now() + ".csv";

			DelimitedFileWriter fw = new DelimitedFileWriter(filename, false);


			String[] headers = new String[]{"EE Code", "SSN", "First Name", "Middle Initial", "Last Name", "Address 1", "Address 2", "City", "State", "Zip Code", "Phone 1", "Current Status Code", "Original Hire Date", "Current Hire Date", "Current Term Date", "Birth Date", "Gender", "Ethinicity", "Position Status", "Pay Frequency", "Standard Hours", "Federal Marital Status", "Salary Amount", "Rate Amount", "Time Clock Number", "State", "Tax Code", "EIC", "Override Fed Tax Type", "Override Fed Tax Value", "Tax Override Type State", "Tax Override Value State", "Number of Dependents", "State Dependents", "Tax Name", "Department", "WCI", "Position", "EEO"};

			for (String h : headers)
				writeField(fw, h);

			fw.endRecord();

			HibernateScrollUtil<Employee> empScr = hsu.createCriteria(Employee.class).in(Employee.PERSONID, empIds).ne(Employee.MARITAL_STATUS, ' ').orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME).scroll();

			while (empScr.next())
				exportDemogEmp(empScr.get(), fw);

			fw.close();

			sendFile(new File(filename));

			return filename;
		} catch (Exception ex) {
			throw new ArahantException(ex);
		}
	}

	public void exportEmployee(BEmployee bemp) {
		try {
			if (bemp.getMaritalStatus() == null || bemp.getMaritalStatus().trim().equals(""))
				throw new ArahantWarning("Employee's marital status must not be unknown for export - " + bemp.getNameLFM() + ".");

			DelimitedFileWriter fw = new DelimitedFileWriter(getFilePath() + "Demog" + DateUtils.now() + ".csv", true);
			exportDemogEmp(bemp.getEmployee(), fw);
			fw.close();

			fw = new DelimitedFileWriter(getFilePath() + "Deduct" + DateUtils.now() + ".csv", true);
			exportDeductionsEmp(bemp.getEmployee(), fw);
			fw.close();

		} catch (ArahantException aw) {
			throw aw;
		} catch (Exception e) {
			logger.error(e);
			throw new ArahantException("Failed to write export to payroll system.", e);
		}
	}

	public String exportDeductions(List<String> empIds) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			String filename = getFilePath() + "Deduct" + DateUtils.now() + ".csv";
			DelimitedFileWriter fw = new DelimitedFileWriter(filename, false);
			//FileWriter fw = new FileWriter("Deduct.txt");

			String headers[] = new String[]{"EE Code", "SSN", "ED Code", "Description", "%", "Amount", "ABA Number", "Bank Account", "In Prenote", "Bank Account Type", "Target Amount", "Balance", "Effective Start Date", "Effective End Date", "Frequency", "Garnishment ID", "Custom Case Number", "FIPS Code", "Name", "Address 1", "Address 2", "City", "State", "Zip Code"};

			for (String s : headers)
				fw.writeField(s);
			fw.endRecord();


			HibernateScrollUtil<Employee> empScr = hsu.createCriteria(Employee.class).in(Employee.PERSONID, empIds).ne(Employee.MARITAL_STATUS, ' ').orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME).scroll();

			while (empScr.next())
				exportDeductionsEmp(empScr.get(), fw);

			fw.close();

			sendFile(new File(filename));
			return filename;
		} catch (Exception ex) {
			throw new ArahantException(ex);
		}
	}

	private void writeBenefit(DelimitedFileWriter fw, String id, String code, Employee emp) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, emp).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, id).orderByDesc(HrBenefitJoin.POLICY_START_DATE).first();

		if (bj == null) {
			writeExpiredBenefit(fw, id, code, emp);
			return;
		}

		// this needs to check to make sure they don't have another newer one in another config with the same code
		if (hasNewer(code, bj))
			return;

		BEmployee bemp = new BEmployee(emp);
		writeField(fw, emp.getExtRef());

		writeField(fw, bj.getPayingPerson().getUnencryptedSsn()); // ed code
		writeField(fw, code); //benefit code
		BHRBenefitConfig conf = new BHRBenefitConfig(id);
		writeField(fw, conf.getBenefitName());


		if (bj.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider() == 'N') {
			writeField(fw, 0);//Deduction Percent Amount
			writeField(fw, bj.getHrBenefitConfig().deprecatedGetEmployeeCost() / bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		} else if (bj.getAmountPaidType() == 'P') {
			writeField(fw, bj.getAmountPaid());//Deduction Percent Amount
			writeField(fw, "");//Deduction Flat Amount
		} else {
			writeField(fw, "");//Deduction Percent Amount
			writeField(fw, bj.getAmountPaid() / bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		}

		for (int loop = 0; loop < 6; loop++)
			writeField(fw, "");

		writeDate(fw, bj.getPolicyStartDate());
		writeDate(fw, bj.getPolicyEndDate());

		writeField(fw, "D");//frequency all D in import

		for (int loop = 0; loop < 9; loop++)
			writeField(fw, "");

		fw.endRecord();
	}

	private void writeGarnishments(DelimitedFileWriter fw, Employee emp) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		for (Garnishment bj : hsu.createCriteria(Garnishment.class).eq(Garnishment.EMPLOYEE, emp).orderBy(Garnishment.PRIORITY).list()) {
			BEmployee bemp = new BEmployee(emp);
			writeField(fw, bemp.getExtRef()); //left blank in deductions file

			writeField(fw, emp.getUnencryptedSsn());

			writeField(fw, bj.getGarnishmentType().getWageType().getPayrollInterfaceCode());//Deduction code

			writeField(fw, bj.getGarnishmentType().getDescription());

			if (bj.getDeductionPercentage() > .001) {
				writeField(fw, bj.getDeductionPercentage());//Deduction Percent Amount
				writeField(fw, "");//Deduction Flat Amount
			} else {
				writeField(fw, "");//Deduction Percent Amount
				writeField(fw, bj.getDeductionAmount());//Deduction Flat Amount
			}

			for (int loop = 0; loop < 6; loop++)
				writeField(fw, "");

			writeDate(fw, bj.getStartDate());//Deduction Effective Date
			writeDate(fw, bj.getEndDate());//Deduction Expiration Date
			writeField(fw, "D");//frequency is a D
			writeField(fw, bj.getDocketNumber());
			writeField(fw, ""); //custom case number, all blank on import
			writeField(fw, bj.getFipsCode());
			writeField(fw, bj.getRemitToName());
			writeField(fw, bj.getRemitTo().getStreet());
			writeField(fw, bj.getRemitTo().getStreet2());
			writeField(fw, bj.getRemitTo().getCity());
			writeField(fw, bj.getRemitTo().getState());
			writeField(fw, bj.getRemitTo().getZip());

			fw.endRecord();
		}
	}

	private void writeEFT(DelimitedFileWriter fw, Employee emp, ElectronicFundTransfer bj) throws Exception {
		BEmployee bemp = new BEmployee(emp);
		writeField(fw, emp.getExtRef());

		writeField(fw, emp.getUnencryptedSsn());

		String code = bj.getWageType().getPayrollInterfaceCode();
		if (code == null || code.isEmpty())
			throw new ArahantException("Payroll Interface Code missing from Wage Type");
		writeField(fw, code); //need a code

		String desc = bj.getWageType().getWageName().trim();

		writeField(fw, desc);

		if (code.equals("DNP")) {
			writeField(fw, "");
			writeField(fw, "");
		} else if (bj.getAmountType() == 'P') {
			writeField(fw, bj.getAmount());//Deduction Percent Amount
			writeField(fw, "");//Deduction Flat Amount
		} else {
			writeField(fw, "");//Deduction Percent Amount
			writeField(fw, bj.getAmount());//Deduction Flat Amount
		}

		writeField(fw, bj.getBankRoute());
		writeField(fw, bj.getBankAccount());
		writeField(fw, "N"); //prenote is N
		writeField(fw, bj.getAccountType() + "");
		writeField(fw, ""); //blank target amount
		writeField(fw, ""); //blank balance

		//Deduction Expiration Date
		int startDate = bemp.getEmploymentDate();
		if (startDate < 20090701)
			startDate = 20090701;
		writeDate(fw, startDate);//Deduction Effective Date

		if (code.equals("DNP"))
			writeField(fw, "");
		else
			writeDate(fw, bemp.getTermDate());
		writeField(fw, "D"); //frequency

		for (int loop = 0; loop < 9; loop++)
			writeField(fw, "");

		fw.endRecord();
	}

	public static void main(String args[]) {
		//ArahantSession.getHSU().setCurrentPersonToArahant();
		//PayDayExport exp=new PayDayExport();
		//exp.exportDemog((List)ArahantSession.getHSU().createCriteria(Employee.class).selectFields(Employee.PERSONID).list());
		//exp.exportDeductions((List)ArahantSession.getHSU().createCriteria(Employee.class).selectFields(Employee.PERSONID).list());
		//logger.info(exp.adjustStartDate(20090901));
		//new PayDayExport().sendFile(new File("/Users/Documents/test.pdf"));
	}

	private boolean hasNewer(String code, IHrBenefitJoin bj) {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson()).gt(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.INSURANCE_CODE, code).exists();
	}

	private void writeExpiredBenefit(DelimitedFileWriter fw, String id, String code, Employee emp) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		HrBenefitJoinH bj = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON, emp).eq(HrBenefitJoinH.HR_BENEFIT_CONFIG_ID, id).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D').orderByDesc(HrBenefitJoinH.POLICY_START_DATE).first();

		if (bj == null)
			return;

		//this needs to check to make sure they don't have another newer one in another config with the same code
		if (hasNewer(code, bj))
			return;

		BEmployee bemp = new BEmployee(emp);
		writeField(fw, emp.getExtRef());

		writeField(fw, bj.getPayingPerson().getUnencryptedSsn()); //ed code
		writeField(fw, code); //benefit code
		BHRBenefitConfig conf = new BHRBenefitConfig(id);
		writeField(fw, conf.getBenefitName());


		if (bj.getHrBenefitConfig().getHrBenefit().deprecatedGetEmployeeIsProvider() == 'N') {
			writeField(fw, 0);//Deduction Percent Amount	
			writeField(fw, bj.getHrBenefitConfig().deprecatedGetEmployeeCost() / bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		} else if (bj.getAmountPaidType() == 'P') {
			writeField(fw, bj.getAmountPaid());//Deduction Percent Amount
			writeField(fw, 0);//Deduction Flat Amount
		} else {
			writeField(fw, 0);//Deduction Percent Amount
			writeField(fw, bj.getAmountPaid() / bemp.getPayPeriodsPerYear());//Deduction Flat Amount
		}

		for (int loop = 0; loop < 6; loop++)
			writeField(fw, "");

		writeDate(fw, bj.getPolicyStartDate());
		writeDate(fw, bj.getPolicyEndDate());

		writeField(fw, "D");//frequency all D in import

		for (int loop = 0; loop < 9; loop++)
			writeField(fw, "");

		fw.endRecord();
	}

	//We passed certification before the quotes were taken out
	//Have to keep them just in case for now
	private void writeField(DelimitedFileWriter fw, String x) throws Exception {
		fw.writeField(x);
	}

	private void writeField(DelimitedFileWriter fw, double x) throws Exception {
		if (x == 0) {
			fw.writeField("0");
			return;
		}
		x *= 100;
		x = Math.round(x);
		x /= 100;
		String s = com.arahant.utils.Formatting.formatNumber(x, 2);
		if (s.endsWith(".00"))
			s = s.substring(0, s.length() - 3);
		else if (s.endsWith("0"))
			s = s.substring(0, s.length() - 1);
		fw.writeField(s);
	}

	private void writeField(DelimitedFileWriter fw, int x) throws Exception {
		fw.writeField(x + "");
	}
	
	private static final String dateFmt = "M/d/yyyy";

	private void writeDate(DelimitedFileWriter fw, int x) throws Exception {
		fw.writeField(DateUtils.dateFormat(dateFmt, x));
	}
	
	private static final String newline = "\r\n";
	private static final char thirtyFour = 34;

	private void sendFile(File fyle) {
        /* no longer functional.  Needs to be updated.
        

		try {
			String destination = BProperty.get("PayrollDestinationURL");
			if (destination == null || destination.equals(""))
				return;

			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			URL url = new URL("https://" + destination + "/PayrollReceiver/FileUploadServlet");
			//	URL url = new URL("http://localhost:8888/PayrollReceiver/FileUploadServlet");

			String boundry = "sgdfds";

			//HttpsURLConnectionOldImpl con = (HttpsURLConnectionOldImpl) url.openConnection();
			HttpURLConnection con =  (HttpURLConnection)url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			HttpsURLConnection.setFollowRedirects(true);
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundry);
			FileInputStream fis;
			try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
				fis = new FileInputStream(fyle);
				String header = "--" + boundry + newline;
				header += "Content-Disposition: form-data;name=\"datafile\";filename=" + thirtyFour + fyle.getName() + thirtyFour + newline;
				header += newline;
				header += "Content-Type: application/octet-stream" + newline;
				header += newline;
				out.write(header.getBytes(Charset.forName("UTF8")));
				byte[] fdat = new byte[(int) fyle.length()];
				fis.read(fdat);
				out.write(fdat);
				String marker = newline + "--" + boundry + "--" + newline;
				out.write(marker.getBytes(Charset.forName("UTF8")));
				out.flush();
			}
			fis.close();

			if (con.getResponseCode() != HttpsURLConnection.HTTP_OK)
				throw new ArahantException("HTTPS Transmit Failed with " + con.getResponseCode() + " " + con.getResponseMessage());

			InputStream is = con.getInputStream();

			InputStreamReader isr = new InputStreamReader(is);
			try (BufferedReader br = new BufferedReader(isr)) {
				String line;
				while ((line = br.readLine()) != null)
					System.out.println(line);
			}
			con.disconnect();
		} catch (IOException | ArahantException ex) {
			logger.error(ex);
		}
       */
	}
}
