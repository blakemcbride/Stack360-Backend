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

import com.arahant.beans.ClientContact;
import com.arahant.beans.CompanyBase;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.ProspectContact;
import com.arahant.business.BClientCompany;
import com.arahant.business.BClientContact;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectContact;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ProspectContactReport extends ReportBase {

	public ProspectContactReport() {
		super("CliCon", "Prospect Contacts Report");
	}

	
	public String build (String id)
	{
		try {
			
			BProspectCompany bc=new BProspectCompany(id);

            PdfPTable table = new PdfPTable(1);
	       
	        writeHeaderLine("Prospect",  bc.getName());
	        
	        addTable(table);
            addHeaderLine();


			List <ProspectContact> l =hsu.createCriteria(ProspectContact.class)
				.orderBy(ProspectContact.LNAME)
				.orderBy(ProspectContact.FNAME)
				.joinTo(ProspectContact.ORGGROUPASSOCIATIONS)
				.joinTo(OrgGroupAssociation.ORGGROUP)
				.joinTo(OrgGroup.OWNINGCOMPANY)
				.eq(CompanyBase.ORGGROUPID, id)
				.list();
			
			


            table = makeTable(new int[]{50,10,20,20});

            writeColHeader(table, "Contact Name");
			writeColHeader(table, "Primary");
			writeColHeader(table, "Job Title");
			writeColHeader(table, "Work Phone");
            
            
            boolean alternateRow = true;

            for (ProspectContact cc : l) {
                // toggle the alternate row
                alternateRow = !alternateRow;

				
				BProspectContact bcc=new BProspectContact(cc);
                write(table, cc.getNameLFM(), alternateRow);
				write(table, bcc.isPrimary(id)?"Yes":"No",alternateRow);
				write(table, bcc.getJobTitle(), alternateRow);
				write(table, bcc.getWorkPhoneNumber(),alternateRow);

            }
            

            addTable(table);

        } 
		catch (Exception e)
		{
			throw new ArahantException(e);
		}
		  finally {
            close();

        }

        return getFilename();
	}
}
