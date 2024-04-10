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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.projectCategory;
import com.arahant.beans.ProjectCategory;
import com.arahant.services.TransmitReturnBase;


/**  
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProjectCategoryReturn extends TransmitReturnBase  {

	public SaveProjectCategoryReturn() {
		super();
	}

  

		private String projectCategoryId;

		private String code;

		private String description;

		/**
		 * @param category
		 */
		public SaveProjectCategoryReturn(final ProjectCategory category) {
			projectCategoryId=category.getProjectCategoryId();
			code=category.getCode();
			description=category.getDescription();
		}
		
		
		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#makeProjectCategory(com.arahant.beans.ProjectCategory)
		 */
		public ProjectCategory makeProjectCategory(final ProjectCategory pc)
		{
			pc.setCode(code);
			pc.setDescription(description);
			pc.setProjectCategoryId(projectCategoryId);
			return pc;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#getCode()
		 */
		public String getCode() {
			return code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#setCode(java.lang.String)
		 */
		public void setCode(final String code) {
			this.code = code;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#getDescription()
		 */
		public String getDescription() {
			return description;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#setDescription(java.lang.String)
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#getProjectCategoryId()
		 */
		public String getProjectCategoryId() {
			return projectCategoryId;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.IProjectCategoryTransmit#setProjectCategoryId(java.lang.String)
		 */
		public void setProjectCategoryId(final String projectCategoryId) {
			this.projectCategoryId = projectCategoryId;
		}
}

	
