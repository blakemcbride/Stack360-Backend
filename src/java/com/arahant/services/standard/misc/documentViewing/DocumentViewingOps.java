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
package com.arahant.services.standard.misc.documentViewing;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscDocumentViewingOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DocumentViewingOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			DocumentViewingOps.class);
	
	public DocumentViewingOps() {
		super();
	}
	
        @WebMethod()
	public ListFoldersForCurrentPersonReturn listFoldersForCurrentPerson(/*@WebParam(name = "in")*/final ListFoldersForCurrentPersonInput in)		
	{
		final ListFoldersForCurrentPersonReturn ret=new ListFoldersForCurrentPersonReturn();
		try
		{
			checkLogin(in);

			ret.setFolders(BCompanyFormFolder.listFoldersForCurrentPerson(null));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetFormReturn getForm(/*@WebParam(name = "in")*/final GetFormInput in)		
	{
		final GetFormReturn ret=new GetFormReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BCompanyForm(in.getDocumentId()).getForm());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDocumentsInFolderReturn listDocumentsInFolder(/*@WebParam(name = "in")*/final ListDocumentsInFolderInput in)		
	{
		final ListDocumentsInFolderReturn ret=new ListDocumentsInFolderReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BCompanyFormFolder(in.getFolderId()).listChildForms());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
