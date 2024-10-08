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
 * Created on May 14, 2008
 * 
 * Arahant
 */
package com.arahant.reports;


import java.io.FileNotFoundException;

import com.arahant.beans.ProjectPhase;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on May 14, 2008
 *
 */
public class ProjectPhaseReport extends ReportBase {
    
    public ProjectPhaseReport() {
    	super("prjph","Project Phase List");
    }

    public String build() throws FileNotFoundException, DocumentException {
        try {
            addHeaderLine();
            
            PdfPTable table = makeTable(new int[] {30, 60});
			
            writeColHeader(table, "Code", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            
            boolean alternateRow = true;
            
            final HibernateCriteriaUtil<ProjectPhase> hcu = this.hsu.createCriteria(ProjectPhase.class).orderBy(ProjectPhase.CODE);
			
            for (ProjectPhase phase : hcu.list()) {
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, phase.getCode(), alternateRow);
                write(table, phase.getDescription(), alternateRow);
            }


            addTable(table);

        } finally {
            close();
        }
        
        return getFilename();
    }
}

	
