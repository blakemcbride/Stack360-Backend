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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * Base class for direct drop PDF reports.  These are existing PDFs where absolute positioning
 * of text will be directly dropped on top of it.
 * 
 * Created on Oct 27, 2008
 */
public abstract class ReportBaseDirectDrop {
	private static final String REPORT_DIR = "reports";
	private String fileName;
	private PdfStamper pdfStamper;
	private BaseFont baseFont;
	private int currentPage;
	protected HibernateSessionUtil hsu = ArahantSession.getHSU();
	
	public ReportBaseDirectDrop(final String reportFileNameStart, final String backgroundPdfFileName) throws ArahantException {
		try {
			File tempFile;
			if (reportFileNameStart.length() > 1  &&  reportFileNameStart.charAt(0) == '/') {
				this.fileName = reportFileNameStart.endsWith(".pdf") ? reportFileNameStart : reportFileNameStart + ".pdf";
				tempFile = new File(this.fileName);
			} else {
				tempFile = createTempFile(reportFileNameStart, reportFileNameStart.endsWith(".pdf") ? "" : ".pdf");
			    this.fileName = tempFile.getName();
			}
			PdfReader backgroundPdfReader = new PdfReader(backgroundPdfFileName);
			this.pdfStamper = new PdfStamper(backgroundPdfReader, new FileOutputStream(tempFile));
			this.baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			this.currentPage = 1;
		} catch (final Exception e) {
			throw new ArahantException("Could not create report.",e);
		} 	
	}
	
	public void close() {
		try {
			if (pdfStamper != null)
				pdfStamper.close();
		} catch (final Exception ignored) {
		} finally {
			pdfStamper = null;
		}	
	}
	
	/**
	 * Executes the building of a the report and returns the report file name
	 * 
	 * @return the new report's file name
	 * @throws com.arahant.exceptions.ArahantException
	 */
	public String executeReport() throws ArahantException {
		try {
			this.build();
		} catch (final Exception e) {
			throw new ArahantException(e);
		} finally {
			close();
        }
		if (this.fileName.length() > 1  &&  this.fileName.charAt(0) == '/')
			return this.fileName;
		else
			return "/" + REPORT_DIR + "/" + this.fileName;
	}
	
	/**
	 * Required method that will render out text to the PDF using the direct drop methods.
	 * @throws java.lang.Exception
	 */
	protected abstract void build() throws Exception;
	
	/**
	 * Checks if a string is empty
	 * @param str string to check
	 * @return true if null or zero length
	 */
	protected boolean isEmpty(final String str) {
		return (str == null || str.trim().equals(""));
	}
	
	/**
	 * Writes text left aligned from a point with the default font size of 12
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 */
	protected void writeTextLeft(String text, float x, float y) {
		this.writeTextLeft(text, x, y, 12);
	}
	
	/**
	 * Writes text left aligned from a point
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 * @param fontSize the font size in which to write the text
	 */
	protected void writeTextLeft(String text, float x, float y, float fontSize) {
		this.writeText(PdfContentByte.ALIGN_LEFT, text, x, y, fontSize);
	}
	
	/**
	 * Writes text right aligned from a point with the default font size of 12
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 */
	protected void writeTextRight(String text, float x, float y) {
		this.writeTextRight(text, x, y, 12);
	}
	
	/**
	 * Writes text right aligned from a point
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 * @param fontSize the font size in which to write the text
	 */
	protected void writeTextRight(String text, float x, float y, float fontSize) {
		this.writeText(PdfContentByte.ALIGN_RIGHT, text, x, y, fontSize);
	}
	
	/**
	 * Writes text centered on a point with the default font size of 12
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 */
	protected void writeTextCentered(String text, float x, float y) {
		this.writeTextCentered(text, x, y, 12);
	}
	
	/**
	 * Writes text centered on a point
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 * @param fontSize the font size in which to write the text
	 */
	protected void writeTextCentered(String text, float x, float y, float fontSize) {
		this.writeText(PdfContentByte.ALIGN_CENTER, text, x, y, fontSize);
	}
	
	/**
	 * Writes text rotated with the default font size of 12
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 * @param angle in degrees
	 */
	protected void writeTextRotated(String text, float x, float y, float angle) {
		this.writeTextRotated(text, x, y, angle, 12);
	}
	
