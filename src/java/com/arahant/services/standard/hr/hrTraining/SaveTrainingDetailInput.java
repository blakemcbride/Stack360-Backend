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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrTraining;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRTrainingDetail;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class SaveTrainingDetailInput extends TransmitInputBase {

	@Validation (required=true)
	private String trainingCategoryId;
	@Validation (type="date",required=true)
	private int trainingDate;
	@Validation (type="date",required=false)
	private int expireDate;
	@Validation (min=.01,required=true)
	private float trainingHours;
	@Validation (required=true)
	private String trainingDetailId;

	/**
	 * @return Returns the trainingDetailId.
	 */
	public String getTrainingDetailId() {
		return trainingDetailId;
	}

	/**
	 * @param trainingDetailId The trainingDetailId to set.
	 */
	public void setTrainingDetailId(final String trainingDetailId) {
		this.trainingDetailId = trainingDetailId;
	}

	public SaveTrainingDetailInput() {
		super();
	}


	/**
	 * @return Returns the expireDate.
	 */
	public int getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate The expireDate to set.
	 */
	public void setExpireDate(final int expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return Returns the trainingCategoryId.
	 */
	public String getTrainingCategoryId() {
		return trainingCategoryId;
	}

	/**
	 * @param trainingCategoryId The trainingCategoryId to set.
	 */
	public void setTrainingCategoryId(final String trainingCategoryId) {
		this.trainingCategoryId = trainingCategoryId;
	}

	/**
	 * @return Returns the trainingDate.
	 */
	public int getTrainingDate() {
		return trainingDate;
	}

	/**
	 * @param trainingDate The trainingDate to set.
	 */
	public void setTrainingDate(final int trainingDate) {
		this.trainingDate = trainingDate;
	}

	/**
	 * @return Returns the trainingHours.
	 */
	public float getTrainingHours() {
		return trainingHours;
	}

	/**
	 * @param trainingHours The trainingHours to set.
	 */
	public void setTrainingHours(final float trainingHours) {
		this.trainingHours = trainingHours;
	}

	/**
	 * @param td
	 */
	void setData(final BHRTrainingDetail td) {
		td.setExpireDate(expireDate);
		td.setTrainingCategoryId(trainingCategoryId);
		td.setTrainingDate(trainingDate);
		td.setTrainingHours(trainingHours);
	}
}

	
