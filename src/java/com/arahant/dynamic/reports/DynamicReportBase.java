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
 *
 */
package com.arahant.dynamic.reports;

import com.arahant.beans.*;
import com.arahant.beans.Report;
import com.arahant.business.BReport;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.kissweb.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 *
 */
public class DynamicReportBase extends PdfPageEventHelper {

	protected static ArahantLogger logger = new ArahantLogger(DynamicReportBase.class);
	public static final int FORMAT_CODES_COUNT = 15;
	PdfWriter writer;
	protected Font baseFont;
	private Document document;
	protected BaseFont watermarkFont;
	protected PdfGState gstate;
	private String fileName;  // without path
	final BaseColor baseRowColor = new BaseColor(192, 192, 192);
	private BaseColor alternateRowColor = new BaseColor(192, 192, 192);
	protected HibernateSessionUtil hsu = ArahantSession.getHSU();
	private int totalColWidth = 110;
	protected String absolutePath;
	private boolean landscape;
	private Report report;
	private String reportName;
	private int reportType;
	private float pageOffsetLeft;
	private float pageOffsetTop;
	private float defaultFontSize;
	private int defaultTitleLines;
	private double defaultColSpacing;
	private char pageLocation;
	private String sortDescription;
	private String selectionDescription;
	List<ReportTitle> titles = new ArrayList<ReportTitle>();
	List<ReportGraphic> graphics = new ArrayList<ReportGraphic>();
	List<ReportColumn> selectedCols = new ArrayList<ReportColumn>();
	Document tempDocument;
	PdfPTable tempTable;
	PdfWriter tempPdfWriter;
	File tempFile;
	File outputFile;  // fileName with path info

	protected boolean isEmpty(final String s) {
		return s == null || s.trim().equals("");
	}

