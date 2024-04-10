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

import com.arahant.beans.ESignature;
import com.arahant.beans.Person;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 */
public class BESignature extends SimpleBusinessObjectBase<ESignature> implements IDBFunctions {
	
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final ArahantLogger logger = new ArahantLogger(BESignature.class);

    public BESignature() {
    }

	/**
     * @param eSignatureId
     * @throws ArahantException
     */
    public BESignature(final String eSignatureId) throws ArahantException {
        load(eSignatureId);
    }

	/**
	 * @param ESignature
	 */
	public BESignature(final ESignature es) {
		bean = es;
	}

	public String getAddressIP() {
		return bean.getAddressIP();
	}

	public void setAddressIP(String addressIP) {
		bean.setAddressIP(addressIP);
	}

	public String getESignatureId() {
		return bean.getESignatureId();
	}

	public void setESignatureId(String eSignatureId) {
		bean.setESignatureId(eSignatureId);
	}

	public String getXmlSum() {
		return bean.getXmlSum();
	}

	public void setXmlSum(String md5Sum) {
		bean.setXmlSum(md5Sum);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	public void setPersonId(String personId) {
		bean.setPersonId(personId);
	}

	public String getFormType() {
		return bean.getFormType();
	}

	public void setFormType(String sigType) {
		bean.setFormType(sigType);
	}

	public Timestamp getTimeSigned() {
		return bean.getTimeSigned();
	}

	public void setTimeSigned(Timestamp timeSigned) {
		bean.setTimeSigned(timeSigned);
	}

	public String getXmlData() {
		return bean.getXmlData();
	}

	public void setXmlData(String xmlData) {
		bean.setXmlData(xmlData);
	}

	public byte [] getFormData() {
		return bean.getFormData();
	}

	public void setFormData(byte [] formData) {
		bean.setFormData(formData);
	}

	public String getFormSum() {
		return bean.getFormSum();
	}

	public void setFormSum(String formSum) {
		bean.setFormSum(formSum);
	}

	public String getSignature() {
		return bean.getSignature();
	}

	public void setSignature(String sig) {
		bean.setSignature(sig);
	}

	public String getSigDate() {
		return bean.getSigDate();
	}

	public void setSigDate(String sigDate) {
		bean.setSigDate(sigDate);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		}
		catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for(String id : ids)
			new BReportTitle(id).delete();
	}

	@Override
	public String create() throws ArahantException {
        bean = new ESignature();
        bean.generateId();
        return getESignatureId();
	}

	@Override
	public final void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ESignature.class, key);
	}
	
	public boolean saveToFile(String fname) {
		OutputStream os = null;
		try {
			byte [] data = bean.getFormData();
			if (data == null)
				return false;
			os = new FileOutputStream(fname);
			os.write(data);
			return true;
		} catch (Exception ex) {
			logger.error(ex);
			return false;
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException ex) {
			}
		}
	}
	
	public static boolean doesSignatureExist(String person_id, String form_type) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<ESignature> crit = hsu.createCriteria(ESignature.class);
		crit.eq(ESignature.PERSON_ID, person_id);
		crit.eq(ESignature.FORM_TYPE, form_type);
		return crit.exists();
	}
	
	public static void store(Person pers, String formType, String pdfFileName, String ip, Date dtSigned, String signature, String sig_date) throws ArahantException {
		BESignature rec = new BESignature();
		rec.create();
		rec.setPerson(pers);
		rec.setTimeSigned(new Timestamp(dtSigned.getTime()));
		rec.setAddressIP(ip);
		rec.setFormType(formType);
		rec.setSignature(signature);
		rec.setSigDate(sig_date);
		InputStream is = null;
		byte [] bytes = null;
		try {
			File file = new File(pdfFileName);
			long len = file.length();
			is = new FileInputStream(file);
			if (len > Integer.MAX_VALUE)
				throw new IOException("File too long");
			bytes = new byte[(int)len];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length  && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
				offset += numRead;
			if (offset < bytes.length)
				throw new IOException("Could not completely read file "+file.getName());
		} catch (IOException ex) {
			throw new ArahantException(ex);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException ex) {
				}
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte [] md5sum = digest.digest(bytes);
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			rec.setFormSum(output);
			rec.setFormData(bytes);
		} catch (NoSuchAlgorithmException ex) {
			throw new ArahantException(ex);
		}
		rec.insert();
	}
	
	
}
