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

package com.arahant.business;

import com.arahant.beans.ApplicantContact;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BApplicantContact extends SimpleBusinessObjectBase<ApplicantContact> {

	static BApplicantContact[] makeArray(List<ApplicantContact> l) {
		BApplicantContact []ret=new BApplicantContact[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BApplicantContact(l.get(loop));
		return ret;
	}

	public BApplicantContact(ApplicantContact o) {
		bean=o;
	}
	
	public BApplicantContact(String id) {
		super(id);
	}
	
	public BApplicantContact() {
	}

	@Override
	public String create() throws ArahantException {
		bean=new ApplicantContact();
		return bean.generateId();
	}

	public int getDate() {
		return bean.getContactDate();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getApplicantContactId();
	}

	public String getMode() {
		return bean.getContactMode()+"";
	}

	public String getStatus() {
		return bean.getContactStatus()+"";
	}

	public int getTime() {
		return bean.getContactTime();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ApplicantContact.class, key);
	}

	public void setApplicant(BApplicant bc) {
		bean.setApplicant(bc.applicant);
	}

	public void setApplication(BApplication app) {
		bean.setApplicant(app.bean.getApplicant());
		bean.setApplication(app.bean);
	}

	public void setDate(int date) {
		bean.setContactDate(date);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setMode(String mode) {
		bean.setContactMode(mode.charAt(0));
	}

	public void setStatus(String status) {
		bean.setContactStatus(status.charAt(0));
	}

	public void setTime(int time) {
		bean.setContactTime(time);
	}

	public String getWhoAdded() {
		return bean.getWhoAdded();
	}

	public void setWhoAdded(String whoAdded) {
		bean.setWhoAdded(whoAdded);
	}
}
