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
 * Created on Feb 27, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Feb 27, 2007
 *
 */
public class HRBenefitReport extends ReportBase {

	private HibernateSessionUtil hsu;
    
    public HRBenefitReport() throws ArahantException {
    	super("HRBCRept","Benefits List",true);
    }

	public void build(final HibernateSessionUtil hsu, final boolean includeConfigs) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {
        	// write out the parts of our report
            this.writeHeader();
            this.writeBenefitCategories(includeConfigs);
            
        } finally {
            close();
            this.hsu = null;
        }
	}
	
	protected void writeHeader() throws DocumentException {
        
        addHeaderLine();
	}
	
	protected void writeBenefitCategories(boolean includeConfigs) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

		HibernateCriteriaUtil hcu = this.hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME)
			.eq(HrBenefit.TIMERELATED,'Y');
		List benefitCategories = hcu.list();
		Iterator iterator = benefitCategories.iterator();
		HrBenefit benefitCategory;
		
        table = makeTable(new int[] { 24, 21, 16 /*, 17, 20 */});
       
        writeColHeader(table, "Benefit Name",Element.ALIGN_LEFT);
        writeColHeader(table, "Rule Name",Element.ALIGN_LEFT);
        writeColHeader(table, "Paid",Element.ALIGN_LEFT);
   //     writeColHeader(table, "Active",Element.ALIGN_LEFT);
    //    writeColHeader(table, "Additional Info", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        while (iterator.hasNext()) {
        	benefitCategory = (HrBenefit)iterator.next();
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, benefitCategory.getName(), alternateRow);
			write(table, benefitCategory.getRuleName(), alternateRow);
			write(table, (benefitCategory.getPaidBenefit()=='Y')?"Yes" : "No", alternateRow);
	//		write(table, (benefitCategory.getActive()=='Y')?"Yes" : "No", alternateRow);
	//		write(table, benefitCategory.getAddInfo(),alternateRow);
        }
        
        addTable(table);
        
        alternateRow = true;

		hcu = this.hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME)
			.eq(HrBenefit.TIMERELATED,'N');
		benefitCategories = hcu.list();
		iterator = benefitCategories.iterator();
		
    //    table = makeTable(new int[] { 8, 10, 8, 6, 15, 5, 8, 8, 8, 4, 10 });

        if (includeConfigs)
        {
            table = makeTable(new int[] {20, 20, 20, 10, 30});

            writeColHeader(table, "Benefit Name",Element.ALIGN_LEFT);
            writeColHeader(table, "Underwriter",Element.ALIGN_LEFT);
            writeColHeader(table, "Employee Provider", Element.ALIGN_LEFT);
            writeColHeader(table, "Pre-tax",Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit Configurations", Element.ALIGN_LEFT);
        }
        else
        {
            table = makeTable(new int[] {25,25,25,25 });

            writeColHeader(table, "Benefit Name",Element.ALIGN_LEFT);
            writeColHeader(table, "Underwriter",Element.ALIGN_LEFT);
      //      writeColHeader(table, "Group ID",Element.ALIGN_LEFT);
      //      writeColHeader(table, "Active",Element.ALIGN_LEFT);
       //     writeColHeader(table, "Covers",Element.ALIGN_LEFT);
      //      writeColHeader(table, "Max Child", Element.ALIGN_LEFT);
            writeColHeader(table, "Employee Provider", Element.ALIGN_LEFT);
       //     writeColHeader(table, "Employee Cost", Element.ALIGN_RIGHT);
         //   writeColHeader(table, "Employer Cost", Element.ALIGN_RIGHT);
            writeColHeader(table, "Pre-tax",Element.ALIGN_LEFT);
        //    writeColHeader(table, "Additional Info", Element.ALIGN_LEFT);
        }

        
        
        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        while (iterator.hasNext()) {
        	benefitCategory = (HrBenefit)iterator.next();
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, benefitCategory.getName(), alternateRow);
			write(table, (benefitCategory.getProvider()==null)?"":benefitCategory.getProvider().getName(), alternateRow);
/*			write(table, benefitCategory.getGroupId(), alternateRow);
			write(table, (benefitCategory.getActive()=='Y')?"Yes" : "No", alternateRow);
			String covers="";
			if (benefitCategory.getEmployee()=='Y')
				covers="Employee, ";
			if (benefitCategory.getSpouse()=='Y')
				covers+="Spouse, ";
			if (benefitCategory.getChildren()=='Y')
				covers+="Children, ";
			if (benefitCategory.getSpouseChildren()=='Y')
				covers+="Spouse+Children  ";
			
			if (!isEmpty(covers))
				covers=covers.substring(0,covers.length()-2);
			
			write(table, covers, alternateRow);
			String maxChild;
			if (benefitCategory.getMaxChildren()!=0)
				maxChild=benefitCategory.getMaxChildren()+"";
			else
				maxChild="No Max";
			write(table,maxChild,alternateRow);
*/
			write(table,benefitCategory.deprecatedGetEmployeeIsProvider()=='Y'?"Yes":"No",alternateRow);
	//		write(table,MoneyUtils.formatMoney(benefitCategory.getEmployeeCost()),alternateRow);
	//		write(table,MoneyUtils.formatMoney(benefitCategory.getEmployerCost()),alternateRow);
			write(table,benefitCategory.getPreTax()=='Y'?"Yes":"No",alternateRow);
	//		write(table, benefitCategory.getAddInfo(),alternateRow);

                        if (includeConfigs)
                        {
                            int i = benefitCategory.getBenefitConfigs().size();

                            for (HrBenefitConfig bc : benefitCategory.getBenefitConfigs())
                            {
                                if (i == 1)  //The last config does not need to be padded by empty cols
                                {
                                    write(table, bc.getName(), alternateRow);
                                }
                                else
                                {
                                    write(table, bc.getName(), alternateRow);
                                    write(table, "", alternateRow);
                                    write(table, "", alternateRow);
                                    write(table, "", alternateRow);
                                    write(table, "", alternateRow);
                                }

                                i--;
                            }

                        }
			
        }
        
        addTable(table);
	}
	

}

	
