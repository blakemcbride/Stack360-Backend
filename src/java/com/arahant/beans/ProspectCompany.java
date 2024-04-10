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


package com.arahant.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = ProspectCompany.TABLE_NAME)
public class ProspectCompany extends CompanyBase implements java.io.Serializable, IAuditedBean {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "prospect";
    public static final String PROSPECT_STATUS = "prospectStatus";
    public static final String DATE = "firstContactDate";
    public static final String LAST_CONTACT_DATE = "lastContactDate";
    public static final String PROSPECT_SOURCE = "prospectSource";
    public static final String PROSPECT_TYPE = "prospectType";
    public static final String PROSPECT_TYPE_ID = "prospectTypeId";
    public static final String SALESPERSON = "salesPerson";
    public static final String PROSPECT_LOG = "prospectLogs";
    public static final String LAST_LOG_DATE = "lastLogDate";
	public static final String LAST_LOG_DATE_WITH_RESULT = "lastLogDateWithResult";
    public static final String LAST_LOG_TIME = "lastLogTime";
	public static final String ADDED_DATE = "addedDate";
	public static final String NEXT_CONTACT_DATE = "nextContactDate";
	public static final String PROSPECT_STATUS_DATE = "statusChangeDate";
	public static final String ASSOCIATED_COMPANY = "associatedCompany";
	public static final String ASSOCIATED_COMPANY_ID = "associatedCompanyId";
	public static final String OPPORTUNITY_VALUE = "opportunity_value";

    private int firstContactDate;
    private ProspectStatus prospectStatus;
	public static final String CERTAINTY = "certainty";
    private short certainty = 0;
    //private String primaryInterests;
    //private String objections;
    private ProspectSource prospectSource;
    private String sourceDetail;
    private Employee salesPerson;
	private String salesPersonId;
    private Integer lastLogDate;
	private Integer lastLogDateWithResult;
	private int lastContactDate;
    private Integer lastLogTime;
    private Set<ProspectLog> prospectLogs = new HashSet<ProspectLog>(0);
	private String associatedCompanyId;
	private CompanyDetail associatedCompany;
	private double opportunity_value = 0.0;
	private int numberOfEmployees = 0;
	private int grossIncome = 0;
	private String website;
//	private Set<ProspectStatusHistory> prospectStatusHistories = new HashSet<ProspectStatusHistory>(0);
  //  public static final String PROSPECT_STATUS_HISTORIES = "prospectStatusHistories";


	private String prospectStatusId;
	private String prospectSourceId;
	private ProspectType prospectType;
	private String prospectTypeId;

	private String recordPersonId;
	private char recordChangeType='N';
	private Date recordChangeDate = new Date();

	private Date statusChangeDate;
	private Date whenAdded = new Date();

	private int nextContactDate;

	public ProspectCompany() {
	}

