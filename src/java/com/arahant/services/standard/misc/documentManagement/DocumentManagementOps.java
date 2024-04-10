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
package com.arahant.services.standard.misc.documentManagement;
import com.arahant.beans.Right;
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
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscDocumentManagementOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DocumentManagementOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			DocumentManagementOps.class);
	
	public DocumentManagementOps() {
		super();
	}
	     
    @WebMethod()
	public ListFoldersAndDocumentsReturn listFoldersAndDocuments(/*@WebParam(name = "in")*/final ListFoldersAndDocumentsInput in)		
	{
		final ListFoldersAndDocumentsReturn ret=new ListFoldersAndDocumentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BCompanyFormFolder().listParentFolders());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewFolderReturn newFolder(/*@WebParam(name = "in")*/final NewFolderInput in)
	{
		final NewFolderReturn ret=new NewFolderReturn();
		try
		{
			checkLogin(in);
			
            final BCompanyFormFolder bcf=new BCompanyFormFolder();
			ret.setId(bcf.create());
			in.setData(bcf);
			
			bcf.setAllCompanies(in.isAllCompanies());

			bcf.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SaveFolderReturn saveFolder(/*@WebParam(name = "in")*/final SaveFolderInput in)		
	{
		final SaveFolderReturn ret=new SaveFolderReturn();
		try
		{
			checkLogin(in);
			
                        final BCompanyFormFolder bcf=new BCompanyFormFolder(in.getId());
			in.setData(bcf);
			bcf.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in)	{
		final ListFormTypesReturn ret=new ListFormTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BFormType.list('E'));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


    @WebMethod()
	public LoadDocumentReturn loadDocument(/*@WebParam(name = "in")*/final LoadDocumentInput in)		
	{
		final LoadDocumentReturn ret=new LoadDocumentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BCompanyForm(in.getDocumentId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
    @WebMethod()
	public SaveDocumentReturn saveDocument(/*@WebParam(name = "in")*/final SaveDocumentInput in)		
	{
		final SaveDocumentReturn ret=new SaveDocumentReturn();
		try
		{
			checkLogin(in);
			
			final BCompanyForm x=new BCompanyForm(in.getId());
			in.setData(x);
			x.update();
			
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
	

	@WebMethod()
    public RemoveDocumentFromFolderReturn removeDocumentFromFolder(/*@WebParam(name = "in")*/final RemoveDocumentFromFolderInput in)
	{
		final RemoveDocumentFromFolderReturn ret=new RemoveDocumentFromFolderReturn();
		try
		{
			checkLogin(in);
			
			for (RemoveDocumentFromFolderInputItem i : in.getIds())
                            new BCompanyForm(i.getDocumentId()).remove(i.getFolderId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public AddDocumentToFolderReturn addDocumentToFolder(/*@WebParam(name = "in")*/final AddDocumentToFolderInput in)		
	{
		final AddDocumentToFolderReturn ret=new AddDocumentToFolderReturn();
		try
		{
			checkLogin(in);

                        for (String id : in.getDocumentIds())
                            new BCompanyForm(id).copyTo(in.getFolderId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteFolderDeepReturn deleteFolderDeep(/*@WebParam(name = "in")*/final DeleteFolderDeepInput in)
	{
		final DeleteFolderDeepReturn ret=new DeleteFolderDeepReturn();
		try
		{
			checkLogin(in);
			
			new BCompanyFormFolder(in.getFolderId()).delete();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListOrgGroupsForFolderReturn listOrgGroupsForFolder(/*@WebParam(name = "in")*/final ListOrgGroupsForFolderInput in)		
	{
		final ListOrgGroupsForFolderReturn ret=new ListOrgGroupsForFolderReturn();
		try
		{
			checkLogin(in);

			ret.setOrgGroup(new BCompanyFormFolder(in.getFolderId()).listOrgGroups());
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)		
	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);


            ret.setOrgGroup(BOrgGroup.listAssociatedOrgGroups(hsu, in.getOrgGroupId(), in.getFolderId(), COMPANY_TYPE, in.getName(), in.getExcludeIds(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public AssociateOrgGroupsToFolderReturn associateOrgGroupsToFolder(/*@WebParam(name = "in")*/final AssociateOrgGroupsToFolderInput in)		
	{
		final AssociateOrgGroupsToFolderReturn ret=new AssociateOrgGroupsToFolderReturn();
		try
		{
			checkLogin(in);
			
                        for (String id : in.getOrgGroupIds())
                            new BOrgGroup(id).associateTo(in.getFolderId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public DisassociateOrgGroupsFromFolderReturn disassociateOrgGroupsFromFolder(/*@WebParam(name = "in")*/final DisassociateOrgGroupsFromFolderInput in)
	{
		final DisassociateOrgGroupsFromFolderReturn ret=new DisassociateOrgGroupsFromFolderReturn();
		try
		{
			checkLogin(in);
			
                        for (String i : in.getOrgGroupIds())
                            new BOrgGroup(i).remove(in.getFolderId());
			
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setCanAccessAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadCompanyForFolderReturn loadCompanyForFolder(/*@WebParam(name = "in")*/final LoadCompanyForFolderInput in)		
	{
		final LoadCompanyForFolderReturn ret=new LoadCompanyForFolderReturn();
		try
		{
			checkLogin(in);

			if (new BCompanyFormFolder(in.getFolderId()).getBean().getOrgGroup()==null)
				ret.setCompanyName("All");
			else
				ret.setCompanyName(ArahantSession.getHSU().getCurrentCompany().getName());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
