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
import com.arahant.beans.PreviousEmployment;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BPreviousEmployment extends SimpleBusinessObjectBase<PreviousEmployment> {

	public BPreviousEmployment() {
	}

	public BPreviousEmployment(String id) {
		super(id);
	}

	public BPreviousEmployment(PreviousEmployment o) {
		bean = o;
	}

	public String getEmploymentId() {
		return bean.getEmploymentId();
	}

	public String getCompany() {
		return bean.getCompany();
	}

	public void setCompany(String company) {
		bean.setCompany(company);
	}

	public char getContactSupervisor() {
		return bean.getContactSupervisor();
	}

	public void setContactSupervisor(char contactSupervisor) {
		bean.setContactSupervisor(contactSupervisor);
	}

	public int getEndDate() {
		return bean.getEndDate();
	}

	public void setEndDate(int endDate) {
		bean.setEndDate(endDate);
	}

	public String getJobTitle() {
		return bean.getJobTitle();
	}

	public void setJobTitle(String jobTitle) {
		bean.setJobTitle(jobTitle);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getPhone() {
		return bean.getPhone();
	}

	public void setPhone(String phone) {
		bean.setPhone(phone);
	}

	public String getReasonForLeaving() {
		return bean.getReasonForLeaving();
	}

	public void setReasonForLeaving(String reasonForLeaving) {
		bean.setReasonForLeaving(reasonForLeaving);
	}

	public String getResponsibilities() {
		return bean.getResponsibilities();
	}

	public void setResponsibilities(String responsibilities) {
		bean.setResponsibilities(responsibilities);
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public int getStartingSalary() {
		return bean.getStartingSalary();
	}

	public int getEndingSalary() {
		return bean.getEndingSalary();
	}

	public void setEndingSalary(int endingSalary) {
		bean.setEndingSalary(endingSalary);
	}

	public void setStartingSalary(int startingSalary) {
		bean.setStartingSalary(startingSalary);
	}

	public String getSupervisor() {
		return bean.getSupervisor();
	}

	public void setSupervisor(String supervisor) {
		bean.setSupervisor(supervisor);
	}

	public String getStreet() {
		return bean.getStreet();
	}

    public void setStreet(String street) {
		bean.setStreet(street);
	}

	public String getCity() {
		return bean.getCity();
	}

	public void setCity(String city) {
		bean.setCity(city);
	}

	public String getState() {
        return bean.getState();
	}

	public void setState(String state) {
		bean.setState(state);
	}

	static BPreviousEmployment[] makeArray(List<PreviousEmployment> l) {
		BPreviousEmployment[] ret = new BPreviousEmployment[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPreviousEmployment(l.get(loop));
		return ret;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BPreviousEmployment(id).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		super.delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new PreviousEmployment();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PreviousEmployment.class, key);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
		super.insert();
	}

	public static BPreviousEmployment[] list(String empId, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(PreviousEmployment.class)
				.orderBy(PreviousEmployment.START_DATE)
				.setMaxResults(max)
				.eq(PreviousEmployment.PERSON, new BPerson(empId).getPerson())
				.list());
	}
}
