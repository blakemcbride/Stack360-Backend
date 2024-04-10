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

import com.arahant.business.BCompany;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.File;

public class QuickReport extends ReportBase {

	public QuickReport(String title) {
		super("QuickRpt", "");
	}
	Image fCrystal;

	public String build(String msg) throws DocumentException {
		try {
			try {
				final File fCrystalf = BCompany.getReportLogo(null);

				fCrystal = Image.getInstance(fCrystalf.getAbsolutePath());
				fCrystal.scaleToFit(1000, 80);
				//fCrystal.scaleToFit(10, 5);
				//fCrystal.scalePercent(50);
			} catch (final Exception e) {
				logger.error(e);
			}

			PdfPTable table = makeTable(new int[]{30, 70});
			//write(table,"",false);
			writeImage(table, fCrystal, true);
			write(table, "", false);
			addTable(table);
			table = makeTable(new int[]{100});
			writeBoldCentered(table, "Requested Benefit Changes", 16F);
			addTable(table);
			super.getDocument().add(new Paragraph(msg));

		} finally {
			close();
		}

		return getFilename();
	}
}
