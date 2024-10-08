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
package com.arahant.services.standard.project.projectComment;

import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CheckRightReturn extends TransmitReturnBase {
	private int AccessLevel;
	private int addAccessLevel;
	private int detailAccessLevel;
	private int internalCommentAccessLevel;

	public int getInternalCommentAccessLevel() {
		return internalCommentAccessLevel;
	}

	public void setInternalCommentAccessLevel(int internalCommentAccessLevel) {
		this.internalCommentAccessLevel = internalCommentAccessLevel;
	}
	
	
	public int getAddAccessLevel() {
		return addAccessLevel;
	}

	public void setAddAccessLevel(int addAccessLevel) {
		this.addAccessLevel = addAccessLevel;
	}
	
	
	public int getDetailAccessLevel() {
		return detailAccessLevel;
	}

	public void setDetailAccessLevel(int detailAccessLevel) {
		this.detailAccessLevel = detailAccessLevel;
	}

	public CheckRightReturn(final String msg) {
		super(msg);
	}

	public CheckRightReturn()
	{
		super();
	}
	/**
	 * @return Returns the accessLevel.
	 */
	public int getAccessLevel() {
		return AccessLevel;
	}

	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final int accessLevel) {
		AccessLevel = accessLevel;
	}
}

	
