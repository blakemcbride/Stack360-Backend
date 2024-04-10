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

import com.arahant.beans.ProjectView;
import com.arahant.beans.ProjectViewJoin;
import com.arahant.business.BProjectViewJoin;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;


/**
 *
 */
public class ProjectViewReport extends ReportBase {
	private static final int maxDepth = 8;
	private static final int numColumns = (maxDepth * 2) + 1; // adjust for each leading column having a bullet, and indent level that does not match column count
	private static final int indentColumnPercentage = 2;
	private int level = -1;
	private int[] columnWidths;
	
	public ProjectViewReport() {
		super("pvrpt", "Project View");
	}
	
	public String build(final String parentId) throws DocumentException {
		try {
            int remainingWidth = 100;
			PdfPTable table;
			ProjectView parent = null;
			
			// each column set is a bullet placeholder and summary column ...
			// the last column will be the remainder
			columnWidths = new int[numColumns];
			for (int idx = 0; idx < columnWidths.length - 1; idx+=2) {
				columnWidths[idx] = 2;
				remainingWidth -= 2;
				
				columnWidths[idx + 1] = indentColumnPercentage;
				remainingWidth -= indentColumnPercentage;
			}
			columnWidths[columnWidths.length - 1] = remainingWidth;
			
			if (!isEmpty(parentId)) {
				ProjectViewJoin pvj = hsu.get(ProjectViewJoin.class, parentId);
				parent=pvj.getChild();
				
				this.writeHeaderLine("Folder", parent.getNodeTitle());
			} else {
				this.writeHeaderLine("Folder", "(top)");
			}
				
			addHeaderLine();
			
			table = makeTable(columnWidths);
			
			doLevel(parent, table);
			
            addTable(table);
        } finally {
            close();
        }

        return getFilename();
	}
	
	private void doLevel(ProjectView parent, PdfPTable table) throws DocumentException {
		level++;
		
		if (level == maxDepth) {
			level--;
			return;
		}
		
		List <ProjectViewJoin> l = hsu.createCriteria(ProjectViewJoin.class)
			.eq(ProjectViewJoin.PARENT, parent)
			.orderBy(ProjectViewJoin.SEQ)
			.joinTo(ProjectViewJoin.CHILD)
			.eq(ProjectView.PERSON, hsu.getCurrentPerson())
			.list();
		
		for (ProjectViewJoin pvj : l) {
			BProjectViewJoin bPvj = new BProjectViewJoin(pvj);
			int columnsForSummary = numColumns - (level * 2) + 1; // adjust for indent level and this column's bullet

			// adjust for indent level
			for (int loop = 0; loop < level; loop++) {
				write(table, " "); // bullet
				write(table, " "); // summary
			}
			
			writeImage(table, "bullet_blue.png", true); // bullet
			writeLeft(table, bPvj.getSummary(), false, columnsForSummary); // summary
			
			// optionally write detail
			if (!isEmpty(bPvj.getDescription())) {
				writeLeft(table, " ", false, numColumns); // spacer
				
				// adjust for indent level
				for (int loop = 0; loop < level; loop++) {
					write(table, " "); // bullet
					write(table, " "); // summary
				}
				
				write(table, " "); // bullet
				writeLeft(table, bPvj.getDescription(), false, columnsForSummary); // detail
				
				writeLeft(table, " ", false, numColumns); // spacer
			}

			doLevel(pvj.getChild(), table);
		}
		
		level--;
	}
	
	public static void main (String args[]) {
		try {
			ProjectViewReport pvr = new ProjectViewReport();
		
			Person p=ArahantSession.getHSU().get(Person.class, "00001-0000000002");
			ArahantSession.getHSU().setCurrentPerson(p);
			pvr.build(null);
		} catch (DocumentException ex) {
			Logger.getLogger(ProjectViewReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
