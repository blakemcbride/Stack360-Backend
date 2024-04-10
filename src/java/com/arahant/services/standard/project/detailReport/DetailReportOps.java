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
package com.arahant.services.standard.project.detailReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exports.ProjectDetailExport;
import com.arahant.fields.ProjectDetailFields;
import com.arahant.reports.ProjectDetailReport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectDetailReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DetailReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			DetailReportOps.class);
	
	public DetailReportOps() {
		super();
	}
	
 	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(NAME = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
		
			ret.setReportUrl(new ProjectDetailReport().build(in.getProjectId(),in.getIds()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in)			{
		final GetExportReturn ret = new GetExportReturn();
		try
		{
			checkLogin(in);
			
			ret.setCsvUrl(new ProjectDetailExport(in.getIds(), in.getProjectId()).build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


    @WebMethod()
	public ListFieldGroupsReturn listFieldGroups(/*@WebParam(NAME = "in")*/final ListFieldGroupsInput in)		
	{
		final ListFieldGroupsReturn ret=new ListFieldGroupsReturn();
		try
		{
			checkLogin(in);

			ListFieldGroupsReturnItem [] r=new ListFieldGroupsReturnItem[ProjectDetailFields.displayGroups.length];
			
			
			for (int loop=0;loop<ProjectDetailFields.displayGroups.length;loop++)
			{
				r[loop]=new ListFieldGroupsReturnItem();
				r[loop].setDescription(ProjectDetailFields.displayGroups[loop][0]);
				
				String []ids=new String[ProjectDetailFields.displayGroups[loop].length-1];
				for (int loop2=1;loop2<ProjectDetailFields.displayGroups[loop].length;loop2++)
					ids[loop2-1]=ProjectDetailFields.displayGroups[loop][loop2];
				
				r[loop].setId(ids);
			}
			
			ret.setItem(r);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListFieldsReturn listFields(/*@WebParam(NAME = "in")*/final ListFieldsInput in)		
	{
		final ListFieldsReturn ret=new ListFieldsReturn();
		try
		{
			checkLogin(in);
			
			
			String []r=new String[ProjectDetailFields.displayIds.length];
			
			List <String> l=new ArrayList<String>(r.length);
			
			Collections.addAll(l, ProjectDetailFields.displayIds);
			
			Collections.sort(l);
			
			r=l.toArray(r);
			
			ListFieldsReturnItem []arr=new ListFieldsReturnItem[r.length];
			
			for (int loop=0;loop<arr.length;loop++)
			{
				arr[loop]=new ListFieldsReturnItem();
				arr[loop].setId(r[loop]);
			}
			
			
			ret.setItem(arr);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
