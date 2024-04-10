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
package com.arahant.services.scanner;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPersonForm;
import com.arahant.utils.Base64;

import java.io.IOException;

public class GetPersonFormReturn extends TransmitReturnBase {

	void setData(BPersonForm bc) throws IOException {
		formData=Base64.encodeBytes(bc.getFormData());
	}
	
	private String formData;

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}
	
/*	
	private byte [] formData;
;

	public byte [] getFormData()
	{
		return formData;
	}
	public void setFormData(byte [] formData)
	{
		this.formData=formData;
	}
*/
}

	
