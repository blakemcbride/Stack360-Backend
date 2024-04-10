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

import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.IDGenerator;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = Timesheet.TABLE_NAME)
public class Timesheet extends ArahantBean implements java.io.Serializable, Comparable<Timesheet>, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "timesheet";
	// Fields
	private String timesheetId;
	public static final String TIMESHEETID = "timesheetId";
	private Message message;
	public static final String MESSAGE = "message";
	private InvoiceLineItem invoiceLineItem;
	public static final String INVOICELINEITEM = "invoiceLineItem";
	private ProjectShift projectShift;
	public static final String PROJECTSHIFT = "projectShift";
	public static final String PROJECT_SHIFT_ID = "projectShiftId";
	private Person person;
	public static final String PERSON = "person";
	private String personId;
	public static final String PERSON_ID = "personId";
	private String description;
	public static final String DESCRIPTION = "description";
	private String privateDescription;
	public static final String PRIVATE_DESCRIPTION = "privateDescription";
	private Date beginningEntryDate;
	public static final String BEGINNING_ENTRY_DATE = "beginningEntryDate";
	public static final String ENDDATE = "endDate";
	private Date endEntryDate;
	private int endDate;
	private Person beginningEntryPerson;
	private Person endingEntryPerson;
	private double totalHours;
	public static final String TOTALHOURS = "totalHours";
	private int workDate;
	public static final String WORKDATE = "workDate";
	private int beginningTime = -1;
	public static final String BEGINNINGTIME = "beginningTime";
	private int endTime = -1;
	public static final String ENDTIME = "endTime";
	private char billable = 'U';
	public static final String BILLABLE = "billable";
	private char state;
	public static final String STATE = "state";
//    private Set<HrAccrual> hrAccruals = new HashSet<HrAccrual>(0);
//    public static final String HRACCRUALS = "hrAccruals";
	private String projectShiftId;
