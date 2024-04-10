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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.projectCategory;
import com.arahant.business.BProjectCategory;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListProjectCategoriesReturn extends TransmitReturnBase {

	private ListProjectCategoriesReturnItem [] projectCategories;
	
	public ListProjectCategoriesReturn() {
		super();
	}

	/**
	 * @return Returns the projectCategories.
	 */
	public ListProjectCategoriesReturnItem[] getProjectCategories() {
		return projectCategories;
	}

	/**
	 * @param projectCategories The projectCategories to set.
	 */
	public void setProjectCategories(final ListProjectCategoriesReturnItem[] projectCategories) {
		this.projectCategories = projectCategories;
	}

	/**
	 * @param categories
	 */
	void setProjectCategories(final BProjectCategory[] cats) {
		projectCategories=new ListProjectCategoriesReturnItem[cats.length];
		for (int loop=0;loop<cats.length;loop++)
			projectCategories[loop]=new ListProjectCategoriesReturnItem(cats[loop]);
	}
}

	
