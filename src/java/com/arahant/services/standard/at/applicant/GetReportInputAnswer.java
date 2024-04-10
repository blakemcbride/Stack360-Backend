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

package com.arahant.services.standard.at.applicant;

import com.arahant.utils.SearchConstants;

/**
 *
 */
public class GetReportInputAnswer {
	private String id;
	private int comparator;
	private String textAnswer;
	private int dateAnswer;
	private double numericAnswer;
	private double triAnswer;

	public int getComparator() {
		return comparator;
	}

	public void setComparator(int comparator) {
		this.comparator = comparator;
	}

	public int getDateAnswer() {
		return dateAnswer;
	}

	public void setDateAnswer(int dateAnswer) {
		this.dateAnswer = dateAnswer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getNumericAnswer() {
		return numericAnswer;
	}

	public void setNumericAnswer(double numericAnswer) {
		this.numericAnswer = numericAnswer;
	}

	public String getTextAnswer() {
		return modifyForSearch(textAnswer, comparator);
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}

	public double getTriAnswer() {
		return triAnswer;
	}

	public void setTriAnswer(double triAnswer) {
		this.triAnswer = triAnswer;
	}
	
	protected String modifyForSearch(String str, final int searchType)
	{
		if (str==null)
			str="";
		str=str.trim();
		switch (searchType)
		{
			case SearchConstants.ENDS_WITH_VALUE: return "%"+str;
			case SearchConstants.STARTS_WITH_VALUE: return str+"%";
			case SearchConstants.CONTAINS_VALUE: return "*"+str+"%";
			case SearchConstants.ALL_VALUE: return "%";
			case SearchConstants.EXACT_MATCH_VALUE: return str;
			default: return str;
		}
	}
	
}