	public DynamicReportBase(Report report) throws ArahantException {
		this.report = report;
		reportName = report.getReportName();
		landscape = report.getPageOrientation() == 'L';
		reportType = report.getReportType();
		pageOffsetLeft = (float) (report.getPageOffsetLeft()) * 72;
		pageOffsetTop = (float) report.getPageOffsetTop() * 72;
		defaultFontSize = (float) report.getDefaultFontSize();
		defaultTitleLines = report.getLinesInColumnTitle();
		defaultColSpacing = report.getDefaultSpaceBetweenColumns();

//		titles.setReportId(report.getReportId());
//		pageLocation = titles.getPageLocation();
		//TODO seqno

		baseFont = new Font(FontFamily.COURIER, defaultFontSize, Font.NORMAL);
//		makeNewDocument(reportName, reportName, landscape, 225F, pageOffsetTop, pageOffsetTop, pageOffsetLeft, pageOffsetLeft);


		PdfWriter pdfWriter;

		try {
			outputFile = FileSystemUtils.createReportFile(reportName + (new Date().getTime()), ".pdf");

			if (landscape)
				document = new Document(PageSize.LETTER.rotate(), pageOffsetLeft, pageOffsetLeft, pageOffsetTop, pageOffsetTop);
			else
				document = new Document(PageSize.LETTER, pageOffsetLeft, pageOffsetLeft, pageOffsetTop, pageOffsetTop);

			pdfWriter = PdfWriter.getInstance(this.document, new BufferedOutputStream(new FileOutputStream(outputFile)));
			pdfWriter.setPageEvent(this);  //  ???

			document.open();
			fileName = outputFile.getName();
			absolutePath = outputFile.getAbsolutePath();
			logger.debug("Creating report with filename " + fileName);

			writer = pdfWriter;

			tempDocument = new Document(PageSize.LETTER, pageOffsetLeft, pageOffsetLeft, pageOffsetTop, pageOffsetTop);
			tempTable = new PdfPTable(1);

			try {
				tempFile = FileSystemUtils.createReportFile(reportName + "Temp" + (new Date().getTime()), ".temp.pdf");
				tempPdfWriter = PdfWriter.getInstance(tempDocument, new BufferedOutputStream(new FileOutputStream(tempFile)));
				tempDocument.open();
			} catch (Exception ex) {
				Logger.getLogger(DynamicReportBase.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			}
		} catch (final Exception e) {
			throw new ArahantException("Could not create report. ", e);
		}
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			createHeader(titles);
		} catch (DocumentException doc) {
		}
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		if (!selectedCols.isEmpty())
			try {
				document.add(createColumnTitles(selectedCols));
			} catch (DocumentException doc) {
			}
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			createFooter(titles);
		} catch (DocumentException doc) {
		}
	}

	protected void createHeader(final List<ReportTitle> titles) throws DocumentException {
		Collections.sort(titles);
		if (titles.size() > 0) {
			ReportTitle[] t = new ReportTitle[titles.size()];

			for (int i = 0; i < titles.size(); i++)
				t[i] = titles.get(i);

			createHeader(t);
		}
	}

	private void createHeader(final ReportTitle[] titles) throws DocumentException {
		if (titles.length > 0) {
			List<ReportTitle> titleList = new ArrayList<ReportTitle>();
			int columns = 0;
			int maxCols = 0;

			for (ReportTitle t : titles)
				if (t.getPageLocation() == 'A') {
					titleList.add(t);
					if (t.getEndLine() == 'Y') {
						maxCols = ++columns > maxCols ? columns : maxCols;
						columns = 0;
					} else
						columns++;
				}
			maxCols = columns > maxCols ? columns : maxCols;
			maxCols = maxCols * 2 - 1;

			if (maxCols > 0) {
				Collections.sort(titleList);
				PdfPCell cell;
				PdfPTable table = new PdfPTable(maxCols);
				float[] colWidths = new float[maxCols];
				for (int i = 0; i < maxCols; i++)
					if ((i % 2) == 0)
						colWidths[i] = (float) (totalColWidth - (((int) (maxCols / 2)) * (defaultColSpacing * 72))) / ((int) (maxCols / 2) + 1);
					else
						colWidths[i] = (float) (defaultColSpacing * 72);
				table.setWidths(colWidths);

				boolean spacerCol = false;
				for (ReportTitle t : titleList) {
					String text;
					float titleFontSize = (float) t.getFontSize();
					int titleFontStyle = Font.NORMAL;
					int justification = Element.ALIGN_LEFT;

					if (t.getFontBold() == 'Y')
						if (t.getFontItalic() == 'Y')
							if (t.getFontUnderline() == 'Y')
								titleFontStyle = Font.BOLD | Font.ITALIC | Font.UNDERLINE;
							else
								titleFontStyle = Font.BOLD | Font.ITALIC;
						else if (t.getFontUnderline() == 'Y')
							titleFontStyle = Font.BOLD | Font.UNDERLINE;
						else
							titleFontStyle = Font.BOLD;
					else if (t.getFontItalic() == 'Y')
						if (t.getFontUnderline() == 'Y')
							titleFontStyle = Font.ITALIC | Font.UNDERLINE;
						else
							titleFontStyle = Font.ITALIC;
					else if (t.getFontUnderline() == 'Y')
						titleFontStyle = Font.UNDERLINE;
					
					Font titleFont = new Font(FontFamily.COURIER, titleFontSize, titleFontStyle);

					if (t.getJustification() == 'C')
						justification = Element.ALIGN_CENTER;
					else if (t.getJustification() == 'R')
						justification = Element.ALIGN_RIGHT;

					switch (t.getFieldType()) {
						case 'T':
							text = t.getVerbiage();
							break;
						case 'D':
							text = DateUtils.getDateFormatted(DateUtils.now());
							break;
						case 'P':
							text = String.valueOf(document.getPageNumber() + 1);
							break;
						case 'R':
							text = reportName;
							break;
						case 'C':
							text = ArahantSession.getHSU().getCurrentCompany().getName();
							break;
						case 'L':
							text = ArahantSession.getHSU().getCurrentPerson().getNameFML();
							break;
						case 'S':
							text = sortDescription;
							break;
						case 'Q':
							text = selectionDescription;
							break;
						default:
							text = "";
					}

					int currentCols = maxCols;

					if (spacerCol) {
						cell = new PdfPCell();
						disableAllBorders(cell);
						table.addCell(cell);
//						spacerCol = false;
					}

					if (t.getEndLine() == 'N') {
						cell = new PdfPCell();
						cell.setPhrase(new Phrase(text, titleFont));
						cell.setHorizontalAlignment(justification);
						if (justification == Element.ALIGN_LEFT)
							cell.setPaddingLeft((float) t.getLeftOffset() * 72);
						disableAllBorders(cell);
						table.addCell(cell);
						currentCols--;
						spacerCol = true;
					} else {
						cell = new PdfPCell();
						cell.setPhrase(new Phrase(text, titleFont));
						cell.setHorizontalAlignment(justification);
						if (justification == Element.ALIGN_LEFT)
							cell.setPaddingLeft((float) t.getLeftOffset() * 72);
						disableAllBorders(cell);
						cell.setColspan(currentCols);
						table.addCell(cell);
						spacerCol = false;
					}
				}
				document.add(table);
			}
		}
	}

	protected void createFooter(final List<ReportTitle> titles) throws DocumentException {
		Collections.sort(titles);
		if (titles.size() > 0) {
			ReportTitle[] t = new ReportTitle[titles.size()];

			for (int i = 0; i < titles.size(); i++)
				t[i] = titles.get(i);

			createFooter(t);
		}
	}

	private void createFooter(final ReportTitle[] titles) throws DocumentException {
		if (titles.length > 0) {
			List<ReportTitle> titleList = new ArrayList<ReportTitle>();
			int columns = 0;
			int maxCols = 0;

			for (ReportTitle t : titles)
				if (t.getPageLocation() == 'B') {
					titleList.add(t);
					if (t.getEndLine() == 'Y') {
						maxCols = ++columns > maxCols ? columns : maxCols;
						columns = 0;
					} else
						columns++;
				}
			maxCols = columns > maxCols ? columns : maxCols;
			maxCols = maxCols * 2 - 1;

			if (maxCols > 0) {
				Collections.sort(titleList);
				PdfPCell cell;
				PdfPTable table = new PdfPTable(maxCols);
				float[] colWidths = new float[maxCols];
				for (int i = 0; i < maxCols; i++)
					if ((i % 2) == 0)
						colWidths[i] = (float) (totalColWidth - (((int) (maxCols / 2)) * (defaultColSpacing * 72))) / ((int) (maxCols / 2) + 1);
					else
						colWidths[i] = (float) (defaultColSpacing * 72);
				table.setWidths(colWidths);

				boolean spacerCol = false;
				for (ReportTitle t : titleList) {
					String text;
					float titleFontSize = (float) t.getFontSize();
					int titleFontStyle = Font.NORMAL;
					int justification = Element.ALIGN_LEFT;

					if (t.getFontBold() == 'Y')
						if (t.getFontItalic() == 'Y')
							if (t.getFontUnderline() == 'Y')
								titleFontStyle = Font.BOLD | Font.ITALIC | Font.UNDERLINE;
							else
								titleFontStyle = Font.BOLD | Font.ITALIC;
						else if (t.getFontUnderline() == 'Y')
							titleFontStyle = Font.BOLD | Font.UNDERLINE;
						else
							titleFontStyle = Font.BOLD;
					else if (t.getFontItalic() == 'Y')
						if (t.getFontUnderline() == 'Y')
							titleFontStyle = Font.ITALIC | Font.UNDERLINE;
						else
							titleFontStyle = Font.ITALIC;
					else if (t.getFontUnderline() == 'Y')
						titleFontStyle = Font.UNDERLINE;
					
					Font titleFont = new Font(FontFamily.COURIER, titleFontSize, titleFontStyle);

					if (t.getJustification() == 'C')
						justification = Element.ALIGN_CENTER;
					else if (t.getJustification() == 'R')
						justification = Element.ALIGN_RIGHT;

					switch (t.getFieldType()) {
						case 'T':
							text = t.getVerbiage();
							break;
						case 'D':
							text = DateUtils.getDateFormatted(DateUtils.now());
							break;
						case 'P':
							text = String.valueOf(document.getPageNumber() + 1);
							break;
						case 'R':
							text = reportName;
							break;
						case 'C':
							text = ArahantSession.getHSU().getCurrentCompany().getName();
							break;
						case 'L':
							text = ArahantSession.getHSU().getCurrentPerson().getNameFML();
							break;
						case 'S':
							text = sortDescription;
							break;
						case 'Q':
							text = selectionDescription;
							break;
						default:
							text = "";
					}

					int currentCols = maxCols;

					if (spacerCol) {
						cell = new PdfPCell();
						disableAllBorders(cell);
						table.addCell(cell);
//						spacerCol = false;
					}

					if (t.getEndLine() == 'N') {
						cell = new PdfPCell();
						final Rectangle page = document.getPageSize();
						disableAllBorders(cell);
						cell.setPhrase(new Phrase(text, titleFont));
						cell.setHorizontalAlignment(justification);
						table.addCell(cell);
						table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
						currentCols--;
						spacerCol = true;
					} else {
						cell = new PdfPCell();
						final Rectangle page = document.getPageSize();
						disableAllBorders(cell);
						cell.setPhrase(new Phrase(text, titleFont));
						cell.setHorizontalAlignment(justification);
						cell.setColspan(currentCols);
						table.addCell(cell);
						table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
						spacerCol = false;
					}
				}
				table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
			}
		}
	}

	protected void createColumns(final ReportColumn[] columns) throws DocumentException {
		List<ReportColumn> colList = new ArrayList<ReportColumn>();
		colList.addAll(Arrays.asList(columns));

		Collections.sort(colList);
		createColumns(colList);
	}

	protected void createColumns(final List<ReportColumn> columns) throws DocumentException {
		BReport br = new BReport(report);
		boolean altColor = false;
		List<ReportColumn> breakCols = new ArrayList<ReportColumn>();
		int reportBreakLevel = 0;
		SortBrokenComparator comparator = new SortBrokenComparator();

		/**
		 * DEFINITIONS "Broken column" - A column with a sort order that
		 * triggers a total row "Displayed column" - A column with the "Display
		 * Total" flag active. Displayed a different total based off of which
		 * column was "broken"/triggered it
		 */
		HashMap<ReportColumn, Double> columnGrandTotals = new HashMap<ReportColumn, Double>();  //Grand total values for all displayed columns (never reset) - Key: Displayed, Value: value
		HashMap<ReportColumn, Double> currentTotals = new HashMap<ReportColumn, Double>();  //Current total values for all displayed columns (reset upon change) - Kay: Displayed, Value = value
		HashMap<ReportColumn, HashMap<ReportColumn, Double>> columnRunningTotals = new HashMap<ReportColumn, HashMap<ReportColumn, Double>>(); //References current total values with their respective displayed columns - Key: Displayed, Value: Broken
		HashMap<ReportColumn, String> currentValues = new HashMap<ReportColumn, String>(); //Key: Displayed, Value: value
		HashMap<ReportColumn, Integer> displayedCols = new HashMap<ReportColumn, Integer>(); //Key: Displayed, Value: value
		HashMap<ReportColumn, List<ReportColumn>> brokenCols = new HashMap<ReportColumn, List<ReportColumn>>();
		List<ColumnDisplayObject> columnValues;

		Collections.sort(columns);
		syncColumnRows(columns);

		for (ReportColumn c : columns) {
			if (c.getDisplayTotals() == 'Y')
				displayedCols.put(c, (int) (c.getBreakLevel()));
			currentValues.put(c, "");
		}

		if (!displayedCols.isEmpty())
			reportBreakLevel = Collections.max(displayedCols.values()).intValue();
		for (ReportColumn c : columns)
			if (c.getSortOrder() <= reportBreakLevel && c.getSortOrder() != 0)
				brokenCols.put(c, ArahantSession.getHSU().createCriteria(ReportColumn.class).eq(ReportColumn.REPORT_ID, br.getReportId()).eq(ReportColumn.DISPLAY_TOTALS, 'Y').ge(ReportColumn.BREAK_LEVEL, c.getSortOrder()).list());
		for (ReportColumn c : brokenCols.keySet())
			currentTotals.put(c, 0.0);
		for (ReportColumn c : displayedCols.keySet()) {
			columnGrandTotals.put(c, 0.0);
			columnRunningTotals.put(c, currentTotals);
		}

		PdfPTable table = createColumnTitles(columns);

		List<ColumnDisplayObject> row = br.next();
		while (row != null) {
			breakCols.clear();
			columnValues = getColumnValues(row, columns);
			for (int i = 0; i < columnValues.size(); i++)
				if (!String.valueOf(columnValues.get(i).getBreakCompareItem()).equals(currentValues.get(columns.get(i)))
						&& !StringUtils.isEmpty(currentValues.get(columns.get(i)))
						&& brokenCols.containsKey(columns.get(i)))
					breakCols.add(columns.get(i)); /**
			 * COMMENT OUT THE LINE BELOW TO TURN OFF BREAK LEVEL DETECTION
				 *
			 */
			//				currentValues.put(columns.get(i), String.valueOf(columnValues.get(i).getBreakCompareItem()));
			boolean totalsRow = !breakCols.isEmpty();

			if (totalsRow) {
				Collections.sort(breakCols, comparator);
				writeTotalsColumn(columns, breakCols, table, !altColor, columnRunningTotals);
			}

			for (int i = 0; i < columnValues.size(); i++) {
				ReportColumn column = columns.get(i);
				Object cell = row.get(i).getDisplay();
				String formattedCode = String.valueOf(columnValues.get(i).getDisplay());
				int columnId = column.getColumnId();
				DynamicReportColumn col = getColumnByTypeIndex(reportType, columnId);

				Class clazz = col.getColumnType();

				writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, column.getUseAllLines(), column.getLines()); //Spacer

				int horAlign = column.getColumnJustification() == 'L' ? Element.ALIGN_LEFT : (column.getColumnJustification() == 'R' ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER);
				int verAlign = column.getVerticalJustification() == 'T' ? Element.ALIGN_TOP : (column.getVerticalJustification() == 'M' ? Element.ALIGN_MIDDLE : Element.ALIGN_BOTTOM);

				writeAlign(table, formattedCode, horAlign, altColor, verAlign, column.getUseAllLines(), column.getLines());

				if (column.getDisplayTotals() == 'Y') {
					double currentTotal = columnGrandTotals.get(column);
					if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
						columnGrandTotals.put(column, currentTotal + ((Integer) cell).intValue());
						for (ReportColumn c : brokenCols.keySet())
							currentTotals.put(c, currentTotals.get(c) + ((Integer) cell).intValue());
						columnRunningTotals.put(column, currentTotals);
					} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
						columnGrandTotals.put(column, currentTotal + ((Double) cell).doubleValue());
						for (ReportColumn c : brokenCols.keySet())
							currentTotals.put(c, currentTotals.get(c) + ((Double) cell).doubleValue());
						columnRunningTotals.put(column, currentTotals);
					} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
						columnGrandTotals.put(column, currentTotal + ((Float) cell).floatValue());
						for (ReportColumn c : brokenCols.keySet())
							currentTotals.put(c, currentTotals.get(c) + ((Float) cell).floatValue());
						columnRunningTotals.put(column, currentTotals);
					} else {
						columnGrandTotals.put(column, currentTotal + 1);
						for (ReportColumn c : brokenCols.keySet())
							currentTotals.put(c, currentTotals.get(c) + 1);
						columnRunningTotals.put(column, currentTotals);
					}
				}
			}

			row = br.next();
		}

		breakCols.clear();
		/**
		 * COMMENT OUT THE SECTION BELOW TO TURN OFF BREAK LEVEL DETECTION
		 *
		 */
