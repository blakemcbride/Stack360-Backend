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
package com.arahant.services.standard.hrConfig.benefitImportConfig;
import com.arahant.beans.ImportColumn;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.imports.GenericFileImport;
import com.arahant.reports.BenefitImportConfigReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigBenefitImportConfigOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitImportConfigOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitImportConfigOps.class);
	
	public BenefitImportConfigOps() {
		super();
	}
	
    @WebMethod()
	public SearchBenefitImportConfigsReturn searchBenefitImportConfigs(/*@WebParam(name = "in")*/final SearchBenefitImportConfigsInput in)		
	{
		final SearchBenefitImportConfigsReturn ret=new SearchBenefitImportConfigsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BImportType.searchBenefitImportConfigs(in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public LoadBenefitImportConfigReturn loadBenefitImportConfig(/*@WebParam(name = "in")*/final LoadBenefitImportConfigInput in)		
	{
		final LoadBenefitImportConfigReturn ret=new LoadBenefitImportConfigReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BImportType(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public LoadBenefitImportFiltersReturn loadBenefitImportFilters(/*@WebParam(name = "in")*/final LoadBenefitImportFiltersInput in)		
	{
		final LoadBenefitImportFiltersReturn ret=new LoadBenefitImportFiltersReturn();
		try
		{
			checkLogin(in);

			ret.setFilters(new BImportType(in.getId()).listFilters());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public LoadBenefitImportColumnsReturn loadBenefitImportColumns(/*@WebParam(name = "in")*/final LoadBenefitImportColumnsInput in)		
	{
		final LoadBenefitImportColumnsReturn ret=new LoadBenefitImportColumnsReturn();
		try
		{
			checkLogin(in);

			if (in.getId().isEmpty())
			{
				ret.setColumns(GenericFileImport.availableColumns, ImportColumn.getAvailableColumnsFilter1Required());
			}
			else
			{
				BImportType bi = new BImportType(in.getTypeId());
				ret.setColumns(new BImportFilter(in.getId()).listColumns(), bi.getImportProgramName());
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveBenefitImportConfigReturn saveBenefitImportConfig(/*@WebParam(name = "in")*/final SaveBenefitImportConfigInput in)		
	{
		final SaveBenefitImportConfigReturn ret=new SaveBenefitImportConfigReturn();
		try
		{
			checkLogin(in);
			
			final BImportType x=new BImportType(in.getId());
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
	public NewBenefitImportConfigReturn newBenefitImportConfig(/*@WebParam(name = "in")*/final NewBenefitImportConfigInput in)		
	{
		final NewBenefitImportConfigReturn ret=new NewBenefitImportConfigReturn();
		try
		{
			checkLogin(in);
			
			final BImportType x=new BImportType();
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
	public ListBenefitImportProgramsReturn listBenefitImportPrograms(/*@WebParam(name = "in")*/final ListBenefitImportProgramsInput in)		
	{
		final ListBenefitImportProgramsReturn ret=new ListBenefitImportProgramsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BImportType.getProgram());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveBenefitImportFilterReturn saveBenefitImportFilter(/*@WebParam(name = "in")*/final SaveBenefitImportFilterInput in)		
	{
		final SaveBenefitImportFilterReturn ret=new SaveBenefitImportFilterReturn();
		try
		{
			checkLogin(in);
			
			final BImportFilter x=new BImportFilter(in.getId());
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
	public NewBenefitImportFilterReturn newBenefitImportFilter(/*@WebParam(name = "in")*/final NewBenefitImportFilterInput in)		
	{
		final NewBenefitImportFilterReturn ret=new NewBenefitImportFilterReturn();
		try
		{
			checkLogin(in);
			
			final BImportFilter x=new BImportFilter();
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
	public SaveBenefitImportColumnReturn saveBenefitImportColumn(/*@WebParam(name = "in")*/final SaveBenefitImportColumnInput in)		
	{
		final SaveBenefitImportColumnReturn ret=new SaveBenefitImportColumnReturn();
		try
		{
			checkLogin(in);
			
			final BImportColumn x=new BImportColumn(in.getId());
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
	public NewBenefitImportColumnReturn newBenefitImportColumn(/*@WebParam(name = "in")*/final NewBenefitImportColumnInput in)		
	{
		final NewBenefitImportColumnReturn ret=new NewBenefitImportColumnReturn();
		try
		{
			checkLogin(in);
			
			final BImportColumn x=new BImportColumn();
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
    public DeleteBenefitImportConfigsReturn deleteBenefitImportConfigs(/*@WebParam(name = "in")*/final DeleteBenefitImportConfigsInput in)
	{
		final DeleteBenefitImportConfigsReturn ret=new DeleteBenefitImportConfigsReturn();
		try
		{
			checkLogin(in);
			
			BImportType.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteBenefitImportFiltersReturn deleteBenefitImportFilters(/*@WebParam(name = "in")*/final DeleteBenefitImportFiltersInput in)
	{
		final DeleteBenefitImportFiltersReturn ret=new DeleteBenefitImportFiltersReturn();
		try
		{
			checkLogin(in);
			
			BImportFilter.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteBenefitImportColumnsReturn deleteBenefitImportColumns(/*@WebParam(name = "in")*/final DeleteBenefitImportColumnsInput in)
	{
		final DeleteBenefitImportColumnsReturn ret=new DeleteBenefitImportColumnsReturn();
		try
		{
			checkLogin(in);
			
			BImportColumn.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();

		try {
			checkLogin(in);

			if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==BRight.ACCESS_LEVEL_WRITE)
			{
				ret.setAllCompanies(true);
			}
			else
				ret.setAllCompanies(false);

			finishService(ret);
		} catch (final Throwable e) {
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
			
			ret.setReportUrl(new BenefitImportConfigReport().build(in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
