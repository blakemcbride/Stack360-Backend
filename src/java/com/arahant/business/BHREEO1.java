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

import com.arahant.beans.HrEeo1;
import com.arahant.exceptions.ArahantException;
import com.arahant.exports.EEO1Export;
import com.arahant.services.standard.hr.eeo1Survey.NewSurveyInputEstablishment;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BHREEO1 extends SimpleBusinessObjectBase<HrEeo1> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BHREEO1(id).delete();
	}

	public static BHREEO1[] list(int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrEeo1.class).orderByDesc(HrEeo1.CREATED_DATE).list());
	}

	static BHREEO1[] makeArray(List<HrEeo1> l) {
		BHREEO1[] ret = new BHREEO1[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHREEO1(l.get(loop));
		return ret;
	}

	public BHREEO1() {
	}

	public BHREEO1(String id) {
		super(id);
	}

	public BHREEO1(HrEeo1 o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new HrEeo1();
		bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrEeo1.class, key);
	}

	public String getId() {
		return bean.getEeo1Id();
	}

	public boolean getCommonOwnership() {
		return bean.getCommonOwnership() == 'Y';
	}

	public void setCommonOwnership(boolean value) {
		bean.setCommonOwnership(value ? 'Y' : 'N');
	}

	public int getCreatedDate() {
		return bean.getCreatedDate();
	}

	public void setCreatedDate(int value) {
		bean.setCreatedDate(value);
	}

	public boolean getGovernmentContractor() {
		return bean.getGovernmentContractor() == 'Y';
	}

	public void setGovernmentContractor(boolean value) {
		bean.setGovernmentContractor(value ? 'Y' : 'N');
	}

	public int getPayPeriodFinalDate() {
		return bean.getPayPeriodFinalDate();
	}

	public void setPayPeriodFinalDate(int value) {
		bean.setPayPeriodFinalDate(value);
	}

	public int getPayPeriodStartDate() {
		return bean.getPayPeriodStartDate();
	}

	public void setPayPeriodStartDate(int value) {
		bean.setPayPeriodStartDate(value);
	}

	public int getUploadedDate() {
		return bean.getUploadedDate();
	}

	public void setUploadedDate(int value) {
		bean.setUploadedDate(value);
	}

	public String getTransmittedData() {
		return bean.getTransmittedData();
	}

	public void setTransmittedData(String value) {
		bean.setTransmittedData(value);
	}

	public String getDataFile() throws IOException {
		File file = FileSystemUtils.createTempFile("eeo1survey", ".txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		bw.write(this.bean.getTransmittedData());

		bw.flush();
		bw.close();

		return FileSystemUtils.getHTTPPath(file);
	}

	public static String generateReportData(int startDate, int finalDate, boolean governmentContractor, boolean commonOwnership,
			String certifierTitle, String certifierName, String certifierPhone, String certifierEmail, NewSurveyInputEstablishment[] establishments) {
		return new EEO1Export().export(startDate, finalDate, governmentContractor, commonOwnership, certifierTitle, certifierName, certifierPhone, certifierEmail, establishments);
	}
}
