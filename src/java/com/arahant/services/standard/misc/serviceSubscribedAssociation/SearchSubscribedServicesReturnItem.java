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
package com.arahant.services.standard.misc.serviceSubscribedAssociation;
import com.arahant.business.BServiceSubscribed;
import com.arahant.business.BServiceSubscribedJoin;
import com.arahant.utils.DateUtils;

public class SearchSubscribedServicesReturnItem {
	
	public SearchSubscribedServicesReturnItem()
	{
		
	}

	SearchSubscribedServicesReturnItem (BServiceSubscribedJoin bs)
	{
		name=bs.getService().getServiceName();
		descriptionPreview=bs.getService().getDescription();
		id=bs.getService().getServiceId();
                beginDate=DateUtils.getDateFormatted(bs.getBeginDate());
                endDate=DateUtils.getDateFormatted(bs.getEndDate());
				externalId = bs.getExternalId();
	}
	
	private String name;
	private String descriptionPreview;
	private String id;
        private String beginDate;
        private String endDate;
	private String externalId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getDescriptionPreview()
	{
		return descriptionPreview;
	}
	public void setDescriptionPreview(String descriptionPreview)
	{
		this.descriptionPreview=descriptionPreview;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
}

	
