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
package com.arahant.services.standard.crm.clientEDI;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.Crypto;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmClientEDIOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ClientEDIOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ClientEDIOps.class);
	
	public ClientEDIOps() {
		super();
	}
	
        @WebMethod()
	public ListEDICommunicationSchemesReturn listEDICommunicationSchemes(/*@WebParam(name = "in")*/final ListEDICommunicationSchemesInput in)		
	{
		final ListEDICommunicationSchemesReturn ret=new ListEDICommunicationSchemesReturn();
		try
		{
			checkLogin(in);

			ListEDICommunicationSchemesReturnItem[] item=new ListEDICommunicationSchemesReturnItem[1];
			item[0]=new ListEDICommunicationSchemesReturnItem();
			item[0].setDefaultPort(21);
			item[0].setId("ftp");
			item[0].setDescription("ftp");
			ret.setItem(item);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public LoadEDIReturn loadEDI(/*@WebParam(name = "in")*/final LoadEDIInput in)		
	{
		final LoadEDIReturn ret=new LoadEDIReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BClientCompany(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SaveEDIReturn saveEDI(/*@WebParam(name = "in")*/final SaveEDIInput in)		
	{
		final SaveEDIReturn ret=new SaveEDIReturn();
		try {
			checkLogin(in);
			
			String key = in.getTransferEncryptionKey();
			String id = in.getTransferEncryptionKeyIdInHex();
			if (!key.isEmpty() && !id.isEmpty()) {
				short result = Crypto.verifyPGPPublicKeyText(key, id);

				if (result == 1)
					throw new ArahantException("The Public Key Text is invalid.");
				else if (result == 2)
					throw new ArahantException("The Public Key ID could not be found.");
				else if (result == 3)
					throw new ArahantException("The Public Key ID was found but the key is not an encryption key.");
				else if (result != 0)
					throw new ArahantException("There was an unexpected error processing the Public Key data.");
			}

			final BClientCompany x = new BClientCompany(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRM"));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
