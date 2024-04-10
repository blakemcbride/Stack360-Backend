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
package com.arahant.utils.generators.wsconfiggen;

import org.kissweb.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generate the web.xml file.
 * <br><br>
 * This program generates two versions - one for development/testing.  And the other for production systems.
 * The development/testing one enables CORS (Cross-Origin Resource Sharing) which allows a web page to access
 * a web service on a different domain.  The production one does not allow CORS and is therefore secure.
 * <br><br>
 * Allowing CORS on a development system is needed so that tablets and mobile devices can be tested on a development system.
 * This is significantly insecure for a production system.
 * <br><br>
 * Other parts of the system are configured to use the correct web.xml file in the correct scenarios so that there
 * should be no reason to manually change anything.
 * <br><br>
 *
 */
public class WsConfigGen {

    /**
     * The following is a list of servlets that are to be exposed to the outside world.
     * <br><br>
     * As an alternative to adding new servlets to <code>servletNames</code> the <code>@WebServlet(urlPatterns="/myservice")</code>
     * may be used in the servlet class.
     */
    private static final String[] servletNames = {"ContentServlet", "ClientStubGeneratorServlet",
            "FileUploadServlet", "ApplicantEntryServlet", "CheckLoginServlet", "AutoLoginServlet",
            "ResetPasswordServlet", "OpenPositionServlet", "W4PDFFormServlet", "KPaulPDFFormServlet",
            "FreeSystemSignUpServlet", "ExcelDashboard", "JavaScriptInterface", "REST", "SMSTwilio"

    };

    private static String maps = "";
    private static String servlets = "";
    private static String endpoints = "";