	@Column(name="status_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	@Column(name = "opportunity_value")
	public double getOpportunityValue() {
		return opportunity_value;
	}

	public void setOpportunityValue(double opportunity_value) {
		this.opportunity_value = opportunity_value;
	}

	@Column(name="when_added")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getWhenAdded() {
		return whenAdded;
	}

	public void setWhenAdded(Date whenAdded) {
		this.whenAdded = whenAdded;
	}

	@Column(name="number_of_employees")
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	@Column(name="gross_income")
	public int getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(int grossIncome) {
		this.grossIncome = grossIncome;
	}

	@Column(name="website")
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prospect_type_id")
	public ProspectType getProspectType() {
		return prospectType;
	}

	public void setProspectType(ProspectType prospectType) {
		this.prospectType = prospectType;
	}

	@Column (name = "prospect_type_id", insertable = false, updatable = false)
	public String getProspectTypeId() {
		return prospectTypeId;
	}

	public void setProspectTypeId(String prospectTypeId) {
		this.prospectTypeId = prospectTypeId;
	}

	@Column(name = "certainty")
    public short getCertainty() {
        return certainty;
    }

    public void setCertainty(short certainty) {
        this.certainty = certainty;
    }

    @Column(name = "first_contact_date")
    public int getFirstContactDate() {
        return firstContactDate;
    }

    public void setFirstContactDate(int firstContactDate) {
        this.firstContactDate = firstContactDate;
    }

/*
	@OneToMany(mappedBy = ProspectStatusHistory.PROSPECT_COMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProspectStatusHistory> getProspectStatusHistories() {
        return this.prospectStatusHistories;
    }

    public void setProspectStatusHistories(final Set<ProspectStatusHistory> prospectStatusHistories) {
        this.prospectStatusHistories = prospectStatusHistories;
    }
 * */
    /*
    public String getObjections() {
    return objections;
    }

    public void setObjections(String objections) {
    this.objections = objections;
    }

    @Column(name="primary_interests")
    public String getPrimaryInterests() {
    return primaryInterests;
    }

    public void setPrimaryInterests(String primaryInterests) {
    this.primaryInterests = primaryInterests;
    }
     */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
	public CompanyDetail getAssociatedCompany() {
		return associatedCompany;
	}

	public void setAssociatedCompany(CompanyDetail associatedCompany) {
		this.associatedCompany = associatedCompany;
	}

	@Column (name = "company_id", insertable = false, updatable = false)
	public String getAssociatedCompanyId() {
		return associatedCompanyId;
	}

	public void setAssociatedCompanyId(String associatedCompanyId) {
		this.associatedCompanyId = associatedCompanyId;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prospect_source_id")
    public ProspectSource getProspectSource() {
        return prospectSource;
    }

    public void setProspectSource(ProspectSource prospectSource) {
		if (prospectSource!=null)
			prospectSourceId=prospectSource.getProspectSourceId();
        this.prospectSource = prospectSource;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prospect_status_id")
    public ProspectStatus getProspectStatus() {
        return prospectStatus;
    }

    public void setProspectStatus(ProspectStatus prospectStatus) {
		if (prospectStatus!=null)
			prospectStatusId=prospectStatus.getProspectStatusId();
        this.prospectStatus = prospectStatus;
    }

    @Column(name = "source_detail")
    public String getSourceDetail() {
        return sourceDetail;
    }

    public void setSourceDetail(String sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    @OneToMany(mappedBy = ProspectLog.ORG_GROUP, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProspectLog> getProspectLogs() {
        return prospectLogs;
    }

    public void setProspectLogs(Set<ProspectLog> prospectLogs) {
        this.prospectLogs = prospectLogs;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    public Employee getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(Employee salesPerson) {
		if (salesPerson!=null)
			salesPersonId=salesPerson.getPersonId();
        this.salesPerson = salesPerson;
    }

    /*
    <property formula="( SELECT MAX (pl.contact_date) FROM prospect_log pl                 WHERE pl.org_group_id =org_group_id )" name="lastLogDate"/>
    <property formula="( SELECT MAX (pl2.contact_time) FROM prospect_log pl2                 WHERE pl2.org_group_id =org_group_id and pl2.contact_date=     (select max(pl3.contact_date) FROM prospect_log pl3                 WHERE pl3.org_group_id =org_group_id ))" name="lastLogTime"/>
     */

	//  Be very careful here.  The following query must exactly match the string in ArahantBeanInterceptor.onPrepareStatement()
    @Formula("( SELECT max(pl.contact_date) FROM prospect_log pl WHERE pl.org_group_id =org_group_id )")
    public int getLastLogDate() {
        if (lastLogDate == null)
            return 0;
        return lastLogDate;
    }

	//  Be very careful here.  The following query must exactly match the string in ArahantBeanInterceptor.onPrepareStatement()
	@Formula("( SELECT max(pl.contact_date) FROM prospect_log pl WHERE pl.org_group_id =org_group_id and pl.sales_activity_result_id is not null)")
    public int getLastLogDateWithResult() {
        if (lastLogDateWithResult == null)
            return 0;
        return lastLogDateWithResult;
    }

    public void setLastLogDate(Integer lastLogDate) {
        this.lastLogDate = lastLogDate;
    }

	public void setLastLogDateWithResult(Integer lastLogDateWithResult) {
        this.lastLogDateWithResult = lastLogDateWithResult;
    }

    @Formula("( SELECT max(pl2.contact_time) FROM prospect_log pl2  WHERE pl2.org_group_id =org_group_id and pl2.contact_date=     (select max(pl3.contact_date) FROM prospect_log pl3   WHERE pl3.org_group_id =org_group_id ))")
    public int getLastLogTime() {
        if (lastLogTime == null) {
            return 0;
        }
        return lastLogTime;
    }

    public void setLastLogTime(Integer lastLogTime) {
        this.lastLogTime = lastLogTime;
    }

	/**
	 * @return Returns the recordChangeDate.
	 */
	@Override
	@Column(name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate()
	{
		return recordChangeDate;
	}

	/**
	 * @param recordChangeDate The recordChangeDate to set.
	 */
	@Override
	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	/**
	 * @return Returns the recordChangePerson.
	 */
	@Override
	@Column(name="record_person_id")
	public String getRecordPersonId()
	{
		return recordPersonId;
	}

	/**
	 * @param recordChangePerson The recordChangePerson to set.
	 */
	@Override
	public void setRecordPersonId(String recordChangePerson) {
		this.recordPersonId = recordChangePerson;
	}

	/**
	 * @return Returns the recordChangeType.
	 */
	@Override
	@Column(name="record_change_type")
	public char getRecordChangeType()
	{
		return recordChangeType;
	}

	/**
	 * @param recordChangeType The recordChangeType to set.
	 */
	@Override
	public void setRecordChangeType(char recordChangeType) {
		this.recordChangeType = recordChangeType;
	}

	@Override
	@Transient
	public String getChangeTypeFormatted() {
		char changeType = getRecordChangeType();
		String changeTypeFormatted = "";

		if (changeType == 'N')
		{
			changeTypeFormatted = "New";
		}
		else if (changeType == 'M')
		{
			changeTypeFormatted = "Modify";
		}
		else if (changeType == 'D')
		{
			changeTypeFormatted = "Delete";
		}

		return changeTypeFormatted;
	}

	@Override
	public ArahantHistoryBean historyObject()
	{
		return new ProspectH();
	}

	@Column(name="prospect_source_id",insertable=false, updatable=false)
	public String getProspectSourceId() {
		return prospectSourceId;
	}

	public void setProspectSourceId(String prospectSourceId) {
		this.prospectSourceId = prospectSourceId;
	}

	@Column(name="prospect_status_id",insertable=false, updatable=false)
	public String getProspectStatusId() {
		return prospectStatusId;
	}

	public void setProspectStatusId(String prospectStatusId) {
		this.prospectStatusId = prospectStatusId;
	}

	@Column(name="person_id",updatable=false,insertable=false)
	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	@Column(name="next_contact_date")
	public int getNextContactDate() {
		return nextContactDate;
	}

	public void setNextContactDate(int nextContactDate) {
		this.nextContactDate = nextContactDate;
	}


	@Column(name="last_contact_date")
	public int getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(int lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	@Override
	public Object keyValue() {
		return getOrgGroupId();
	}


}
