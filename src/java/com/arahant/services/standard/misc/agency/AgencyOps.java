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
package com.arahant.services.standard.misc.agency;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.utils.Crypto;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscAgencyOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AgencyOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AgencyOps.class);
	
	public AgencyOps() {
		super();
	}
	
        @WebMethod()
	public SearchAgencyReturn searchAgency(/*@WebParam(name = "in")*/final SearchAgencyInput in)		
	{
		final SearchAgencyReturn ret=new SearchAgencyReturn();
		try
		{
                        checkLogin(in);

                        final BAgency a[]=BAgency.search(hsu, in.getName(),in.getAgentFirstName(),
					in.getAgentLastName(),in.getIdentifier(),ret.getCap());

			ret.setItem(a);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
    public DeleteAgencyReturn deleteAgency(/*@WebParam(name = "in")*/final DeleteAgencyInput in)		
	{
		final DeleteAgencyReturn ret=new DeleteAgencyReturn();
		try
		{
			checkLogin(in);
			
			BAgency.deleteCompanies(hsu, in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public LoadAgencyReturn loadAgency(/*@WebParam(name = "in")*/final LoadAgencyInput in) {
		final LoadAgencyReturn ret=new LoadAgencyReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setData(new BAgency(in.getAgencyId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public NewAgencyReturn newAgency(/*@WebParam(name = "in")*/final NewAgencyInput in)	{
		final NewAgencyReturn ret=new NewAgencyReturn();

		try {
			checkLogin(in);
			
			final BAgency a=new BAgency();
			ret.setId(a.create());
			in.makeAgency(a);
			a.insert();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public DeleteCompanyReturn deleteCompany(/*@WebParam(name = "in")*/final DeleteCompanyInput in) {
		final DeleteCompanyReturn ret=new DeleteCompanyReturn();

		try {
			checkLogin(in);

			BVendorCompany.deleteCompanies(hsu, in.getVendorCompanyId());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SaveAgencyReturn saveAgency(/*@WebParam(name = "in")*/final SaveAgencyInput in) {
		final SaveAgencyReturn ret=new SaveAgencyReturn();

		try
		{
			checkLogin(in);
			
			final BAgency a=new BAgency(in.getOrgGroupId());
			in.makeAgency(a);
			a.update();
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}

	@WebMethod()
	public ListEDICommunicationSchemesReturn listEDICommunicationSchemes(/*@WebParam(name = "in")*/final ListEDICommunicationSchemesInput in)		
	{
		final ListEDICommunicationSchemesReturn ret=new ListEDICommunicationSchemesReturn();
		try
		{
			checkLogin(in);

			ListEDICommunicationSchemesReturnItem[] item=new ListEDICommunicationSchemesReturnItem[1];
			item[0]=new ListEDICommunicationSchemesReturnItem();
			item[0].setDefaultPort(21);
			item[0].setId("ftp");
			item[0].setDescription("ftp");
			ret.setItem(item);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in)	{
		final SearchScreenGroupsReturn ret=new SearchScreenGroupsReturn();
		
		try
		{
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(),in.getExtId(),in.getSearchTopLevelOnly()?2:0,null,2,ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in)	{
		final SearchSecurityGroupsReturn ret=new SearchSecurityGroupsReturn();
		
		try
		{
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(),ret.getCap()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public VerifyEncryptionKeyReturn verifyEncryptionKey(/*@WebParam(name = "in")*/final VerifyEncryptionKeyInput in)		
	{
		final VerifyEncryptionKeyReturn ret=new VerifyEncryptionKeyReturn();
		try
		{
			checkLogin(in);
			
			short result = Crypto.verifyPGPPublicKeyText(in.getKeyText(), in.getKeyIdInHex());
			
			if (result == 1)
				throw new ArahantException("The Public Key Text is invalid.");
			else if (result == 2)
				throw new ArahantException("The Public Key ID could not be found.");
			else if (result == 3)
				throw new ArahantException("The Public Key ID was found but the key is not an encryption key.");
			else if (result != 0)
				throw new ArahantException("There was an unexpected error processing the Public Key data.");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
        @WebMethod()
	public SearchAssCompaniesReturn searchAssCompanies(/*@WebParam(name = "in")*/final SearchAssCompaniesInput in)
	{
		final SearchAssCompaniesReturn ret=new SearchAssCompaniesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BAgency(in.getAgencyId()).listAssociatedCompanies(in.getName(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in)		
	{
		final SearchCompaniesReturn ret=new SearchCompaniesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BAgency(in.getAgencyId()).search(in.getName(), in.getAssociatedIndicator(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public AddCompanyToAgencyReturn addCompanyToAgency(/*@WebParam(name = "in")*/final AddCompanyToAgencyInput in)		
	{
		final AddCompanyToAgencyReturn ret=new AddCompanyToAgencyReturn();
		try
		{
			checkLogin(in);
			
			new BAgency(in.getAgencyId()).assignToThisGroup(in.getCompanyId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	

	@WebMethod()
        public RemoveCompaniesFromAgencyReturn removeCompaniesFromAgency(/*@WebParam(name = "in")*/final RemoveCompaniesFromAgencyInput in)
	{
		final RemoveCompaniesFromAgencyReturn ret=new RemoveCompaniesFromAgencyReturn();
		try
		{
			checkLogin(in);
			
			new BAgency(in.getAgencyId()).removeCompanies(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
