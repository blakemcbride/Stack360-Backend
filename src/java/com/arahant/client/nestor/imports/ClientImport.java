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

package com.arahant.client.nestor.imports;

import com.arahant.beans.CompanyDetail;
import com.arahant.business.BClientCompany;

import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;

import static org.kissweb.StringUtils.take;


/**
 * User: Blake McBride
 * Date: 10/3/16
 */
public class ClientImport {

    private static String getZip(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.length() == 0)
            return "";
        final int n = s.length();
        int i;
        for (i=n-1 ; i >= 0  &&  Character.isDigit(s.charAt(i)) ; i-- ) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)  &&  Character.compare(c, '-') != 0)
                break;
        }
        StringBuilder sb = new StringBuilder();
        for (i++ ; i < n ; i++)
            sb.append(s.charAt(i));
        return sb.toString();
    }

    private static String getCity(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.length() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i=0 ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == ',')
                break;
            sb.append(c);
        }
        return sb.toString();
    }

    private static String getState(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.length() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        int i;
        for (i=0 ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == ',')
                break;
        }
        for (i++ ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c != ' ')
                break;
        }
        for ( ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == ' ')
                break;
            sb.append(c);
        }
        return sb.toString();
    }

    private static String getFName(String s) {
        if (s == null)
            return ".";
        s = s.trim();
        if (s.length() == 0)
            return ".";
        StringBuilder sb = new StringBuilder();
        for (int i=0 ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == ' ')
                break;
            sb.append(c);
        }
        if (sb.length() == 0)
            return ".";
        return sb.toString();
    }

    private static String getLName(String s) {
        int i;
        if (s == null)
            return ".";
        s = s.trim();
        if (s.length() == 0)
            return ".";
        StringBuilder sb = new StringBuilder();
        for (i=0 ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == ' ')
                break;
        }
        for (i++ ; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c != ' ')
                break;
        }
        for ( ; i < s.length() ; i++) {
            char c = s.charAt(i);
            sb.append(c);
        }
        if (sb.length() == 0)
            return ".";
        return sb.toString();
    }

    private static String maxLen(int len, String s) {
        if (s == null  ||  s.length() <= len)
            return s;
        return take(s, len);
    }

    private static void spinRows(DelimitedFileReader df, HibernateSessionUtil hsu) {
        int recnum = 0;  // keep track of number of records we are dealing with for Hibernate flush purposes

        df.readHeader();

        while (true) {
            try {
                if (!df.nextLine())
                    break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            String clientName = df.getString("Client Name");
            System.out.println(clientName);

            hsu.beginTransaction();
            BClientCompany bcc = new BClientCompany();
            bcc.create();

            bcc.setIdentifier(df.getString("Client"));
            bcc.setName(df.getString("Client Name"));
            bcc.setStreet(df.getString("Address Line 1"));
            bcc.setStreet2(df.getString("Address Line 2"));

            String csz = df.getString("City ST ZIP");
            bcc.setCity(getCity((csz)));
            bcc.setState(getState(csz));
            bcc.setZip(getZip(csz));

            String name = df.getString("Bill To Contact");
            bcc.setMainContactFname(getFName(name));
            bcc.setMainContactLname(getLName(name));
            bcc.setMainContactWorkPhone(maxLen(20, df.getString("Telephone 1")));
            bcc.setMainContactMobilePhone(maxLen(20, df.getString("Telephone 2")));
            bcc.setMainContactWorkFax(maxLen(20, df.getString("Fax Number")));
            bcc.setMainContactPersonalEmail(df.getString("Email Address"));


            bcc.insert();

            hsu.commitTransaction();
        }
    }

    public static void main(String [] argv) {
        try {
            HibernateSessionUtil hsu = ArahantSession.getHSU();
            hsu.dontAIIntegrate();
            ArahantSession.multipleCompanySupport = false;
            hsu.setCurrentCompany(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).eq(CompanyDetail.NAME, "Nestor CPA Services, Inc.").first());
            hsu.getCurrentCompany();
            DelimitedFileReader df = new DelimitedFileReader("/home/blake/Arahant/Clients/Nestor/2017 Tickler.csv");
            spinRows(df, hsu);
            df.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