	/**
	 * Writes text rotated with the default font size of 12
	 * 
	 * @param text the text to write
	 * @param x the x coordinate relative to the bottom left of the page
	 * @param y the y coordinate relative to the bottom left of the page
	 * @param angle in degrees
	 * @param fontSize the font size in which to write the text
	 */
	private void writeTextRotated(String text, float x, float y, float angle, float fontSize) {
		double angleInRadians = Math.toRadians(angle);
		float a = (float) Math.cos(angleInRadians);
		float b = (float) Math.sin(angleInRadians);
		float c = (float) -Math.sin(angleInRadians);
		float d = (float) Math.cos(angleInRadians);
		float e = x;
		float f = y;

		PdfContentByte pdfOverContent = this.pdfStamper.getOverContent(this.currentPage);
		
		pdfOverContent.beginText();
					
		pdfOverContent.setFontAndSize(this.baseFont, fontSize);
		
		pdfOverContent.setTextMatrix((float)a, b, c, d, e, f);
		
		pdfOverContent.showText(text);
		
		pdfOverContent.endText();
	}
	
	/**
	 * Moves to the next direct drop page of the PDF
	 * 
	 * @throws com.itextpdf.text.DocumentException
	 */
	protected void nextDirectDropPage() throws DocumentException {
		// get the current page at least once before moving on to be sure it is included in final
		// document in correct order
		PdfContentByte pdfOverContent = this.pdfStamper.getOverContent(this.currentPage);
		
		this.currentPage++;
	}
	
	/**
	 * 
	 * @param wdth  line width
	 * @param weight 0=black, 1=white
	 * @param x1  starting point
	 * @param y1
	 * @param x2  ending point
	 * @param y2 
	 */
	protected void drawLine(float wdth, float weight, float x1, float y1, float x2, float y2) {
		PdfContentByte cb = pdfStamper.getOverContent(currentPage);
		cb.setLineWidth(wdth);
		cb.setGrayStroke(weight);  //  0=black, 1=white
		cb.moveTo(x1, y1);
		cb.lineTo(x2, y2);
		cb.stroke();
	}
	
	protected void addGrid() {
		final int fontSize = 5;
		final int leftOffset = 15;  //  10=too small
		final int bottomOffset = 15;
		final int topOffset = 760;
		final int rightOffset = 580;
		final int smallLine = 10;
		final int longLine = 20;
		int x, y;
		
		for (y = 20; y < 780; y += 10)
			if (y % 50 == 0) {
				drawLine(.025f, 0, leftOffset, y, leftOffset + longLine, y);
				writeTextLeft("" + y, leftOffset, y + 3, fontSize);
				drawLine(.025f, 0, rightOffset, y, rightOffset + longLine, y);
				writeTextLeft("" + y, rightOffset + (longLine - smallLine), y + 3, fontSize);
			} else if (y % 10 == 0) {
				drawLine(.01f, 0, leftOffset, y, leftOffset + smallLine, y);
				drawLine(.01f, 0, rightOffset + (longLine - smallLine), y, rightOffset + smallLine + (longLine - smallLine), y);
			}
		for (x=20 ; x < 600 ; x += 10)
			if (x % 50 == 0) {
				drawLine(.025f, 0, x, bottomOffset, x, bottomOffset + longLine);
				writeTextLeft("" + x, x + 3, bottomOffset + smallLine + 3, fontSize);
				drawLine(.025f, 0, x, topOffset, x, topOffset + longLine);
				writeTextLeft("" + x, x + 3, topOffset + (longLine - smallLine) - 7, fontSize);
			} else if (x % 10 == 0) {
				drawLine(.025f, 0, x, bottomOffset, x, bottomOffset + smallLine);
				drawLine(.025f, 0, x, topOffset + (longLine - smallLine), x, topOffset + smallLine + (longLine - smallLine));
			}
	}
	
	private void writeText(int alignment, String text, float x, float y, float fontSize) {
		if (text == null  ||  text.length() == 0)
			return;
		PdfContentByte pdfOverContent = this.pdfStamper.getOverContent(this.currentPage);
		
		pdfOverContent.beginText();
					
		pdfOverContent.setFontAndSize(this.baseFont, fontSize);
		
		pdfOverContent.showTextAligned(alignment, text, x, y, 0);
			
		pdfOverContent.endText();
	}
	
	private static File createTempFile(final String prefix, final String suffix) throws IOException {
		File reportDir = new File(FileSystemUtils.getWorkingDirectory(), REPORT_DIR);
		
		if (!reportDir.exists()) {
			reportDir.mkdir();
		}
		
		final File tempFile = File.createTempFile(prefix, suffix, reportDir);
		final File[] tempFiles = reportDir.listFiles();
		
		if (tempFiles != null) {
			// look for any files with this prefix and suffix that are already there and a couple of days old
			long twoDaysAgo = new Date().getTime() - (2*24*60*60*1000);
		
			for (final File element : tempFiles)
				if (element.lastModified() < twoDaysAgo)
					element.delete();
		}
		
		return tempFile;
	}
}

	
