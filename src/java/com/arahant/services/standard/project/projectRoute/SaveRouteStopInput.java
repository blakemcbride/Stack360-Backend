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
package com.arahant.services.standard.project.projectRoute;
import com.arahant.annotation.Validation;

import com.arahant.business.BRouteStop;
import com.arahant.business.BRouteStopChecklist;
import com.arahant.services.TransmitInputBase;
import java.util.ArrayList;
import java.util.List;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveRouteStopInput extends TransmitInputBase {

	void setData(final BRouteStop bc)
	{
		
		bc.setRouteStopId(routeStopId);
		bc.setDescription(routeStopName);
		if (statusIds==null)
			statusIds=new String[0];
		bc.setProjectStatusIds(statusIds);
		bc.setPhaseId(phaseId);
		bc.setAutoAssignToSupervisors(autoAssignToSupervisors);
		
		getCheckListItems();//make sure it's not null
		
		//need to clear the ones being deleted
		List <String> keepIds=new ArrayList<String>();
		
		for (SaveRouteStopInputItem i : checkListItems)
			keepIds.add(i.getId());
		
		bc.clearChecklist(keepIds);
	
		for (int loop=0;loop<checkListItems.length;loop++)
			if (checkListItems[loop].getId()==null || "".equals(checkListItems[loop].getId()))
				bc.addCheckListItem(checkListItems[loop].getActiveDate(),
						checkListItems[loop].getInactiveDate(),
						checkListItems[loop].getDescription(),checkListItems[loop].getDetail(),
						checkListItems[loop].getRequired(), loop);
			else
			{
				BRouteStopChecklist brsc=new BRouteStopChecklist(checkListItems[loop].getId());
				brsc.setActiveDate(checkListItems[loop].getActiveDate());
				brsc.setDescription(checkListItems[loop].getDescription());
				brsc.setDetail(checkListItems[loop].getDetail());
				brsc.setInactiveDate(checkListItems[loop].getInactiveDate());
				brsc.setPriority(loop);
				brsc.setRequired(checkListItems[loop].getRequired());
				brsc.update();
			}

	}
	
	@Validation (table="route_stop",column="route_stop_id",required=true)
	private String routeStopId;
	@Validation (table="route_stop",column="description",required=true)
	private String routeStopName;
	@Validation (required=false)
	private String []statusIds;
	@Validation (required=true)
	private String phaseId;
	@Validation (required=false)
	private boolean autoAssignToSupervisors;
	@Validation (required=false)
	private SaveRouteStopInputItem[] checkListItems;

	public SaveRouteStopInputItem[] getCheckListItems() {
		if (checkListItems==null)
			checkListItems=new SaveRouteStopInputItem[0];
		return checkListItems;
	}

	public void setCheckListItems(SaveRouteStopInputItem[] checkListItems) {
		this.checkListItems = checkListItems;
	}
	
	

	public boolean getAutoAssignToSupervisors() {
		return autoAssignToSupervisors;
	}

	public void setAutoAssignToSupervisors(boolean autoAssignToSupervisors) {
		this.autoAssignToSupervisors = autoAssignToSupervisors;
	}

	
	
	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}
	
	

	public String[] getStatusIds() {
		if (statusIds==null)
			return new String[0];
		return statusIds;
	}

	public void setStatusIds(String[] statusIds) {
		this.statusIds = statusIds;
	}

	
	public String getRouteStopId()
	{
		return routeStopId;
	}
	public void setRouteStopId(final String routeStopId)
	{
		this.routeStopId=routeStopId;
	}
	public String getRouteStopName()
	{
		return routeStopName;
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}

}

	
