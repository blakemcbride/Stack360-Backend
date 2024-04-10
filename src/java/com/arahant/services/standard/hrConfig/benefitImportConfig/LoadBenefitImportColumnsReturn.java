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
package com.arahant.services.standard.hrConfig.benefitImportConfig;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BImportColumn;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.imports.GenericFileImport;

public class LoadBenefitImportColumnsReturn extends TransmitReturnBase {
	LoadBenefitImportColumnsReturnItem columns[];
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public LoadBenefitImportColumnsReturnItem[] getColumns() {
		return columns;
	}

	/**
	 * @param item The item to set.
	 */
	public void setColumns(final LoadBenefitImportColumnsReturnItem[] item) {
		this.columns = item;
	}

	/**
	 * @param accounts
	 */
	void setColumns(final BImportColumn[] a, final String programName) {

		if (programName.equals("DRC Benefit Import"))
		{
			String filters1[] = GenericFileImport.availableColumns;
			int filterLength1 = filters1.length;
			columns=new LoadBenefitImportColumnsReturnItem[filterLength1];
			for (int loop=0;loop<filterLength1;loop++)
			{
				boolean found=false;
				for (int existingLoop=0;existingLoop<a.length;existingLoop++)
				{
					if (filters1[loop].equals(a[existingLoop].getName()))
					{
						columns[loop]=new LoadBenefitImportColumnsReturnItem(a[existingLoop]);
						found=true;
						break;
					}
				}

				if (!found)
				{
					columns[loop]=new LoadBenefitImportColumnsReturnItem(filters1[loop], BImportColumn.getAvailableColumnsFilter1Required()[loop]);
				}
			}
		}
	}

	void setColumns(final String[] filterColumns) {
		columns=new LoadBenefitImportColumnsReturnItem[filterColumns.length];
		for (int loop=0;loop<filterColumns.length;loop++)
			columns[loop]=new LoadBenefitImportColumnsReturnItem(filterColumns[loop], false);
	}

	void setColumns(final String[] filterColumns, final boolean[] b) {
		columns=new LoadBenefitImportColumnsReturnItem[filterColumns.length];
		for (int loop=0;loop<filterColumns.length;loop++)
			columns[loop]=new LoadBenefitImportColumnsReturnItem(filterColumns[loop], b[loop]);
	}

}

	
