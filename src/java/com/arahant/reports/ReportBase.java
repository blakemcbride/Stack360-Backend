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

import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Image;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 */
public class ReportBase extends PdfPageEventHelper {

	protected static final boolean demoMode = false;

	final public static int STYLE_BOLD = Font.BOLD;
	final public static int STYLE_UNDERLINE = Font.UNDERLINE;
	final public static int STYLE_BOLDITALIC = Font.BOLDITALIC;
	final public static int STYLE_ITALIC = Font.ITALIC;
	final public static int STYLE_STRIKETHRU = Font.STRIKETHRU;
	final public static int STYLE_NORMAL = Font.NORMAL;

	
	private static class ColHeader {

		public ColHeader() {
		}
		
		private String name;
		private int alignment;
		private int width;

		public int getAlignment() {
			return alignment;
		}

		public void setAlignment(int alignment) {
			this.alignment = alignment;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
	}
	
	protected static ArahantLogger logger = new ArahantLogger(ReportBase.class);
	protected Font baseFont;
	private Document document;
	protected BaseFont watermarkFont;
	protected PdfGState gstate;
	final BaseColor baseRowColor = new BaseColor(192, 192, 192);
	private BaseColor alternateRowColor = new BaseColor(192, 192, 192);
	protected HibernateSessionUtil hsu = ArahantSession.getHSU();
	private boolean confidential;
	private String subHeader;
	private File outputFile;  // with path

	protected boolean isEmpty(final String s) {
		return s == null || s.trim().equals("");
	}

	public ReportBase() throws ArahantException {
		this("", "", false);
	}

	public ReportBase(final String reportFileNameStart, final String title) throws ArahantException {
		this(reportFileNameStart, title, false);
	}

	public ReportBase(final String reportFileNameStart, final String title, boolean landscape) throws ArahantException {
		this(reportFileNameStart, title, landscape, 225F);
	}

	public ReportBase(final String reportFileNameStart, final String title, boolean landscape, float titleWidth) throws ArahantException {
		this(reportFileNameStart, title, landscape, titleWidth, 50F);
	}

	public ReportBase(final String reportFileNameStart, final String title, boolean landscape, float titleWidth, float top) throws ArahantException {
		this(reportFileNameStart, title, landscape, titleWidth, top, 50F, 50F, 50F);
	}

	public ReportBase(final String reportFileNameStart, final String title, boolean landscape, float titleWidth, float top, float bottom, float left, float right) throws ArahantException {
		makeNewDocument(reportFileNameStart, title, landscape, titleWidth, top, bottom, left, right);
	}

	public ReportBase(File reportFilename, final String title, boolean landscape, float titleWidth, float top) throws ArahantException {
		makeNewDocument(reportFilename, title, landscape, titleWidth, top, 50F, 50F, 50F);
	}

	public ReportBase(File reportFilename, final String title, boolean landscape, float titleWidth, float top, float bottom, float left, float right) throws ArahantException {
		makeNewDocument(reportFilename, title, landscape, titleWidth, top, bottom, left, right);
	}

	public void resetLandscape() throws Exception {
		document.close();
		makeNewDocument("rpt", title, true, 225F, 50F, 50F, 50F, 50F);
	}

	public static File createTempFile(final String prefix, final String suffix) throws IOException {
		return FileSystemUtils.createReportFile(prefix, suffix);
	}

	final public void makeNewDocument(final String reportFileNameStart, final String title, boolean landscape, float titleWidth, float top, float bottom, float left, float right) throws ArahantException {
		try {
			makeNewDocument(createTempFile(reportFileNameStart, ".pdf"), title, landscape, titleWidth, top, bottom, left, right);
		} catch (IOException e) {
			throw new ArahantException("Could not create report.", e);
		}
	}
	
	private String title;
	protected String absolutePath;
	private boolean landscape;

	final public void makeNewDocument(File reportFile, final String title, boolean landscape, float titleWidth, float top, float bottom, float left, float right) throws ArahantException {
		this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);

		PdfWriter pdfWriter;

		try {
			outputFile = reportFile;
			this.landscape = landscape;
			if (landscape)
				this.document = new Document(PageSize.LETTER.rotate(), left, right, top, bottom);
			else
				this.document = new Document(PageSize.LETTER, left, right, top, bottom);

			pdfWriter = PdfWriter.getInstance(this.document, new BufferedOutputStream(new FileOutputStream(outputFile)));
			pdfWriter.setPageEvent(this);

			this.document.open();
			// name without path
			String fileName = outputFile.getName();
			absolutePath = outputFile.getAbsolutePath();
			logger.debug("Creating report with filename " + fileName);

			if (!isEmpty(title))
				createHeader(title, titleWidth);

			this.title = title;

			writer = pdfWriter;
		} catch (final Exception e) {
			throw new ArahantException("Could not create report.", e);
		}
	}

