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

import com.arahant.beans.Filtered;
import com.arahant.beans.IHrBenefitJoinCurrent;
import com.arahant.utils.ArahantSession;


public class Hibernate20 {
	public static void main (String []args)
	{
		ArahantSession.getHSU().createCriteria(IHrBenefitJoinCurrent.class).scroll();

		for (Filtered cat : ArahantSession.getHSU().createCriteria(Filtered.class).list())
		{
			System.out.println(cat.getOrgGroup().getName());
		}
	}
}