//	private char scope;
	public static final String WAGE_TYPE = "wageType";
	private WageType wageType;
	public static final String BEGINNING_TIMEZONE_OFFSET = "beginningTimeZoneOffset";
	private int beginningTimeZoneOffset;
	public static final String END_TIMEZONE_OFFSET = "endTimeZoneOffset";
	private int endTimeZoneOffset = 2000;
	private float totalExpenses;
	private double fixedPay = 0.0;
    private static TimeType defaultTimeType;
    private TimeType timeType = defaultTimeType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}

	@Column(name = "beginning_tz_offset")
	public int getBeginningTimeZoneOffset() {
		return beginningTimeZoneOffset;
	}

	public void setBeginningTimeZoneOffset(int beginningTimeZoneOffset) {
		this.beginningTimeZoneOffset = beginningTimeZoneOffset;
	}

	@Column(name = "end_tz_offset")
	public int getEndTimeZoneOffset() {
		return endTimeZoneOffset;
	}

	public void setEndTimeZoneOffset(int endTimeZoneOffset) {
		this.endTimeZoneOffset = endTimeZoneOffset;
	}

	@Column(name = "project_shift_id", insertable = false, updatable = false)
	public String getProjectShiftId() {
		return projectShiftId;
	}

	public void setProjectShiftId(final String projectShiftId) {
		this.projectShiftId = projectShiftId;
		//  the following trick does not work in Hibernate
//		this.setProjectShift(new BProjectShift(projectShiftId).getProjectShift());
	}

	/**
	 * default constructor
	 */
	public Timesheet() {
	}
    
    /*
         This convoluted way of initializing this variable seems to be required 
         because of some initialization sequence issue having to do with Hibernate.
    */
    public static void setupDefaultTimeType() {
        if (defaultTimeType == null) {
            defaultTimeType = ArahantSession.getHSU().createCriteria(TimeType.class).eq(TimeType.DEFAULT_TYPE, 'Y').first();
            if (defaultTimeType == null)
                defaultTimeType = ArahantSession.getHSU().get(TimeType.class, "00001-0000000001");
        }
    }

	// Property accessors
	@Id
	@Column(name = "timesheet_id")
	public String getTimesheetId() {
		return this.timesheetId;
	}

	public void setTimesheetId(final String timesheetId) {
		this.timesheetId = timesheetId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "message_id")
	public Message getMessage() {
		return this.message;
	}

	public void setMessage(final Message message) {
		this.message = message;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_line_item_id")
	public InvoiceLineItem getInvoiceLineItem() {
		return this.invoiceLineItem;
	}

	public void setInvoiceLineItem(final InvoiceLineItem invoiceLineItem) {
		this.invoiceLineItem = invoiceLineItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_shift_id")
	public ProjectShift getProjectShift() {
		return this.projectShift;
	}

	public void setProjectShift(final ProjectShift projectShift) {
		this.projectShift = projectShift;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Column(name = "private_description")
	public String getPrivateDescription() {
		return privateDescription;
	}

	public void setPrivateDescription(String privateDescription) {
		this.privateDescription = privateDescription;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Column(name = "beginning_entry_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getBeginningEntryDate() {
		return this.beginningEntryDate;
	}

	public void setBeginningEntryDate(final Date entryDate) {
		this.beginningEntryDate = entryDate;
	}

	@Column(name = "total_hours")
	public double getTotalHours() {
		return this.totalHours;
	}

	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	@Column(name = "beginning_date")
	public int getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(final int workDate) {
		this.workDate = workDate;
	}

	@Column(name = "beginning_time")
	public int getBeginningTime() {
		return this.beginningTime;
	}

	public void setBeginningTime(final int beginningTime) {
		this.beginningTime = beginningTime;
	}

	@Column(name = "end_time")
	public int getEndTime() {
		return this.endTime;
	}

	public void setEndTime(final int endTime) {
		this.endTime = endTime;
	}

	@Column(name = "billable")
	public char getBillable() {
		return this.billable;
	}

	public void setBillable(final char billable) {
		this.billable = billable;
	}

	@Column(name = "entry_state")
	public char getState() {
		return this.state;
	}

	public void setState(final char state) {
		this.state = state;
	}

	@Column(name = "total_expenses")
	public float getTotalExpenses() {
		return totalExpenses;
	}

	public void setTotalExpenses(float totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	@Column(name = "fixed_pay")
    public double getFixedPay() {
        return fixedPay;
    }

    public void setFixedPay(double fixedPay) {
        this.fixedPay = fixedPay;
    }
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "time_type_id")
	public TimeType getTimeType() {
		return this.timeType;
	}

	public void setTimeType(final TimeType timeType) {
		String oldId = "";
		String newId = "";
		if (timeType != null)
			newId = timeType.getTimeTypeId();
		if (this.timeType != null)
			oldId = this.timeType.getTimeTypeId();
		firePropertyChange("timeTypeId", oldId, newId);
		this.timeType = timeType;
	}
    
	@Transient
	public String getTimeTypeId() {
		if (timeType == null)
			return "";
		return timeType.getTimeTypeId();
	}

	public void setTimeTypeId(String timeTypeId) {
        setTimeType(ArahantSession.getHSU().get(TimeType.class, timeTypeId));
	}

//    @OneToMany(mappedBy = HrAccrual.TIMESHEET, fetch = FetchType.LAZY)
//    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
//    public Set<HrAccrual> getHrAccruals() {
//        return this.hrAccruals;
//    }
//
//    public void setHrAccruals(final Set<HrAccrual> hrAccruals) {
//        this.hrAccruals = hrAccruals;
//    }

	@Override
	public String keyColumn() {
		return "timesheet_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setTimesheetId(IDGenerator.generate(this));
		return timesheetId;
	}

	@Override
	public int compareTo(Timesheet o) {
		if (o == null)
			return -1;
		if (workDate - o.workDate != 0)
			return workDate - o.workDate;
		if (beginningTime != o.beginningTime)
			return beginningTime - o.beginningTime;
		String n1 = getPerson().getNameLFM();
		String n2 = o.getPerson().getNameLFM();
		return n1.compareTo(n2);
	}

	@Override
	public boolean equals(Object o) {
		if (timesheetId == null && o == null)
			return true;
		if (timesheetId != null && o instanceof Timesheet)
			return timesheetId.equals(((Timesheet) o).getTimesheetId());

		return false;
	}

	@Override
	public int hashCode() {
		if (timesheetId == null)
			return 0;
		return timesheetId.hashCode();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beginning_entry_person_id")
	public Person getBeginningEntryPerson() {
		return beginningEntryPerson;
	}

	public void setBeginningEntryPerson(Person beginningEntryPerson) {
		this.beginningEntryPerson = beginningEntryPerson;
	}

	@Column(name = "end_date")
	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	@Column(name = "end_entry_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndEntryDate() {
		return endEntryDate;
	}

	public void setEndEntryDate(Date endEntryDate) {
		this.endEntryDate = endEntryDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "end_entry_person_id")
	public Person getEndingEntryPerson() {
		return endingEntryPerson;
	}

	public void setEndingEntryPerson(Person endingEntryPerson) {
		this.endingEntryPerson = endingEntryPerson;
	}

	@Override
	public String notifyId() {
		return timesheetId;
	}

	@Override
	public String notifyClassName() {
		return "Timesheet";
	}

	@Override
	public Object clone() {
		Timesheet ts = new Timesheet();
		ts.generateId();
		ts.setBeginningEntryPerson(beginningEntryPerson);
		ts.setBeginningTime(beginningTime);
		ts.setBillable(billable);
		ts.setDescription(description);
		ts.setEndDate(endDate);
		ts.setEndEntryDate(endEntryDate);
		ts.setEndTime(endTime);
		ts.setEndingEntryPerson(endingEntryPerson);
		ts.setBeginningEntryDate(beginningEntryDate);
		//ts.setHrAccruals(hrAccruals);
		ts.setInvoiceLineItem(invoiceLineItem);
		ts.setMessage(message);
		ts.setPerson(person);
		ts.setProjectShift(projectShift);
		//  ts.setScope(scope);
		ts.setState(state);
		ts.setTotalHours(totalHours);
		ts.setWageType(wageType);
		ts.setWorkDate(workDate);
		ts.setBeginningTimeZoneOffset(beginningTimeZoneOffset);
		ts.setEndTimeZoneOffset(endTimeZoneOffset);
        ts.setTotalExpenses(totalExpenses);
        ts.setFixedPay(fixedPay);
		return ts;
	}
}