//		for(ReportColumn c : brokenCols.keySet())
//			breakCols.add(c);
//		Collections.sort(breakCols, comparator);
//		writeTotalsColumn(columns, breakCols, table, !altColor, columnRunningTotals);
		/**
		 *
		 *
		 */
		writeGrandTotalsColumn(columns, table, !altColor, columnGrandTotals);

		document.add(table);
	}

	private void writeTotalsColumn(final List<ReportColumn> columns, List<ReportColumn> breakCols, PdfPTable table, boolean altColor, HashMap<ReportColumn, HashMap<ReportColumn, Double>> columnRunningTotals) {
		for (ReportColumn brokenCol : breakCols)
			for (ReportColumn c : columns)
				if (c.getDisplayTotals() == 'Y') {
					int horAlign = c.getColumnJustification() == 'L' ? Element.ALIGN_LEFT : (c.getColumnJustification() == 'R' ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER);
					int verAlign = c.getVerticalJustification() == 'T' ? Element.ALIGN_TOP : (c.getVerticalJustification() == 'M' ? Element.ALIGN_MIDDLE : Element.ALIGN_BOTTOM);
					String pattern = "0";
					if (c.getNumericDigits() != 0) {
						pattern += ".";
						for (int j = 0; j < c.getNumericDigits(); j++)
							pattern += "0";
					}
					DecimalFormat df = new DecimalFormat(pattern);
					Class clazz = getColumnByTypeIndex(reportType, c.getColumnId()).getColumnType();
					writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines()); //Spacer
					if (clazz.equals(String.class))
						writeAlign(table, formatCode(c, columnRunningTotals.get(c).get(brokenCol).intValue(), df), horAlign, altColor, verAlign, c.getUseAllLines(), c.getLines());
					else
						writeAlign(table, formatCode(c, columnRunningTotals.get(c).get(brokenCol), df), horAlign, altColor, verAlign, c.getUseAllLines(), c.getLines());

					columnRunningTotals.get(c).put(brokenCol, 0.0);
				} else {
					writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines());
					writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines());
				}
