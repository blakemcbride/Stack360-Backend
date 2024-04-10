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
 *
 */

package com.arahant.business;

import com.arahant.beans.Education;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BEducation extends SimpleBusinessObjectBase<Education> {

	public BEducation()
	{
		super();
	}

	public BEducation(String id)
	{
		super(id);
	}

	public BEducation(Education o)
	{
		super();
		bean=o;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BEducation(id).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		super.delete();
	}

	public String getEducationId() {
		return bean.getEducationId();
	}

	public int getEndDate() {
		return bean.getEndDate();
	}

	public void setEndDate(int endDate) {
		bean.setEndDate(endDate);
	}

	public char getGraduate() {
		return bean.getGraduate();
	}

	public void setGraduate(char graduate) {
		bean.setGraduate(graduate);
	}

	public String getOtherType() {
		return bean.getOtherType();
	}

	public void setOtherType(String otherType) {
		bean.setOtherType(otherType);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getSchoolLocation() {
		return bean.getSchoolLocation();
	}

	public void setSchoolLocation(String schoolLocation) {
		bean.setSchoolLocation(schoolLocation);
	}

	public String getSchoolName() {
		return bean.getSchoolName();
	}

	public void setSchoolName(String schoolName) {
		bean.setSchoolName(schoolName);
	}

	public char getSchoolType() {
		return bean.getSchoolType();
	}

	public void setSchoolType(char schoolType) {
		bean.setSchoolType(schoolType);
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public String getSubject() {
		return bean.getSubject();
	}

	public void setSubject(String subject) {
		bean.setSubject(subject);
	}

	public Short getGpa() {
		return bean.getGpa();
	}

	public void setGpa(Short gpa) {
		bean.setGpa(gpa);
	}

	public Character getCurrent() {
		return bean.getCurrent();
	}

	public void setCurrent(Character current) {
		bean.setCurrent(current);
	}

	static BEducation[] makeArray(List<Education> l)
	{
		BEducation[] ret=new BEducation[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BEducation(l.get(loop));
		return ret;
	}

	public static BEducation[] list(String empId, int max)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(Education.class)
				.orderBy(Education.START_DATE)
				.setMaxResults(max)
				.eq(Education.PERSON, new BPerson(empId).getPerson())
				.list());
	}

	@Override
	public String create() throws ArahantException {
		bean=new Education();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(Education.class, key);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException
	{
		ArahantSession.getHSU().insert(bean);
		super.insert();
	}

}
