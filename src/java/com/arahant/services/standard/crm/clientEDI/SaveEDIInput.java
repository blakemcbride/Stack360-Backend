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
 *
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.crm.clientEDI;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BClientCompany;


public class SaveEDIInput extends TransmitInputBase {

	@Validation(min = 1, max = 16, required = true)
	private String id;
	@Validation(column = "application_sender_id", table = "company_base", required = false)
	private String applicationSenderId;
	@Validation(column = "application_receiver_id", table = "company_base", required = false)
	private String applicationReceiverId;
	@Validation(column = "interchange_sender_id", table = "company_base", required = false)
	private String interchangeSenderId;
	@Validation(column = "interchange_receiver_id", table = "company_base", required = false)
	private String interchangeReceiverId;
	@Validation(required = false)
	private String transferSchemeId;
	@Validation(required = false)
	private String transferHost;
	@Validation(required = false)
	private int transferPort;
	@Validation(column = "com_login", table = "company_base", required = false)
	private String transferUsername;
	@Validation(column = "com_password", table = "company_base", required = false)
	private String transferPassword;
	@Validation(column = "com_directory", table = "company_base", required = false)
	private String transferDirectory;
	@Validation(column = "public_encryption_key", table = "company_base", required = false)
	private String transferEncryptionKey;
	@Validation(column = "encryption_key_id", table = "company_base", required = false)
	private String transferEncryptionKeyIdInHex;

	void setData(BClientCompany bc) {
		bc.setEdiApplicationSenderId(applicationSenderId);
		bc.setEdiApplicationReceiverId(applicationReceiverId);
		bc.setEdiInterchangeSenderId(interchangeSenderId);
		bc.setEdiInterchangeReceiverId(interchangeReceiverId);
		bc.setEdiTransferURL(transferSchemeId, transferHost, transferPort, transferUsername);
		bc.setEdiTransferPassword(transferPassword);
		bc.setEdiTransferDirectory(transferDirectory);
		bc.setEdiTransferEncryptionKey(transferEncryptionKeyIdInHex);
		bc.setEdiTransferEncryptionKeyText(transferEncryptionKey);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationSenderId() {
		return applicationSenderId;
	}

	public void setApplicationSenderId(String applicationSenderId) {
		this.applicationSenderId = applicationSenderId;
	}

	public String getApplicationReceiverId() {
		return applicationReceiverId;
	}

	public void setApplicationReceiverId(String applicationReceiverId) {
		this.applicationReceiverId = applicationReceiverId;
	}

	public String getInterchangeSenderId() {
		return interchangeSenderId;
	}

	public void setInterchangeSenderId(String interchangeSenderId) {
		this.interchangeSenderId = interchangeSenderId;
	}

	public String getInterchangeReceiverId() {
		return interchangeReceiverId;
	}

	public void setInterchangeReceiverId(String interchangeReceiverId) {
		this.interchangeReceiverId = interchangeReceiverId;
	}

	public String getTransferSchemeId() {
		return transferSchemeId;
	}

	public void setTransferSchemeId(String transferSchemeId) {
		this.transferSchemeId = transferSchemeId;
	}

	public String getTransferHost() {
		return transferHost;
	}

	public void setTransferHost(String transferHost) {
		this.transferHost = transferHost;
	}

	public int getTransferPort() {
		return transferPort;
	}

	public void setTransferPort(int transferPort) {
		this.transferPort = transferPort;
	}

	public String getTransferUsername() {
		return transferUsername;
	}

	public void setTransferUsername(String transferUsername) {
		this.transferUsername = transferUsername;
	}

	public String getTransferPassword() {
		return transferPassword;
	}

	public void setTransferPassword(String transferPassword) {
		this.transferPassword = transferPassword;
	}

	public String getTransferDirectory() {
		return transferDirectory;
	}

	public void setTransferDirectory(String transferDirectory) {
		this.transferDirectory = transferDirectory;
	}

	public String getTransferEncryptionKey() {
		return transferEncryptionKey;
	}

	public void setTransferEncryptionKey(String transferEncryptionKey) {
		this.transferEncryptionKey = transferEncryptionKey;
	}

	public String getTransferEncryptionKeyIdInHex() {
		return transferEncryptionKeyIdInHex;
	}

	public void setTransferEncryptionKeyIdInHex(String transferEncryptionKeyIdInHex) {
		this.transferEncryptionKeyIdInHex = transferEncryptionKeyIdInHex;
	}
}

	
