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

import com.arahant.beans.Person;
import com.arahant.beans.Phone;
import com.arahant.beans.ProspectCompany;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.Iterator;
import java.util.Set;

public class ProspectReport extends ReportBase
{
	public ProspectReport() throws ArahantException
	{
		super("ProspectRep", "Prospects", false);
	}

	public String build(int sortType, boolean sortAsc, String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, int cap, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone) throws DocumentException
	{
		try {
			PdfPTable table = null;
			addHeaderLine();

			//ArahantSession.getHSU().setCurrentPersonToArahant();
			//int sortType, boolean sortAsc, String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean includeSorting)
			HibernateCriteriaUtil<ProspectCompany> hcu = BProspectCompany.search(sortType, sortAsc, identifier, fname, lname, name, statusId, sourceId, excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, sortAsc);
			HibernateScrollUtil<ProspectCompany> bpc = hcu.scroll();

			int count = 0;
			ProspectCompany pc;
			for (; bpc.next() ; hsu.evict(pc)) {
				BProspectCompany bProspectCompany = new BProspectCompany(pc=bpc.get());

				if (!isEmpty(bProspectCompany.getName())) {
					//Write out the company name
					table = makeTable(25, 50, 25);
					write(table, "");
					writeCenteredWithBorder(table, bProspectCompany.getName().trim());
					write(table, "");
					addTable(table);

					table = makeTable(25, 25, 25, 25);
					//writeColHeader(table, "ID", Element.ALIGN_LEFT);
					writeColHeader(table, "Phone", Element.ALIGN_LEFT);
					writeColHeader(table, "Fax", Element.ALIGN_LEFT);
					writeColHeader(table, "Status", Element.ALIGN_LEFT);
					writeColHeader(table, "Source", Element.ALIGN_LEFT);

					//write(table, isEmpty(bpc[loop].getIdentifier())?"":bpc[loop].getIdentifier().trim());
					write(table, isEmpty(bProspectCompany.getMainPhoneNumber()) ? "" : bProspectCompany.getMainPhoneNumber().trim());
					write(table, isEmpty(bProspectCompany.getMainFaxNumber()) ? "" : bProspectCompany.getMainFaxNumber().trim());
					write(table, isEmpty(bProspectCompany.getStatusCode()) ? "" : bProspectCompany.getStatusCode().trim());
					write(table, isEmpty(bProspectCompany.getSourceCode()) ? "" : bProspectCompany.getSourceCode().trim());
					addTable(table);
				}

				//get all the phone numbers for the company
				HibernateCriteriaUtil<Person> hcu2 = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.COMPANYBASE, bProspectCompany.getBean());

				HibernateScrollUtil<Person> scr = hcu2.scroll();

				boolean firstTime = true;
				boolean addTable = false;

				boolean alternateRow = false;

				while (scr.next()) {
					Set<Phone> p = scr.get().getPhones();
					Iterator<Phone> i = p.iterator();

					if (p.size() > 0) {
						if (firstTime) {
							table = makeTable(25, 50, 25);
							write(table, "");
							writeCentered(table, "Contacts");
							write(table, "");
							addTable(table);

							//Write out the column headers
							table = makeTable(20, 20, 20, 20, 20);
							writeColHeader(table, "Name", Element.ALIGN_LEFT);
							writeColHeader(table, "Work", Element.ALIGN_LEFT);
							writeColHeader(table, "Fax", Element.ALIGN_LEFT);
							writeColHeader(table, "Home", Element.ALIGN_LEFT);
							writeColHeader(table, "Cell", Element.ALIGN_LEFT);

							firstTime = false;
						}

						String workPhone = "";
						String homePhone = "";
						String cellPhone = "";
						String fax = "";

						while (i.hasNext()) {
							Phone ph = i.next();

							switch (ph.getPhoneType()) {
								case 0:
									homePhone = ph.getPhoneNumber();
									break;
								case 1:
									workPhone = ph.getPhoneNumber();
									break;
								case 2:
									fax = ph.getPhoneNumber();
									break;
								case 3:
									cellPhone = ph.getPhoneNumber();
									break;
							}
						}

						String fullName = (isEmpty(scr.get().getFname()) ? "" : scr.get().getFname().trim()) + " " + (isEmpty(scr.get().getLname()) ? "" : scr.get().getLname().trim());

						if (new BPerson(scr.get()).isPrimary(bProspectCompany.getOrgGroupId()))
							fullName += " (Primary)";

						write(table, fullName, alternateRow);
						write(table, isEmpty(workPhone) ? "" : workPhone.trim(), alternateRow);
						write(table, isEmpty(fax) ? "" : fax.trim(), alternateRow);
						write(table, isEmpty(homePhone) ? "" : homePhone.trim(), alternateRow);
						write(table, isEmpty(cellPhone) ? "" : cellPhone.trim(), alternateRow);

						alternateRow = !alternateRow;

						addTable = true;
					}
				}

				if (addTable)
					addTable(table);

				scr.close();
			}

		} finally {
			close();
		}
		return getFilename();
	}

	public static void main(String[] args) {
		// build(String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, int cap, boolean hasPhone)
		try {
			//new ProspectReport().build("", "", "", "", "", "", "", 10000, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