	final public void setConfidential() {
		this.confidential = true;
	}

	protected void writeHeaderLine(final String name) throws DocumentException {
		PdfPCell cell;
		PdfPTable table;

		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(5F);

		cell = new PdfPCell(new Paragraph(name, this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
		this.document.add(table);
	}

	protected void writeHeaderLine(final String name, final String value) throws DocumentException {
		PdfPCell cell;
		PdfPTable table;

		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(5F);

		cell = new PdfPCell(new Paragraph(name + ": " + value.trim(), this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
		this.document.add(table);
	}

	protected void writeHeaderLine(final String name, final String value, final float space) throws DocumentException {
		PdfPCell cell;
		PdfPTable table;

		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(space);

		cell = new PdfPCell(new Paragraph(name + ": " + value.trim(), this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
		this.document.add(table);
	}

	protected void createHeader(final String title) throws DocumentException {
		this.createHeader(title, 225F);
	}

	protected void createHeader(final String title, final float titleWidth) throws DocumentException {
		PdfPCell cell;
		PdfPTable table;

		// report header
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setTotalWidth(titleWidth);
		table.setLockedWidth(true);

		cell = new PdfPCell(new Paragraph(title, new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8F);
		cell.setPaddingTop(5F);
		table.addCell(cell);

		this.document.add(table);

		// run date
		if (demoMode)
			writeHeaderLine("Run Date", "9/15/20xx 3:20 PM");
		else
			writeHeaderLine("Run Date", DateUtils.getDateTimeFormatted(DateUtils.now(), DateUtils.nowTime()));
	}

	protected void addSortInfo(final String sortBy, final String sortDirection) throws DocumentException {
		PdfPCell cell;
		PdfPTable table;

		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(5F);

		cell = new PdfPCell(new Paragraph("Sort By: " + sortBy, this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Sort Direction: " + sortDirection, this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);

	}

	protected void seperatorLine(PdfPTable table) throws DocumentException {
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.setBackgroundColor(new BaseColor(192, 192, 192));
		cell.disableBorderSide(1 | 4 | 8);
		table.addCell(cell);
	}

	protected void addHeaderLine() throws DocumentException {
		PdfPCell cell;
		PdfPTable table;
//		 header line
		table = new PdfPTable(1);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.setBackgroundColor(new BaseColor(192, 192, 192));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);
	}

	protected void writeImage(final PdfPTable table, final String name) throws ArahantException {
		writeImage(table, readImageBytes(name));
	}

	protected void writeImage(final PdfPTable table, final String name, boolean autoScale) throws ArahantException {
		writeImage(table, readImageBytes(name), autoScale);
	}

	protected void writeImage(final PdfPTable table, final byte[] imageBytes) throws ArahantException {
		writeImage(table, imageBytes, false);
	}

	protected void writeImage(final PdfPTable table, final byte[] imageBytes, boolean autoScale) throws ArahantException {
		try {
			final Image image = Image.getInstance(imageBytes);

			writeImage(table, image, autoScale);
		} catch (final Exception e) {
			throw new ArahantException(e);
		}
	}

	protected void writeImage(final PdfPTable table, final Image image, boolean autoScale) throws ArahantException {
		try {
			PdfPCell cell;

			if (autoScale) {
				cell = new PdfPCell();
				cell.addElement(image);
			} else
				cell = new PdfPCell(image);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.disableBorderSide(1 | 2 | 4 | 8);
			table.addCell(cell);
		} catch (final Exception e) {
			throw new ArahantException(e);
		}

	}

	protected Image readImage(final String name) throws ArahantException {
		try {
			return Image.getInstance(readImageBytes(name));
		} catch (final Exception e) {
			throw new ArahantException(e);
		}
	}

	protected Image readImage(final String name, final float height, final float width) throws ArahantException {
		final Image image = this.readImage(name);

		image.scaleToFit(width, height);

		return image;
	}

	protected byte[] readImageBytes(final String name) throws ArahantException {
		try {
			if (isEmpty(name))
				return null;

			final InputStream is = ReportBase.class.getResourceAsStream(name);

			final ByteArrayOutputStream bos = new ByteArrayOutputStream();

			final byte[] b = new byte[1024];

			int count = is.read(b);

			while (count > 0) {
				bos.write(b, 0, count);
				count = is.read(b);
			}

			return bos.toByteArray();
		} catch (final Exception e) {
			throw new ArahantException(e);
		}
	}

	protected void writeColHeader(final PdfPTable table, final String str) {
		writeColHeader(table, str, Element.ALIGN_CENTER);
		table.setHeaderRows(1);
	}

	protected void writeColHeader(final PdfPTable table, final String str, final int alignment) {
		this.writeColHeader(table, str, alignment, 1);
	}

	protected void writeColHeader(final PdfPTable table, final String str, final int alignment, int colSpan) {
		this.writeColHeader(table, str, alignment, colSpan, false);
	}

	protected void writeColHeader(final PdfPTable table, final String str, final int alignment, int colSpan, final boolean altColor) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setColspan(colSpan);
		table.addCell(cell);
		cell.setRotation(90);
	}

	protected void writeColHeaderRotate(final PdfPTable table, final String str) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setRotation(270);
		table.addCell(cell);
	}

	protected void writeColHeaderBold(PdfPTable table, String str, float f) {
		final Font font = new Font(FontFamily.COURIER, f, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeColHeaderBold(PdfPTable table, String str, int alignment, float size) {
		final Font font = new Font(FontFamily.COURIER, size, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeColHeaderBold(PdfPTable table, String str, int alignment, float size, int colSpan) {
		final Font font = new Font(FontFamily.COURIER, size, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setColspan(colSpan);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeCentered(final PdfPTable table, final String str) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeCenteredWithBorder(final PdfPTable table, final String str) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		//cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeCenteredWithBorder(final PdfPTable table, final String str, final Font font) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		//cell.disableBorderSide(1 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeCenteredNoBorder(final PdfPTable table, final String str) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeCenteredNoBorder(final PdfPTable table, final String str, final boolean altColor, int colSpan) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, this.baseFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(colSpan);
		table.addCell(cell);
	}

	protected void writeBoldCentered(final PdfPTable table, final String str, final float size) {
		final Font font = new Font(FontFamily.COURIER, size, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
	}

	protected void writeBoldRight(final PdfPTable table, final String str, final float size) {
		final Font font = new Font(FontFamily.COURIER, size, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
	}

	protected void writeBoldLeft(final PdfPTable table, final String str, final float size) {
		this.writeBoldLeft(table, str, size, 1);
	}

	protected void writeBoldLeft(final PdfPTable table, final String str, final float size, int colSpan) {
		final Font font = new Font(FontFamily.COURIER, size, Font.BOLD);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, font));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(colSpan);
		table.addCell(cell);
	}

	protected void writeHeader(final PdfPTable table, final String str) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeRight(final PdfPTable table, final String str, final boolean altColor) {
		writeRight(table, str, altColor, 1);
	}

	protected void writeRight(final PdfPTable table, final String str, final boolean altColor, final int colSpan) {
		writeRight(table, str, altColor, 1, Element.ALIGN_MIDDLE);
	}

	protected void writeRight(final PdfPTable table, final String str, final boolean altColor, final int colSpan, final int vertAlign) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(str, this.baseFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(vertAlign);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(colSpan);
		table.addCell(cell);
	}

	protected void writeRight(final PdfPTable table, final String str, final Font bodyFont) {
		if (bodyFont == null) {
			writeRight(table, str, false);
			return;
		}
		Paragraph p = new Paragraph(str, bodyFont);

		final PdfPCell cell = new PdfPCell(p);

		cell.setLeading(0f, 1.25f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
	}

	protected void writeLeft(final PdfPTable table, final String str, final boolean altColor) {
		writeLeft(table, str, altColor, 1);
	}

	protected void writeLeft(final PdfPTable table, final String str, final boolean altColor, int colSpan) {
		writeLeft(table, str, altColor, colSpan, Element.ALIGN_MIDDLE);
	}

	protected void writeLeft(final PdfPTable table, final String str, final boolean altColor, int colSpan, int vertAlign) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, this.baseFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(vertAlign);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(colSpan);
		table.addCell(cell);
	}

	protected void writeAlign(final PdfPTable table, final String str, final boolean altColor) {
		if (str == null) {
			writeAlign(table, "", Element.ALIGN_LEFT, altColor);
			return;
		}
		if (str.startsWith("$")) {
			writeAlign(table, str, Element.ALIGN_RIGHT, altColor);
			return;
		}

		if (str.startsWith("($")) {
			writeAlign(table, str, Element.ALIGN_RIGHT, altColor);
			return;
		}
		writeAlign(table, str, Element.ALIGN_LEFT, altColor);
	}

	protected void writeAlign(final PdfPTable table, final String str, final int horAlign, final boolean altColor) {
		writeAlign(table, str, horAlign, altColor, 1, baseFont.getSize(), Element.ALIGN_MIDDLE);
	}

	protected void writeAlign(final PdfPTable table, final String str, final int horAlign, final boolean altColor, final int colSpan) {
		writeAlign(table, str, horAlign, altColor, colSpan, baseFont.getSize(), Element.ALIGN_MIDDLE);
	}

	protected void writeAlign(final PdfPTable table, final String str, final int horAlign, final boolean altColor, final int colSpan, final float fontSize) {
		writeAlign(table, str, horAlign, altColor, colSpan, fontSize, Element.ALIGN_MIDDLE);
	}

	protected void writeAlign(final PdfPTable table, final String str, final int horAlign, final boolean altColor, final int colSpan, final float fontSize, final int vertAlign) {
		Font currentFont = new Font(FontFamily.COURIER, fontSize, Font.NORMAL);
//		baseFont.setSize(fontSize);
		final PdfPCell cell = new PdfPCell(new Paragraph(str, currentFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.setHorizontalAlignment(horAlign);
		cell.setVerticalAlignment(vertAlign);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(colSpan);
		table.addCell(cell);
	}

	protected void writeAlign(final PdfPTable table, final int i, final boolean altColor) {
		writeRight(table, i + "", altColor);
	}

	protected void writeAlign(final PdfPTable table, final double d, final boolean altColor) {
		writeRight(table, d + "", altColor);
	}

	protected void writeAlign(final PdfPTable table, final double d, final int places, final boolean altColor) {
		writeRight(table, com.arahant.utils.Formatting.formatNumber(d, places), altColor);
	}

	protected void writeAlign(final PdfPTable table, final String str) {
		if (str == null) {
			writeLeft(table, "", false);
			return;
		}
		if (str.startsWith("$")) {
			writeRight(table, str, false);
			return;
		}
		writeLeft(table, str, false);
	}

	protected void writeAlign(final PdfPTable table, final int i) {
		writeRight(table, i + "", false);
	}

	protected void writeAlign(final PdfPTable table, final double d) {
		writeRight(table, d + "", false);
	}

	protected void write(final PdfPTable table, final String str, final boolean altColor) {
		if (str == null) {
			writeLeft(table, "", altColor);
			return;
		}
		if (str.startsWith("$")) {
			writeRight(table, str, altColor);
			return;
		}

		if (str.startsWith("($")) {
			writeRight(table, str, altColor);
			return;
		}
		writeLeft(table, str, altColor);
	}

	protected void write(final PdfPTable table, final int i, final boolean altColor) {
		writeRight(table, i + "", altColor);
	}

	protected void write(final PdfPTable table, final double d, final boolean altColor) {
		writeRight(table, d + "", altColor);
	}

	protected void write(final PdfPTable table, final double d, final int places, final boolean altColor) {
		writeRight(table, com.arahant.utils.Formatting.formatNumber(d, places), altColor);
	}

	protected void write(final PdfPTable table, final String str) {
		if (str == null) {
			writeLeft(table, "", false);
			return;
		}
		if (str.startsWith("$")) {
			writeRight(table, str, false);
			return;
		}
		writeLeft(table, str, false);
	}

	protected void write(final PdfPTable table, final int i) {
		writeRight(table, i + "", false);
	}

	protected void write(final PdfPTable table, final double d) {
		writeRight(table, d + "", false);
	}

	@Override
	final public void onOpenDocument(final PdfWriter writer, final Document document) {
		try {
			this.watermarkFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		} catch (final Exception e) {
			logger.error(e);
			throw new ExceptionConverter(e);
		}

		this.gstate = new PdfGState();
		this.gstate.setFillOpacity(0.2f);
		this.gstate.setStrokeOpacity(0.2f);
	}

	protected void setSubHeader(String s) {
		subHeader = s;
	}

	protected void setNoPageNumbers() {
		pageNumbers = false;
	}
	private boolean pageNumbers = true;

	protected void setNoColumnHeadersOnNextPage() {
		columnHeaders = false;
	}
	
	private boolean columnHeaders = true;
	private List<ColHeader> columns;

	protected void addColumnHeader(final String columnName, final int alignment, final int width) {
		if (columns == null)
			columns = new ArrayList<ColHeader>();
		ColHeader col = new ColHeader();
		col.setName(columnName);
		col.setAlignment(alignment);
		col.setWidth(width);
		columns.add(col);
	}

	public int[] getColHeaderWidths() {
		int[] w = new int[columns.size()];
		int i = 0;
		for (ColHeader c : columns) {
			w[i] = c.getWidth();
			i++;
		}

		return w;
	}

	public void writeColHeaders(final PdfPTable table) {
		if (columns != null)
			for (ColHeader c : columns)
				writeColHeader(table, c.getName(), c.getAlignment());
	}

	@Override
	public void onStartPage(final PdfWriter writer, final Document document) {
		//called on every page at the start
		final int pageNum = writer.getPageNumber();

		if (columnHeaders && pageNum > 1)
			try {

				if (columns != null) {
					//somewhere before this make a call and save column header info
					final Rectangle page = document.getPageSize();
					final PdfPTable header = makeTable(getColHeaderWidths());
					header.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
					header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					header.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

					for (ColHeader c : columns)
						writeColHeader(header, c.getName(), c.getAlignment());
					if (!isEmpty(subHeader))
						writeColHeader(header, subHeader, Element.ALIGN_CENTER, columns.size());
					float offset = baseFont.getSize() + 2;

					if (!isEmpty(subHeader))
						offset *= 2;

					if (landscape)
						offset += 8;
					header.writeSelectedRows(0, -1, document.leftMargin(), document.getPageSize().getHeight() - document.topMargin() + offset + 1, writer.getDirectContent());


				}
			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}
	}

	@Override
	public void onEndPage(final PdfWriter writer, final Document document) {
		final int pageNum = writer.getPageNumber();

		if (pageNum > 1)
			try {
				final Rectangle page = document.getPageSize();
				final Font defaultFont = new Font(FontFamily.COURIER, 10F, Font.ITALIC);
				final PdfPTable footer = new PdfPTable(1);
				footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
				footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				if (pageNumbers)
					footer.addCell(new Paragraph("Page " + pageNum, defaultFont));
				footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}

		if (confidential)
			try {
				final PdfContentByte contentUnder = writer.getDirectContentUnder();
				contentUnder.saveState();
				contentUnder.setGState(gstate);
				contentUnder.beginText();
				contentUnder.setFontAndSize(this.watermarkFont, 68);
				contentUnder.showTextAligned(Element.ALIGN_CENTER, "CONFIDENTIAL", document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
				contentUnder.endText();
				contentUnder.restoreState();
			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}
	}
	
	private PdfWriter writer;

	public void writeFooter(final String text) {

		try {
			final Rectangle page = document.getPageSize();
			final PdfPTable footer = new PdfPTable(1);
			footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(new Paragraph(text, this.baseFont));
			footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

		} catch (final Exception e) {
			throw new ExceptionConverter(e);
		}

	}

	public void writeFooter(final String text, final Font font) {

		try {
			final Rectangle page = document.getPageSize();
			final PdfPTable footer = new PdfPTable(1);
			footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(new Paragraph(text, font));
			footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

		} catch (final Exception e) {
			throw new ExceptionConverter(e);
		}

	}

	protected void close() {
		try {
			if (this.document != null) {
				this.document.close();
				this.document = null;
			}
		} catch (final Exception ignored) {
		}

	}

	protected int getColumnCount(final int base, final boolean[] col) {
		int columnCount = base; //name is always there

		for (final boolean element : col)
			if (element)
				columnCount++;

		return columnCount;
	}

	final public String getFilename() {
		return FileSystemUtils.getHTTPPath(outputFile);
	}

	protected PdfPTable createTable(final int columns, final boolean[] optionalCols) {
		final PdfPTable table = new PdfPTable(this.getColumnCount(columns, optionalCols));
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
		return table;
	}

	protected PdfPTable makeTable(final int ... widths) throws DocumentException {
		final PdfPTable table = new PdfPTable(widths.length);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(widths);
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
		return table;
	}

	protected PdfPTable makeSubTable(final int ... widths) throws DocumentException {
		final PdfPTable table = new PdfPTable(widths.length);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(0F);
		table.setWidths(widths);
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
		return table;
	}

	protected void addTable(final PdfPTable table) throws DocumentException {
		document.add(table);
	}

	protected void newPage() throws DocumentException {
		document.newPage();
	}

	protected void blankLine() throws DocumentException {
		final PdfPTable table = makeTable(100);
		write(table, "");
		addTable(table);
	}

	protected void writeLine(String txt) throws DocumentException {
		PdfPTable table = makeTable(100);
		write(table, txt, false);
		addTable(table);
	}

	protected void writeLine(String txt, float fontSize, int style) throws DocumentException {
		PdfPTable table = makeTable(100);
//		Font currentFont = new Font(FontFamily.COURIER, fontSize, Font.NORMAL);
//		Font currentFont = new Font(FontFamily.COURIER, fontSize, Font.BOLD);
		Font currentFont = new Font(FontFamily.COURIER, fontSize, style);
		PdfPCell cell = new PdfPCell(new Paragraph(txt, currentFont));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		table.addCell(cell);
		addTable(table);
	}

	protected void resetPageCount() throws DocumentException {
		document.setPageCount(1);
	}

	/**
	 * @param str string
	 * @throws DocumentException
	 */
	protected void writeCentered(final String str) throws DocumentException {
		final PdfPTable table = makeTable(100);
		writeCentered(table, str);
		addTable(table);
	}

	protected void writeHeaderLine(String string, boolean includeAccepting) throws DocumentException {
		writeHeaderLine(string, includeAccepting ? "Yes" : "No");
	}

	/**
	 * DO NOT USE THIS METHOD. IT IS HERE FOR InvoiceReport ONLY FOR A LIMITED
	 * TIME.
	 *
	 * @return
	 */
	protected Document getDocument() {
		// TODO Remove method when InvoiceReport is totally converted
		return this.document;
	}

	protected double roundToTenths(final double x) {
		return (double) Math.round(x * 10) / 10;
	}

	protected double roundToHundredths(final double x) {
		return (double) Math.round(x * 100) / 100;
	}

	private static class Header {
		int width;
		String title;

		private Header(String s, int i) {
			title = s;
			width = i;
		}
	}

	protected void addHeader(String title, int width) {
		headers.add(new Header(title, 10));
	}
	
	private final ArrayList<Header> headers = new ArrayList<>();

	protected PdfPTable makeTableFromHeader() throws DocumentException {
		int[] t = new int[headers.size()];
		for (int loop = 0; loop < t.length; loop++)
			t[loop] = headers.get(loop).width;
		PdfPTable ret = makeTable(t);

		for (Header h : headers)
			writeColHeader(ret, h.title);

		return ret;
	}

	protected void writeNameAndAddress(PdfPTable table, BPerson bp, Font bodyFont) {
		String addr = bp.getNameFML() + "\n";
		addr += bp.getStreet() + "\n";
		if (!isEmpty(bp.getStreet2()))
			addr += bp.getStreet2();


		//write(table, bp.getNameFML(), bodyFont);
		//write(table, bp.getStreet(), bodyFont);
		//if (!isEmpty(bp.getStreet2()))
		//	write(table, bp.getStreet2(), bodyFont);
		String csz = bp.getCity() + ", " + bp.getState() + "  " + bp.getZip();
		//if (!csz.trim().equals(","))
		//	write(table, bp.getCity()+", "+bp.getState()+"  "+bp.getZip(), bodyFont);

		addr += csz + "\n";
		write(table, addr, bodyFont);
	}

	protected void write(final PdfPTable table, final String str, final Font bodyFont) {
		if (bodyFont == null) {
			write(table, str);
			return;
		}
		Paragraph p = new Paragraph(str, bodyFont);

		final PdfPCell cell = new PdfPCell(p);

		cell.setLeading(0f, 1.25f);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
	}

	protected BaseColor getAlternateRowColor() {
		return alternateRowColor;
	}

	protected void setAlternateRowColor(BaseColor alternateRowColor) {
		this.alternateRowColor = alternateRowColor;
	}

	protected void setToBaseRowColor() {
		this.alternateRowColor = baseRowColor;
	}

	protected int getBaseFontSize() {
		return (int) baseFont.getSize();
	}

	protected void setBaseFontSize(int fontSize) {
		this.baseFont.setSize(fontSize);
	}

	protected void setBaseFontSize(float fontSize) {
		this.baseFont.setSize(fontSize);
	}

	protected int[] createColumnWidths(int colNumber) {
		int[] widths = new int[colNumber];

		for (int i = 0; i < colNumber; i++)
			widths[i] = 100 / colNumber;

		return widths;
	}
}
