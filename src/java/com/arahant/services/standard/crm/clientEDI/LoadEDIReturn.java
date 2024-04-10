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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BClientCompany;


/**
 * 
 *
 *
 */
public class LoadEDIReturn extends TransmitReturnBase {

	void setData(BClientCompany bc)
	{
		
		applicationSenderId=bc.getEdiApplicationSenderId();
		applicationReceiverId=bc.getEdiApplicationReceiverId();
		interchangeSenderId=bc.getEdiInterchangeSenderId();
		interchangeReceiverId=bc.getEdiInterchangeReceiverId();
		transferSchemeId=bc.getEdiTransferSchemeId();
		transferHost=bc.getEdiTransferHost();
		transferPort=bc.getEdiTransferPort();
		transferUsername=bc.getEdiTransferUsername();
		transferPassword=bc.getEdiTransferPassword();
		transferDirectory=bc.getEdiTransferDirectory();
		transferEncryptionKey=bc.getEditTransferEncryptionKeyText();
		transferEncryptionKeyIdInHex=bc.getEdiTransferEncrpytionKey();

	}
	
	private String applicationSenderId;
private String applicationReceiverId;
private String interchangeSenderId;
private String interchangeReceiverId;
private String transferSchemeId;
private String transferHost;
private int transferPort;
private String transferUsername;
private String transferPassword;
private String transferDirectory;
private String transferEncryptionKey;
private String transferEncryptionKeyIdInHex;
;

	public String getApplicationSenderId()
	{
		return applicationSenderId;
	}
	public void setApplicationSenderId(String applicationSenderId)
	{
		this.applicationSenderId=applicationSenderId;
	}
	public String getApplicationReceiverId()
	{
		return applicationReceiverId;
	}
	public void setApplicationReceiverId(String applicationReceiverId)
	{
		this.applicationReceiverId=applicationReceiverId;
	}
	public String getInterchangeSenderId()
	{
		return interchangeSenderId;
	}
	public void setInterchangeSenderId(String interchangeSenderId)
	{
		this.interchangeSenderId=interchangeSenderId;
	}
	public String getInterchangeReceiverId()
	{
		return interchangeReceiverId;
	}
	public void setInterchangeReceiverId(String interchangeReceiverId)
	{
		this.interchangeReceiverId=interchangeReceiverId;
	}
	public String getTransferSchemeId()
	{
		return transferSchemeId;
	}
	public void setTransferSchemeId(String transferSchemeId)
	{
		this.transferSchemeId=transferSchemeId;
	}
	public String getTransferHost()
	{
		return transferHost;
	}
	public void setTransferHost(String transferHost)
	{
		this.transferHost=transferHost;
	}
	public int getTransferPort()
	{
		return transferPort;
	}
	public void setTransferPort(int transferPort)
	{
		this.transferPort=transferPort;
	}
	public String getTransferUsername()
	{
		return transferUsername;
	}
	public void setTransferUsername(String transferUsername)
	{
		this.transferUsername=transferUsername;
	}
	public String getTransferPassword()
	{
		return transferPassword;
	}
	public void setTransferPassword(String transferPassword)
	{
		this.transferPassword=transferPassword;
	}
	public String getTransferDirectory()
	{
		return transferDirectory;
	}
	public void setTransferDirectory(String transferDirectory)
	{
		this.transferDirectory=transferDirectory;
	}
	public String getTransferEncryptionKey()
	{
		return transferEncryptionKey;
	}
	public void setTransferEncryptionKey(String transferEncryptionKey)
	{
		this.transferEncryptionKey=transferEncryptionKey;
	}
	public String getTransferEncryptionKeyIdInHex()
	{
		return transferEncryptionKeyIdInHex;
	}
	public void setTransferEncryptionKeyIdInHex(String transferEncryptionKeyIdInHex)
	{
		this.transferEncryptionKeyIdInHex=transferEncryptionKeyIdInHex;
	}

}

	
