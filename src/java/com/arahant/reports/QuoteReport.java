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
 *
 */

package com.arahant.reports;

import com.arahant.beans.QuoteAdjustment;
import com.arahant.beans.QuoteProduct;
import com.arahant.business.BClientCompany;
import com.arahant.business.BQuoteTable;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class QuoteReport extends ReportBase
{
	public QuoteReport() throws ArahantException {
		super("QuoteRep", "", false, 225F, 30F);
    }

    public String build(final String quoteId) throws DocumentException {

        try {

			BQuoteTable bqt = new BQuoteTable(quoteId);

			writeHeader(bqt);
			
			PdfPTable table;

			table = makeTable(new int[]{35, 5, 10, 10, 20, 20});

			writeColHeader(table, "Item Description", Element.ALIGN_LEFT);
			writeColHeader(table, "QTY", Element.ALIGN_RIGHT);
			writeColHeader(table, "Hours", Element.ALIGN_RIGHT);
			writeColHeader(table, "Rate", Element.ALIGN_RIGHT);
			writeColHeader(table, "Adjusted Rate", Element.ALIGN_RIGHT);
			writeColHeader(table, "Price", Element.ALIGN_RIGHT);

            boolean alternateRow = true;

			double totalCost = 0;
			double subTotal = 0;

			//Get Services
			for (QuoteProduct qp : bqt.getQuoteProducts(new char[]{'S', 'B'}))
			{
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, qp.getProduct().getDescription(), alternateRow);

				short quantity = qp.getQuantity();
                write(table, quantity, alternateRow);

				double hours = qp.getProduct().getManHours();
                write(table, hours, alternateRow);


				double defaultRate = new BClientCompany(qp.getQuote().getClient().getOrgGroupId()).getBillingRate();
				if(defaultRate == 0)
				{
					defaultRate = ArahantSession.getHSU().getCurrentCompany().getBillingRate();
				}

				write(table, MoneyUtils.formatMoney(defaultRate), alternateRow);
				//write(table, MoneyUtils.formatMoney(qp.getRetailPrice()), alternateRow);

				double adjustedPrice = qp.getAdjustedRetailPrice();

//				if(adjustedPrice == 0)
//				{
//					if(qp.getSellAsType() == 'S')
//					{
//						adjustedPrice = defaultRate;
//					}
//					else if(qp.getSellAsType() == 'B')
//					{
//						adjustedPrice = 0;
//					}
//				}
				
                write(table, MoneyUtils.formatMoney(adjustedPrice), alternateRow);
				
				double total = 0;

				if(qp.getSellAsType() == 'S')
				{
					total = adjustedPrice * quantity * hours;
				}
				else if(qp.getSellAsType() == 'B')
				{
					total = (defaultRate * quantity * hours);
				}

				write(table, MoneyUtils.formatMoney(total), alternateRow);

				subTotal += total;
				totalCost += total;
            }

			addTable(table);

			table = makeTable(new int[]{80, 20});
			writeRight(table, "Services Total Cost:", false);
			write(table, MoneyUtils.formatMoney(subTotal));

			addTable(table);


			table = makeTable(new int[]{40, 10, 10, 20, 20});

			writeColHeader(table, "Item Description", Element.ALIGN_LEFT);
			writeColHeader(table, "QTY", Element.ALIGN_RIGHT);
			writeColHeader(table, "Retail", Element.ALIGN_RIGHT);
			writeColHeader(table, "Adjusted Retail", Element.ALIGN_RIGHT);
			writeColHeader(table, "Price", Element.ALIGN_RIGHT);

			alternateRow = true;

			subTotal = 0;

			//Get Products
			for (QuoteProduct qp : bqt.getQuoteProducts(new char[]{'P', 'B'}))
			{
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, qp.getProduct().getDescription(), alternateRow);

				short quantity = qp.getQuantity();
                write(table, quantity, alternateRow);

				double hours = qp.getProduct().getManHours();

				double retailPrice = qp.getRetailPrice();
				write(table, MoneyUtils.formatMoney(retailPrice), alternateRow);

				double adjustedPrice = qp.getAdjustedRetailPrice();

//				if(adjustedPrice == 0)
//				{
//					if(qp.getSellAsType() == 'P')
//					{
//						adjustedPrice = retailPrice;
//					}
//					else if(qp.getSellAsType() == 'B')
//					{
//						adjustedPrice = 0;
//					}
//				}
                write(table, MoneyUtils.formatMoney(adjustedPrice), alternateRow);

				double total = adjustedPrice * quantity * hours;

				if(qp.getSellAsType() == 'P')
				{
					total = adjustedPrice * quantity;
				}
				else if(qp.getSellAsType() == 'B')
				{
					total = (retailPrice * quantity);
				}

				write(table, MoneyUtils.formatMoney(total), alternateRow);

				subTotal += total;
				totalCost += total;
            }

            addTable(table);

			table = makeTable(new int[]{80, 20});
			writeRight(table, "Materials Total Cost:", false);
			write(table, MoneyUtils.formatMoney(subTotal));

			addTable(table);

			if (bqt.getQuoteAdjustments().size() > 0)
			{
				table = makeTable(new int[]{40, 20, 20, 20});

				writeColHeader(table, "Adjustment Description", Element.ALIGN_LEFT);
				writeColHeader(table, "QTY", Element.ALIGN_RIGHT);
				writeColHeader(table, "Retail Price", Element.ALIGN_RIGHT);
				writeColHeader(table, "Price", Element.ALIGN_RIGHT);

				alternateRow = true;

				for (QuoteAdjustment qa : bqt.getQuoteAdjustments())
				{
					// toggle the alternate row
					alternateRow = !alternateRow;

					write(table, qa.getAdjustmentDescription(), alternateRow);

					int quantity = qa.getQuantity();

					write(table, quantity, alternateRow);

					double rate = qa.getAdjustedCost();

					write(table, MoneyUtils.formatMoney(rate), alternateRow);

					double adjustmentCost = quantity * rate;

					write(table, MoneyUtils.formatMoney(adjustmentCost), alternateRow);

					totalCost += adjustmentCost;
				}

				addTable(table);
			}


			table=makeTable(new int[]{80, 20});

            writeRight(table, "Subtotal:", false);
			write(table,  MoneyUtils.formatMoney(totalCost));

			double locationCost = bqt.getLocationCost().getLocationCost();
            writeRight(table, "Travel and Living:", false);
			write(table, MoneyUtils.formatMoney(locationCost));

			writeRight(table, "Additional Cost:", false);
			write(table, MoneyUtils.formatMoney(bqt.getAdditionalCost()));

			double markup = (bqt.getMarkupPercent() / 100) + 1;
            writeRight(table, "Adjustment Percent:", false);
			writeRight(table, bqt.getMarkupPercent() + "%", false);

			double grandTotal = (totalCost + locationCost + bqt.getAdditionalCost()) * markup;
            writeRight(table, "Grand Total:", false);
			write(table, MoneyUtils.formatMoney(grandTotal));

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

	private void writeHeader(BQuoteTable bqt) throws DocumentException
	{
		PdfPTable table = makeTable(new int[]{35, 30, 35});

		write(table, "", false);
		writeBoldCentered(table, bqt.getQuoteName(), 14F);
		write(table, "", false);

		String presenter = "Not Specified";
		
		if (bqt.getFinalizedByPerson() != null && !isEmpty(bqt.getFinalizedByPerson().getNameFL()))
			presenter = bqt.getFinalizedByPerson().getNameFL();
		else if (bqt.getCreatedByPerson() != null && !isEmpty(bqt.getCreatedByPerson().getNameFL()))
			presenter = bqt.getCreatedByPerson().getNameFL();

		writeLeft(table, "Presented by " + presenter, false);
		write(table, "", false);
		writeRight(table, DateUtils.getDateSpelledOut(DateUtils.now()), false);

		writeLeft(table, bqt.getClient().getName(), false);
		write(table, "", false);
		writeRight(table, bqt.getLocationCost().getDescription(), false);

		addTable(table);

	}

    public static void main(String args[]) {
        try {
            new QuoteReport().build("00001-0000000011");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
