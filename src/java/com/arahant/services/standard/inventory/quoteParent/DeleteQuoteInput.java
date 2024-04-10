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
package com.arahant.services.standard.inventory.quoteParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class DeleteQuoteInput extends TransmitInputBase {

	@Validation(table="quote_table", column="quote_id", required=true)
	private String[] quoteIds;

	public String[] getQuoteIds() {
		return quoteIds;
	}

	public void setQuoteIds(String[] quoteIds) {
		this.quoteIds = quoteIds;
	}

	
}

	