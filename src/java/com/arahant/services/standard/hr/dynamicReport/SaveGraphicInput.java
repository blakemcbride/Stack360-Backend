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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BReportGraphic;


public class SaveGraphicInput extends TransmitInputBase {

	@Validation (required = true)
	private String graphicId;
	@Validation (required = true)
	private String description;
	@Validation (required = false)
	private double xPos;
	@Validation (required = false)
	private double yPos;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGraphicId() {
		return graphicId;
	}

	public void setGraphicId(String graphicId) {
		this.graphicId = graphicId;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public SaveGraphicInput() {
	}

	public void setData(BReportGraphic brg) {
		brg.setDescription(description);
		brg.setXPos(xPos);
		brg.setYPos(yPos);
	}
}

	
