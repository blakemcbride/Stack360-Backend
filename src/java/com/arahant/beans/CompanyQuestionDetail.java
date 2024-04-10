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

package com.arahant.beans;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.Date;

/**
 *
 */
@Entity
@Table(name="company_question_detail")
public class CompanyQuestionDetail extends ArahantBean {
	public static final String COMPANY="company";
	public static final String COMPANY_QUESTION="question";
	private String companyQuesDetId;
	private CompanyBase company;
	private CompanyQuestion question;
	private String response;
	private Date whenAdded;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="org_group_id")
	public CompanyBase getCompany() {
		return company;
	}

	public void setCompany(CompanyBase company) {
		this.company = company;
	}

	@Id
	@Column (name="company_ques_det_id")
	public String getCompanyQuesDetId() {
		return companyQuesDetId;
	}

	public void setCompanyQuesDetId(String companyQuesDetId) {
		this.companyQuesDetId = companyQuesDetId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="company_ques_id")
	public CompanyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(CompanyQuestion question) {
		this.question = question;
	}

	@Column (name="response")
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Column (name="when_added")
	public Date getWhenAdded() {
		return whenAdded;
	}

	public void setWhenAdded(Date whenAdded) {
		this.whenAdded = whenAdded;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CompanyQuestionDetail other = (CompanyQuestionDetail) obj;
		if (this.companyQuesDetId != other.getCompanyQuesDetId() && (this.companyQuesDetId == null || !this.companyQuesDetId.equals(other.companyQuesDetId))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (this.companyQuesDetId != null ? this.companyQuesDetId.hashCode() : 0);
		return hash;
	}

	@Override
	public String tableName() {
		return "company_question_detail";
	}

	@Override
	public String keyColumn() {
		return "company_ques_det_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return companyQuesDetId=IDGenerator.generate(this);
	}
	
	

}
