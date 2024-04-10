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
package com.arahant.services.standard.misc.developerHomepage;
import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;

public class SearchProjectsReturn extends TransmitReturnBase {
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}


	

	private SearchProjectsReturnItem []item;
	
	public SearchProjectsReturn() {
		super();
	}

	public SearchProjectsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchProjectsReturnItem[] item) {
		this.item = item;
	}



	/**
	 * @param projects2
	 */
	void setProjects(final BProject[] p) {
		item=new SearchProjectsReturnItem[p.length];
		for (int loop=0;loop<p.length;loop++)
			item[loop]=new SearchProjectsReturnItem(p[loop]);
	}
}

	
