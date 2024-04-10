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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrPosition;

import com.arahant.business.BBenefitClass;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRPosition;
import com.arahant.business.BRight;
import com.arahant.reports.HRPositionReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrPositionOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRPositionOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRPositionOps.class);

	public HRPositionOps() {
		super();
	}
	
	
	
	@WebMethod()
	public DeletePositionsReturn deletePositions(/*@WebParam(name = "in")*/final DeletePositionsInput in)	{
		final DeletePositionsReturn ret=new DeletePositionsReturn();
		try
		{
			checkLogin(in);
			
			BHRPosition.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public GetPositionsReportReturn getPositionsReport(/*@WebParam(name = "in")*/final GetPositionsReportInput in)	{
		final GetPositionsReportReturn ret=new GetPositionsReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setFileName(new HRPositionReport().build(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in)	{
		final ListPositionsReturn ret=new ListPositionsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHRPosition.list(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public NewPositionReturn newPosition(/*@WebParam(name = "in")*/final NewPositionInput in)			{
		final NewPositionReturn ret=new NewPositionReturn();
		try
		{
			checkLogin(in);
			
			final BHRPosition x=new BHRPosition();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SavePositionReturn savePosition(/*@WebParam(name = "in")*/final SavePositionInput in)	{
		final SavePositionReturn ret=new SavePositionReturn();
		try
		{
			checkLogin(in);
			
			final BHRPosition x=new BHRPosition(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
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
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in)		
	{
		final ListBenefitClassesReturn ret=new ListBenefitClassesReturn();
		try
		{
			checkLogin(in);


			BBenefitClass []actives=BBenefitClass.listActive();

			BBenefitClass retAr[]=actives;

			if (!isEmpty(in.getBenefitClassId()))
			{
				BBenefitClass c=new BBenefitClass(in.getBenefitClassId());

				boolean found=false;
				for (int loop=0;loop<actives.length && !found;loop++)
					found=c.getId().equals(actives[loop].getId());
				
				if (!found)
				{
					retAr=new BBenefitClass[actives.length+1];
					retAr[0]=c;
					for (int loop=0;loop<actives.length;loop++)
						retAr[loop+1]=actives[loop];
				}
			}


			ret.setItem(retAr);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
