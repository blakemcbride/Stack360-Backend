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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrNote;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BPerson;
import com.arahant.business.BPersonNote;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrNoteOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRNoteOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRNoteOps.class);

	public HRNoteOps() {
		super();
	}

	
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("HRNotes"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public DeleteNotesReturn deleteNotes(/*@WebParam(name = "in")*/final DeleteNotesInput in)	{
		final DeleteNotesReturn ret=new DeleteNotesReturn();
	
		try {
			checkLogin(in);
			
			BPersonNote.delete(hsu,in.getIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public GetNotesReportReturn getNotesReport(/*@WebParam(name = "in")*/final GetNotesReportInput in)	{
		final GetNotesReportReturn ret=new GetNotesReportReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setFileName(BPersonNote.getReport(hsu, in.getPersonId(), in.getShowAsDependent()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListPersonNotesReturn listPersonNotes(/*@WebParam(name = "in")*/final ListPersonNotesInput in)	{
		final ListPersonNotesReturn ret=new ListPersonNotesReturn();

		try { 
			checkLogin(in);
			
			ret.setItem(new BPerson(in.getPersonId()).listNotes());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	@WebMethod()
	public LoadNoteReturn loadNote(/*@WebParam(name = "in")*/final LoadNoteInput in)	{
		final LoadNoteReturn ret=new LoadNoteReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPersonNote(in.getId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewNoteReturn newNote(/*@WebParam(name = "in")*/final NewNoteInput in)	{
		final NewNoteReturn ret=new NewNoteReturn();
		
		try
		{
			checkLogin(in);
			final BPersonNote n=new BPersonNote();
			ret.setId(n.create());
			in.setData(n);
			n.insert();
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SaveNoteReturn saveNote(/*@WebParam(name = "in")*/final SaveNoteInput in)	{
		final SaveNoteReturn ret=new SaveNoteReturn();
		
		try
		{
			checkLogin(in);
			final BPersonNote n=new BPersonNote(in.getNoteId());
			in.setData(n);
			n.update();
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}

	/*
HrNote		HumanResourcesManagementService.deleteEmployeeNotesObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.getEmployeeNotesReportObj	*** MISSING																																																																																																																																																																																																																																																												
HumanResourcesManagementService.listEmployeeNotesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
HumanResourcesManagementService.loadEmployeeNoteObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.newEmployeeNoteObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.saveEmployeeNoteObj																																																																																																																																																																																																																																																													
SecurityManagementService.checkRights	*** MISSING AccessHR																																																																																																																																																																																																																																																												
*/
