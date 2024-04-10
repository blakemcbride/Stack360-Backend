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
package com.arahant.services.standard.inventory.productByType;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListProductsAndTypesInput extends TransmitInputBase {

	@Validation (required=false)  
	private String productTypeId;
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	

	public String getProductTypeId()
	{
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId)
	{
		this.productTypeId=productTypeId;
	}

        public SearchMetaInput getSearchMeta() {
            return searchMeta;
        }

        public void setSearchMeta(SearchMetaInput searchMeta) {
            this.searchMeta = searchMeta;
        }



	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);
		} else {
			return new BSearchMetaInput(searchMeta,new String[]{"description"});
		}
	}


}

	