//		for(int i = 0; i < table.getAbsoluteWidths().length; i++)
//			writeAlign(table, "", Element.ALIGN_CENTER, !altColor, Element.ALIGN_MIDDLE, breakCols.get(0).getUseAllLines(), breakCols.get(0).getLines());
	}

	private void writeGrandTotalsColumn(final List<ReportColumn> columns, PdfPTable table, boolean altColor, HashMap<ReportColumn, Double> columnGrandTotals) {
		for (ReportColumn c : columns)
			if (c.getDisplayTotals() == 'Y') {
				int horAlign = c.getColumnJustification() == 'L' ? Element.ALIGN_LEFT : (c.getColumnJustification() == 'R' ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER);
				int verAlign = c.getVerticalJustification() == 'T' ? Element.ALIGN_TOP : (c.getVerticalJustification() == 'M' ? Element.ALIGN_MIDDLE : Element.ALIGN_BOTTOM);
				String pattern = "0";
				if (c.getNumericDigits() != 0) {
					pattern += ".";
					for (int j = 0; j < c.getNumericDigits(); j++)
						pattern += "0";
				}
				DecimalFormat df = new DecimalFormat(pattern);
				Class clazz = getColumnByTypeIndex(reportType, c.getColumnId()).getColumnType();
				writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines()); //Spacer
				if (clazz.equals(String.class))
					writeAlign(table, formatCode(c, columnGrandTotals.get(c).intValue(), df), horAlign, altColor, verAlign, c.getUseAllLines(), c.getLines());
				else
					writeAlign(table, formatCode(c, columnGrandTotals.get(c), df), horAlign, altColor, verAlign, c.getUseAllLines(), c.getLines());
			} else {
				writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines());
				writeAlign(table, "", Element.ALIGN_CENTER, altColor, Element.ALIGN_MIDDLE, c.getUseAllLines(), c.getLines());
			}
	}

	private List<ColumnDisplayObject> getColumnValues(List<ColumnDisplayObject> row, List<ReportColumn> columns) {
		List<ColumnDisplayObject> vals = new ArrayList<ColumnDisplayObject>();
		for (int i = 0; i < row.size(); i++) {
			ReportColumn column = columns.get(i);
			ColumnDisplayObject n = new ColumnDisplayObject();

			String pattern = "0";
			if (column.getNumericDigits() != 0) {
				pattern += ".";
				for (int j = 0; j < column.getNumericDigits(); j++)
					pattern += "0";
			}

			DecimalFormat df = new DecimalFormat(pattern);
			String formattedCode = formatCode(column, row.get(i).getDisplay(), df);
			n.setBreakCompareItem(row.get(i).getBreakCompareItem());
			n.setDisplay(formattedCode);

			vals.add(n);
		}

		return vals;
	}

	protected void writeAlign(final PdfPTable table, final String str, final int horAlign, final boolean altColor, final int vertAlign, final char fixedLines, final int lines) {
		final PdfPCell cell = new PdfPCell(new Phrase(str, baseFont));
		if (altColor)
			cell.setBackgroundColor(alternateRowColor);
		cell.setHorizontalAlignment(horAlign);
		cell.setVerticalAlignment(vertAlign);
		disableAllBorders(cell);

		tempTable.addCell(cell);
		try {
			tempDocument.add(tempTable);
		} catch (DocumentException ex) {
			Logger.getLogger(DynamicReportBase.class.getName()).log(Level.SEVERE, null, ex);
		}

//		System.out.println("After: " + tempTable.getRowHeight(tempTable.getRows().size() - 1));

		if (fixedLines == 'Y')
			cell.setFixedHeight(tempTable.getRowHeight(tempTable.getRows().size() - 1) * lines);
		else if (lines > 0) {
			PdfPRow row = table.getRow(table.getRows().size() - 1);
			row.setMaxHeights(tempTable.getRowHeight(tempTable.getRows().size() - 1) * lines);
		}

		table.addCell(cell);
	}

	public static String formatCode(ReportColumn rc, Object cell, DecimalFormat df) {
		DynamicReportColumn drc = getColumnByTypeIndex(rc.getReport().getReportType(), rc.getColumnId());
		Class cellClass = drc.getColumnType();
		int cellType = drc.getFormatType();
		int code = rc.getFormatCode();

		if (cellClass.equals(int.class))
			switch (cellType) {
				case DynamicReportColumn.TYPE_DATE:
					return formatDate(DateUtils.getDate((Integer) cell), code);
				case DynamicReportColumn.TYPE_TIME:
					return formatTime(((Integer) cell).intValue(), code);
				case DynamicReportColumn.TYPE_MONEY:
					return formatIntMoney(((Integer) cell).intValue(), code, df);
				case DynamicReportColumn.TYPE_PERCENTAGE:
					return formatIntPercent(((Integer) cell).intValue(), code, df);
				default:
					return String.valueOf(df.format(cell));
			}
		else if (cellClass.equals(double.class))
			switch (cellType) {
				case DynamicReportColumn.TYPE_MONEY:
					return formatDoubleMoney(((Double) cell).doubleValue(), code, df);
				case DynamicReportColumn.TYPE_PERCENTAGE:
					return formatDoublePercent(((Double) cell).doubleValue(), code, df);
				default:
					return String.valueOf(df.format(cell));
			}
		else
			return String.valueOf(cell);
	}

	private static String formatDate(Date date, int code) {
		String formattedDate;
		String oldDate = String.valueOf(date).replaceAll("-", "").replaceAll("/", "");
		String year = oldDate.substring(0, 4);
		String yearAbridged = oldDate.substring(2, 4);
		String month = oldDate.substring(4, 6);
		String day = oldDate.substring(6, 8);

		/*
		 * 1: YYYYMMDD 2: MM-DD-YYYY 3: MM-DD-YY 4: MM/DD/YYYY 5: MM/DD/YY
		 * Default: MM-DD-YY
		 */
		switch (code) {
			case 1:
				formattedDate = oldDate;
				break;
			case 2:
				formattedDate = month + "-" + day + "-" + year;
				break;
			case 3:
				formattedDate = month + "-" + day + "-" + yearAbridged;
				break;
			case 4:
				formattedDate = month + "/" + day + "/" + year;
				break;
			case 5:
				formattedDate = month + "-" + day + "-" + yearAbridged;
				break;
			default:
				formattedDate = month + "-" + day + "-" + yearAbridged;
				break; //formattedDate = oldDate; break;
		}

		return formattedDate;
	}

	private static String formatIntMoney(int money, int code, DecimalFormat df) {
		String oldMoney = String.valueOf(df.format(money));
		String newMoney;
		boolean negative = money < 0;

		/*
		 * 6: $_______ 7: ($_,___,___) Default: ($_,___,___)
		 */
		switch (code) {
			case 6:
				newMoney = "$" + oldMoney;
				negative = false;
				break;
			case 7:
			default:
				int leading = oldMoney.length() % 3;
				int degree = (oldMoney.length() / 3) - 1;
				newMoney = oldMoney.substring(0, leading);
				for (int i = 0; i < degree; i++)
					newMoney += "," + oldMoney.substring(leading + (3 * i), leading + 3 + (3 * i));
				newMoney = "$" + newMoney;
				break;
		}

		return negative ? "(" + newMoney.replaceAll("-", "") + ")" : newMoney;
	}

	private static String formatDoubleMoney(double money, int code, DecimalFormat df) {
		String oldMoney = String.valueOf(df.format(money));
		String[] splitString = oldMoney.split("\\.");
		String oldMoneyDecimal = "00";
		if (splitString.length > 1)
			oldMoneyDecimal = splitString[splitString.length - 1];
		if (oldMoneyDecimal.length() == 1)
			oldMoneyDecimal += "0";
		oldMoney = splitString[0];
		String newMoney;
		boolean negative = money < -0.00001;

		/*
		 * 6: $_______.__ 7: ($_,___,___.__) Default: ($_,___,___.__)
		 */
		switch (code) {
			case 6:
				newMoney = "$" + oldMoney + "." + oldMoneyDecimal;
				negative = false;
				break;
			case 7:
			default:
				int leading = oldMoney.length() % 3;
				int degree = (oldMoney.length() / 3);
				if (leading == 0) {
					newMoney = oldMoney.substring(0, 3);
					degree--;
					leading = 3;
				} else
					newMoney = oldMoney.substring(0, leading);
				for (int i = 0; i < degree; i++)
					newMoney += "," + oldMoney.substring(leading + (3 * i), leading + 3 + (3 * i));
				newMoney = "$" + newMoney + "." + oldMoneyDecimal;
				break;
		}

		return negative ? "(" + newMoney.replaceAll("-", "") + ")" : newMoney;
	}

	private static String formatIntPercent(int percent, int code, DecimalFormat df) {
		String oldPercent = String.valueOf(df.format(percent));
		String newPercent;
		boolean negative = percent < 0;

		/*
		 * 8: _______% 9: (_,___,___%) Default: (_,___,___%)
		 */
		switch (code) {
			case 8:
				newPercent = oldPercent + "%";
				negative = false;
				break;
			case 9:
			default:
				int leading = oldPercent.length() % 3;
				int degree = (oldPercent.length() / 3) - 1;
				newPercent = oldPercent.substring(0, leading);
				for (int i = 0; i < degree; i++)
					newPercent += "," + oldPercent.substring(leading + (3 * i), leading + 3 + (3 * i));
				newPercent = newPercent + "%";
				break;
		}

		return negative ? "(" + newPercent.replaceAll("-", "") + ")" : newPercent;
	}

	private static String formatDoublePercent(double percent, int code, DecimalFormat df) {
		String oldPercent = String.valueOf(df.format(percent));
		String newPercent;
		String[] splitString = oldPercent.split(".");
		String oldPercentDecimal = splitString[splitString.length - 1];
		oldPercent = splitString[0];
		boolean negative = percent < -0.00001;

		/*
		 * 8: _______% 9: (_,___,___.__%) Default: (_,___,___.__%)
		 */
		switch (code) {
			case 8:
				newPercent = oldPercent + "." + oldPercentDecimal + "%";
				negative = false;
				break;
			case 9:
			default:
				int leading = oldPercent.length() % 3;
				int degree = (oldPercent.length() / 3) - 1;
				newPercent = oldPercent.substring(0, leading);
				for (int i = 0; i < degree; i++)
					newPercent += "," + oldPercent.substring(leading + (3 * i), leading + 3 + (3 * i));
				newPercent = newPercent + "." + oldPercentDecimal + "%";
				break;
		}

		return negative ? "(" + newPercent.replaceAll("-", "") + ")" : newPercent;
	}

	private static String formatTime(int time, int code) {
		String newTime;
		String oldTime = String.valueOf(time);
		int hours = Integer.valueOf(oldTime.substring(0, 2));
		int minutes = Integer.valueOf(oldTime.substring(2, 4));
		int seconds = Integer.valueOf(oldTime.substring(4, 6));
		int milliseconds = Integer.valueOf(oldTime.substring(6, 8));
		String ampm = Integer.valueOf(hours) >= 12 ? "pm" : "am";

		/*
		 * 10: Military Time - HH:MM:SS 11: Military Time - HH:MM:SS:mmm 12:
		 * HH:MM:SS am/pm 13: HH:MM:SS:mmm am/pm 14: Military Time - HH:MM 15:
		 * HH:MM am/pm Default: HH:MM am/pm
		 */
		switch (code) {
			case 10:
				newTime = hours + ":" + minutes + ":" + seconds;
				break;
			case 11:
				newTime = hours + ":" + minutes + ":" + seconds + ":" + milliseconds;
				break;
			case 12:
				newTime = (hours % 12) + "-" + minutes + ":" + seconds;
				break;
			case 13:
				newTime = (hours % 12) + ":" + minutes + ":" + seconds + ":" + milliseconds + ampm;
				break;
			case 14:
				newTime = hours + ":" + minutes;
				break;
			case 15:
				newTime = (hours % 12) + ":" + minutes + ampm;
				break;
			default:
				newTime = (hours % 12) + ":" + minutes + ampm;
				break;
		}

		return newTime;
	}

