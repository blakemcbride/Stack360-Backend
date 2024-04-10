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


package com.arahant.services.standard.hr.employeePayroll;
import com.arahant.beans.EmployeeChanged;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exports.CompuPayExport;
import com.arahant.exports.EvolutionExport;
import com.arahant.reports.EmployeePayrollReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmployeePayrollOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeePayrollOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeePayrollOps.class);
	
	public EmployeePayrollOps() {
		super();
	}
	
        @WebMethod()
	public LoadPayrollReturn loadPayroll(/*@WebParam(name = "in")*/final LoadPayrollInput in)		
	{
		final LoadPayrollReturn ret=new LoadPayrollReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getPersonId()));
			
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
			
			ret.setAccessLevel(BRight.checkRight("HRTraining"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public ListW4StatusesReturn listW4Statuses(/*@WebParam(name = "in")*/final ListW4StatusesInput in)	{
		final ListW4StatusesReturn ret=new ListW4StatusesReturn();
		try
		{
			checkLogin(in);
			
			//ret.setItem(BHRW4Status.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
        @WebMethod()
	public SavePayrollReturn savePayroll(/*@WebParam(name = "in")*/final SavePayrollInput in)		
	{
		final SavePayrollReturn ret=new SavePayrollReturn();
		try
		{
			checkLogin(in);
			
			final BEmployee x=new BEmployee(in.getPersonId());
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
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new EmployeePayrollReport().build(new BEmployee(in.getPersonId())));
				
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public RunPayrollExportReturn runPayrollExport(/*@WebParam(name = "in")*/final RunPayrollExportInput in)		
	{
		final RunPayrollExportReturn ret=new RunPayrollExportReturn();
		try
		{
			checkLogin(in);
			
			BEmployee bemp=new BEmployee(in.getId());
			
			//remove them from export list
			hsu.createCriteria(EmployeeChanged.class)
				.eq(EmployeeChanged.INTERFACEID, EmployeeChanged.TYPE_COMPUPAY_INTERFACE)
				.eq(EmployeeChanged.EMPLOYEE, bemp.getEmployee())
				.delete();


			if (BProperty.getBoolean("UseCompupay",false))
				new CompuPayExport().exportEmployee(bemp);
			if (BProperty.getBoolean("UseEvolution",true))
				new EvolutionExport().exportEmployee(bemp);
			

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListBankAccountsReturn listBankAccounts(/*@WebParam(name = "in")*/final ListBankAccountsInput in)		
	{
		final ListBankAccountsReturn ret=new ListBankAccountsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBankAccount.list(in.getPersonId())); //list actives
			
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
	
	

}
