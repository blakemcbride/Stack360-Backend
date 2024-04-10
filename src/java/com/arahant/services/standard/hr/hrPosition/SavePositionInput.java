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
 *
 *  Created on Feb 22, 2007
*/

package com.arahant.services.standard.hr.hrPosition;

import com.arahant.annotation.Validation;
import com.arahant.business.BHRPosition;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;
import org.kissweb.database.Connection;

import java.sql.SQLException;

public class SavePositionInput extends TransmitInputBase {

	@Validation (required=true)
	private String id;
	@Validation (table="hr_position",column="position_name",required=true)
	private String name;
	@Validation(type="date", required=false)
	private int firstActiveDate;
	@Validation(type="date", required=false)
	private int lastActiveDate;
	@Validation(required=false)
	private String benefitClassId;
	@Validation(required=false)
	private float weeklyPerDiem;
	private short seqno;
	private String applicantDefault;

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}


	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public float getWeeklyPerDiem() {
		return weeklyPerDiem;
	}

	public void setWeeklyPerDiem(float weeklyPerDiem) {
		this.weeklyPerDiem = weeklyPerDiem;
	}

	public short getSeqno() {
		return seqno;
	}

	public void setSeqno(short seqno) {
		this.seqno = seqno;
	}

	public String getApplicantDefault() {
		return applicantDefault;
	}

	public void setApplicantDefault(String applicantDefault) {
		this.applicantDefault = applicantDefault;
	}

	void setData(final BHRPosition x) throws SQLException {
		if ("Y".equals(applicantDefault)) {
			Connection db = ArahantSession.getKissConnection();
			db.execute("update hr_position set applicant_default = 'N'");
		}
		x.setName(name);
		x.setSeqno(seqno);
		x.setFirstActiveDate(firstActiveDate);
		x.setWeeklyPerDiem(weeklyPerDiem);
		x.setLastActiveDate(lastActiveDate);
		x.setBenefitClassId(benefitClassId);
		x.setApplicantDefault(applicantDefault.charAt(0));
	}
}

	
