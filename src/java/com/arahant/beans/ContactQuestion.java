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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "contact_question")
public class ContactQuestion extends ArahantBean {

    public static final String SEQ = "seqno";
    private String contactQuestionId;
    private short seqno;
    private String question;
    private Set<ContactQuestionDetail> details = new HashSet<ContactQuestionDetail>();

	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY="company";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column (name="company_id", insertable=false, updatable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	private int lastActiveDate = 0;
	@Column (name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContactQuestion other = (ContactQuestion) obj;
        if (this.contactQuestionId != other.getContactQuestionId() && (this.contactQuestionId == null || !this.contactQuestionId.equals(other.contactQuestionId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.contactQuestionId != null ? this.contactQuestionId.hashCode() : 0);
        return hash;
    }

    @Id
    @Column(name = "contact_question_id")
    public String getContactQuestionId() {
        return contactQuestionId;
    }

    public void setContactQuestionId(String contactQuestionId) {
        this.contactQuestionId = contactQuestionId;
    }

    @Column(name = "question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Column(name = "seqno")
    public short getSeqno() {
        return seqno;
    }

    public void setSeqno(short seqno) {
        this.seqno = seqno;
    }

    @Override
    public String tableName() {
        return "contact_question";
    }

    @Override
    public String keyColumn() {
        return "contact_question_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return contactQuestionId = IDGenerator.generate(this);
    }

    @OneToMany(mappedBy = ContactQuestionDetail.QUESTION, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ContactQuestionDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<ContactQuestionDetail> details) {
        this.details = details;
    }
}