//	protected void createColumnTitles(final ReportColumn[] columns) throws DocumentException {
//		List<ReportColumn> colList = new ArrayList<ReportColumn>();
//
//		for(int i = 0; i < columns.length; i++)
//			colList.add(columns[i]);
//
//		Collections.sort(colList);
//		createColumnTitles(colList);
//	}
	protected PdfPTable createColumnTitles(final List<ReportColumn> columns) throws DocumentException {
		int cols = columns.size();
		int maxCols = cols * 2;

		Collections.sort(columns);
		PdfPCell cell;
		PdfPTable table = new PdfPTable(maxCols);
		float[] colWidths = new float[maxCols];
		double columnSpacing;
		double totalSpacing = 0.0;
		int columnIndex = 0;
		for (int i = 0; i < maxCols; i++)
			if (i == 0) {
//				columnSpacing = Utils.doubleEqual(columns.get(columnIndex).getLeadingSpace(), 0, 0.00001) ? defaultColSpacing : columns.get(columnIndex).getLeadingSpace();
				colWidths[i] = (float) columns.get(i).getLeadingSpace();
				totalSpacing += colWidths[i];
				columnIndex++;
			} else if ((i % 2) == 0) {
				columnSpacing = Utils.doubleEqual(columns.get(columnIndex).getLeadingSpace(), 0, 0.00001) ? defaultColSpacing : columns.get(columnIndex).getLeadingSpace();
				colWidths[i] = (float) (columnSpacing * 13);  //Check if "13" = 1 inch  TODO
				totalSpacing += colWidths[i];
				columnIndex++;
			}
		for (int i = 1; i < maxCols; i += 2)
			colWidths[i] = (float) ((totalColWidth - totalSpacing) / ((int) (maxCols / 2) + 1));
		table.setWidths(colWidths);

		boolean spacerCol = true;
		for (ReportColumn c : columns) {
			String text = c.getTitle();
			int justification = Element.ALIGN_CENTER;

			if (spacerCol) {
				cell = new PdfPCell();
				disableAllBorders(cell);
				table.addCell(cell);
//				spacerCol = false;
			}

			if (c.getTitleJustification() == 'L')
				justification = Element.ALIGN_LEFT;
			else if (c.getTitleJustification() == 'R')
				justification = Element.ALIGN_RIGHT;

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(text, baseFont));
			cell.setHorizontalAlignment(justification);
			cell.disableBorderSide(Rectangle.TOP);
			cell.disableBorderSide(Rectangle.LEFT);
			cell.disableBorderSide(Rectangle.RIGHT);
			table.addCell(cell);
			spacerCol = true;
		}
