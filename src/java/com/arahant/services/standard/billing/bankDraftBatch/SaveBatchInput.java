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
 *
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.billing.bankDraftBatch;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BBillingBatch;
import com.arahant.utils.ArahantSession;

public class SaveBatchInput extends TransmitInputBase {
	@Validation (required=false)
	private boolean allCompanies;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=true)
	private String description;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	void setData(BBillingBatch bc)
	{
		bc.setDescription(description);
		if (allCompanies)
			bc.setCompany(null);
		else
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id=id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description=description;
	}
}

	
