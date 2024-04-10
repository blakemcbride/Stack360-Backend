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
package com.arahant.services.standard.inventory.productAttribute;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProductAttributesReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardInventoryProductAttributeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProductAttributeOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            ProductAttributeOps.class);

    public ProductAttributeOps() {
        super();
    }

    @WebMethod()
    public ListAttributesReturn listAttributes(/*@WebParam(name = "in")*/final ListAttributesInput in) {
        final ListAttributesReturn ret = new ListAttributesReturn();
        try {
            checkLogin(in);

            ret.setItem(BProductAttribute.list(in.getName(), in.getActiveType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public MoveAttributeReturn moveAttribute(/*@WebParam(name = "in")*/final MoveAttributeInput in) {
        final MoveAttributeReturn ret = new MoveAttributeReturn();
        try {
            checkLogin(in);

            final BProductAttribute x = new BProductAttribute(in.getId());
            in.setData(x);
            x.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

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
    public DeleteAttributesReturn deleteAttributes(/*@WebParam(name = "in")*/final DeleteAttributesInput in) {
        final DeleteAttributesReturn ret = new DeleteAttributesReturn();
        try {
            checkLogin(in);

            BProductAttribute.delete(in.getIds());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }
    @WebMethod()
	public LoadAttributeReturn loadAttribute(/*@WebParam(name = "in")*/final LoadAttributeInput in)		
	{
		final LoadAttributeReturn ret=new LoadAttributeReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BProductAttributeChoice.list(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewAttributeReturn newAttribute(/*@WebParam(name = "in")*/final NewAttributeInput in)		
	{
		final NewAttributeReturn ret=new NewAttributeReturn();
		try
		{
			checkLogin(in);
			
			final BProductAttribute x=new BProductAttribute();
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
	public SaveAttributeReturn saveAttribute(/*@WebParam(name = "in")*/final SaveAttributeInput in)		
	{
		final SaveAttributeReturn ret=new SaveAttributeReturn();
		try
		{
			checkLogin(in);
			
			final BProductAttribute x=new BProductAttribute(in.getId());
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

			ret.setReportUrl(new ProductAttributesReport().build(BProductAttribute.list(in.getActiveType())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
