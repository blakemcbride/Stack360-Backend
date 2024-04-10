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
 * Created on Jun 6, 2007
 * 
 */
package com.arahant.services.standard.security.securityToken;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 *
 * Created on Jun 6, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSecuritySecurityTokenOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class SecurityTokenOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			SecurityTokenOps.class);


	@WebMethod()
	public ListSecurityTokensReturn listSecurityTokens (/*@WebParam(name = "in")*/final ListSecurityTokensInput in)	{
		final ListSecurityTokensReturn ret=new ListSecurityTokensReturn();
		try {
			checkLogin(in);
			
			ret.setItem(BRight.list());
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public DeleteSecurityTokensReturn deleteSecurityTokens (/*@WebParam(name = "in")*/final DeleteSecurityTokensInput in)	{
		final DeleteSecurityTokensReturn ret=new DeleteSecurityTokensReturn();
		try {
			checkLogin(in);
			
			BRight.delete(in.getIds());
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public GetSecurityTokenReportReturn getSecurityTokenReport (/*@WebParam(name = "in")*/final GetSecurityTokenReportInput in)	{
		final GetSecurityTokenReportReturn ret=new GetSecurityTokenReportReturn();
		try {
			checkLogin(in);
			
			ret.setReportUrl(BRight.getReport());
					
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public NewSecurityTokenReturn newSecurityToken (/*@WebParam(name = "in")*/final NewSecurityTokenInput in)	{
		final NewSecurityTokenReturn ret=new NewSecurityTokenReturn();
		try {
			checkLogin(in);
			
			final BRight b=new BRight();
			ret.setId(b.create());
			in.setData(b);
			b.insert();
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public SaveSecurityTokenReturn saveSecurityToken (/*@WebParam(name = "in")*/final SaveSecurityTokenInput in)	{
		final SaveSecurityTokenReturn ret=new SaveSecurityTokenReturn();
		try {
			checkLogin(in);
			final BRight b=new BRight(in.getTokenId());
			in.setData(b);
			b.update();
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}

/*
listSecurityTokens
input: none
output: name, description, tokenId

deleteSecurityTokens
input: array of security tokenIds

getSecurityTokenReport
input: none
output: reportUrl

newSecurityToken
input: name, description

saveSecurityToken
input: name, tokenId, description

*/
