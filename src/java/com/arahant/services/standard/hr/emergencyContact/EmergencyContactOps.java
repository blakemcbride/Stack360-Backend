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
package com.arahant.services.standard.hr.emergencyContact;
import com.arahant.beans.HrEmergencyContact;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.EmergencyContactReport;
import com.arahant.utils.ArahantSession;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmergencyContactOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmergencyContactOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmergencyContactOps.class);
	
	public EmergencyContactOps() {
		super();
	}
	
    @WebMethod()
	public ListEmergencyContactsReturn listEmergencyContacts(/*@WebParam(name = "in")*/final ListEmergencyContactsInput in)		
	{
		final ListEmergencyContactsReturn ret=new ListEmergencyContactsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHrEmergencyContact.list(new BPerson(in.getEmployeeId()).getPerson()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public LoadEmergencyContactReturn loadEmergencyContact(/*@WebParam(name = "in")*/final LoadEmergencyContactInput in)		
	{
		final LoadEmergencyContactReturn ret=new LoadEmergencyContactReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHrEmergencyContact(in.getContactId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewEmergencyContactReturn newEmergencyContact(/*@WebParam(name = "in")*/final NewEmergencyContactInput in)		
	{
		final NewEmergencyContactReturn ret=new NewEmergencyContactReturn();
		try
		{
			checkLogin(in);
			
			final BHrEmergencyContact x=new BHrEmergencyContact();
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
	public SaveEmergencyContactReturn saveEmergencyContact(/*@WebParam(name = "in")*/final SaveEmergencyContactInput in)		
	{
		final SaveEmergencyContactReturn ret=new SaveEmergencyContactReturn();
		try
		{
			checkLogin(in);
			
			final BHrEmergencyContact x=new BHrEmergencyContact(in.getContactId());
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
    public DeleteEmergencyContactsReturn deleteEmergencyContacts(/*@WebParam(name = "in")*/final DeleteEmergencyContactsInput in)
	{
		final DeleteEmergencyContactsReturn ret=new DeleteEmergencyContactsReturn();
		try
		{
			checkLogin(in);


            BPerson bp = new BPerson(new BHrEmergencyContact(in.getContactIds()[0]).getPerson().getPersonId());
			
			BHrEmergencyContact.delete(in.getContactIds());

            List<HrEmergencyContact> ecl = ArahantSession.getHSU().createCriteria(HrEmergencyContact.class)
                    .eq(HrEmergencyContact.PERSON, bp.getPerson())
                    .notIn(HrEmergencyContact.CONTACT_ID, in.getContactIds())
                    .list();
            
			short count = 0;
            for(HrEmergencyContact ec : ecl)
            {
                BHrEmergencyContact bec = new BHrEmergencyContact(ec);
                bec.setSeqno(count);
                count++;
                bec.update();
            }

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ReorderEmergencyContactsReturn reorderEmergencyContacts(/*@WebParam(name = "in")*/final ReorderEmergencyContactsInput in)		
	{
		final ReorderEmergencyContactsReturn ret=new ReorderEmergencyContactsReturn();
		try
		{
			checkLogin(in);

			int i = 0;
			for (String s : in.getContactIds())
			{
				final BHrEmergencyContact bec = new BHrEmergencyContact(s);
				bec.setSeqno(i + 10000);
				bec.update();
				i++;
				hsu.flush();
			}

			i = 0;
			for (String s : in.getContactIds())
			{
				final BHrEmergencyContact bec = new BHrEmergencyContact(s);
				bec.setSeqno(i);
				bec.update();
				i++;
				hsu.flush();
			}
			
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
			
			ret.setReportUrl(new EmergencyContactReport().build(in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
