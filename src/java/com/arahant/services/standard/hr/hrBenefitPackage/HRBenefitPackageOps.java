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
 * Created on Mar 15, 2007
 * 
 */
package com.arahant.services.standard.hr.hrBenefitPackage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefitPackage;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Mar 15, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrBenefitPackageOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRBenefitPackageOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRBenefitPackageOps.class);

	public HRBenefitPackageOps() {
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
	public ListAssignedBenefitsReturn listAssignedBenefits(/*@WebParam(name = "in")*/final ListAssignedBenefitsInput in)	{
		final ListAssignedBenefitsReturn ret=new ListAssignedBenefitsReturn();
	
		try {
			checkLogin(in);

			ret.setItem(new BHRBenefitPackage(in.getPackageId()).listAssignedBenefits());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListUnassignedBenefitsReturn listUnassignedBenefits(/*@WebParam(name = "in")*/final ListUnassignedBenefitsInput in)	{
		final ListUnassignedBenefitsReturn ret=new ListUnassignedBenefitsReturn();
	
		try {
			checkLogin(in);

			ret.setItem(new BHRBenefitPackage(in.getPackageId()).listUnassignedBenefits());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewBenefitPackageReturn newBenefitPackage(/*@WebParam(name = "in")*/final NewBenefitPackageInput in)	{
		final NewBenefitPackageReturn ret=new NewBenefitPackageReturn();
	
		try {
			checkLogin(in);

			final BHRBenefitPackage bp=new BHRBenefitPackage();
			ret.setId(bp.create());
			in.setData(bp);
			bp.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveBenefitPackageReturn saveBenefitPackage(/*@WebParam(name = "in")*/final SaveBenefitPackageInput in)	{
		final SaveBenefitPackageReturn ret=new SaveBenefitPackageReturn();
	
		try {
			checkLogin(in);

			final BHRBenefitPackage bp=new BHRBenefitPackage(in.getId());
			in.setData(bp);
			bp.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public DeleteBenefitPackageReturn deleteBenefitPackage(/*@WebParam(name = "in")*/final DeleteBenefitPackageInput in)	{
		final DeleteBenefitPackageReturn ret=new DeleteBenefitPackageReturn();
	
		try {
			checkLogin(in);

			new BHRBenefitPackage(in.getId()).delete();
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListBenefitPackagesReturn listBenefitPackages(/*@WebParam(name = "in")*/final ListBenefitPackagesInput in)	{
		final ListBenefitPackagesReturn ret=new ListBenefitPackagesReturn();
	
		try {
			checkLogin(in);

			ret.setItem(BHRBenefitPackage.list(hsu));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public AssignBenefitReturn assignBenefit(/*@WebParam(name = "in")*/final AssignBenefitInput in)	{
		final AssignBenefitReturn ret=new AssignBenefitReturn();
		
		try {
			checkLogin(in);

			new BHRBenefitPackage(in.getPackageId()).assign(in.getBenefitIds());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public UnassignBenefitReturn unassignBenefit(/*@WebParam(name = "in")*/final UnassignBenefitInput in)	{
		final UnassignBenefitReturn ret=new UnassignBenefitReturn();
		
		try {
			checkLogin(in);

			new BHRBenefitPackage(in.getPackageId()).unassign(in.getBenefitIds());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public GetBenefitPackageReportReturn getBenefitPackageReport(/*@WebParam(name = "in")*/final GetBenefitPackageReportInput in)	{
		final GetBenefitPackageReportReturn ret=new GetBenefitPackageReportReturn();
		
		try {
			checkLogin(in);

			ret.setFileName(BHRBenefitPackage.getReport(in.getPackageId(), hsu));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}

/*
HrBenefitPackage (new)

1. Need CheckRight (setups version of hr access)
2. Need List Benefit Categories Assigned to Package
3. Need List Benefit Categories Not Assigned to Package
4. Need New Benefit Package (input is name)
5. Need Edit Benefit Package (input is name, id)
6. Need Delete Benefit Package (input is only one Benefit Package id)
7. Need List Benefit Packages (name and id)
8. Need Assign Beneift Category to Benefit Package (package id, 1 or more categories)
9. Need Unassign Benefit Category to Benefit Package (package id, 1 or more categories)
10. Need Report (package id)
*/
