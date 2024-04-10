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
package com.arahant.services.standard.crm.companyQuestion;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BCompanyQuestion;
import com.arahant.utils.ArahantSession;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewQuestionInput extends TransmitInputBase {

	void setData(BCompanyQuestion bc) {
		bc.setQuestion(question);
		bc.setAddAfterId(addAfterId);
		bc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bc.setCompany(null);
		else
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}
	@Validation(min = 1, max = 16, required = false)
	private String addAfterId;
	@Validation(table = "company_question", column = "question", required = true)
	private String question;
	@Validation(table = "company_question", column = "last_active_date", required = false, type = "date")
	private int lastActiveDate;
	@Validation(required = false)
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}


	public String getAddAfterId() {
		return addAfterId;
	}

	public void setAddAfterId(String addAfterId) {
		this.addAfterId = addAfterId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}

	
