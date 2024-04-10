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
package com.arahant.services.standard.billing.glAccount;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;

import com.arahant.beans.GlAccount;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BGlAccount;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

import java.util.Iterator;
import java.util.List;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingGlAccountOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class GlAccountOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			GlAccountOps.class);
	
	public GlAccountOps() {
		super();
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);

			ret.setCanSeeAllCompanies(BRight.checkRight("CanAccessAllCompanies") == BRight.ACCESS_LEVEL_WRITE);

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListGLAccountsReturn listGLAccounts (/*@WebParam(name = "in")*/final ListGLAccountsInput in)	{
		final ListGLAccountsReturn ret=new ListGLAccountsReturn();

		try {
			checkLogin(in);

			ret.setItem(makeArray(hsu.createCriteria(GlAccount.class).orderBy(GlAccount.ACCOUNTNUMBER).orderBy(GlAccount.ACCOUNTNAME).list()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private static BGlAccount[] makeArray(final List<GlAccount> plist) {
		final BGlAccount[] glat = new BGlAccount[plist.size()];

		int index = 0;

		final Iterator plistItr = plist.iterator();

		while (plistItr.hasNext())
			glat[index++] = new BGlAccount((GlAccount) plistItr.next());

		return glat;
	}


	@WebMethod()
	public NewGLAccountReturn newGLAccount (/*@WebParam(name = "in")*/final NewGLAccountInput in)	{
		final NewGLAccountReturn ret=new NewGLAccountReturn();

		try {
			checkLogin(in);

			final BGlAccount b=new BGlAccount();
			ret.setAccountId(b.create());
			in.setData(b);
			b.insert();
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public SaveGLAccountReturn saveGLAccount (/*@WebParam(name = "in")*/final SaveGLAccountInput in)	{
		final SaveGLAccountReturn ret=new SaveGLAccountReturn();

		try {
			checkLogin(in);

			final BGlAccount b=new BGlAccount(in.getAccountId());
			in.setData(b);
			b.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public DeleteGLAccountsReturn deleteGLAccounts (/*@WebParam(name = "in")*/final DeleteGLAccountsInput in)	{
		final DeleteGLAccountsReturn ret=new DeleteGLAccountsReturn();

		try {
			checkLogin(in);

			BGlAccount.delete(in.getAccountIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public GetGLAccountsReportReturn getGLAccountsReport (/*@WebParam(name = "in")*/final GetGLAccountsReportInput in)	{
		final GetGLAccountsReportReturn ret=new GetGLAccountsReportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl(BGlAccount.getReport());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public ListGLAccountTypesReturn listGLAccountTypes (/*@WebParam(name = "in")*/final ListGLAccountTypesInput in)	{
		final ListGLAccountTypesReturn ret=new ListGLAccountTypesReturn();

		try {
			checkLogin(in);
			
			ret.setItem(BGlAccount.getTypes());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
