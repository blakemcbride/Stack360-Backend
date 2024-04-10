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

package com.arahant.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 *
 */
public class ActiveDirectoryTester {


  private String domain;
  private String ldapHost;
  private String searchBase;


  public static void main(String args[])
  {
	  Map data=new ActiveDirectoryTester().authenticate("administrator", "arahant");

	  for (Object x : data.keySet())
	  {
		  System.out.println(x+" "+data.get(x));
	  }
  }


  public ActiveDirectoryTester()
  {
    this.domain = "arahant.test";
    this.ldapHost = "ldap://192.168.168.153";
    this.searchBase = "dc=arahant,dc=test";
  }

  public ActiveDirectoryTester(String domain, String host, String dn)
  {
    this.domain = domain;
    this.ldapHost = host;
    this.searchBase = dn;
  }

  public Map authenticate(String user, String pass)
  {
    String returnedAtts[] ={ "cn", "name", "description" }; //{ "sn", "givenName", "mail" };
    String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";

    //Create the search controls
    SearchControls searchCtls = new SearchControls();
    searchCtls.setReturningAttributes(returnedAtts);

    //Specify the search scope
    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, ldapHost);
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
    env.put(Context.SECURITY_CREDENTIALS, pass);

    LdapContext ctxGC;

    try {
      ctxGC = new InitialLdapContext(env, null);
      //Search objects in GC using filters
      NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
      while (answer.hasMoreElements()) {
        SearchResult sr = (SearchResult) answer.next();
        Attributes attrs = sr.getAttributes();
        Map amap = null;
        if (attrs != null) {
          amap = new HashMap();
          NamingEnumeration ne = attrs.getAll();
          while (ne.hasMore()) {
            Attribute attr = (Attribute) ne.next();
            amap.put(attr.getID(), attr.get());
          }
          ne.close();
        }
        return amap;
      }
    }
    catch (NamingException ex) {
      ex.printStackTrace();
    }

    return null;
  }

}
