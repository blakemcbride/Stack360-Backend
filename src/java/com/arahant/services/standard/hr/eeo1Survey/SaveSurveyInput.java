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


package com.arahant.services.standard.hr.eeo1Survey;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREEO1;

public class SaveSurveyInput extends TransmitInputBase {

	@Validation (table="hr_eeo1",column="eeo1_id",required=true)
	private String id;
	@Validation (table="hr_eeo1",column="date_uploaded",required=false,type="date")
	private int uploadedDate;

	void setData(BHREEO1 eeo1)
	{

        eeo1.setUploadedDate(uploadedDate);

	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(int uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}

	
