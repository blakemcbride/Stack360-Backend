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
package com.arahant.services.standard.time.timesheetEntryByWeek;
import com.arahant.business.BProjectType;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class SearchProjectTypesReturnItem {


	public SearchProjectTypesReturnItem() {
		super();
	}


	
		private String projectTypeId;

		private String code;

		private String description;


		/**
		 * @param type
		 */
		SearchProjectTypesReturnItem(final BProjectType pt) {
			super();
			projectTypeId=pt.getProjectTypeId();
			code=pt.getCode();
			description=pt.getDescription();
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectTypeTransmit#getCode()
		 */
		public String getCode() {
			return code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectTypeTransmit#setCode(java.lang.String)
		 */
		public void setCode(final String code) {
			this.code = code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectTypeTransmit#getDescription()
		 */
		public String getDescription() {
			return description;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectTypeTransmit#setDescription(java.lang.String)
		 */
		public void setDescription(final String description) {
			this.description = description;
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

	
