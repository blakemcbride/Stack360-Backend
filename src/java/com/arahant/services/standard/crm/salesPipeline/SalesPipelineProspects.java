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
 *
 */

package com.arahant.services.standard.crm.salesPipeline;

import com.arahant.beans.Appointment;
import com.arahant.business.BAppointment;
import com.arahant.business.BProspectCompany;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;

public class SalesPipelineProspects {

	public SalesPipelineProspects() {
	}

	public SalesPipelineProspects(BProspectCompany bpc, boolean showAll)
	{
		htmlText = bpc.getMainContactName() + " - " + bpc.getName() + "\nIC: " + DateUtils.getDateFormatted(bpc.getFirstContactDate());

		if (showAll)
			htmlText += "  {" + bpc.getSalesPerson().getFirstName().toUpperCase().charAt(0) + bpc.getSalesPerson().getLastName().toUpperCase().charAt(0) +"}";

		BAppointment ba = new BAppointment(ArahantSession.getHSU().createCriteria(Appointment.class).eq(Appointment.COMPANY, bpc.getBean()).ge(Appointment.DATE, DateUtils.now()).eq(Appointment.STATUS, 'A').first());

		if (ba.getBean() != null)
			htmlText += "\nNC: " + DateUtils.getDateFormatted(ba.getDate());

		//TODO: htmlText = Closing date

		prospectName = bpc.getName();
		prospectId = bpc.getCompanyId();
		identifier = bpc.getIdentifier();
	}

	String htmlText;
	String prospectName;
	String prospectId;
	String identifier;

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getProspectId() {
		return prospectId;
	}

	public void setProspectId(String prospectId) {
		this.prospectId = prospectId;
	}

	public String getProspectName() {
		return prospectName;
	}

	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String createHTML(String contactName, String companyName, int contactDate)
	{
		return contactName + " - " + companyName + "\nInitial Contact: " + DateUtils.getDateFormatted(contactDate);
				//"<font size=\"8\"><b>" + contactName + " - " + companyName + "</b>\nIC: " + DateUtils.getDateFormatted(contactDate) + "</font>";
	}

}
