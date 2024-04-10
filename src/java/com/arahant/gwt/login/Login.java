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


package com.arahant.gwt.login;

import com.arahant.beans.CompanyDetail;
import com.arahant.business.BPerson;
import com.arahant.business.BProphetLogin;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObjectMap;
import java.util.List;

/**
 *
 * @author Blake McBride
 */
public class Login {

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service) {
		hsu = ArahantSession.openHSU();  //  only needed because this special service bypasses the normal service login procedure
		String user = in.getString("_user");
		String pw = in.getString("_password");
		if (BProphetLogin.checkPassword(user, pw)) {
			BPerson bp = BPerson.getCurrent();
			if (BPerson.getCurrent().getTopLevelNoCompanyGroup() == null) {
				List<CompanyDetail> compList = bp.getAllowedCompanies();
				out.put("numberOfCompanies", compList.size());
				if (compList.size() == 1)
					out.put("company", compList.get(0).getOrgGroupId());
				else {
					String[] companyList = new String[compList.size()];
					for (int loop = 0; loop < compList.size(); loop++)
						companyList[loop] = compList.get(loop).getOrgGroupId();
					out.put("companies", companyList);
				}
			} else
				out.put("numberOfCompanies", -1);
		} else
			out.put("numberOfCompanies", -1);
	}
}
