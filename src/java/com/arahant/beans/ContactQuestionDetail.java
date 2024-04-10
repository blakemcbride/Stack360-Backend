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
@Table(name="contact_question_detail")
public class ContactQuestionDetail extends ArahantBean {
	public static final String PERSON="person";
	public static final String QUESTION="question";

	private String contactQuestionDetId;
	private Person person;
	private ContactQuestion question;
	private String response;
	private Date whenAdded;

	@Override
	public String tableName() {
		return "contact_question_detail";
	}

	@Override
	public String keyColumn() {
		return "contact_question_det_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return contactQuestionDetId=IDGenerator.generate(this);
	}

	@Id
	@Column (name="contact_question_det_id")
	public String getContactQuestionDetId() {
		return contactQuestionDetId;
	}

	public void setContactQuestionDetId(String contactQuestionDetId) {
		this.contactQuestionDetId = contactQuestionDetId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="contact_question_id")
	public ContactQuestion getQuestion() {
		return question;
	}

	public void setQuestion(ContactQuestion question) {
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
	@Temporal (TemporalType.TIMESTAMP)
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
		final ContactQuestionDetail other = (ContactQuestionDetail) obj;
		if (this.contactQuestionDetId != other.contactQuestionDetId && (this.contactQuestionDetId == null || !this.contactQuestionDetId.equals(other.contactQuestionDetId))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + (this.contactQuestionDetId != null ? this.contactQuestionDetId.hashCode() : 0);
		return hash;
	}
	
	

}
