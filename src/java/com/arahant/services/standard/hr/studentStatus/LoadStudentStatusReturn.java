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
package com.arahant.services.standard.hr.studentStatus;

import com.arahant.beans.StudentVerification;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPerson;
import com.arahant.business.BStudentVerification;
import com.arahant.utils.ArahantSession;
import java.util.List;


/**
 * 
 *
 *
 */
public class LoadStudentStatusReturn extends TransmitReturnBase {

	void setData(BPerson bc)
	{
		
		termType=bc.getTermType();
		student=bc.getStudent();

		List<StudentVerification> l=ArahantSession.getHSU().createCriteria(StudentVerification.class)
			.eq(StudentVerification.PERSON, bc.getPerson())
			.orderBy(StudentVerification.YEAR)
			.orderBy(StudentVerification.TERM)
			.list();

		item=new LoadStudentStatusReturnItem[l.size()];
		for (int loop=0;loop<item.length;loop++)
		{
			item[loop]=new LoadStudentStatusReturnItem(new BStudentVerification(l.get(loop)));
		}
	}
	
	private String termType;
	private boolean student;
	private LoadStudentStatusReturnItem []item;
	

	public String getTermType()
	{
		return termType;
	}
	public void setTermType(String termType)
	{
		this.termType=termType;
	}
	public boolean getStudent()
	{
		return student;
	}
	public void setStudent(boolean student)
	{
		this.student=student;
	}

	public LoadStudentStatusReturnItem[] getItem() {
		return item;
	}

	public void setItem(LoadStudentStatusReturnItem[] item) {
		this.item = item;
	}

}

	
