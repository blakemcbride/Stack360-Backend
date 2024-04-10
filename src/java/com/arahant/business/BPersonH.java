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
package com.arahant.business;

import com.arahant.beans.PersonH;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Date;
import java.util.List;

public class BPersonH extends SimpleBusinessObjectBase<PersonH> {

	public BPersonH() {
	}

	public BPersonH(String key) throws ArahantException {
		internalLoad(key);
	}

	public BPersonH(final PersonH ph) {
		bean = ph;
	}

	@Override
	public String create() throws ArahantException {
		bean = new PersonH();
		return bean.generateId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PersonH.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	public String getRecordPersonId() {
		return bean.getRecordPersonId();
	}

	public char getRecordChangeType() {
		return bean.getRecordChangeType();
	}

	public int getRecordChangeDateFormatted() {
		return DateUtils.getDate(bean.getRecordChangeDate());
	}

	public Date getRecordChangeDate() {
		return bean.getRecordChangeDate();
	}

	public String getLname() {
		return bean.getLname();
	}

	public String getMname() {
		return bean.getMname();
	}

	public String getFname() {
		return bean.getFname();
	}

	public String getNameLFM() {
		return bean.getNameLFM();
	}

	public String getNameFL() {
		return bean.getNameFL();
	}

	public String getNameFML() {
		return bean.getNameFML();
	}

	public String getNickName() {
		return bean.getNickName();
	}

	public String getSsn() {
		return bean.getSsn();
	}

	public String getAutoInsuranceStart() {
		return DateUtils.getDateFormatted(bean.getAutoInsuranceStart());
	}

	public String getAutoInsurancePolicy() {
		return bean.getAutoInsurancePolicy();
	}

	public String getAutoInsuranceExp() {
		return DateUtils.getDateFormatted(bean.getAutoInsuranceExp());
	}

	public String getAutoInsuranceCoverage() {
		return bean.getAutoInsuranceCoverage();
	}

	public String getAutoInsuranceCarrier() {
		return bean.getAutoInsuranceCarrier();
	}

	public String getSmoker() {
		return "" + bean.getSmoker();
	}

	public String getDriversLicenseState() {
		return bean.getDriversLicenseState();
	}

	public String getDriversLicenseNumber() {
		return bean.getDriversLicenseNumber();
	}

	public String getDriversLicenseExp() {
		return DateUtils.getDateFormatted(bean.getDriversLicenseExp());
	}

	public String getI9Part1() {
		return "" + bean.getI9Part1();
	}

	public String getI9Part2() {
		return "" + bean.getI9Part2();
	}

	public String getVisaStatusDate() {
		return DateUtils.getDateFormatted(bean.getVisaStatusDate());
	}

	public String getVisaExpirationDate() {
		return DateUtils.getDateFormatted(bean.getVisaExpirationDate());
	}

	public String getVisa() {
		return bean.getVisa();
	}

	public String getCitizenship() {
		return bean.getCitizenship();
	}

	public String getSex() {
		return "" + bean.getSex();
	}

	public String getDob() {
		return DateUtils.getDateFormatted(bean.getDob());
	}

	public String getPersonalEmail() {
		return bean.getPersonalEmail();
	}

	public String getJobTitle() {
		return bean.getTitle();
	}

	public String getHandicap() {
		return "" + bean.getHandicap();
	}

	public String getStudent() {
		return "" + bean.getStudent();
	}

	public char getStudentCalendarType() {
		return bean.getStudentCalendarType();
	}

	public void setStudentCalendarType(char studentCalendarType) {
		bean.setStudentCalendarType(studentCalendarType);
	}

	public char getRecordType() {
		return bean.getRecordType();
	}

	public void setRecordType(char recordType) {
		bean.setRecordType(recordType);
	}

	public String getDefaultProjectName() {
		if (isEmpty(bean.getDefaultProjectId()))
			return "";
		return new BProject(bean.getDefaultProjectId()).getProjectName();
	}

	public String getHistoryId() {
		return bean.getHistory_id();
	}

	public short getHeight() {
		return bean.getHeight();
	}

	public void setHeight(short height) {
		bean.setHeight(height);
	}

	public short getWeight() {
		return bean.getWeight();
	}

	public void setWeight(short weight) {
		bean.setWeight(weight);
	}

	public char getSmokingProgram() {
		return bean.getSmokingProgram();
	}

	public void setSmokingProgram(char smokingProgram) {
		bean.setSmokingProgram(smokingProgram);
	}

	public char getMilitaryBranch() {
		return bean.getMilitaryBranch();
	}

	public void setMilitaryBranch(char militaryBranch) {
		bean.setMilitaryBranch(militaryBranch);
	}

	public String getMilitaryDischargeExplain() {
		return bean.getMilitaryDischargeExplain();
	}

	public void setMilitaryDischargeExplain(String militaryDischargeExplain) {
		bean.setMilitaryDischargeExplain(militaryDischargeExplain);
	}

	public char getMilitaryDischargeType() {
		return bean.getMilitaryDischargeType();
	}

	public void setMilitaryDischargeType(char militaryDischargeType) {
		bean.setMilitaryDischargeType(militaryDischargeType);
	}

	public int getMilitaryEndDate() {
		return bean.getMilitaryEndDate();
	}

	public void setMilitaryEndDate(int militaryEndDate) {
		bean.setMilitaryEndDate(militaryEndDate);
	}

	public String getMilitaryRank() {
		return bean.getMilitaryRank();
	}

	public void setMilitaryRank(String militaryRank) {
		bean.setMilitaryRank(militaryRank);
	}

	public int getMilitaryStartDate() {
		return bean.getMilitaryStartDate();
	}

	public void setMilitaryStartDate(int militaryStartDate) {
		bean.setMilitaryStartDate(militaryStartDate);
	}

	public char getConvictedOfCrime() {
		return bean.getConvictedOfCrime();
	}

	public void setConvictedOfCrime(char convictedOfCrime) {
		bean.setConvictedOfCrime(convictedOfCrime);
	}

	public String getConvictedOfWhat() {
		return bean.getConvictedOfWhat();
	}

	public void setConvictedOfWhat(String convictedOfWhat) {
		bean.setConvictedOfWhat(convictedOfWhat);
	}

	public char getWorkedForCompanyBefore() {
		return bean.getWorkedForCompanyBefore();
	}

	public void setWorkedForCompanyBefore(char workedForCompanyBefore) {
		bean.setWorkedForCompanyBefore(workedForCompanyBefore);
	}

	public String getWorkedForCompanyWhen() {
		return bean.getWorkedForCompanyWhen();
	}

	public void setWorkedForCompanyWhen(String workedForCompanyWhen) {
		bean.setWorkedForCompanyWhen(workedForCompanyWhen);
	}

	public String getHicNumber() {
		return bean.getHicNumber();
	}

	public void setHicNumber(String hicNumber) {
		bean.setHicNumber(hicNumber);
	}

	public String getAgreementAddressIp() {
		return bean.getAgreementAddressIp();
	}

	public void setAgreementAddressIp(String agreementAddressIp) {
		bean.setAgreementAddressIp(agreementAddressIp);
	}

	public String getAgreementAddressUrl() {
		return bean.getAgreementAddressUrl();
	}

	public void setAgreementAddressUrl(String agreementAddressUrl) {
		bean.setAgreementAddressUrl(agreementAddressUrl);
	}

	public Date getAgreementDate() {
		return bean.getAgreementDate();
	}

	public void setAgreementDate(Date agreementDate) {
		bean.setAgreementDate(agreementDate);
	}

	public int getAgreementRevision() {
		return bean.getAgreementRevision();
	}

	public void setAgreementRevision(int agreementRevision) {
		bean.setAgreementRevision(agreementRevision);
	}

	public String getDrugAlcoholAbust() {
		return bean.getDrugAlcoholAbust() + "";
	}

	public void setDrugAlcoholAbust(char drugAlcoholAbust) {
		bean.setDrugAlcoholAbust(drugAlcoholAbust);
	}

	public void setDrugAlcoholAbust(String drugAlcoholAbust) {
		bean.setDrugAlcoholAbust(drugAlcoholAbust.charAt(0));
	}

	public void setDrugAlcoholAbust(boolean drugAlcoholAbust) {
		bean.setDrugAlcoholAbust(drugAlcoholAbust ? 'Y' : 'N');
	}

	public String getNotMissedFiveDays() {
		return bean.generateId() + "";
	}

	public void setNotMissedFiveDays(char notMissedFiveDays) {
		bean.setNotMissedFiveDays(notMissedFiveDays);
	}

	public void setNotMissedFiveDays(String notMissedFiveDays) {
		bean.setNotMissedFiveDays(notMissedFiveDays.charAt(0));
	}

	public void setNotMissedFiveDays(boolean notMissedFiveDays) {
		bean.setNotMissedFiveDays(notMissedFiveDays ? 'Y' : 'N');
	}

	public String getReplaceEmployerPlan() {
		return bean.getReplaceEmployerPlan() + "";
	}

	public void setReplaceEmployerPlan(char replaceEmployerPlan) {
		bean.setReplaceEmployerPlan(replaceEmployerPlan);
	}

	public void setReplaceEmployerPlan(String replaceEmployerPlan) {
		bean.setReplaceEmployerPlan(replaceEmployerPlan.charAt(0));
	}

	public void setReplaceEmployerPlan(boolean replaceEmployerPlan) {
		bean.setReplaceEmployerPlan(replaceEmployerPlan ? 'Y' : 'N');
	}

	public String getTwoFamilyCancer() {
		return bean.getTwoFamilyCancer() + "";
	}

	public void setTwoFamilyCancer(char twoFamilyCancer) {
		bean.setTwoFamilyCancer(twoFamilyCancer);
	}

	public void setTwoFamilyCancer(String twoFamilyCancer) {
		bean.setTwoFamilyCancer(twoFamilyCancer.charAt(0));
	}

	public void setTwoFamilyCancer(boolean twoFamilyCancer) {
		bean.setTwoFamilyCancer(twoFamilyCancer ? 'Y' : 'N');
	}

	public String getTwoFamilyDiabetes() {
		return bean.getTwoFamilyDiabetes() + "";
	}

	public void setTwoFamilyDiabetes(char twoFamilyDiabetes) {
		bean.setTwoFamilyDiabetes(twoFamilyDiabetes);
	}

	public void setTwoFamilyDiabetes(String twoFamilyDiabetes) {
		bean.setTwoFamilyDiabetes(twoFamilyDiabetes.charAt(0));
	}

	public void setTwoFamilyDiabetes(boolean twoFamilyDiabetes) {
		bean.setTwoFamilyDiabetes(twoFamilyDiabetes ? 'Y' : 'N');
	}

	public String getTwoFamilyHeartCond() {
		return bean.getTwoFamilyHeartCond() + "";
	}

	public void setTwoFamilyHeartCond(char twoFamilyHeartCond) {
		bean.setTwoFamilyHeartCond(twoFamilyHeartCond);
	}

	public void setTwoFamilyHeartCond(String twoFamilyHeartCond) {
		bean.setTwoFamilyHeartCond(twoFamilyHeartCond.charAt(0));
	}

	public void setTwoFamilyHeartCond(boolean twoFamilyHeartCond) {
		bean.setTwoFamilyHeartCond(twoFamilyHeartCond ? 'Y' : 'N');
	}

	public static BPersonH[] listHistory(final int max, final String personId, int fromDate, int toDate) {
		HibernateCriteriaUtil<PersonH> hcu = ArahantSession.getHSU().createCriteria(PersonH.class)
				.dateBetween(PersonH.RECORD_CHANGE_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate))
				.orderByDesc(PersonH.RECORD_CHANGE_DATE).orderBy(PersonH.LNAME).setMaxResults(max);

		if (!isEmpty(personId))
			hcu.eq(PersonH.PERSONID, personId);

		return makeArray(hcu.list());
	}

	public static BPersonH getNextHistory(final int max, final Date date, final String personId) {
		HibernateCriteriaUtil<PersonH> hcu = ArahantSession.getHSU().createCriteria(PersonH.class)
				.orderBy(PersonH.RECORD_CHANGE_DATE)
				.dateAfter(PersonH.RECORD_CHANGE_DATE, date)
				.eq(PersonH.PERSONID, personId)
				.setMaxResults(max);

		if (hcu.exists())
			return new BPersonH(hcu.first());

		return null;
	}

	public static BPersonH getPreviousHistory(final int max, final Date date, final String personId) {

		HibernateCriteriaUtil<PersonH> hcu = ArahantSession.getHSU().createCriteria(PersonH.class)
				.orderByDesc(PersonH.RECORD_CHANGE_DATE)
				.dateBefore(PersonH.RECORD_CHANGE_DATE, date)
				.eq(PersonH.PERSONID, personId)
				.setMaxResults(max);

		if (hcu.exists())
			return new BPersonH(hcu.first());

		return null;
	}

	public static BPersonH[] makeArray(final List<PersonH> l) throws ArahantException {

		final BPersonH[] ret = new BPersonH[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPersonH(l.get(loop));

		return ret;
	}
}
