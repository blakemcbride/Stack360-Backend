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
package com.arahant.tutorial.OldHibernateExamples;

import com.arahant.beans.ClientCompany;
import com.arahant.beans.IArahantBean;
import com.arahant.beans.IHrBenefitJoinCurrent;
import com.arahant.business.BClientCompany;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate22 {

	public static void main(String[] args) throws Exception {
	/*	HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();

		for (Class c : HibernateUtil.getClasses("com.arahant.beans")) {
			for (Annotation annot : c.getAnnotations()) {
				if (annot.annotationType().equals(javax.persistence.Entity.class)) {
					hsu.getAll(c);
				}
			}
		}

		hsu.commitTransaction();
	 * */

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();

		hsu.createCriteria(IArahantBean.class).scroll();

		hsu.commitTransaction();
	}
}
