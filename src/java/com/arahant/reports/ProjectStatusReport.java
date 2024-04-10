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
 * Created on Jan 24, 2007
 * 
 */
package com.arahant.reports;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.ProjectStatus;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



/**
 * 
 *
 * Created on Jan 24, 2007
 *
 */
public class ProjectStatusReport extends ReportBase {

	
    
    public ProjectStatusReport() {
    	super("prjst","Project Status List");
    }

	public String build() throws FileNotFoundException, DocumentException {

        
        try {
            addHeaderLine();
			
			PdfPTable table = makeTable(new int[]{30,50,20});
			
            writeColHeader(table, "Code", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
			writeColHeader(table, "Type", Element.ALIGN_LEFT);

			
			boolean alternateRow = true;
			 
			final HibernateCriteriaUtil <ProjectStatus> hcu = this.hsu.createCriteria(ProjectStatus.class).orderBy(ProjectStatus.CODE);
			
			for (ProjectStatus ps : hcu.list())
			{
				
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, ps.getCode(), alternateRow);
				write(table, ps.getDescription(), alternateRow);
				write(table, (ps.getActive()=='Y')?"Active":"Inactive", alternateRow);

			}
			
			
			addTable(table);

        } finally {
            close();
            
        }
		return getFilename();
	}
	
	
}

	
