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
import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.File;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class PendingChanges extends ReportBase {

	public PendingChanges() throws ArahantException {
		super("report", "");

		try {
			final File companyLogo = BCompany.getReportLogo(null);

        	logo = Image.getInstance(companyLogo.getAbsolutePath());
			logo.scaleToFit(1000, 80);
        } catch (final Exception e) {
        	logger.error(e);
        }
    }

	Image logo;

	private boolean hasSpouse(Node doc) throws Exception
	{
		NodeList nl=DOMUtils.getNodes(doc, "GenericWizardData/dependents/dependent/relationship");

		for (int loop=0;loop<nl.getLength();loop++)
			if (DOMUtils.getString(nl.item(loop), ".").equals("Spouse"))
				return true;

		return false;

	}

    public String build(String personId) throws DocumentException, Exception {
		NodeList nodes;

        try {

			PdfPTable table = makeTable(new int[]{30,70});
			//write(table,"",false);
			writeImage(table, logo, true);
			write(table,"",false);
			addTable(table);

			table = makeTable(new int[]{100});
			writeBoldCentered(table, "Benefit Change Request", 12F);
			addTable(table);
			
			writeHeaderLine("Run Date", DateUtils.getDateTimeFormatted(DateUtils.now(), DateUtils.nowTime()));

            //PdfPTable table;

			//BEmployee p=new BEmployee(personId);

            addHeaderLine();

			PersonChangeRequest pcr=hsu.createCriteria(PersonChangeRequest.class)
		//			.eq(PersonChangeRequest.PERSON,p.getEmployee())
					.orderByDesc(PersonChangeRequest.REQUEST_DATE)
					.joinTo(PersonChangeRequest.PERSON)
					.eq(Person.PERSONID, personId)
					.first();

			if (pcr==null)
				throw new ArahantWarning("There are no pending change requests for this employee.");

			org.w3c.dom.Document doc=DOMUtils.createDocument(pcr.getRequestData());

            table = makeTable(new int[]{20,80});

			Node personNode=DOMUtils.getNode(doc, "GenericWizardData/demographics");

			write(table, personNode, "First Name","firstName");
			write(table, personNode, "Middle Name","middleName");
			write(table, personNode, "Last Name","lastName");
			write(table, "DOB", DateUtils.getDateFormatted(DOMUtils.getInt(personNode, "dob")));

			String sex=DOMUtils.getString(personNode, "sex");
			write(table, "Gender", sex.equals("M")?"Male":"Female"); //write(table, personNode, "Gender", "sex");
			
			write(table, personNode, "Home Phone", "homePhone");
			write(table, personNode, "Work Phone", "workPhone");

			write(table, personNode, "Street 1", "street1");
			write(table, personNode, "Street 2", "street2");
			write(table, personNode, "City", "city");
			write(table, personNode, "State", "state");
			write(table, personNode, "Zip", "zip");

			boolean smoker=DOMUtils.getString(personNode, "nonSmokerDiscount").equals("FALSE");
			write(table, "Employee Smoker", smoker?"Yes":"No");
			if (hasSpouse(doc))
			{
				smoker=DOMUtils.getString(personNode, "spouseNonSmokerDiscount").equals("FALSE");
				write(table, "Spouse/Domestic Partner Smoker", smoker?"Yes":"No");
			}
			else
			{
				write(table, "Spouse/Domestic Partner Smoker", "N/A");
			}
			NodeList depNodes=DOMUtils.getNodes(doc, "GenericWizardData/dependents/dependent");

			seperatorLine(table);
			seperatorLine(table);
			
			for (int loop=0;loop<depNodes.getLength();loop++)
			{
				//write(table, "Dependent", false);
				writeBoldLeft(table, "Dependent", 10);
				write(table, "", false);

				Node dependent=depNodes.item(loop);

				write(table, dependent, "First Name","firstName");
				write(table, dependent, "Middle Name","middleName");
				write(table, dependent, "Last Name","lastName");
				write(table, dependent, "Relationship","relationship");
				
				sex=DOMUtils.getString(dependent, "sex");
				write(table, "Gender", sex.equals("M")?"Male":"Female");  //write(table, dependent, "Gender", "sex");

				write(table, "DOB", DateUtils.getDateFormatted(DOMUtils.getInt(dependent, "dob")));

				boolean student=DOMUtils.getString(dependent, "student").equals("FALSE");
				write(table, "Student Smoker", student?"Yes":"No");  //write(table, dependent, "Student", "student");

				boolean handicap=DOMUtils.getString(dependent, "handicap").equals("FALSE");
				write(table, "Handicap", handicap?"Yes":"No");  //write(table, dependent, "Handicap", "handicap");
				
			}


			seperatorLine(table);
			seperatorLine(table);


			NodeList beneNodes=DOMUtils.getNodes(doc, "GenericWizardData/benefits/benefit");

			for (int loop=0;loop<beneNodes.getLength();loop++)
			{
				Node beneNode=beneNodes.item(loop);
				//see if selected
				String selectedId=DOMUtils.getString(beneNode, "selectedId");

				if (isEmpty(selectedId))
					continue;

				seperatorLine(table);
				seperatorLine(table);

				HrBenefit bene=hsu.get(HrBenefit.class, selectedId);
				write(table, "Benefit", bene.getName());

				double amt=DOMUtils.getDouble(beneNode, "selectedAmount");

				if (amt>0)
					write(table, "Amount", MoneyUtils.formatMoney(amt));


				NodeList enrollNodes=DOMUtils.getNodes(beneNode, "enrollees/enrollee");

				for (int enloop=0;enloop<enrollNodes.getLength();enloop++)
				{
					Node enrollee=enrollNodes.item(enloop);
					write(table, "Enrollee", DOMUtils.getString(enrollee, "lastName")+", "+
							DOMUtils.getString(enrollee, "firstName")
							+(" "+DOMUtils.getString(enrollee, "middleName")));



					if (!isEmpty(DOMUtils.getString(enrollee, "physician")))
						write(table, enrollee, "PCP","physician");

					if (!isEmpty(DOMUtils.getString(enrollee, "otherInsurance")))
						write(table, enrollee, "Other Insurance","otherInsurance");

					String otherIsPrime=DOMUtils.getString(enrollee, "otherInsurancePrimary");
					if (!"FALSE".equals(otherIsPrime))
						write(table, "Other Insurance is Primary","True");

				}

				NodeList primaryNodes=DOMUtils.getNodes(beneNode, "primaryBeneficiaries/primaryBeneficiary");

				for (int prloop=0;prloop<primaryNodes.getLength();prloop++)
				{
					Node primary=primaryNodes.item(prloop);

					write(table, primary, "Beneficiary", "beneficiary");
					write(table, "DOB", DateUtils.getDateFormatted(DOMUtils.getInt(primary, "dob")));
					write(table, primary, "Relationship", "relationship");
					write(table, primary, "Percentage", "percent");
					write(table, primary, "Address", "address");
				}


				NodeList secondaryNodes=DOMUtils.getNodes(beneNode, "contingentBeneficiaries/contingentBeneficiary");

				for (int prloop=0;prloop<secondaryNodes.getLength();prloop++)
				{
					Node primary=primaryNodes.item(prloop);

					write(table, primary, "Contingent Beneficiary", "beneficiary");
					write(table, "DOB", DateUtils.getDateFormatted(DOMUtils.getInt(primary, "dob")));
					write(table, primary, "Relationship", "relationship");
					write(table, primary, "Percentage", "percent");
					write(table, primary, "Address", "address");
				}
			}

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

	public void write(PdfPTable table, String field, String value) throws Exception
	{
		write(table,field,false);
		write(table,value,false);

	}


	public void write(PdfPTable table, Node node, String field, String valueXPath) throws Exception
	{
		write(table,field,false);
		write(table,DOMUtils.getString(node, valueXPath),false);

	}

    public static void main(String args[]) {
        try {
            new PendingChanges().build("00001-0000005738");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
