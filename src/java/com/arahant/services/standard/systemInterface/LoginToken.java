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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
import java.util.*;

/**
 *
 * @author Blake McBride
 */
public class LoginToken {
	
	private static final transient ArahantLogger logger = new ArahantLogger(LoginToken.class);

	private final static List<Token> list = Collections.synchronizedList(new LinkedList<Token>());
	private final static long maxMinutes = 5L;

	static String makeSSOGuid(String userGuid, String ip) {
		boolean SSOLogging = BProperty.getBoolean("SSOLogging");
		boolean forceNewToken = BProperty.getBoolean("FivePointsOperation", false);
		long currentDate = (new Date()).getTime();
		if (SSOLogging) {
			System.out.println("SSO: Begin makeSSOGuid " + DateUtils.getDateAndTimeFormatted(new Date()));
			System.out.println("SSO: UserGUID = " + userGuid);
			System.out.println("SSO: IP = " + ip);
		}
		synchronized (list) {
					if (!forceNewToken) {
				for (ListIterator li = list.listIterator(); li.hasNext();) {
					Token elm = (Token) li.next();
					if (currentDate - elm.getWhenCreated() > (maxMinutes * 60000L))
						li.remove();
					else if (elm.isUser(userGuid, ip))
						if (SSOLogging)
							System.out.println("SSO: SSOGuid = " + elm.getSSOGuid());
					return elm.getSSOGuid();
				}
			}
			Token tkn = new Token(userGuid, ip);
			list.add(tkn);
			if (SSOLogging)
				System.out.println("SSO: new SSOGuid = " + tkn.getSSOGuid());
			return tkn.getSSOGuid();
		}
	}

	public static String getUserGuid(String ssoGuid, String ip) {
		boolean SSOLogging = BProperty.getBoolean("SSOLogging");
		long currentDate = (new Date()).getTime();
		if (SSOLogging) {
			System.out.println("SSO: Begin getUserGuid " + DateUtils.getDateAndTimeFormatted(new Date()));
			System.out.println("SSO: ssoGUID = " + ssoGuid);
			System.out.println("SSO: IP = " + ip);
		}
		synchronized (list) {
			for (ListIterator li = list.listIterator(); li.hasNext();) {
				Token elm = (Token) li.next();
				if (currentDate - elm.getWhenCreated() > (maxMinutes * 60000L))
					li.remove();
				else if (elm.isValidSSO(ssoGuid, ip)) {
					String userGuid = elm.getUserGuid();
					li.remove();  //  only works once
					if (SSOLogging)
						System.out.println("SSO: SSO attempt successful! " + userGuid);
					return userGuid;
				}
			}
			if (SSOLogging)
				System.out.println("SSO: SSO attempt failed");
			return null;
		}
	}

	private static class Token {

		private String ssoGuid;
		private String IPAddress;
		private long whenCreated;
		private String userGuid;

		Token(String userGuid, String ip) {
			this.userGuid = userGuid;
			IPAddress = ip;
			ssoGuid = UUID.randomUUID().toString();
			ssoGuid = ssoGuid.replace("-", "").toUpperCase();
			whenCreated = (new Date()).getTime();
		}

		boolean isUser(String userGuid, String ip) {
			return this.userGuid.equals(userGuid) && (StringUtils.isEmpty(IPAddress) || this.IPAddress.equals(ip));
		}

		boolean isValidSSO(String loginGuid, String ip) {
			return this.ssoGuid.equals(loginGuid) && (StringUtils.isEmpty(IPAddress) || this.IPAddress.equals(ip));
		}

		String getSSOGuid() {
			return ssoGuid;
		}

		String getUserGuid() {
			return userGuid;
		}

		String getIPAddress() {
			return IPAddress;
		}

		long getWhenCreated() {
			return whenCreated;
		}
	}
}
