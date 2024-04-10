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
 */
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.beans.HrNoteCategory;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRNoteCategory;
import com.arahant.business.BPersonNote;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class TerminateEmployeeInput extends TransmitInputBase {

	void setData(BEmployee bc)
	{
		if (benefitCancelDate != null)
		{
			for (CancelBenefitItem cb : benefitCancelDate)
			{
				if (cb.getCancelDate() == 0)
					new BHRBenefitJoin(cb.getId()).terminate(finalDate);
				else
					new BHRBenefitJoin(cb.getId()).terminate(cb.getCancelDate());
			}
		}
		
		bc.setStatusId(statusId, finalDate, notes);

		if (misconduct)
		{
			BPersonNote note=new BPersonNote();
			note.create();
			note.setPersonId(personId);

			//query for note category with name "Misconduct"
			HrNoteCategory cat=ArahantSession.getHSU().createCriteria(HrNoteCategory.class).eq(HrNoteCategory.NAME, "Misconduct").first();
			// if it's not there, add it
			String catId=null;
			if (cat==null)
			{
				BHRNoteCategory nc=new BHRNoteCategory();
				catId=nc.create();
				nc.setName("Misconduct");
				nc.insert();
			}
			else
				catId=cat.getCatId();

			note.setNote(misconductInfo);
			note.setNoteCategoryId(catId);
			note.insert();
		}

		bc.setCanLogin(inactiveateLogin?'N':'Y');


		

	}

	@Validation (required=false)
	private String personId;
	@Validation (type="date",required=true)
	private int finalDate;
	@Validation (required=true)
	private String notes;
	@Validation (required=false)
	private boolean misconduct;
	@Validation (required=false)
	private String misconductInfo;
	@Validation (required=false)
	private boolean inactiveateLogin;
	@Validation (type="array",required=false)
	private CancelBenefitItem[] benefitCancelDate;
	@Validation (required=true)
	private String statusId;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public String getNotes()
	{
		return notes;
	}
	public void setNotes(String notes)
	{
		this.notes=notes;
	}
	public boolean getMisconduct()
	{
		return misconduct;
	}
	public void setMisconduct(boolean misconduct)
	{
		this.misconduct=misconduct;
	}
	public String getMisconductInfo()
	{
		return misconductInfo;
	}
	public void setMisconductInfo(String misconductInfo)
	{
		this.misconductInfo=misconductInfo;
	}
	public boolean getInactiveateLogin()
	{
		return inactiveateLogin;
	}
	public void setInactiveateLogin(boolean inactiveateLogin)
	{
		this.inactiveateLogin=inactiveateLogin;
	}

	public CancelBenefitItem[] getBenefitCancelDate() {
		return benefitCancelDate;
	}

	public void setBenefitCancelDate(CancelBenefitItem[] benefitCancelDate) {
		this.benefitCancelDate = benefitCancelDate;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

}

	
