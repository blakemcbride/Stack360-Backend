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
package com.arahant.services.standard.hr.benefitClass;
import com.arahant.beans.HrBenefit;


/**
 * 
 *
 *
 */
public class ListAvailableBenefitsReturnItem {
	
	public ListAvailableBenefitsReturnItem()
	{
	}

        private String benefitName;
        private String benefitId;


	ListAvailableBenefitsReturnItem (HrBenefit bc)
	{
		
            this.benefitId = bc.getBenefitId();
            this.benefitName = bc.getName();

	}
	
	private String[] item;
	

	public String[] getItem()
	{
		return item;
	}
	public void setItem(String[] item)
	{
		this.item=item;
	}

    /**
     * @return the benefitName
     */
    public String getBenefitName() {
        return benefitName;
    }

    /**
     * @param benefitName the benefitName to set
     */
    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    /**
     * @return the benefitId
     */
    public String getBenefitId() {
        return benefitId;
    }

    /**
     * @param benefitId the benefitId to set
     */
    public void setBenefitId(String benefitId) {
        this.benefitId = benefitId;
    }

   

}

	
