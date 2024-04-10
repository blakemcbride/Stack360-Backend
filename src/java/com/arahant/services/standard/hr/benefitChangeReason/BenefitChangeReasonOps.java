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
/**
/**
 * 
 */
package com.arahant.services.standard.hr.benefitChangeReason;

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.Right;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitChangeReasonOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitChangeReasonOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitChangeReasonOps.class);
	
	public BenefitChangeReasonOps() {
		super();
	}
	
	@WebMethod()
	public ListReasonsReturn listReasons(/*@WebParam(name = "in")*/final ListReasonsInput in)			{
		final ListReasonsReturn ret=new ListReasonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitChangeReason.list(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public NewReasonReturn newReason(/*@WebParam(name = "in")*/final NewReasonInput in)			{
		final NewReasonReturn ret=new NewReasonReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitChangeReason x=new BHRBenefitChangeReason();
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
	public SaveReasonReturn saveReason(/*@WebParam(name = "in")*/final SaveReasonInput in)			{
		final SaveReasonReturn ret=new SaveReasonReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitChangeReason x=new BHRBenefitChangeReason(in.getReasonId());
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
	public DeleteReasonsReturn deleteReasons(/*@WebParam(name = "in")*/final DeleteReasonsInput in)			{
		final DeleteReasonsReturn ret=new DeleteReasonsReturn();
		try
		{
			checkLogin(in);
			
			BHRBenefitChangeReason.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)			{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BHRBenefitChangeReason().getReport());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public LoadReasonReturn loadReason(/*@WebParam(name = "in")*/final LoadReasonInput in)			{
		final LoadReasonReturn ret=new LoadReasonReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHRBenefitChangeReason(in.getReasonId()));
			
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
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HR));
			ret.setMultipleCompanies((BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==BRight.ACCESS_LEVEL_WRITE));

			finishService(ret);
		} catch (final Throwable e) {
			hsu.rollbackTransaction();
			logger.error(e);
			return new CheckRightReturn("Failed: Contact Administrator");
		}
		
		return ret;
	} 
	


	@WebMethod()
	public ListTypesReturn listTypes(/*@WebParam(name = "in")*/final ListTypesInput in)			{
		final ListTypesReturn ret=new ListTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitChangeReason.listTypes());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadQualifyingEventsReturn loadQualifyingEvents(/*@WebParam(name = "in")*/final LoadQualifyingEventsInput in)		
	{
		final LoadQualifyingEventsReturn ret=new LoadQualifyingEventsReturn();
		try
		{
			checkLogin(in);

//        public static final short EVENT_TYPE_UNKNOWN=0;
//	public static final short EVENT_TYPE_DIVORCE=1;
//	public static final short EVENT_TYPE_DEPENDENT=2;
//	public static final short EVENT_TYPE_DEATH=3;
//	public static final short EVENT_TYPE_VOLUNTARY_TERM=4;
//	public static final short EVENT_TYPE_INVOLUNTARY_TERM=5;
//	public static final short EVENT_TYPE_ELECTED_COBRA=6;
//	public static final short EVENT_TYPE_MARRIAGE=7;
//	public static final short EVENT_TYPE_GAINED_OTHER_COVERAGE=8;
//	public static final short EVENT_TYPE_DEPENDENT_INELIGIBLE=9;
//	public static final short EVENT_TYPE_REINSTATEMENT=10;
//	public static final short EVENT_TYPE_REDUCTION_IN_HOURS=11;
//	public static final short EVENT_TYPE_ENTITLED_MEDICARE=12;

//        public static final short EVENT_TYPE_RETIRED=13;
//        public static final short EVENT_TYPE_LAIDOFF=14;
//        public static final short EVENT_TYPE_NAME_CHANGE=15;
//        public static final short EVENT_TYPE_TRANSFER=16;
//        public static final short EVENT_TYPE_WORK_STATUS=17;
//        public static final short EVENT_TYPE_PCP_CHANGE=18;
//        public static final short EVENT_TYPE_FCR_DCCR=19;


                        LoadQualifyingEventsReturnItem[] item = new LoadQualifyingEventsReturnItem[20];
                        item[0] = new LoadQualifyingEventsReturnItem();
                        item[0].setEventId(HrBenefitChangeReason.EVENT_TYPE_UNKNOWN);
                        item[0].setEventName("Unknown");

                        item[1] = new LoadQualifyingEventsReturnItem();
                        item[1].setEventId(HrBenefitChangeReason.EVENT_TYPE_DIVORCE);
                        item[1].setEventName("Divorce");

                        item[2] = new LoadQualifyingEventsReturnItem();
                        item[2].setEventId(HrBenefitChangeReason.EVENT_TYPE_DEPENDENT);
                        item[2].setEventName("Dependent");

                        item[3] = new LoadQualifyingEventsReturnItem();
                        item[3].setEventId(HrBenefitChangeReason.EVENT_TYPE_DEATH);
                        item[3].setEventName("Death");

                        item[4] = new LoadQualifyingEventsReturnItem();
                        item[4].setEventId(HrBenefitChangeReason.EVENT_TYPE_VOLUNTARY_TERM);
                        item[4].setEventName("Voluntary Termination");

                        item[5] = new LoadQualifyingEventsReturnItem();
                        item[5].setEventId(HrBenefitChangeReason.EVENT_TYPE_INVOLUNTARY_TERM);
                        item[5].setEventName("InVoluntary Termination");

                        item[6] = new LoadQualifyingEventsReturnItem();
                        item[6].setEventId(HrBenefitChangeReason.EVENT_TYPE_ELECTED_COBRA);
                        item[6].setEventName("Elected Cobra");

                        item[7] = new LoadQualifyingEventsReturnItem();
                        item[7].setEventId(HrBenefitChangeReason.EVENT_TYPE_MARRIAGE);
                        item[7].setEventName("Marriage");

                        item[8] = new LoadQualifyingEventsReturnItem();
                        item[8].setEventId(HrBenefitChangeReason.EVENT_TYPE_GAINED_OTHER_COVERAGE);
                        item[8].setEventName("Other Coverage");

                        item[9] = new LoadQualifyingEventsReturnItem();
                        item[9].setEventId(HrBenefitChangeReason.EVENT_TYPE_DEPENDENT_INELIGIBLE);
                        item[9].setEventName("Dependent Ineligible");

                        item[10] = new LoadQualifyingEventsReturnItem();
                        item[10].setEventId(HrBenefitChangeReason.EVENT_TYPE_REINSTATEMENT);
                        item[10].setEventName("Reinstated");

                        item[11] = new LoadQualifyingEventsReturnItem();
                        item[11].setEventId(HrBenefitChangeReason.EVENT_TYPE_REDUCTION_IN_HOURS);
                        item[11].setEventName("Reduction in Hours");

                        item[12] = new LoadQualifyingEventsReturnItem();
                        item[12].setEventId(HrBenefitChangeReason.EVENT_TYPE_ENTITLED_MEDICARE);
                        item[12].setEventName("Entitled to Medicare");

                        item[13] = new LoadQualifyingEventsReturnItem();
                        item[13].setEventId(HrBenefitChangeReason.EVENT_TYPE_RETIRED);
                        item[13].setEventName("Retired/Retiree");

                        item[14] = new LoadQualifyingEventsReturnItem();
                        item[14].setEventId(HrBenefitChangeReason.EVENT_TYPE_LAIDOFF);
                        item[14].setEventName("Laidoff");

                        item[15] = new LoadQualifyingEventsReturnItem();
                        item[15].setEventId(HrBenefitChangeReason.EVENT_TYPE_NAME_CHANGE);
                        item[15].setEventName("Name Changed");

                        item[16] = new LoadQualifyingEventsReturnItem();
                        item[16].setEventId(HrBenefitChangeReason.EVENT_TYPE_TRANSFER);
                        item[16].setEventName("Transfer");

                        item[17] = new LoadQualifyingEventsReturnItem();
                        item[17].setEventId(HrBenefitChangeReason.EVENT_TYPE_WORK_STATUS);
                        item[17].setEventName("Work Status (Salary,Part/Full Time,Hourly");

                        item[18] = new LoadQualifyingEventsReturnItem();
                        item[18].setEventId(HrBenefitChangeReason.EVENT_TYPE_PCP_CHANGE);
                        item[18].setEventName("PCP Change");

                        item[19] = new LoadQualifyingEventsReturnItem();
                        item[19].setEventId(HrBenefitChangeReason.EVENT_TYPE_FCR_DCCR);
                        item[19].setEventName("FCR/DDCR");

			ret.setItem(item);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
