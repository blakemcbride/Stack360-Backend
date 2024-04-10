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

package com.arahant.reports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashMap;
import org.w3c.dom.NodeList;

/**
 *
 */
public class PendingMedicalChanges extends ReportBase {

	public PendingMedicalChanges() throws ArahantException {
        super("report", "Medical Change Requests");
    }

    public String build() throws DocumentException, Exception {
		NodeList nodes;

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class)
					.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
					.joinTo(Person.CHANGE_REQUESTS);


			HashMap<String, Integer> counts=new HashMap<String,Integer>();

            table = makeTable(new int[]{40,60});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);

			writeColHeader(table, "Medical Benefit", Element.ALIGN_LEFT);

            boolean alternateRow = true;

			for (Person p : hcu.list())
			{
				PersonChangeRequest pcr=hsu.createCriteria(PersonChangeRequest.class)
					.eq(PersonChangeRequest.PERSON,p)
					.orderByDesc(PersonChangeRequest.REQUEST_DATE)
					.first();

				if (pcr.getPerson().getLname().toLowerCase().startsWith("test"))
					continue;

				org.w3c.dom.Document doc=DOMUtils.createDocument(pcr.getRequestData());

				//get all the selected ids

				nodes=DOMUtils.getNodes(doc, "//selectedId");


				HrBenefit benefit=null;
				String output="";
				for (int loop=0;loop<nodes.getLength();loop++)
				{
					String id=nodes.item(loop).getTextContent();

					if (isEmpty(id))
						continue;

					benefit=hsu.get(HrBenefit.class,id);
					if (benefit.getHrBenefitCategory().getBenefitType()==HrBenefitCategory.HEALTH)
						output=benefit.getName();

				}

				if (isEmpty(output))
					output="No Medical Election";
				else
					continue;
				//if (output.indexOf("HSA")==-1)
				//	continue;

				// toggle the alternate row
                alternateRow = !alternateRow;

				Integer i=counts.get(output);
				if (i==null)
					i=0;

				counts.put(output, ++i);

                write(table, p.getNameLFM(), alternateRow);
				write(table, output, alternateRow);

            }

            addTable(table);
/*
			table=makeTable(new int[]{100});

			List <String> keys=new ArrayList();
			keys.addAll(counts.keySet());
			Collections.sort(keys);
			for (String nm : keys)
			{
				write(table,nm+" "+counts.get(nm));
			}

            addTable(table);
*/
        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new PendingMedicalChanges().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
