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
package com.arahant.services.standard.project.projectTimesheetReport;
import com.arahant.beans.ProjectStatus;
import com.arahant.business.BProjectStatus;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class SearchProjectStatusesReturnItem {


	public SearchProjectStatusesReturnItem() {
		super();
	}


	
		private String projectStatusId;
		private String code;
		private String description;


		/**
		 * @param status
		 */
		SearchProjectStatusesReturnItem(final BProjectStatus s) {
			super();
			projectStatusId=s.getProjectStatusId();
			code=s.getCode();
			description=s.getDescription();
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#makeProjectStatus(com.arahant.beans.ProjectStatus)
		 */
		public ProjectStatus makeProjectStatus(final ProjectStatus ps)
		{
			ps.setCode(code);
			ps.setDescription(description);
			ps.setProjectStatusId(projectStatusId);
			
			return ps;
		}
		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getCode()
		 */
		public String getCode() {
			return code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setCode(java.lang.String)
		 */
		public void setCode(final String code) {
			this.code = code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getDescription()
		 */
		public String getDescription() {
			return description;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setDescription(java.lang.String)
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getProjectStatusId()
		 */
		public String getProjectStatusId() {
			return projectStatusId;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setProjectStatusId(java.lang.String)
		 */
		public void setProjectStatusId(final String projectStatusId) {
			this.projectStatusId = projectStatusId;
		}
}

	
