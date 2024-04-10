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
package com.arahant.services.standard.hrConfig.benefitSetup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.BenefitConfigsReport;
import com.arahant.utils.Crypto;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigBenefitSetupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitSetupOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitSetupOps.class);
	
	public BenefitSetupOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 	

	@WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in)			{
		final ListBenefitsReturn ret=new ListBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.list(hsu));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteBenefitsReturn deleteBenefits(/*@WebParam(name = "in")*/final DeleteBenefitsInput in)			{
		final DeleteBenefitsReturn ret=new DeleteBenefitsReturn();
		try
		{
			checkLogin(in);
			
			BHRBenefit.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetBenefitReportReturn getBenefitReport(/*@WebParam(name = "in")*/final GetBenefitReportInput in)			{
		final GetBenefitReportReturn ret=new GetBenefitReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(BHRBenefit.getReport(hsu, in.getIncludeConfigurations()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchVendorsReturn searchVendors(/*@WebParam(name = "in")*/final SearchVendorsInput in)		
	{
		final SearchVendorsReturn ret=new SearchVendorsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BVendorCompany.searchVendors(in.getName(),ret.getHighCap()));
			
			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchVendorsReturnItem(new BVendorCompany(in.getId())));
			

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListGlAccountsByTypeReturn listGlAccountsByType(/*@WebParam(name = "in")*/final ListGlAccountsByTypeInput in)	{
		
		final ListGlAccountsByTypeReturn ret=new ListGlAccountsByTypeReturn();	

		try {
			checkLogin(in);
			
			ret.setGLAccounts(BGlAccount.listByType(hsu, 10));
			
			finishService(ret);
	
		} catch (final Throwable e) {
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
	public LoadVendorCompanyReturn loadVendorCompany(/*@WebParam(name = "in")*/final LoadVendorCompanyInput in) {
		final LoadVendorCompanyReturn ret=new LoadVendorCompanyReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setData(new BVendorCompany(in.getVendorCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public NewVendorCompanyReturn newVendorCompany(/*@WebParam(name = "in")*/final NewVendorCompanyInput in)	{
		final NewVendorCompanyReturn ret=new NewVendorCompanyReturn();

		try {
			checkLogin(in);
			
			final BVendorCompany v=new BVendorCompany();
			ret.setId(v.create());
			in.makeVendor(v);
			v.insert();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SaveVendorCompanyReturn saveVendorCompany(/*@WebParam(name = "in")*/final SaveVendorCompanyInput in) {
		final SaveVendorCompanyReturn ret=new SaveVendorCompanyReturn();

		try
		{
			checkLogin(in);
			
			final BVendorCompany v=new BVendorCompany(in.getOrgGroupId());
			in.makeCompany(v);
			v.update();
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}

	@WebMethod()
	public ListRulesReturn listRules(/*@WebParam(name = "in")*/final ListRulesInput in)			{
		final ListRulesReturn ret=new ListRulesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listRules());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*@WebParam(name = "in")*/final ListBenefitCategoriesInput in)			{
		final ListBenefitCategoriesReturn ret=new ListBenefitCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in)	{
		final SearchServicesReturn ret=new SearchServicesReturn();

		try {
			checkLogin(in);
			
			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(), ret.getHighCap()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewBenefitReturn newBenefit(/*@WebParam(name = "in")*/final NewBenefitInput in)			{
		final NewBenefitReturn ret=new NewBenefitReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefit x=new BHRBenefit();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in)		
	{
		final ListWageTypesReturn ret=new ListWageTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BWageType.list(ret.getHighCap()));
			

			if (!isEmpty(in.getWageTypeId()))
				ret.setSelectedItem(new ListWageTypesReturnItem(new BWageType(in.getWageTypeId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveBenefitReturn saveBenefit(/*@WebParam(name = "in")*/final SaveBenefitInput in)		
	{
		final SaveBenefitReturn ret=new SaveBenefitReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefit x=new BHRBenefit(in.getBenefitId());
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
	public LoadBenefitForConfigReturn loadBenefitForConfig(/*@WebParam(name = "in")*/final LoadBenefitForConfigInput in)			{
		final LoadBenefitForConfigReturn ret=new LoadBenefitForConfigReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHRBenefit(in.getBenefitId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in)		
	{
		final ListBenefitClassesReturn ret=new ListBenefitClassesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBenefitClass.list(in.getExcludeIds()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadConfigReturn loadConfig(/*@WebParam(name = "in")*/final LoadConfigInput in)			{
		final LoadConfigReturn ret=new LoadConfigReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHRBenefitConfig(in.getConfigId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewConfigReturn newConfig(/*@WebParam(name = "in")*/final NewConfigInput in)			{
		final NewConfigReturn ret=new NewConfigReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitConfig x=new BHRBenefitConfig();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveConfigReturn saveConfig(/*@WebParam(name = "in")*/final SaveConfigInput in)			{
		final SaveConfigReturn ret=new SaveConfigReturn();
		try
		{
			checkLogin(in);
			//ArahantSession.getAI().watchAll();
			final BHRBenefitConfig x=new BHRBenefitConfig(in.getConfigId());
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
	public ListAllBenefitsReturn listAllBenefits(/*@WebParam(name = "in")*/final ListAllBenefitsInput in)			{
		final ListAllBenefitsReturn ret=new ListAllBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.list(hsu));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadBenefitReturn loadBenefit(/*@WebParam(name = "in")*/final LoadBenefitInput in)			{
		final LoadBenefitReturn ret=new LoadBenefitReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHRBenefit(in.getBenefitId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CopyBenefitReturn copyBenefit(/*@WebParam(name = "in")*/final CopyBenefitInput in)		
	{
		final CopyBenefitReturn ret=new CopyBenefitReturn();
		try
		{
			checkLogin(in);
			
			new BHRBenefit(in.getBenefitId()).deepCopy(in.getName());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BenefitConfigsReport().build(in.getBenefitId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)		
	{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(in.getExcludeIds()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*@WebParam(name = "in")*/final ListOrgGroupsForGroupInput in) {
		final ListOrgGroupsForGroupReturn ret = new ListOrgGroupsForGroupReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getOrgGroupId())) {
				ret.setItem(new BOrgGroup(in.getOrgGroupId()).getChildren(in.getExcludeIds()));
			} else {
				ret.setItem(new BOrgGroup[]{new BOrgGroup(hsu.getCurrentCompany())});
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
