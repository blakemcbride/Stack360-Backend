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
import com.arahant.business.BClientCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Mar 29, 2007
 *
 */
public class ClientReport extends ReportBase  {



	public ClientReport() throws ArahantException {
		super("ClntRpt","Client Report");
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
	public String getReport(final BClientCompany[] c, final boolean addr, final boolean billRate, final boolean phone, final boolean contractDate, final boolean identifier, final boolean contactName, final int sortType, final boolean sortAsc) throws ArahantException {

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
            this.writeCompanies(c, addr,billRate,phone,contractDate,identifier,contactName);
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
	
	protected void writeCompanies(final BClientCompany[] c, final boolean addr, final boolean billRate, final boolean phone, final boolean contractDate, final boolean identifier, final boolean contactName) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;
		  	
		BClientCompany company;		
		
        table = createTable(1, new boolean[]{addr, billRate, phone, contractDate, identifier, contactName});
        
        table.setWidths(this.getColumnWidths(new boolean[]{addr, billRate, phone, contractDate, identifier, contactName}));
        

		if (identifier) 
			writeCentered(table,"Identifier");
		
		writeCentered(table,"Name");
        
		if (contactName) 
			writeCentered(table,"Primary Contact");
		
		if (phone) 
			writeCentered(table,"Phone");
		
		if (addr) 
			writeCentered(table,"Address");
		
		if (contractDate) 
			writeCentered(table,"Contract Date");
		
		if (billRate) 
			writeCentered(table,"Billing Rate");
		
        table.setHeaderRows(1);
		
    	for (final BClientCompany element : c) {
        	company = element;

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

			if (contractDate) 
				write(table, DateUtils.getDateFormatted(company.getContractDate()), alternateRow);
			
			if (billRate) 
				write(table, MoneyUtils.formatMoney(company.getBillingRate()), alternateRow);
	          
			
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
				
	   
			if (contractDate) 
				write(table, "", alternateRow);
			
			if (billRate) 
				write(table, "", alternateRow);
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
				
	   
			if (contractDate) 
				write(table, "", alternateRow);
			
			if (billRate) 
				write(table, "", alternateRow);
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

	
