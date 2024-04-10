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
package com.arahant.services.standard.misc.announcement;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BAnnouncement;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscAnnouncementOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AnnouncementOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AnnouncementOps.class);
	
	public AnnouncementOps() {
		super();
	}
	
	@WebMethod()
	public ListAnnouncementsReturn listAnnouncements(/*@WebParam(name = "in")*/final ListAnnouncementsInput in)			{
		final ListAnnouncementsReturn ret=new ListAnnouncementsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAnnouncement.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public MoveAnnouncementUpReturn moveAnnouncementUp(/*@WebParam(name = "in")*/final MoveAnnouncementUpInput in)			{
		final MoveAnnouncementUpReturn ret=new MoveAnnouncementUpReturn();
		try
		{
			checkLogin(in);

			BAnnouncement.moveUp(in.getAnnouncementId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public MoveAnnouncementDownReturn moveAnnouncementDown(/*@WebParam(name = "in")*/final MoveAnnouncementDownInput in)			{
		final MoveAnnouncementDownReturn ret=new MoveAnnouncementDownReturn();
		try
		{
			checkLogin(in);
			
			BAnnouncement.moveDown(in.getAnnouncementId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public DeleteAnnouncementsReturn deleteAnnouncements(/*@WebParam(name = "in")*/final DeleteAnnouncementsInput in)			{
		final DeleteAnnouncementsReturn ret=new DeleteAnnouncementsReturn();
		try
		{
			checkLogin(in);
			
			BAnnouncement.delete(in.getAnnouncementIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetAnnouncementReportReturn getAnnouncementReport(/*@WebParam(name = "in")*/final GetAnnouncementReportInput in)			{
		final GetAnnouncementReportReturn ret=new GetAnnouncementReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BAnnouncement().getReport(in.getOrgGroupId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public NewAnnouncementReturn newAnnouncement(/*@WebParam(name = "in")*/final NewAnnouncementInput in)			{
		final NewAnnouncementReturn ret=new NewAnnouncementReturn();
		try
		{
			checkLogin(in);
			
			final BAnnouncement x=new BAnnouncement();
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
	public SaveAnnouncementReturn saveAnnouncement(/*@WebParam(name = "in")*/final SaveAnnouncementInput in)			{
		final SaveAnnouncementReturn ret=new SaveAnnouncementReturn();
		try
		{
			checkLogin(in);
			
			final BAnnouncement x=new BAnnouncement(in.getAnnouncementId());
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
