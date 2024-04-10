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
package com.arahant.services.standard.hr.personHistory;
import com.arahant.business.BPerson;
import com.arahant.business.BPersonH;
import com.arahant.utils.DateUtils;

public class SearchPersonHistoryReturnItem {
	
	public SearchPersonHistoryReturnItem()
	{

	}

	SearchPersonHistoryReturnItem (BPersonH bch)
	{
		personName=bch.getNameLFM();
		changeDate=bch.getRecordChangeDateFormatted();
		changePerson=new BPerson(bch.getRecordPersonId()).getNameLFM();
		changeType = bch.getRecordChangeType()=='N'?"New":(bch.getRecordChangeType()=='M'?"Modify":"Delete");
		historyId = bch.getHistoryId();
	}
	
	private String personName;
	private int changeDate;
	private String changePerson;
	private String changeType;
	private String historyId;

	SearchPersonHistoryReturnItem(String personId) 
	{
		BPerson bp = new BPerson(personId);
		personName=bp.getNameLFM();
		changeDate=DateUtils.getDate(bp.getPerson().getRecordChangeDate());
		changePerson=new BPerson(bp.getPerson().getRecordPersonId()).getNameLFM();
		changeType = bp.getRecordType()=='R'?"Current Record":"Change Request";
		historyId = "";
	}
	

	public String getPersonName()
	{
		return personName;
	}
	public void setPersonName(String personName)
	{
		this.personName=personName;
	}
	public int getChangeDate()
	{
		return changeDate;
	}
	public void setChangeDate(int changeDate)
	{
		this.changeDate=changeDate;
	}
	public String getChangePerson()
	{
		return changePerson;
	}
	public void setChangePerson(String changePerson)
	{
		this.changePerson=changePerson;
	}
	public String getChangeType()
	{
		return changeType;
	}
	public void setChangeType(String changeType)
	{
		this.changeType=changeType;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
}

	
