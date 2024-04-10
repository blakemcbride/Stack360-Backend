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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BServiceSubscribed;

public class LoadServiceReturn extends TransmitReturnBase {

	void setData(BServiceSubscribed bs)
	{
		
		name=bs.getServiceName();
		description=bs.getDescription();
		firstActiveDate=bs.getFirstActiveDate();
		lastActiveDate=bs.getLastActiveDate();
                applyToAll=bs.getAllCompanies();
				serviceType = bs.getInterfaceCode();
	}
	
	private String name;
	private String description;
	private int firstActiveDate;
	private int lastActiveDate;
        private boolean applyToAll;
	

	private short serviceType;

	public short getServiceType() {
		return serviceType;
	}

	public void setServiceType(short serviceType) {
		this.serviceType = serviceType;
	}

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

	
