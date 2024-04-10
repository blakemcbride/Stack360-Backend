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
package com.arahant.services.standard.misc.serviceSubscribed;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BServiceSubscribed;
import com.arahant.annotation.Validation;

public class NewServiceInput extends TransmitInputBase {

	void setData(BServiceSubscribed bs)
	{
		
		bs.setServiceName(name);
		bs.setDescription(description);
		bs.setFirstActiveDate(firstActiveDate);
		bs.setLastActiveDate(lastActiveDate);
                bs.setAllCompanies(applyToAll);
                bs.setInterfaceCode(serviceType);
	}
	@Validation (required=true)
	private short serviceType;

	public short getServiceType() {
		return serviceType;
	}

	public void setServiceType(short serviceType) {
		this.serviceType = serviceType;
	}
	@Validation (required=true)
	private String name;
	@Validation (required=true)
	private String description;
	@Validation (type="date", required=true)
	private int firstActiveDate;
	@Validation (type="date", required=true)
	private int lastActiveDate;
        @Validation (required=false)
        private boolean applyToAll;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getFirstActiveDate()
	{
		return firstActiveDate;
	}
	public void setFirstActiveDate(int firstActiveDate)
	{
		this.firstActiveDate=firstActiveDate;
	}
	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}

        public boolean isApplyToAll() {
            return applyToAll;
        }

        public void setApplyToAll(boolean applyToAll) {
            this.applyToAll = applyToAll;
        }
}

	
