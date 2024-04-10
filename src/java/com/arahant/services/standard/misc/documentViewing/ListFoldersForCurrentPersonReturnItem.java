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
package com.arahant.services.standard.misc.documentViewing;

import com.arahant.business.BCompanyForm;
import com.arahant.business.BCompanyFormFolder;

public class ListFoldersForCurrentPersonReturnItem {

	public ListFoldersForCurrentPersonReturnItem() {
	}

	public ListFoldersForCurrentPersonReturnItem(BCompanyFormFolder bcff) {
		setData(bcff);
	}

	public ListFoldersForCurrentPersonReturnItem(BCompanyForm bcf) {
		setData(bcf);
	}

	public void setData(BCompanyFormFolder bcff) {
		id = bcff.getFolderId();
		name = bcff.getFolderName();

		BCompanyFormFolder[] folders = BCompanyFormFolder.listFoldersForCurrentPerson((bcff != null) ? bcff.getFolderId() : null);

		child = new ListFoldersForCurrentPersonReturnItem[folders.length];
		for (int loop = 0; loop < folders.length; loop++) {
			child[loop] = new ListFoldersForCurrentPersonReturnItem(folders[loop]);
		}

		childCount = getChild().length;

		type = "f";

	}

	public void setData(BCompanyForm bcf) {
		if (bcf.getComments().length() > 25) {
			name = bcf.getFormType().getDescription() + " - " + bcf.getComments().substring(0, 25) + "...";
		} else {
			name = bcf.getFormType().getDescription() + " - " + bcf.getComments();
		}

		id = bcf.getCompanyFormId();

		child = null;

		childCount = 0;

		type = "d";

	}
	private String id;
	private String type;
	private ListFoldersForCurrentPersonReturnItem[] child;
	private int childCount;
	private String name;

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

	public ListFoldersForCurrentPersonReturnItem[] getChild() {
		if (child == null) {
			child = new ListFoldersForCurrentPersonReturnItem[0];
		}
		return child;
	}

	public void setChild(ListFoldersForCurrentPersonReturnItem[] child) {
		this.child = child;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

	
