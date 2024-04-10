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

package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exports.*;
import com.arahant.groovy.GroovyClass;
import com.arahant.utils.*;
import java.io.File;
import java.util.Date;
import java.util.List;

public final class BEDITransaction extends SimpleBusinessObjectBase<EDITransaction> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BEDITransaction.class);

	private static BEDITransaction[] makeArray(List<EDITransaction> list) {
		BEDITransaction[] ret = new BEDITransaction[list.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BEDITransaction(list.get(loop));

		return ret;
	}

	public BEDITransaction() {
	}

	public BEDITransaction(String ediId) {
		internalLoad(ediId);
	}
	
	/**
	 * Create a new EDITransaction with the specified vendor defaulting in the
	 * ICN, GCN, TSCN, date, and status.
	 * 
	 * @param vend 
	 */
	public BEDITransaction(CompanyBase vend) {
		bean = new EDITransaction();
		setReceiver(vend.getOrgGroupId());
		int icn = getStartingICN();
		bean.setIcn(icn);
		int gcn = getStartingGCN();
		bean.setGcn(gcn);
		int tscn = getStartingTscn();
		bean.setTscn(tscn);
		bean.setTransactionDatetime(new Date());

		setStatus(6);
		setStatusDesc("Started");
		bean.generateId();
	}

	private BEDITransaction(EDITransaction et) {
		bean = et;
	}

	@Override
	public String create() throws ArahantException {
		bean = new EDITransaction();
		setStatus(6);
		setStatusDesc("Started");

		return bean.generateId();
	}

	public String getControlNumber() {
		return bean.getIcn() + "";
	}

	public int getDate() {
		return DateUtils.getDate(bean.getTransactionDatetime());
	}

	public String getDateTime() {
		return DateUtils.getDateFormatted(bean.getTransactionDatetime());
	}

	/**
	 * Return the time (to the millisecond) of the last good EDI transmission.
	 * A good transmission is defined as one with a transaction_status of zero.
	 *
	 * @return the Date object
	 */
	public Date getLastExportDate() {
		EDITransaction t = getLastGoodTransaction();
		if (t == null)
			return new Date(0);
		return t.getTransactionDatetime();
	}

	public String getStatus() {
		return bean.getTransactionStatusDesc();
	}

	public int getTime() {
		return DateUtils.getTime(bean.getTransactionDatetime());
	}

	public String getVendorName() {
		return bean.getCompany().getName();
	}

	public static BEDITransaction[] search(int fromDate, int toDate, String vendorId, int max) {
		HibernateCriteriaUtil<EDITransaction> hcu = ArahantSession.getHSU().createCriteria(EDITransaction.class).orderByDesc(EDITransaction.TRANSACTION_DATE).setMaxResults(max);

		if (fromDate != 0) {
			Date d1 = DateUtils.getDate(fromDate);
			hcu.ge(EDITransaction.TRANSACTION_DATE, d1);
		}

		if (toDate != 0) {
			Date d2 = DateUtils.getDate(toDate);
			hcu.le(EDITransaction.TRANSACTION_DATE, d2);
		}

		if (!isEmpty(vendorId))
			hcu.joinTo(EDITransaction.COMPANY).eq(CompanyBase.ORGGROUPID, vendorId);

		return makeArray(hcu.list());
	}
	
	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(EDITransaction.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public void setStatus(String name, int val) {
		try {
			//	String id=bean.getEdiTransactionId();
			//	hsu.flush();
			//	hsu.evict(bean);
			//	bean=hsu.get(EDITransaction.class, bean.getEdiTransactionId());
			setStatus(val);
			setStatusDesc(name);
			//	update();
			//	hsu.commitTransaction();
			//	hsu.beginTransaction();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void encryptAndSend(String filename) {

		try {
			if (!BProperty.getBoolean("EDIProduction"))
				return;

			CompanyBase co = bean.getCompany();
			//encrypt
			File decryptedFile = new File(filename);
			File encryptedFile = new File(filename + ".pgp");

			boolean failed = false;
			try {
				Crypto.encryptPGP(decryptedFile, encryptedFile, co.getPublicEncryptionKey(), co.getEncryptionKeyId());
				setStatus("File encrypted", 3);
			} catch (Exception e) {
				logger.error(e);
				setStatus("Encryption failed", 4);
				failed = true;
			}

			if (!failed) {
				//ftp
				FTP ftp = new FTP();
				ftp.send(co.getComUrl(), co.getComPassword(), co.getComDirectory(), encryptedFile, true);
				setStatus("File transmitted", 10);
			}
		} catch (Exception e) {
			logger.error(e);
			setStatus("Error: " + e.getMessage().substring(0, (e.getMessage().length() > 33) ? 33 : e.getMessage().length()), 2);
			throw new ArahantException(e);
		}
	}
	
	private void encryptAndSend(String filename, boolean useSftp) {
		String [] fileNames = new String[1];
		fileNames[0] = filename;
		encryptAndSend(fileNames, useSftp);
	}
	
	private void encryptAndSend(String [] filenames, boolean useSftp) {

		try {
			if (!BProperty.getBoolean("EDIProduction"))
				return;

			CompanyBase co = bean.getCompany();
			//encrypt
			File [] decryptedFiles = new File[filenames.length];
			File [] encryptedFiles = new File[filenames.length];
			String [] encryptedFileNames = new String[filenames.length];
			for (int i=0 ; i < filenames.length ; i++) {
				decryptedFiles[i] = new File(filenames[i]);
				encryptedFiles[i] = new File(filenames[i] + ".pgp");
				encryptedFileNames[i] = filenames[i] + ".pgp";
			}
			
			boolean failed = false;
			try {
				for (int i=0 ; i < filenames.length ; i++)
					Crypto.encryptPGP(decryptedFiles[i], encryptedFiles[i], co.getPublicEncryptionKey(), co.getEncryptionKeyId());
				setStatus("File encrypted", 3);
			} catch (Exception e) {
				logger.error(e);
				setStatus("Encryption failed", 4);
				failed = true;
			}

			if (!failed) {
				if (useSftp) {
					SFTP sftp = new SFTP();

					String url = co.getComUrl();

					String username = url.substring(url.lastIndexOf("/") + 1, url.indexOf("@"));
					String host = url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
					int port = Integer.parseInt(url.substring(url.lastIndexOf(":") + 1));

					if (port == 22)
						if (sftp.sendSFTP(host, username, co.getComPassword(), encryptedFileNames, co.getComDirectory()))
							setStatus("Files transmitted", 10);
						else
							setStatus("An error occurred while transmitting.", 10);
					else if (sftp.sendSFTP(host, port, username, co.getComPassword(), encryptedFileNames, co.getComDirectory()))
						setStatus("Files transmitted", 10);
					else
						setStatus("An error occurred while transmitting.", 10);
					
				} else {
					FTP ftp = new FTP();
					for (int i=0 ; i < filenames.length ; i++)
						ftp.send(co.getComUrl(), co.getComPassword(), co.getComDirectory(), encryptedFiles[i], true);
					setStatus("File transmitted", 10);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			setStatus("Error: " + e.getMessage().substring(0, (e.getMessage().length() > 33) ? 33 : e.getMessage().length()), 2);
			throw new ArahantException(e);
		}
	}
	
	private void sendSFTP(String filename) {
		String [] fileNames = new String[1];
		fileNames[0] = filename;
		sendSFTP(fileNames);
	}

	private void sendSFTP(String[] filenames) {
		if (!BProperty.getBoolean("EDIProduction"))
			return;

		CompanyBase co = bean.getCompany();

		SFTP sftp = new SFTP();

		String url = co.getComUrl();

		String username = url.substring(url.lastIndexOf("/") + 1, url.indexOf("@"));
		String host = url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
		int port = Integer.parseInt(url.substring(url.lastIndexOf(":") + 1));

		if (port == 22)
			if (sftp.sendSFTP(host, username, co.getComPassword(), filenames, co.getComDirectory()))
				setStatus("Files transmitted", 10);
			else
				setStatus("An error occurred while transmitting.", 10);
		else if (sftp.sendSFTP(host, port, username, co.getComPassword(), filenames, co.getComDirectory()))
			setStatus("Files transmitted", 10);
		else
			setStatus("An error occurred while transmitting.", 10);
	}
	
	private void sendFTP(String [] filenames) {
		try {
			if (!BProperty.getBoolean("EDIProduction"))
				return;

			CompanyBase co = bean.getCompany();
			FTP ftp = new FTP();
			for (int i=0 ; i < filenames.length ; i++)
				ftp.send(co.getComUrl(), co.getComPassword(), co.getComDirectory(), new File(filenames[i]), true);
			setStatus("File transmitted", 10);
		} catch (Exception e) {
			logger.error(e);
			setStatus("Error: " + e.getMessage().substring(0, (e.getMessage().length() > 33) ? 33 : e.getMessage().length()), 2);
			throw new ArahantException(e);
		}
	}

	public void sendExport() {
		if (bean.getCompany() != null) {
			BVendorCompany bv = new BVendorCompany(bean.getCompany().getOrgGroupId());
			sendExport(false, bv.getEdiFileType(), bv.getEdiFileStatus());
		}
	}
	
	private boolean isEncrypted() 
	{
		CompanyBase comp = bean.getCompany();
		String keyId = comp.getEncryptionKeyId();
		String key = comp.getPublicEncryptionKey();
		return keyId != null  &&  key != null  &&  keyId.length() > 0  &&  key.length() > 0;
	}

	/**
	 * Sends an EDI (EDI384, CSV, XML, etc. export.
	 * 
	 * @param overrideActivated
	 * @param fileType Unknown / Full file / Change file
	 * @param fileStatus Unknown / Production / Test
	 */
	public void sendExport(boolean overrideActivated, String fileType, String fileStatus) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			Date lastRun = getLastExportDate();
			boolean transmit = true;
			boolean useSFTP = false;

			if (!isEmpty(bean.getCompany().getComUrl())) {
				if (bean.getCompany().getComUrl().charAt(0) == 's')
					useSFTP = true;
			} else
				throw new ArahantException("EDI information must be filled out.");

			String[] filenames = new String[0];

			//Check which interface will be run for this company
			BVendorCompany bv = new BVendorCompany(bean.getCompany().getOrgGroupId());
			short interfaceId = bv.getInterfaceId();
			hsu.setCurrentCompany(bv.getAssociatedCompany());

			if (bv.getEdiActivated().equals("Y") || overrideActivated) {
				if (overrideActivated)
					logger.info("Manually sending EDI for " + bv.getName());
				if (interfaceId == VendorCompany.BCBS) {
					CompanyDetail cd = bv.getAssociatedCompany();
					if (cd.getOrgGroupId().equals("00001-0000073334") && BProperty.getBoolean("IsDRC")) {
						EDI834BCBSImagine e = new EDI834BCBSImagine(fileType);
						filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
					} else {
						EDI834BCBS e = new EDI834BCBS(fileType);
						filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
					}
				} else if (interfaceId == VendorCompany.BCN) {
					EDI834BCN e = new EDI834BCN(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.HUMANA) {
					EDI834Humana e = new EDI834Humana(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.EBC) {
					EDI834EBC e = new EDI834EBC(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.CBG) {
					EDI834CBG e = new EDI834CBG(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.EHIM) {
					EDI834EHIM e = new EDI834EHIM(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.DELTA_DENTAL)
					if (BProperty.getBoolean("WmCoEDI")) {
						EDI834DeltaWmCo e = new EDI834DeltaWmCo(fileType);
						filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
					} else {
						EDI834DeltaDental e = new EDI834DeltaDental(fileType);
						filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
					}
				else if (interfaceId == VendorCompany.VSP) {
					EDI834VSP e = new EDI834VSP(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T")); //true is debug
				} else if (interfaceId == VendorCompany.MUTUAL) {
					EDI834Mutual e = new EDI834Mutual(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.EYEMED) {
					EDI834EyeMed e = new EDI834EyeMed();
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.AMERITAS) {
					EDI834Ameritas e = new EDI834Ameritas(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.CONSOCIATES) {
					if (BProperty.getBoolean("WmCoEDI")) {
						ExportPCPs ex = new ExportPCPs();
						encryptAndSend(ex.export(lastRun));
						FlexExport flexport = new FlexExport();
						encryptAndSend(flexport.export(lastRun));
					}

					EDI834Consociates e = new EDI834Consociates(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));

					if (BProperty.getBoolean("WmCoEDI"))
						new AuditMedicalBenefits().doAuditHealthCurrent();
				} else if (interfaceId == VendorCompany.CAREMARK) {
					//CaremarkRECAP160Export recap = new CaremarkRECAP160Export(fileType);
					//filenames = recap.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.CIGNA) {
					EDI834CIGNA e = new EDI834CIGNA(fileType);
					filenames = e.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.AETNA) {
					EDI834Aetna recap = new EDI834Aetna(fileType);
					filenames = recap.dumpEDI(bean.getCompany(), this, fileStatus.equals("T"));
				} else if (interfaceId == VendorCompany.NORTH_AMERICA_ADMINISTRATORS) {
					defaultEDICodes();
					if (FileSystemUtils.isUnderIDE())
						GroovyClass.reset();  //  in debug environment - make sure to reload all files
					GroovyClass gc = new GroovyClass(true, "com/arahant/groovy/exports/NorthAmericaAdministratorsExport.groovy");
					Object instance = gc.invokeConstructor();
					filenames = new String[1];
					filenames[0] = (String) gc.invoke("export", instance);
					filenames[0] = FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/" + filenames[0];
				} else if (interfaceId == VendorCompany.MADISON_DENTAL) {
					//VendorCompany vc = bv.getBean();
					//MadisonDentalExport mde = (new MadisonDentalExport());
					//mde.build(fileType.charAt(0), this, bv);
					//mde.transmit(vc);
					transmit = false;  // Already done in this block
				} else
					throw new ArahantException("Interface not set up for this vendor OR EDI not currently activated.  (Should be setup in BEDITransaction.)");

				if (transmit) {
					setStatus("File created", 1);
					
					if (isEncrypted())
						encryptAndSend(filenames, useSFTP);
					else if (useSFTP)
						sendSFTP(filenames);
					else
						sendFTP(filenames);
						
					String override = (overrideActivated ? " (Manual)" : "");
					String type = (fileType.equals("F") ? " (F File)" : " (C File)");
					String status = (fileStatus.equals("T") ? " (T)" : " (P)");
					setStatus("Files Processed" + type + status + override, 0);
				}
			}

		} catch (Throwable e) {
			logger.error(e);
			setStatus("Error: " + e.getMessage().substring(0, (e.getMessage().length() > 37) ? 37 : e.getMessage().length()), 2);
			bean.setGcn(getStartingGCN());
			bean.setIcn(getStartingICN());
			bean.setTscn(getStartingTscn());
		} finally {
			insert();
			hsu.commitTransaction();
			hsu.beginTransaction();
		}
	}

	public void setGCN(int gcn) {
		bean.setGcn(gcn);
	}

	public void setICN(int icn) {
		bean.setIcn(icn);
	}

	public void setReceiver(String vendorId) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyBase.class, vendorId));
	}

	public void setStatus(int i) {
		bean.setTransactionStatus(i);
	}

	public void setStatusDesc(String string) {
		bean.setTransactionStatusDesc(string);
	}

	@Override
	public void insert() {
		bean.setTransactionDatetime(new Date());
		super.insert();
	}

	public void setTransactionSetNumber(int i) {
		bean.setTscn(i);
	}

	/**
	 * Gets the last good EDI transaction. This is defined as having a
	 * transaction_status of zero.
	 *
	 * @return
	 */
	private EDITransaction getLastGoodTransaction() {
		return ArahantSession.getHSU().createCriteria(EDITransaction.class).eq(EDITransaction.COMPANY, bean.getCompany()).eq(EDITransaction.STATUS, 0).orderByDesc(EDITransaction.TRANSACTION_DATE).first();
	}

	private EDITransaction getLastTransaction() {
		return ArahantSession.getHSU().createCriteria(EDITransaction.class).eq(EDITransaction.COMPANY, bean.getCompany()).orderByDesc(EDITransaction.TRANSACTION_DATE).first();
	}

	public int getStartingICN() {
		EDITransaction tr = getLastTransaction();
		if (tr == null)
			return 1;

		return tr.getIcn() + 1;
	}

	public int getStartingGCN() {
		EDITransaction tr = getLastTransaction();
		if (tr == null)
			return 1;

		return tr.getGcn() + 1;
	}

	public int getStartingTscn() {
		EDITransaction tr = getLastTransaction();
		if (tr == null)
			return 1;

		return tr.getTscn() + 1;
	}
	
	/**
	 * This defaults the EDI codes.  It is useful when we are dealing with 
	 * CSV or XML exports rather than true EDI.
	 * 
	 * @return 
	 */
	public BEDITransaction defaultEDICodes() {
		int icn = getStartingICN();
		bean.setIcn(icn);
		int gcn = getStartingGCN();
		bean.setGcn(gcn);
		int tscn = getStartingTscn();
		bean.setTscn(tscn);
		return this;
	}

	public static void main(String[] args) {
		CompanyBase co = ArahantSession.getHSU().get(CompanyBase.class, "00001-0000072633");

		SFTP sftp = new SFTP();

		String[] filenames = {/*
		 * "/home/xichen/Desktop/EDIs/EDI_1_20110826_1.edi.txt"
		 */};  //Filepth here

		String url = co.getComUrl();

		String username = url.substring(url.lastIndexOf("/") + 1, url.indexOf("@"));
		String host = url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
		int port = Integer.parseInt(url.substring(url.lastIndexOf(":") + 1));

		if (port == 22)
			if (sftp.sendSFTP(host, username, co.getComPassword(), filenames, co.getComDirectory()))
				System.out.println("SUCCESS!");
			else
				System.out.println("FAIL!");
		else if (sftp.sendSFTP(host, port, username, co.getComPassword(), filenames, co.getComDirectory()))
			System.out.println("SUCCESS!");
		else
			System.out.println("FAIL!");
	}
}
