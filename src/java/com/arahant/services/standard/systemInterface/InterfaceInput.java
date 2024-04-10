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


package com.arahant.services.standard.systemInterface;

import com.arahant.business.BProperty;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantLogger;

/**
 *
 * @author Blake McBride
 */
public class InterfaceInput extends TransmitInputBase {
	
	private static final transient ArahantLogger logger = new ArahantLogger(InterfaceInput.class);

	private static final String FivePointsCode = "Five Points TEST 3322";
	private static final String PRISMCode = "PRISM TEST CODE 1234";
	private int vendorCode;
	private String authCode;
	private int wsMethodVersion;
	
	/* package */ boolean SSOLogging = BProperty.getBoolean("SSOLogging");

	/**
	 * This method determines if communications between the Arahant server and 
	 * the remote server are permitted.
	 * <p>
	 * The remote server passes in:
	 *    vendorCode
	 *    authCode
	 *    (and we know their IP address)
	 * <p>
	 * In order for the web services to accept communications the following
	 * must pass:
	 * <p>
	 * 1.  The system property "SSOIP" must be set to the IP of the remote
	 * server wishing to initiate auto-login (not the users browser).  If
	 * SSOIP is set to "AcceptAll" any IP will be accepted.  If the property
	 * is not set at all, no communications will be allowed.  SSOIP can also
	 * contain a list of IP's to accept separated by commas.
	 * <p>
	 * 2.  One of the following must pass:
	 * <p>
	 * a.  If system properties "SSOKEY" and "SSOVENDOR" are set then they
	 * must match the authCode and vendorCode passed respectively into the
	 * web service.
	 * <p>
	 * b.  Otherwise, the passed in vendorCode and authCode must match values
	 * hard-coded into the system.
	 * 
	 * @param remoteIP
	 * @return true if communications is allowed, false otherwise
	 */
	public boolean isValid(String remoteIP) {
		if ("127.0.0.1".equals(remoteIP))
			return true;
		boolean remoteIPMatch = false;
		String ssoIpProp = BProperty.get("SSOIP");
		String ssoKey = BProperty.get("SSOKEY");
		String ssoVendor = BProperty.get("SSOVENDOR");
		if (isEmpty(ssoIpProp)) {
			if (SSOLogging)
				System.out.println("System interface is failing because the SSOIP system property is not set.");
			return false;
		}
		if (ssoIpProp.equals("AcceptAll")  ||  "127.0.0.1".equals(remoteIP))
			remoteIPMatch = true;
		else if (!isEmpty(remoteIP))
			for (String s : ssoIpProp.split(","))
				if (remoteIP.equals(s.trim()))
					remoteIPMatch = true;
		if (!remoteIPMatch) {
			if (SSOLogging) {
				System.out.println("System interface is failing because the remote IP doesn't match any of the acceptable IP's.");
				System.out.println("Acceptable incoming IP's (SSOIP property):  " + ssoIpProp);
				System.out.println("Actual incoming IP is " + remoteIP);
			}
			return false;
		}
		boolean res;
		if (!isEmpty(ssoKey) && !isEmpty(ssoVendor)) {
			int vendid;
			try {
				vendid = Integer.parseInt(ssoVendor);
			} catch (Throwable t) {
				if (SSOLogging)
					System.out.println("SSO failing because the SSOVENDOR system property is not numeric.");
				return false;
			}
			res = vendid == vendorCode && ssoKey.equals(authCode);
			if (SSOLogging)
				if (res)
					System.out.println("SSO commnications is being accepted from IP " + remoteIP + ", vendor " + vendorCode);
				else
					if (vendid != vendorCode)
						System.out.println("SSO interface is failing because invalid vendor code (accepiable=" + ssoVendor + ", received=" + vendorCode + ")");
					else if (ssoKey.equals(authCode))
						System.out.println("SSO interface is failing because of an invalid vendor authorization code of " + authCode);
			return res;
		}
		// the following code only gets used if SSOKEY or SSOVENDOR are not set
		switch (vendorCode) {
			case 1:  //  FivePoints
				res = FivePointsCode.equals(authCode);
				break;
			case 2:  //  PRISM
				res = PRISMCode.equals(authCode);
				break;
			default:
				if (SSOLogging)
					System.out.println("System interface is failing because of an invalid vendor code of " + vendorCode);
				return false;
		}
		if (SSOLogging)
			if (res)
				System.out.println("SSO commnications is being accepted from IP " + remoteIP + ", vendor " + vendorCode);
			else
				System.out.println("SSO interface is failing because vendor " + vendorCode + " has an invalid vendor authorization code of " + authCode);
		return res;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public int getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(int vendorCode) {
		this.vendorCode = vendorCode;
	}

	public int getWsMethodVersion() {
		return wsMethodVersion;
	}

	public void setWsMethodVersion(int wsMethodVersion) {
		this.wsMethodVersion = wsMethodVersion;
	}
}
