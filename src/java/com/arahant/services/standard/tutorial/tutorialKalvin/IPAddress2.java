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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.services.standard.tutorial.tutorialKalvin;


import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class IPAddress2 {

	public IPAddress2(){

	}
	public DataObject test(DataObject in){
		System.out.println("Hello World.  Testing encryption. 33");
		return in;
	}
    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ipAddress = addr.getHostAddress();
            String hostName = addr.getHostName();
            String host = "";
            String ip = "";
            if (ipAddress != null && ipAddress.length() > 0) {
                ip = ipAddress;
            }
            if (!ip.equals(hostName)) {
                host = hostName;
            }
            System.out.println("IP " + ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(IPAddress2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

