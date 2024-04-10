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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.site.changePassword;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;

import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.BPerson;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

 
/**
 *  
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSiteChangePasswordOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ChangePasswordOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(ChangePasswordOps.class);

	@WebMethod()
	public ChangePasswordReturn changePassword(/*@WebParam(name = "in")*/final ChangePasswordInput in) {
		final ChangePasswordReturn ret = new ChangePasswordReturn();

		try {
			checkLogin(in);

			String oldPw = in.getOldPassword();
			String newPw = in.getNewPassword();

			BPerson bp = BPerson.getCurrent();
			if (!bp.getUserPassword().equals(oldPw))
				throw new ArahantWarning("Invalid old password");

			bp.setNewPassword(newPw);

			this.changePassword(oldPw, newPw);

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadPasswordRulesReturn loadPasswordRules(/*@WebParam(name = "in")*/final LoadPasswordRulesInput in) {
		final LoadPasswordRulesReturn ret = new LoadPasswordRulesReturn();
		try {
			checkLogin(in);


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


}
