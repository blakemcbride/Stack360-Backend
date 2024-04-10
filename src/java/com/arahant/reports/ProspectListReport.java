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
 * Created on Mar 29, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.beans.ProspectCompany;
import com.arahant.business.BClientCompany;
import com.arahant.business.BProspectCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Mar 29, 2007
 *
 */
public class ProspectListReport extends ReportBase  {



	public ProspectListReport() throws ArahantException {
		super("ClntRpt","Prospect Report");
		setConfidential();
		
	}


	
	/**
	 * @param hsu
	 * @param c
	 * @param addr
	 * @param billRate
	 * @param phone
	 * @param contractDate
	 * @param identifier
	 * @param contactName
	 * @return
	 * @throws ArahantException 
	 */
	public String build(final boolean addr, final boolean phone, final boolean identifier, final boolean contactName, final int sortType, final boolean sortAsc) throws ArahantException {

		try
		{
    		String sortBy;
    		final String sortDirection = sortAsc ? "Ascending" : "Descending";
    		
    		//0=idenftifer, 1=name, 2=contract date sortAsc (bool)
    		switch (sortType) 
    		{
    			case 0: sortBy = "Identifier"; break;
    			case 1: sortBy = "Name"; break;
    			case 2: sortBy = "Contract Date"; break;
    			default: sortBy = "Unknown"; break;
    					
    		}
         
        	// write out the parts of our report
            this.writeHeader(sortBy, sortDirection);
            this.writeCompanies(addr,phone,identifier,contactName);
		}
		catch (final Exception e)
		{
			throw new ArahantException(e);
		}
		finally {
            close();
        }
		return getFilename();
		
	}
	
	protected void writeHeader(final String sortBy, final String sortDirection) throws DocumentException {

       
        
        addSortInfo(sortBy,sortDirection);
        
        addHeaderLine();
        
        
	}
	
	protected void writeCompanies(final boolean addr, final boolean phone,  final boolean identifier, final boolean contactName) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;
		  	
		BProspectCompany company;		
		
        table = createTable(1, new boolean[]{addr, phone, identifier, contactName});
        
        table.setWidths(this.getColumnWidths(new boolean[]{addr, phone, identifier, contactName}));
        

		if (identifier) 
			writeCentered(table,"Identifier");
		
		writeCentered(table,"Name");
        
		if (contactName) 
			writeCentered(table,"Primary Contact");
		
		if (phone) 
			writeCentered(table,"Phone");
		
		if (addr) 
			writeCentered(table,"Address");
		
		
        table.setHeaderRows(1);
		
		HibernateCriteriaUtil<ProspectCompany> hcu=hsu.createCriteria(ProspectCompany.class).orderBy(ProspectCompany.NAME);
		BProspectCompany.addSalesPersonFilter(hcu);
		
    	for (final ProspectCompany pc : hcu.list()) {
        	company = new BProspectCompany(pc);

        	// toggle the alternate row
			alternateRow = !alternateRow;

			if (identifier) 
				write(table, company.getIdentifier(), alternateRow);
			
			write(table, company.getName(), alternateRow);
			
			if (contactName) 
				write(table, company.getMainContactName(), alternateRow);

			if (phone) 
				write(table, company.getMainPhoneNumber(), alternateRow);
	           
			if (addr) 
				write(table, company.getStreet(), alternateRow);

	          
			
///////////////////////////////////////////////////
			if (identifier) 
				write(table, "", alternateRow);
			
			write(table, "", alternateRow);

			if (contactName) 
				write(table, "", alternateRow);
			
			if (phone) 
				write(table, "", alternateRow);
			
			if (addr) 
				write(table, company.getStreet2(), alternateRow);
				
	   
			///////////////////////////////////////////////////
			if (identifier) 
				write(table, "", alternateRow);
			
			write(table, "", alternateRow);

			if (contactName) 
				write(table, "", alternateRow);
			
			if (phone) 
				write(table, "", alternateRow);
			
			if (addr) {
				String addrLine=company.getCity()+", "+company.getState()+"  "+company.getZip();
				if (addrLine.trim().equals(","))
					addrLine="";
				write(table, addrLine, alternateRow);
			}
				
	   
        }
        
        addTable(table);
	}

	private int[] getColumnWidths(final boolean []cols) {
		final int columnCount = this.getColumnCount(1, cols);

		final int[] columnWidths = new int[columnCount];
		int percentage = (int)Math.floor(100 / columnCount);
		int total = 0;
		final int nameGetsAtLeastPercent = 15;
		int startFromIdx = 0;
		
		// name must get "nameGetsAtLeastPercent" percent of the table at least
		if (percentage < nameGetsAtLeastPercent) {
			// recalculate the remaining column widths with 
			// name getting "nameGetsAtLeastPercent" percent
			percentage = (int)Math.floor((100 - nameGetsAtLeastPercent) / (columnCount - 1));
			
			// fill out name's percent and adjust the rest of the calculation starting index
			startFromIdx = 1;
			total += columnWidths[0] = nameGetsAtLeastPercent;
		}
		
		// spin through the columns and assign the widths
		for (int idx = startFromIdx; idx < columnCount; idx++) {
			total += columnWidths[idx] = percentage;
		
			// if this is the last column, adjust to total 100%
			if (idx == columnCount - 1)
				columnWidths[idx] += 100 - total;
		}
		
		return columnWidths;
		
	}
		
}

	
