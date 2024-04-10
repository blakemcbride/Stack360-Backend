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
 * Created on Feb 7, 2007
 * 
 */
package com.arahant.services.peachtreeApp;

import com.arahant.business.BService;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 7, 2007
 *
 */
public class ListProductsAndServicesReturn extends TransmitReturnBase {
	private Products [] pst;

	/**
	 * @return Returns the pst.
	 */
	public Products[] getPst() {
		return pst;
	}

	/**
	 * @param pst The pst to set.
	 */
	public void setPst(final Products[] pst) {
		this.pst = pst;
	}

	/**
	 * @param products
	 */
	void setPst(final BService[] products) {
		pst=new Products[products.length];
		for (int loop=0;loop<pst.length;loop++)
			pst[loop]=new Products(products[loop]);
	}
}

	
