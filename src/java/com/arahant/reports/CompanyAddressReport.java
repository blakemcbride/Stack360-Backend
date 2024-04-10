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

package com.arahant.reports;

import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

public class CompanyAddressReport extends ReportBase {
	
	private PdfPTable table;

	public CompanyAddressReport(String title) throws ArahantException {
		super("AddRep", title);
	}

	public String build(String id, boolean showContacts) throws DocumentException {
		try {
			table = makeTable(new int[]{100});

			BCompanyBase company = BCompanyBase.get(id);

			writeHeaderLine("Company", company.getName());

			addHeaderLine();

			writeLeft(table, "", false);

			writeBoldLeft(table, "Address", 10);

			addTable(table);

			table = makeTable(new int[]{5, 95});

			writeNotEmpty(company.getStreet());
			writeNotEmpty(company.getStreet2());
			String csz = "";
			if (!isEmpty(company.getCity()))
				csz += company.getCity();

			if (!isEmpty(company.getState())) {
				if (!isEmpty(csz))
					csz += ", ";
				csz += company.getState();
			}

			if (!isEmpty(company.getZip())) {
				if (!isEmpty(csz))
					csz += "  ";
				csz += company.getZip();
			}

			writeNotEmpty(csz);
//			writeNotEmpty(company.getMainContactWorkPhone());
			writeNotEmpty(company.getMainPhoneNumber());
					
			addTable(table);

			table = makeTable(new int[]{100});
			writeLeft(table, "", false);
			addTable(table);

			boolean altColor = true;

			if (showContacts) {
				table = makeTable(new int[]{100});
				writeBoldLeft(table, "Contacts", 10);
				addTable(table);

				//get the main contacts
				for (Person p : hsu.createCriteria(Person.class)
						.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
						.joinTo(Person.ORGGROUPASSOCIATIONS)
						.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
						.eq(OrgGroupAssociation.ORG_GROUP_ID, company.getOrgGroupId())
						.list()) {
					altColor = !altColor;

					BPerson bp = new BPerson(p);
					table = makeTable(new int[]{5, 33, 19, 19, 19});
					writeLeft(table, "", false);
					writeLeft(table, bp.getNameFML(), altColor);
					writeLeft(table, bp.getJobTitle(), altColor, 2);
					writeLeft(table, bp.getWorkPhone(), altColor);

					if (!isEmpty(bp.getStreet())) {
						writeLeft(table, "", false);
						writeLeft(table, bp.getStreet() + bp.getStreet2(), altColor);
						writeLeft(table, bp.getCity(), altColor);
						writeLeft(table, bp.getState(), altColor);
						writeLeft(table, bp.getZip(), altColor);
					}
					addTable(table);
				}

				//get the other contacts
				for (Person p : hsu.createCriteria(Person.class)
						.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
						.joinTo(Person.ORGGROUPASSOCIATIONS)
						.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'N')
						.eq(OrgGroupAssociation.ORG_GROUP_ID, company.getOrgGroupId())
						.list()) {
					altColor = !altColor;

					BPerson bp = new BPerson(p);
					table = makeTable(new int[]{5, 33, 19, 19, 19});
					writeLeft(table, "", false);
					writeLeft(table, bp.getNameFML(), altColor);
					writeLeft(table, bp.getJobTitle(), altColor, 2);
					writeLeft(table, bp.getWorkPhone(), altColor);

					if (!isEmpty(bp.getStreet())) {
						table = makeTable(new int[]{5, 38, 19, 19, 19});
						writeLeft(table, "", false);
						writeLeft(table, bp.getStreet() + bp.getStreet2(), altColor);
						writeLeft(table, bp.getCity(), altColor);
						writeLeft(table, bp.getState(), altColor);
						writeLeft(table, bp.getZip(), altColor);
					}
					addTable(table);
				}
			}
		} finally {
			close();
		}

		return getFilename();
	}

	private void writeNotEmpty(String s) throws DocumentException {
		if (!isEmpty(s)) {
			writeLeft(table, "", false);
			writeLeft(table, s, false);
		}
	}

	public static void main(String args[]) {
		try {
			new CompanyAddressReport("Client Report").build("00001-0000000007", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
