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
package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.JessBean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ArahantBean extends JessBean implements IArahantBean, Cloneable {

	public abstract String tableName();

	public abstract String keyColumn();

	public abstract String generateId() throws ArahantException;
	private ArahantBean originalBean;

	public ArahantBean() {
		super((HibernateSessionUtil) null);
	}

	public ArahantBean getOriginalBean() {
		return originalBean;
	}

	public void saveOriginalBean() {
		if (originalBean == null)
			try {
				originalBean = (ArahantBean) this.clone();
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(AuditedBean.class.getName()).log(Level.SEVERE, null, ex);
			}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
