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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "report_graphic")
public class ReportGraphic extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "report_graphic";
	public static final String REPORT_GRAPHIC_ID = "reportGraphicId";
	private String reportGraphicId;
	public static final String REPORT = "report";
	private Report report;
	public static final String REPORT_ID = "reportId";
	private String reportId;
	public static final String DESCRIPTION = "description";
	private String description;
	public static final String X_POS = "xPos";
	private double xPos;
	public static final String Y_POS = "yPos";
	private double yPos;
	public static final String GRAPHIC = "graphic";
	private byte[] graphic;

	//Default constructor
	public ReportGraphic() {
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name = "report_graphic_id")
	public String getReportGraphicId() {
		return reportGraphicId;
	}

	public void setReportGraphicId(String reportGraphicId) {
		this.reportGraphicId = reportGraphicId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@Column(name = "report_id", insertable = false, updatable = false)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Column(name = "graphic")
	public byte[] getGraphic() {
		return graphic;
	}

	public void setGraphic(byte[] graphic) {
		this.graphic = graphic;
	}

	@Column(name = "x_pos")
	public double getXPos() {
		return xPos;
	}

	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	@Column(name = "y_pos")
	public double getYPos() {
		return yPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "report_graphic_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setReportGraphicId(IDGenerator.generate(this));
		return reportGraphicId;
	}

	@Override
	public ReportGraphic clone() {
		ReportGraphic rg = new ReportGraphic();
		rg.generateId();
		rg.setDescription(description);
		rg.setGraphic(graphic);
		rg.setReportId(reportId);
		rg.setXPos(xPos);
		rg.setYPos(yPos);
		return rg;
	}

	@Override
	public boolean equals(Object o) {
		if (reportId == null && o == null)
			return true;
		if (reportId != null && o instanceof ReportGraphic)
			return reportId.equals(((ReportGraphic) o).getReportId());

		return false;
	}

	@Override
	public int hashCode() {
		if (reportId == null)
			return 0;
		return reportId.hashCode();
	}
}
