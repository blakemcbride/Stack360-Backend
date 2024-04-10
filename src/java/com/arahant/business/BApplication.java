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

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.ExternalFile;
import org.kissweb.DateUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.Date;
import java.util.List;

public class BApplication extends SimpleBusinessObjectBase<ApplicantApplication> {

	public BApplication(ApplicantApplication o) {
		bean = o;
	}

	public BApplication(String id) {
		super(id);
	}

	public BApplication() {
	}

	@Override
	public void delete() {
		ArahantSession.getHSU().createCriteria(ApplicantContact.class).eq(ApplicantContact.APPLICATION, bean).delete();
		ExternalFile.deleteAllExternalFiles(ApplicantApplication.TABLE_NAME, bean.getApplicantApplicationId());
		super.delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ApplicantApplication();
		return bean.generateId();
	}

	public void deleteContactsNotIn(List<String> contactIds) {
		ArahantSession.getHSU().createCriteria(ApplicantContact.class).eq(ApplicantContact.APPLICATION, bean).notIn(ApplicantContact.ID, contactIds).delete();
	}

	public BApplicantContact[] getApplicationContacts() {
		return BApplicantContact.makeArray(ArahantSession.getHSU().createCriteria(ApplicantContact.class).orderByDesc(ApplicantContact.DATE).orderByDesc(ApplicantContact.TIME).eq(ApplicantContact.APPLICATION, bean).list());
	}

	public int getDate() {
		return bean.getApplicationDate();
	}

	public String getId() {
		return bean.getApplicantApplicationId();
	}

	public String getStatus() {
		return bean.getAppStatus().getStatusName();
	}

	public String getStatusId() {
		return bean.getAppStatus().getApplicantAppStatusId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ApplicantApplication.class, key);
	}

	public void setApplicant(BApplicant bc) {
		bean.setApplicant(bc.applicant);
	}

	public void setDate(int date) {
		bean.setApplicationDate(date);
	}

	public void setStatusId(String statusId) {
		bean.setAppStatus(ArahantSession.getHSU().get(ApplicantAppStatus.class, statusId));
	}

	public Date getOfferFirstGenerated() {
		return bean.getOfferFirstGenerated();
	}

	public void setOfferFirstGenerated(Date offerFirstGenerated) {
		bean.setOfferFirstGenerated(offerFirstGenerated);
	}

	public Date getOfferLastGenerated() {
		return bean.getOfferLastGenerated();
	}

	public void setOfferLastGenerated(Date offerLastGenerated) {
		bean.setOfferLastGenerated(offerLastGenerated);
	}

	public Date getOfferFirstEmailed() {
		return bean.getOfferFirstEmailed();
	}

	public void setOfferFirstEmailed(Date offerFirstEmailed) {
		bean.setOfferFirstEmailed(offerFirstEmailed);
	}

	public Date getOfferLastEmailed() {
		return bean.getOfferLastEmailed();
    }

	public void setOfferLastEmailed(Date offerLastEmailed) {
		bean.setOfferLastEmailed(offerLastEmailed);
	}

	public Date getOfferElectronicallySignedDate() {
		return bean.getOfferElectronicallySignedDate();
	}

	public void setOfferElectronicallySignedDate(Date offerElectronicallySignedDate) {
		bean.setOfferElectronicallySignedDate(offerElectronicallySignedDate);
	}

	public String getOfferElectronicallySignedIp() {
		return bean.getOfferElectronicallySignedIp();
	}

	public void setOfferElectronicallySignedIp(String offerElectronicallySignedIp) {
		bean.setOfferElectronicallySignedIp(offerElectronicallySignedIp);
	}

	public Float getPayRate() {
		return bean.getPayRate();
	}

	public void setPayRate(Float payRate) {
		bean.setPayRate(payRate);
	}

	public ApplicantPosition getApplicantPosition() {
		return bean.getApplicantPosition();
	}

	public void setApplicantPosition(ApplicantPosition hrPosition) {
		bean.setApplicantPosition(hrPosition);
	}

	public HrPosition getPosition() {
		return bean.getPosition();
	}

	public void setPosition(HrPosition position) {
		bean.setPosition(position);
	}

	public short getPhase() {
		return bean.getPhase();
	}

	/**
	 * This may also update the applicant_app_status column.
	 *
	 * @param newPhase
	 */
    public void setPhase(short newPhase) {
        bean.setPhase(newPhase);
		// may need to update applicant_app_status column

		final Connection db = ArahantSession.getKissConnection();
		final Command cmd = db.newCommand();
		try {
			final Record rec = cmd.fetchOne("select phase from applicant_app_status where applicant_app_status_id = ?", bean.getAppStatus().getApplicantAppStatusId());
			final short oldPhase = rec.getShort("phase");
			if (oldPhase == newPhase)
				return;
			final String statusId = calcApplicationStatusId(newPhase);
			if (statusId != null)
				bean.setAppStatus(new BApplicationStatus(statusId).getBean());
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

	/**
	 * Calculate the correct application_status_id that corresponds with the given phase.
	 *
	 * @param phase
	 * @return
	 */
	public static String calcApplicationStatusId(short phase) {
		final Connection db = ArahantSession.getKissConnection();
		final Command cmd = db.newCommand();
		try {
			final Record rec = cmd.fetchOne("select applicant_app_status_id " +
					"from applicant_app_status " +
					"where phase = ? " +
					"      and (last_active_date = 0 or last_active_date >= ?) " +
					"order by status_order", phase, DateUtils.today());
			return rec == null ? null : rec.getString("applicant_app_status_id");
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

	/**
	 * Used with KISS records to auto-update applicant_app_status_id on an applicant_application record based on phase.
	 *
	 * @param rec must be an applicant_application record
	 * @param phase
	 */
	public static void updateApplicationStatusId(Record rec, short phase) throws Exception {
		final String asid = calcApplicationStatusId(phase);
		if (asid != null) {
			if (!asid.equals(rec.getString("applicant_app_status_id"))) {
				BChangeLog.applicantAppStatusChange(rec.getString("person_id"), rec.getString("applicant_app_status_id"), asid, null);
				rec.set("applicant_app_status_id", asid);
			}
		}
	}

	public Date getOfferDeclinedDate() {
		return bean.getOfferDeclinedDate();
	}

	public void setOfferDeclinedDate(Date offerDeclinedDate) {
        bean.setOfferDeclinedDate(offerDeclinedDate);
    }

	public Date getOfferRetractedDate() {
		return bean.getOfferRetractedDate();
	}

	public void setOfferRetractedDate(Date offerRetractedDate) {
        bean.setOfferRetractedDate(offerRetractedDate);
    }

	public Date getOfferLastViewedDate() {
		return bean.getOfferLastViewedDate();
	}

	public void setOfferLastViewedDate(Date offerLastViewedDate) {
        bean.setOfferLastViewedDate(offerLastViewedDate);
    }

	static BApplication[] makeArray(List<ApplicantApplication> lst) {
		int size = 0;
		// This check for a null position is caused because of an SQL left join elsewhere.
		// It causes a null record to be generated when there is no applications.
		// This code compensates for that.
		for (ApplicantApplication aa : lst)
			if (aa.getApplicantPosition() != null)
				size++;
		BApplication[] ret = new BApplication[size];
		for (int i=0, j = 0; j < lst.size() ; j++) {
			ApplicantApplication aa = lst.get(j);
			if (aa.getApplicantPosition() != null) {
				ret[i++] = new BApplication(aa);
			}
		}
		return ret;
	}

}
