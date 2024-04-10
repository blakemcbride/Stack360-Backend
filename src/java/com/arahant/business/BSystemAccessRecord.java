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

import com.arahant.beans.Person;
import com.arahant.beans.SystemAccessRecord;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Date;
import java.util.List;

public class BSystemAccessRecord extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BSystemAccessRecord.class);
	private SystemAccessRecord systemAccessRecord;

	public BSystemAccessRecord(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BSystemAccessRecord() {
	}

	public BSystemAccessRecord(final SystemAccessRecord sar) {
		systemAccessRecord = sar;
	}

	public List<SystemAccessRecord> getRecordsForPersonId(String personId) {
		return getRecordsForPersonId(personId, 0, 0);
	}

	public List<SystemAccessRecord> getRecordsForPersonId(String personId, int startingDate, int endingDate) {
		try {
			HibernateCriteriaUtil<SystemAccessRecord> recordQuery = ArahantSession.getHSU().createCriteria(SystemAccessRecord.class);
			recordQuery.eq(SystemAccessRecord.PERSON_ID, personId);
			recordQuery.orderBy(SystemAccessRecord.ENTRY_DATE);

			if (startingDate > 0 && endingDate > 0) {
				recordQuery.dateAfter(SystemAccessRecord.ENTRY_DATE, DateUtils.getDate(startingDate));
				recordQuery.dateBefore(SystemAccessRecord.ENTRY_DATE, DateUtils.getDate(endingDate));
			} else if (startingDate > 0 && endingDate <= 0)
				recordQuery.dateAfter(SystemAccessRecord.ENTRY_DATE, DateUtils.getDate(startingDate));
			else if (startingDate <= 0 && endingDate > 0)
				recordQuery.dateBefore(SystemAccessRecord.ENTRY_DATE, DateUtils.getDate(endingDate));

			List<SystemAccessRecord> personList = recordQuery.list();
			return personList;

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return null;
	}

	public String getAccessRecordId() {
		return systemAccessRecord.getAccessRecordId();
	}

	public void setAccessRecordId(String accessRecordId) {
		this.systemAccessRecord.setAccessRecordId(accessRecordId);
	}

	public Person getPerson() {
		return systemAccessRecord.getPerson();
	}

	public void setPerson(Person person) {
		this.systemAccessRecord.setPerson(person);
	}

	public String getPersonId() {
		return this.systemAccessRecord.getPerson().getPersonId();
	}

	public void setPersonId(String personId) {
		this.systemAccessRecord.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public Date getEntryDate() {
		return systemAccessRecord.getEntryDate();
	}

	public void setEntryDate(Date entryDate) {
		this.systemAccessRecord.setEntryDate(entryDate);
	}

	public String getWebmethodName() {
		return systemAccessRecord.getWebmethodName();
	}

	public void setWebmethodName(String webmethodName) {
		this.systemAccessRecord.setWebmethodName(webmethodName);
	}

	public String getClassName() {
		return systemAccessRecord.getClassName();
	}

	public void setClassName(String className) {
		this.systemAccessRecord.setClassName(className);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(systemAccessRecord);
	}

	@Override
	public void insert() throws ArahantException {

		ArahantSession.getHSU().insert(systemAccessRecord);
		ArahantSession.getHSU().commitTransaction();

	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(systemAccessRecord);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public String create() throws ArahantException {

		systemAccessRecord = new SystemAccessRecord();

		return systemAccessRecord.generateId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		systemAccessRecord = ArahantSession.getHSU().get(SystemAccessRecord.class, key);
		if (systemAccessRecord != null)
			System.out.println(systemAccessRecord.getEntryDate());
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void main(String[] args) {
		/*
		 BSystemAccessRecord bs = new BSystemAccessRecord();  // Saves to the database
		 bs.create();
		 bs.setWebmethodName("TestWebMethod");

		 bs.setPerson("00001-0000000005");
		 bs.setScreenId("00001-0000000067");
		 bs.insert();*/

		BSystemAccessRecord bs = new BSystemAccessRecord();  // load from database
		bs.load("00001-0000000130");
		//return BEmployee.makeArray(ArahantSession.getHSU().createCriteria(Employee.class).list())
	}
}