//		document.add(table);

		return table;
	}

	protected void createGraphic(final List<ReportTitle> titles) throws DocumentException {
		ReportTitle[] t = new ReportTitle[titles.size()];

		for (int i = 0; i < titles.size(); i++)
			t[i] = titles.get(i);

		createFooter(t);
	}

	protected void createGraphic(final ReportGraphic[] graphics) throws DocumentException {
		List<ReportGraphic> graphicList = new ArrayList<ReportGraphic>();
		graphicList.addAll(Arrays.asList(graphics));

		for (ReportGraphic g : graphicList) {
			double xOffset = g.getXPos();
			double yOffset = g.getYPos();

			PdfPCell cell;
			PdfPTable table;

			// report header
			//		table = new PdfPTable(selectedCols);
			//		table.setHorizontalAlignment(justification);
			//		table.setTotalWidth(titleWidth);
			//		table.setLockedWidth(false);

			Paragraph p = new Paragraph();

//			if(t.getEndLine() == 'Y') {
//				p.add(new Paragraph(text, titleFont));
//				table.addCell(p);
//			}
//			else
//				p.add(new Phrase(text, titleFont));
//			cell = new PdfPCell(p);
//	        cell.setHorizontalAlignment(justification);
//	        cell.setPaddingBottom((float)t.getLeftOffset() * 72);
//	        table.addCell(cell);
//
//	        document.add(table);
		}
	}

	void disableAllBorders(PdfPCell cell) {
		cell.disableBorderSide(PdfPCell.LEFT);
		cell.disableBorderSide(PdfPCell.RIGHT);
		cell.disableBorderSide(PdfPCell.TOP);
		cell.disableBorderSide(PdfPCell.BOTTOM);
	}

	private void syncColumnRows(List<ReportColumn> columns) {
		int maxLines = 0;
		boolean useLines = false;

		for (ReportColumn c : columns) {
			if (c.getLines() > maxLines)
				maxLines = c.getLines();
			if (c.getUseAllLines() == 'Y')
				useLines = true;
		}

		for (ReportColumn c : columns) {
			c.setLines((short) maxLines);
			c.setUseAllLines(useLines ? 'Y' : 'N');
		}
	}

	public static DynamicReportColumn getColumnByTypeIndex(int reportType, int index) {
		switch (reportType) {
			case 1:
				return HrBenefitJoinColumns.getColumnByIndex(index);
			case 2:
				return EmployeeColumns.getColumnByIndex(index);
			case 3:
				return TimesheetColumns.getColumnByIndex(index);
			default:
				return null;
		}
	}

	public static DynamicJoinColumn getJoinColumnByTypeIndex(int reportType, int index) {
		switch (reportType) {
			case 1:
				return HrBenefitJoinColumns.getJoinColumnByIndex(index);
			case 2:
				return EmployeeColumns.getJoinColumnByIndex(index);
			case 3:
				return TimesheetColumns.getJoinColumnByIndex(index);
			default:
				return null;
		}
	}

	public static DynamicJoinColumn getJoinColumnByClass(Class ownerClass, int index) {
		if (ownerClass == HrBenefitJoin.class)
			return HrBenefitJoinColumns.getJoinColumnByIndex(index);
		else if (ownerClass == Employee.class)
			return EmployeeColumns.getJoinColumnByIndex(index);
		else if (ownerClass == Timesheet.class)
			return TimesheetColumns.getJoinColumnByIndex(index);
		else
			return null;
	}

	public static List<DynamicReportColumn> getAvailableColumns(Report r) {
		//return ArahantSession.getHSU().createCriteria(ReportColumn.class).orderBy(ReportColumn.SEQ_NO).eq(ReportColumn.REPORT_ID, getReportId()).list();
		List<DynamicReportColumn> drcl = new ArrayList<DynamicReportColumn>();
		BReport br = new BReport(r);

		switch (br.getReportType()) {
			case 1:
				drcl = HrBenefitJoinColumns.getColumns();
				for (ReportColumn rc : br.getReportColumns()) {
					List<DynamicReportColumn> removeList = new ArrayList<DynamicReportColumn>();
					DynamicReportColumn drc = HrBenefitJoinColumns.getColumnByIndex(rc.getColumnId());
					for (DynamicReportColumn d : drcl)
						if (d.equals(drc))
							removeList.add(d);
					drcl.removeAll(removeList);
				}
				break;
			case 2:
				drcl = EmployeeColumns.getColumns();
				for (ReportColumn rc : br.getReportColumns()) {
					List<DynamicReportColumn> removeList = new ArrayList<DynamicReportColumn>();
					DynamicReportColumn drc = EmployeeColumns.getColumnByIndex(rc.getColumnId());
					for (DynamicReportColumn d : drcl)
						if (d.equals(drc))
							removeList.add(d);
					drcl.removeAll(removeList);
				}
				break;
			case 3:
				drcl = TimesheetColumns.getColumns();
				for (ReportColumn rc : br.getReportColumns()) {
					List<DynamicReportColumn> removeList = new ArrayList<DynamicReportColumn>();
					DynamicReportColumn drc = TimesheetColumns.getColumnByIndex(rc.getColumnId());
					for (DynamicReportColumn d : drcl)
						if (d.equals(drc))
							removeList.add(d);
					drcl.removeAll(removeList);
				}
				break;
		}

		return drcl;
	}

	public static Class getReportClassForCriteria(Report r) {
		switch (r.getReportType()) {
			case 1:
				return HrBenefitJoin.class;
			case 2:
				return Employee.class;
			case 3:
				return Timesheet.class;
			default:
				return null;
		}
	}

	public static String getPersonFilterCriteria(int reportType) {
		switch (reportType) {
			case 1:
				return "HrBenefitJoin.payingPersonId";
			case 2:
				return "Employee.personId";
			case 3:
				return "Timesheet.personId";
			default:
				return null;
		}
	}

	public static String getFormatCodeDescription(int formatCode) {
		String description;

		switch (formatCode) {
			case 1:
				description = "Date Formatted: CCYYMMDD";
				break;
			case 2:
				description = "Date Formatted: MM-DD-CCYY";
				break;
			case 3:
				description = "Date Formatted: MM-DD-YY";
				break;
			case 4:
				description = "Date Formatted: MM/DD/CCYY";
				break;
			case 5:
				description = "Date Formatted: MM/DD/YY";
				break;
			case 6:
				description = "Money Formatted: No Comma Separators";
				break;
			case 7:
				description = "Money Formatted: With Comma Separators";
				break;
			case 8:
				description = "Percent Value Formatted: No Comma Separators";
				break;
			case 9:
				description = "Percent Value Formatted: With Comma Separators";
				break;
			case 10:
				description = "Military Time - HH:MM:SS";
				break;
			case 11:
				description = "Military Time - HH:MM:SS:mmm";
				break;
			case 12:
				description = "12-Hour Time - HH:MM:SS (am/pm)";
				break;
			case 13:
				description = "12-Hour Time - HH:MM:SS:mmm (am/pm)";
				break;
			case 14:
				description = "Military Time - HH:MM";
				break;
			case 15:
				description = "12-Hour Time - HH:MM (am/pm)";
				break;
			default:
				description = "12-Hour Time - HH:MM (am/pm)";
				break;
		}

		return description;
	}

	public static String getFormatCodeDescriptionByType(int formatCode, int type) {
		String description = "";

		if (type == DynamicReportColumn.TYPE_DATE)
			switch (formatCode) {
				case 1:
					description = "Date Formatted: CCYYMMDD";
					break;
				case 2:
					description = "Date Formatted: MM-DD-CCYY";
					break;
				case 3:
					description = "Date Formatted: MM-DD-YY";
					break;
				case 4:
					description = "Date Formatted: MM/DD/CCYY";
					break;
				case 5:
					description = "Date Formatted: MM/DD/YY";
					break;
				default:
					description = "";
					break;
			}
		else if (type == DynamicReportColumn.TYPE_MONEY)
			switch (formatCode) {
				case 6:
					description = "Money Formatted: No Comma Separators";
					break;
				case 7:
					description = "Money Formatted: With Comma Separators";
					break;
				default:
					description = "";
					break;
			}
		else if (type == DynamicReportColumn.TYPE_PERCENTAGE)
			switch (formatCode) {
				case 8:
					description = "Percent Value Formatted: No Comma Separators";
					break;
				case 9:
					description = "Percent Value Formatted: With Comma Separators";
					break;
				default:
					description = "";
					break;
			}
		else if (type == DynamicReportColumn.TYPE_TIME)
			switch (formatCode) {
				case 10:
					description = "Military Time - HH:MM:SS";
					break;
				case 11:
					description = "Military Time - HH:MM:SS:mmm";
					break;
				case 12:
					description = "12-Hour Time - HH:MM:SS (am/pm)";
					break;
				case 13:
					description = "12-Hour Time - HH:MM:SS:mmm (am/pm)";
					break;
				case 14:
					description = "Military Time - HH:MM";
					break;
				case 15:
					description = "12-Hour Time - HH:MM (am/pm)";
					break;
				default:
					description = "";
					break;
			}

		return description;
	}

	final public String getFilename() {
		return FileSystemUtils.getHTTPPath(outputFile);
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

	protected void addTable(final PdfPTable table) throws DocumentException {
		document.add(table);
	}

	protected void newPage() throws DocumentException {
		document.newPage();
	}

	protected void resetPageCount() throws DocumentException {
		document.setPageCount(1);
	}

	public List<ReportTitle> getTitles() {
		return titles;
	}

	public void setTitles(List<ReportTitle> titles) {
		this.titles = titles;
	}

	private class SortBrokenComparator implements Comparator {

		@Override
		public int compare(Object rc1, Object rc2) {
			if (((ReportColumn) rc1).getSortOrder() == 0)
				return ((ReportColumn) rc1).getSortOrder() == 0 ? 0 : -1;
			else if (((ReportColumn) rc2).getSortOrder() == 0)
				return 1;
			else
				return ((ReportColumn) rc1).getSortOrder() <= ((ReportColumn) rc1).getSortOrder() ? -1 : 1;
		}
	}
}
