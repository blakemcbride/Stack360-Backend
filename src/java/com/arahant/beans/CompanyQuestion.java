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

import java.io.Serializable;
import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "company_question")
public class CompanyQuestion extends ArahantBean implements Serializable {

    public static final String SEQ = "seqno";
	public static final String QUESTION = "question";
	public static final String ID = "companyQuesId";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
    private String companyQuesId;
    private short seqno;
    private String question;
	private int lastActiveDate = 0;
    private Set<CompanyQuestionDetail> details = new HashSet<CompanyQuestionDetail>();

	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY="company";

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

	@Column (name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

    @Id
    @Column(name = "company_ques_id")
    public String getCompanyQuesId() {
        return companyQuesId;
    }

    public void setCompanyQuesId(String companyQuesId) {
        this.companyQuesId = companyQuesId;
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
        return "company_question";
    }

    @Override
    public String keyColumn() {
        return "company_ques_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return companyQuesId = IDGenerator.generate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompanyQuestion other = (CompanyQuestion) obj;
        if (this.companyQuesId != other.getCompanyQuesId() && (this.companyQuesId == null || !this.companyQuesId.equals(other.companyQuesId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.companyQuesId != null ? this.companyQuesId.hashCode() : 0);
        return hash;
    }

    @OneToMany(mappedBy = CompanyQuestionDetail.COMPANY_QUESTION, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<CompanyQuestionDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<CompanyQuestionDetail> details) {
        this.details = details;
    }
}
