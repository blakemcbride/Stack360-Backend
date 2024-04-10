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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.hr.callLogListParent;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BProjectType;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class ListProjectTypesReturnItem  {
	private String projectTypeId;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	private String codeAndCount;

	public String getCodeAndCount() {
		return codeAndCount;
	}

	public void setCodeAndCount(String codeAndCount) {
		this.codeAndCount = codeAndCount;
	}


	
	public ListProjectTypesReturnItem()
	{
		;
	}

	
	/**
	 * @param type
	 */
	ListProjectTypesReturnItem(final BProjectType pt, BPerson emp) {
		super();
		projectTypeId=pt.getProjectTypeId();
		codeAndCount=pt.getCode();
		code=pt.getCode();
		
		if (emp!=null)
		{
			int count=emp.getProjectTypeCount(pt);
			if (count>0)
				codeAndCount+=" ("+count+")";
		}
	}

	


	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectTypeTransmit#getProjectTypeId()
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectTypeTransmit#setProjectTypeId(java.lang.String)
	 */
	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
}

	
