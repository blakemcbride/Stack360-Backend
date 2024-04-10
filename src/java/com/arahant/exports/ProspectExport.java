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
package com.arahant.exports;

import com.arahant.beans.Person;
import com.arahant.beans.Phone;
import com.arahant.beans.ProspectCompany;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;
import org.kissweb.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProspectExport {

	private final HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(int sortType, boolean sortAsc, String identifier, String fname, String lname, String name, String statusId, String sourceId, String typeId, String excludeId, int cap, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("Prospects", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			writer.writeField("Company Name");
			writer.writeField("Phone");
			writer.writeField("Fax");
			writer.writeField("Status");
			writer.writeField("Source");
			writer.writeField("Type");
			writer.writeField("First Contact");
			writer.writeField("Last Contact");
			writer.writeField("Next Contact");
			writer.writeField("Opportunity");
			writer.writeField("Certainty");
			writer.writeField("Weighted Value");
			writer.writeField("Contact 1 Name");
			writer.writeField("Contact 1 Work Phone");
			writer.writeField("Contact 1 Fax");
			writer.writeField("Contact 1 Home Phone");
			writer.writeField("Contact 1 Cell Phone");
			writer.writeField("Contact 1 E-mail");
			writer.writeField("Contact 2 Name");
			writer.writeField("Contact 2 Work Phone");
			writer.writeField("Contact 2 Fax");
			writer.writeField("Contact 2 Home Phone");
			writer.writeField("Contact 2 Cell Phone");
			writer.writeField("Contact 2 E-mail");
			writer.writeField("Contact 3 Name");
			writer.writeField("Contact 3 Work Phone");
			writer.writeField("Contact 3 Fax");
			writer.writeField("Contact 3 Home Phone");
			writer.writeField("Contact 3 Cell Phone");
			writer.writeField("Contact 3 E-mail");
			writer.writeField("Contact 4 Name");
			writer.writeField("Contact 4 Work Phone");
			writer.writeField("Contact 4 Fax");
			writer.writeField("Contact 4 Home Phone");
			writer.writeField("Contact 4 Cell Phone");
			writer.writeField("Contact 4 E-mail");
			writer.writeField("Contact 5 Name");
			writer.writeField("Contact 5 Work Phone");
			writer.writeField("Contact 5 Fax");
			writer.writeField("Contact 5 Home Phone");
			writer.writeField("Contact 5 Cell Phone");
			writer.writeField("Contact 5 E-mail");
			writer.writeField("Contact 6 Name");
			writer.writeField("Contact 6 Work Phone");
			writer.writeField("Contact 6 Fax");
			writer.writeField("Contact 6 Home Phone");
			writer.writeField("Contact 6 Cell Phone");
			writer.writeField("Contact 6 E-mail");
			writer.endRecord();

			hsu.setCurrentPersonToArahant();

			//BProspectCompany[] bpc = BProspectCompany.search("", "", "", "", "", "", "", 10000, true);
			HibernateCriteriaUtil<ProspectCompany> hcu = BProspectCompany.search(sortType, sortAsc, identifier, fname, lname, name, statusId, sourceId, typeId, excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, sortAsc);
			HibernateScrollUtil<ProspectCompany> bpc = hcu.scroll();
			ProspectCompany pc;

			//loop through all the companies
			for (; bpc.next() ; hsu.evict(pc)) {
				BProspectCompany bProspectCompany = new BProspectCompany(pc=bpc.get());

				if (!StringUtils.isEmpty(bProspectCompany.getName())) {
					//Write out the company name
					writer.writeField(bProspectCompany.getName().trim());
					writer.writeField(StringUtils.isEmpty(bProspectCompany.getMainPhoneNumber()) ? "" : bProspectCompany.getMainPhoneNumber().trim());
					writer.writeField(StringUtils.isEmpty(bProspectCompany.getMainFaxNumber()) ? "" : bProspectCompany.getMainFaxNumber().trim());
					writer.writeField(StringUtils.isEmpty(bProspectCompany.getStatusCode()) ? "" : bProspectCompany.getStatusCode().trim());
					writer.writeField(StringUtils.isEmpty(bProspectCompany.getSourceCode()) ? "" : bProspectCompany.getSourceCode().trim());
					writer.writeField(StringUtils.isEmpty(bProspectCompany.getTypeCode()) ? "" : bProspectCompany.getTypeCode().trim());
					writer.writeDate(bProspectCompany.getFirstContactDate());
					writer.writeDate(bProspectCompany.getLastContactDate());
					writer.writeDate(bProspectCompany.getNextContactDate());
					writer.writeField(bProspectCompany.getOpportunityValue());
					int cert = bProspectCompany.getCertainty();
					if (cert == 0)
						cert = bProspectCompany.getStatus().getCertainty();
					writer.writeField(cert);
					writer.writeField(bProspectCompany.getOpportunityValue() * (double) cert / 100.0);
				}

				//get all the phone numbers for the company
				HibernateCriteriaUtil<Person> PersonHcu = hsu.createCriteria(Person.class).eq(Person.COMPANYBASE, bProspectCompany.getBean());

				HibernateScrollUtil<Person> scr = PersonHcu.scroll();

				while (scr.next()) {
					Set<Phone> p = scr.get().getPhones();
					Iterator<Phone> i = p.iterator();

					if (p.size() > 0) {
						String workPhone = "";
						String homePhone = "";
						String cellPhone = "";
						String fax = "";
						String email = "";

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

						String fullName = (StringUtils.isEmpty(scr.get().getFname()) ? "" : scr.get().getFname().trim()) + " " + (StringUtils.isEmpty(scr.get().getLname()) ? "" : scr.get().getLname().trim());

						if (new BPerson(scr.get()).isPrimary(bProspectCompany.getOrgGroupId()))
							fullName += " (Primary)";

						writer.writeField(fullName);
						writer.writeField(StringUtils.isEmpty(workPhone) ? "" : workPhone.trim());
						writer.writeField(StringUtils.isEmpty(fax) ? "" : fax.trim());
						writer.writeField(StringUtils.isEmpty(homePhone) ? "" : homePhone.trim());
						writer.writeField(StringUtils.isEmpty(cellPhone) ? "" : cellPhone.trim());
						writer.writeField(new BPerson(scr.get()).getPersonalEmail());
					}
				}

				writer.endRecord();

				scr.close();
			}

		} finally {
			writer.close();
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String[] args) {
		try {
			//new ProspectExport().build();
		} catch (Exception ex) {
			Logger.getLogger(ProspectExport.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
