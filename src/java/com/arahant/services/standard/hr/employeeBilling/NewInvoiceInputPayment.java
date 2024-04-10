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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.hr.employeeBilling;

/**
 *
 */
public class NewInvoiceInputPayment {
/*
 * id (string), date (number), type (string), amount (number), availableForThisInvoice (number), 
 appliedToThisInvoice (number), description (string)
 */
	private String id;
	private int date;
	private String type;
	private double amount;
	private double appliedToThisInvoice;
	private String description;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAppliedToThisInvoice() {
		return appliedToThisInvoice;
	}

	public void setAppliedToThisInvoice(double appliedToThisInvoice) {
		this.appliedToThisInvoice = appliedToThisInvoice;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