    public static void main(String[] args) {
        BufferedWriter webProd = null;  // web.xml.production
        BufferedWriter webDev = null;   // web.xml.development
        BufferedWriter jaxws = null;

        try {
            webProd = new BufferedWriter(new FileWriter("web/WEB-INF/web.xml.production"));
            webDev = new BufferedWriter(new FileWriter("web/WEB-INF/web.xml.development"));
            jaxws = new BufferedWriter(new FileWriter("web/WEB-INF/sun-jaxws.xml"));
            writeBoth(webProd, webDev, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writeBoth(webProd, webDev, "<web-app version=\"3.0\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee  http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" metadata-complete=\"false\">\n");
            writeBoth(webProd, webDev, "\t<absolute-ordering />\n");
            writeBoth(webProd, webDev, "\t<listener>\n");
            writeBoth(webProd, webDev, "\t\t<listener-class>com.sun.xml.ws.transport.http.servlet.WSServletContextListener</listener-class>\n");
            writeBoth(webProd, webDev, "\t</listener>\n");
            writeBoth(webProd, webDev, "\t<listener>\n");
            writeBoth(webProd, webDev, "\t\t<listener-class>com.arahant.utils.StartupListener</listener-class>\n");
            writeBoth(webProd, webDev, "\t</listener>\n");

            writeBoth(webProd, webDev, "<!--  The following can also be turned on and off by changing between \"CONFIDENTIAL\" and \"NONE\".  -->");

            writeBoth(webProd, webDev, "\n\t<security-constraint>\n");
            writeBoth(webProd, webDev, "\t\t<web-resource-collection>\n");
            writeBoth(webProd, webDev, "\t\t\t<web-resource-name>securedapp</web-resource-name>\n");
            writeBoth(webProd, webDev, "\t\t\t<url-pattern>/*</url-pattern>\n");
            writeBoth(webProd, webDev, "\t\t</web-resource-collection>\n");
            writeBoth(webProd, webDev, "\t\t<user-data-constraint>\n");

            webProd.write("\t\t<transport-guarantee>CONFIDENTIAL</transport-guarantee>\n");
            webDev.write( "\t\t<transport-guarantee>NONE</transport-guarantee>\n");

            writeBoth(webProd, webDev, "\t\t</user-data-constraint>\n");
            writeBoth(webProd, webDev, "\t</security-constraint>\n");
            
            writeBoth(webProd, webDev, "\t<filter>\n");

            writeBoth(webProd, webDev, "\t\t<filter-name>CorsFilter</filter-name>\n");

            writeBoth(webProd, webDev, "\t\t<filter-class>org.apache.catalina.filters.CorsFilter</filter-class>\n");

            writeBoth(webProd, webDev, "\t\t<init-param>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-name>cors.allowed.headers</param-name>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-value>");
            writeBoth(webProd, webDev, "Content-Type,"
                    + "X-Requested-With,"
                    + "Accept,"
                    + "Accept-Encoding,"
                    + "Accept-Language,"
                    + "Cache-Control,"
                    + "Connection,"
                    + "Host,"
                    + "Pragma,"
                    + "Origin,"
                    + "Referer,"
                    + "User-Agent,"
                    + "Access-Control-Request-Method,"
                    + "Access-Control-Request-Headers");
            writeBoth(webProd, webDev, "</param-value>\n");
            writeBoth(webProd, webDev, "\t\t</init-param>\n");

            writeBoth(webProd, webDev, "\t\t<init-param>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-name>cors.exposed.headers</param-name>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-value>");
            writeBoth(webProd, webDev, "Access-Control-Allow-Origin," +
                    "Content-Length," +
                    "Content-Type," +
                    "Date," +
                    "Server," +
                    "Access-Control-Allow-Credentials");
            writeBoth(webProd, webDev, "</param-value>\n");
            writeBoth(webProd, webDev, "\t\t</init-param>\n");

            writeBoth(webProd, webDev, "\t\t<init-param>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-name>cors.allowed.origins</param-name>\n");
            webDev.write("\t\t\t<param-value>*</param-value>\n");  // allow all CORS - for testing/dev use only - DO NOT RELEASE WITH THIS!
            webProd.write("\t\t\t<param-value>http://localhost:63342</param-value>\n");  // only intellij
            writeBoth(webProd, webDev, "\t\t</init-param>\n");

            writeBoth(webProd, webDev, "\t\t<init-param>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-name>cors.allowed.methods</param-name>\n");
            writeBoth(webProd, webDev, "\t\t\t<param-value>GET, POST, HEAD, OPTIONS</param-value>\n");
            writeBoth(webProd, webDev, "\t\t</init-param>\n");

            writeBoth(webProd, webDev, "\t</filter>\n");
            writeBoth(webProd, webDev, "\t<filter-mapping>\n");
            writeBoth(webProd, webDev, "\t\t<filter-name>CorsFilter</filter-name>\n");
            writeBoth(webProd, webDev, "\t\t<url-pattern>/*</url-pattern>\n");
            writeBoth(webProd, webDev, "\t</filter-mapping>\n");

            // security header filter
            webProd.write("\t<filter>\n");
            webProd.write("\t\t<filter-name>SecurityHeaderFilter</filter-name>\n");
            webProd.write("\t\t<filter-class>com.arahant.services.utils.SecurityHeaderFilter</filter-class>\n");
            webProd.write("\t</filter>\n");
            webProd.write("\t<filter-mapping>\n");
            webProd.write("\t\t<filter-name>SecurityHeaderFilter</filter-name>\n");
            webProd.write("\t\t<url-pattern>/*</url-pattern>\n");
            webProd.write("\t</filter-mapping>\n");

            jaxws.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            jaxws.write("<endpoints version=\"2.0\" xmlns=\"http://java.sun.com/xml/ns/jax-ws/ri/runtime\">\n");

            File base = new File("src/java/com/arahant/services");

            recurseDirs(base);

            writeBoth(webProd, webDev, servlets);

            for (String name : servletNames)
                writeBoth(webProd, webDev, "\t<servlet>\n"
                        + "\t\t<servlet-name>" + name + "</servlet-name>\n"
                        + "\t\t<servlet-class>com.arahant.servlets." + name + "</servlet-class>\n"
                        + "\t\t<load-on-startup>1</load-on-startup>\n"
                        + "\t</servlet>\n");

            writeBoth(webProd, webDev, maps);

            for (String name : servletNames)
                writeBoth(webProd, webDev, "\t<servlet-mapping>\n"
                        + "\t\t<servlet-name>" + name + "</servlet-name>\n"
                        + "\t\t<url-pattern>/" + name + "</url-pattern>\n"
                        + "\t</servlet-mapping>\n");

            jaxws.write(endpoints);

            writeBoth(webProd, webDev, "\t<session-config>\n");
            writeBoth(webProd, webDev, "\t\t<session-timeout>30</session-timeout>\n");
            writeBoth(webProd, webDev, "\t</session-config>\n");
            writeBoth(webProd, webDev, "\t<welcome-file-list>\n");
            writeBoth(webProd, webDev, "\t\t<welcome-file>index.html</welcome-file>\n");
            writeBoth(webProd, webDev, "\t</welcome-file-list>\n");
            writeBoth(webProd, webDev, "</web-app>\n");

            jaxws.write("</endpoints>\n");

            webProd.close();
            webDev.close();
            jaxws.close();

           // FileUtils.copy("web/WEB-INF/web.xml.development", "web/WEB-INF/web.xml");  // make development (disallow-CORS) the default
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (webProd != null)
                    webProd.close();
                if (jaxws != null)
                    jaxws.close();
            } catch (IOException ex) {
                Logger.getLogger(WsConfigGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void recurseDirs(File current) {
        if (current.isDirectory())
            for (File f : current.listFiles())
                recurseDirs(f);
        else if (current.getName().endsWith("Ops.java")) {
            String abspath = current.getAbsolutePath().replace('\\', '/');

            String className = capitalize(abspath.substring(0, abspath.length() - current.getName().length()));
            className = className + "Ops";

            servlets += "\t<servlet>\n";
            servlets += "\t\t<servlet-name>" + className + "</servlet-name>\n";
            servlets += "\t\t<servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>\n";
            servlets += "\t\t<load-on-startup>1</load-on-startup>\n";
            servlets += "\t</servlet>\n";
            maps += "\t<servlet-mapping>\n";
            maps += "\t\t<servlet-name>" + className + "</servlet-name>\n";
            maps += "\t\t<url-pattern>/" + className + "</url-pattern>\n";
            maps += "\t</servlet-mapping>\n";

            String nameWithPack = abspath.substring(abspath.indexOf("com/arahant/services/") + "com/arahant/services/".length());

            nameWithPack = "com.arahant.services." + nameWithPack.replace('/', '.');
            nameWithPack = nameWithPack.substring(0, nameWithPack.length() - 5);

            endpoints += "<endpoint implementation=\"" + nameWithPack + "\" name=\"" + className + "\" url-pattern=\"/" + className + "\"/>\n";
        }
    }

    private static void writeBoth(BufferedWriter webProd, BufferedWriter webDev, String text) throws IOException {
        webProd.write(text);
        webDev.write(text);
    }

    private static String capitalize(String packageName) {
        packageName = packageName.replace('\\', '/');
        packageName = packageName.substring(packageName.indexOf("com/arahant/services/") + "com/arahant/services/".length());
        StringBuilder ret = new StringBuilder();
        StringTokenizer stok = new StringTokenizer(packageName, "/");
        while (stok.hasMoreTokens()) {
            String tok = stok.nextToken();
            ret.append(tok.substring(0, 1).toUpperCase()).append(tok.substring(1));
        }
        return ret.toString();
    }
}
