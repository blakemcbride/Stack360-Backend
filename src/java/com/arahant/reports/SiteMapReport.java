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

import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;

/**
 * Created on Feb 28, 2008
 */
public class SiteMapReport extends ReportBase {
    private HibernateSessionUtil hsu = ArahantSession.getHSU();
    private boolean showIds;
    private boolean showLabels;
    private boolean showSubHeaders;
    private int totalColumns;
    private Image screenImage = readImage("screen.png");
    private Image screenGroupImage = readImage("screen-group.png");
    private Image screenGroupParentImage = readImage("parent-screen-group.png");
    private Image wizardImage = readImage("wizard.png");
	private HashSet<String> depth=new HashSet<String>();

    public SiteMapReport() throws ArahantException {
        super("SiteMap", "Site Map", false);
    }
    
    public String build(String startFromScreenGroupId, boolean showIds, boolean showLabels, boolean showSubHeaders) throws ArahantException {
        try {
            PdfPTable table;
            BScreenGroup screenGroup = isEmpty(startFromScreenGroupId) ? null : new BScreenGroup(startFromScreenGroupId);
            
            this.showIds = showIds;
            this.showLabels = showLabels;
            this.showSubHeaders = showSubHeaders;
            
            writeHeaderLine("Start from Screen Group", (screenGroup==null ? "(top level)" : screenGroup.getName()));
            writeHeaderLine("Show IDs", showIds?"Yes":"No");
            writeHeaderLine("Show Labels", showLabels?"Yes":"No");
            writeHeaderLine("Show Sub-Headers", showLabels?"Yes":"No");
            
            addHeaderLine();
            
            this.writeLegend();
            
            if (this.showIds) {
                if (this.showLabels) {
                    table = makeTable(new int[] { 3, 7, 45, 45 });
                    this.totalColumns = 4;                
                } else {
                    table = makeTable(new int[] { 3, 7, 90 });
                    this.totalColumns = 3;
                }
            } else {
                if (showLabels) {
                    table = makeTable(new int[] { 3, 49, 48 });
                    this.totalColumns = 3;
                } else {
                    table = makeTable(new int[] { 3, 97 });
                    this.totalColumns = 2;
                }
            }
            
            listForScreenGroup(table, screenGroup);
                        
            addTable(table);
		} catch (final Exception e) {
			throw new ArahantException(e);
		} finally {
            close();
        }
        
		return getFilename();
    }
    
    private void writeLegend() throws DocumentException {
        final PdfPTable table = makeTable(new int[] { 10, 3, 18, 1, 3, 12, 1, 3, 27, 1, 3, 18 });

        writeBordered(table, null, "Legend", 8);
        writeBordered(table, this.screenGroupImage, null, 4 | 8);
        writeBordered(table, null, "Screen Group", 4 | 8);
        writeBordered(table, null, " ", 4 | 8);
        writeBordered(table, this.screenImage, null, 4 | 8);
        writeBordered(table, null, "Screen", 4 | 8);
        writeBordered(table, null, " ", 4 | 8);
        writeBordered(table, this.screenGroupParentImage, null, 4 | 8);
        writeBordered(table, null, "Parent Screen Group", 4 | 8);
        writeBordered(table, null, " ", 4 | 8);
        writeBordered(table, this.wizardImage, null, 4 | 8);
        writeBordered(table, null, "Wizard Screen", 4);
        
        writeLeft(table, " ", false, 12);
        
        this.addTable(table);
    }
    
    private void writeBordered(PdfPTable table, Image image, String str, int disableBorders) {
        PdfPCell cell;
		
        if (image != null)
            cell = new PdfPCell(image);
        else
            cell = new PdfPCell(new Paragraph(str, this.baseFont));
        
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(disableBorders);
        cell.setPaddingBottom(2);
        cell.setPaddingTop(2);
        cell.setPaddingLeft(2);
        cell.setPaddingRight(2);
        table.addCell(cell);
    }
    
    private void listForScreenGroup(PdfPTable table, BScreenGroup parentScreenGroup) throws DocumentException {
        BScreenOrGroup[] screenOrGroups;
        boolean alternateRow = true;
//		if (parentScreenGroup!=null)
//			System.out.println("Doing screen group "+parentScreenGroup.getName());
        // write header
        this.writeScreenGroupHeader(table, parentScreenGroup);
        
        if (parentScreenGroup == null)
            screenOrGroups = BScreenGroup.listWithoutChildren(hsu);
        else
            screenOrGroups = parentScreenGroup.listChildren();
        
        // write sub-header
        if (this.showSubHeaders) {
            writeBoldLeft(table, " ", 8f);
            if (this.showIds)
                writeBoldRight(table, "ID", 8f);
            writeBoldLeft(table, "Name", 8f);
            if (this.showLabels)
                writeBoldLeft(table, "Label", 8f);
            
            alternateRow = false;
        }
        
        // write everything for this group (breadth)
        for (BScreenOrGroup screenOrGroup : screenOrGroups) {
            alternateRow = !alternateRow;
            if (screenOrGroup instanceof BScreen)
                this.writeScreen(table, parentScreenGroup, (BScreen)screenOrGroup, alternateRow);
            else
                this.writeScreenGroup(table, parentScreenGroup, (BScreenGroup)screenOrGroup, alternateRow);
        }

		if (parentScreenGroup!=null)
			depth.add(parentScreenGroup.getScreenGroupId());

        // write all sub-groups for this group (depth)
		for (BScreenOrGroup screenOrGroup : screenOrGroups)
			if (screenOrGroup instanceof BScreenGroup)
			{
				if (!depth.contains(screenOrGroup.getId()))
					this.listForScreenGroup(table, (BScreenGroup)screenOrGroup);
			}

		if (parentScreenGroup!=null)
			depth.remove(parentScreenGroup.getScreenGroupId());
    }
    
    private void writeScreenGroupHeader(PdfPTable table, BScreenGroup parentScreenGroup) throws DocumentException {        
        if (parentScreenGroup == null)
            writeColHeaderBold(table, "Top Level", Element.ALIGN_LEFT, 12f, this.totalColumns);
        else {
            writeLeft(table, " ", false, this.totalColumns);
            writeColHeaderBold(table, parentScreenGroup.getName(), Element.ALIGN_LEFT, 12f, this.totalColumns);
        }
    }
    
    private void writeScreenGroup(PdfPTable table, BScreenGroup parentScreenGroup, BScreenGroup screenGroup, boolean alternateRow) {
alternateRow=false;
        writeImage(table, (isEmpty(screenGroup.getParentScreenId())?this.screenGroupImage:this.screenGroupParentImage), false);
        if (this.showIds)
            writeRight(table, screenGroup.getExtId(), alternateRow);
        writeLeft(table, screenGroup.getName(), alternateRow);
        if (this.showLabels)
            writeLeft(table, parentScreenGroup==null?" ":parentScreenGroup.getScreenGroupButtonName(screenGroup.getScreenGroupId()), alternateRow);    
    }
    
    private void writeScreen(PdfPTable table, BScreenGroup parentScreenGroup, BScreen screen, boolean alternateRow) {
alternateRow=false;
        writeImage(table, (screen.getScreenType()==4?this.wizardImage:this.screenImage), false);
        if (this.showIds)
            writeRight(table, screen.getExtId(), alternateRow);
        writeLeft(table, screen.getName(), alternateRow);
        if (this.showLabels)
            writeLeft(table, parentScreenGroup.getScreenButtonName(screen.getScreenId()), alternateRow);    
    }

    public static void main(String args[]) {
        try {
            new SiteMapReport().build(null, true, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
