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
package com.arahant.services.standard.misc.userAgreement;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscUserAgreementOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class UserAgreementOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			UserAgreementOps.class);
	
	public UserAgreementOps() {
		super();
	}
	
    @WebMethod()
	public ListDocumentsReturn listDocuments(/*@WebParam(name = "in")*/final ListDocumentsInput in)		
	{
		final ListDocumentsReturn ret=new ListDocumentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAgreementForm.listNotAccepted());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public AcceptDocumentsReturn acceptDocuments(/*@WebParam(name = "in")*/final AcceptDocumentsInput in)		
	{
		final AcceptDocumentsReturn ret=new AcceptDocumentsReturn();
		try
		{
			checkLogin(in);
			
			for (String id : in.getIdArray())
				new BAgreementForm(id).accept();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public DeclineLogoutReturn declineLogout(/*@WebParam(name = "in")*/final DeclineLogoutInput in)		
	{
		final DeclineLogoutReturn ret=new DeclineLogoutReturn();
		try
		{
			checkLogin(in);
			
			ArahantSession.setReturnCode(100);
			ArahantSession.addReturnMessage("You will now be logged out because you declined the agreement.");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public GetDocumentReportReturn getDocumentReport(/*@WebParam(name = "in")*/final GetDocumentReportInput in)		
	{
		final GetDocumentReportReturn ret=new GetDocumentReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BAgreementForm(in.getId()).getReport());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteDocumentReturn deleteDocument(/*@WebParam(name = "in")*/final DeleteDocumentInput in)		
	{
		final DeleteDocumentReturn ret=new DeleteDocumentReturn();
		try
		{
			checkLogin(in);

			new BAgreementForm(in.getId()).delete();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListAllDocumentsReturn listAllDocuments(/*@WebParam(name = "in")*/final ListAllDocumentsInput in)		
	{
		final ListAllDocumentsReturn ret=new ListAllDocumentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAgreementForm.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)			{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_ANNOUNCEMENTS));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
