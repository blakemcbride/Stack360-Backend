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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.beans.ReportGraphic;

/**
 *
 * Arahant
 */
public class SaveDynamicReportGraphic {

	private String description;
	private double xPos;
	private double yPos;
	private String graphicId;

	public SaveDynamicReportGraphic() {
	}

	public SaveDynamicReportGraphic(ReportGraphic rg) {
		description = rg.getDescription();
		xPos = rg.getXPos();
		yPos = rg.getYPos();
		graphicId = rg.getReportGraphicId();

	}

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

	
}
