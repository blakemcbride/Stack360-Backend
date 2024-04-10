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

package com.arahant.client.nestor;

import com.arahant.beans.ClientCompany;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.business.BProject;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;

/**
 * User: Blake McBride
 * Date: 11/5/16
 */
public class RenameProjects {
    public static void main(String [] argv) {
        try {
            HibernateSessionUtil hsu = ArahantSession.getHSU();
            hsu.dontAIIntegrate();
            ArahantSession.multipleCompanySupport = false;
            hsu.setCurrentCompany(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).eq(CompanyDetail.NAME, "Nestor CPA Services, Inc.").first());
            hsu.getCurrentCompany();
            hsu.setCurrentPerson(hsu.get(Person.class, "00000-0000000000"));

            HibernateScrollUtil<Project> scr = hsu.createCriteria((Project.class)).eq(Project.DESCRIPTION, "Accounting Services").scroll();
            while (scr.next()) {
                Project rec = scr.get();

                rec.setDescription("Accounting Rate 1");
                hsu.update(rec);

                System.out.println(rec.getDescription());
            }
            hsu.commitTransaction();
            hsu.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}