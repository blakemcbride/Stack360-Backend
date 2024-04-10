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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.aqDevImport;
import com.arahant.beans.CompanyBase;
import com.arahant.business.BCompanyBase;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class Companies extends TransmitReturnBase 
{


	public Companies() {
		super();
	}

		
//		 Fields    

		private String orgGroupId;
		private String name;
		
		private String identifier;
	
		/**
		 * @param base
		 */
		Companies(final BCompanyBase b) {
			super();
			 orgGroupId=b.getOrgGroupId();
			 name=b.getName();
                         identifier=b.getIdentifier();
		}

    Companies(CompanyBase companyBase) {
        name=companyBase.getName();
        orgGroupId=companyBase.getOrgGroupId();
        identifier=companyBase.getIdentifier();
        
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

	
    
    
    
//		 Property accessors
		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.ICompanyTransmit#getCompanyId()
		 */
		public String getCompanyId() {
			return this.orgGroupId;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.ICompanyTransmit#setCompanyId(java.lang.String)
		 */
		public void setCompanyId(final String companyId) {
			this.orgGroupId = companyId;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.ICompanyTransmit#getName()
		 */
		public String getName() {
			return this.name;
		}

		/* (non-Javadoc)
		 * @see com.arahant.operations.transmit.ICompanyTransmit#setName(java.lang.String)
		 */
		public void setName(final String name) {
			this.name = name;
		}

	
		/**
		 * @return Returns the orgGroupId.
		 */
		public String getOrgGroupId() {
			return orgGroupId;
		}



		/**
		 * @param orgGroupId The orgGroupId to set.
		 */
		public void setOrgGroupId(final String orgGroupId) {
			this.orgGroupId = orgGroupId;
		}


}

	
