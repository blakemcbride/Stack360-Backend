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

package com.arahant.services.standard.ping

import com.arahant.servlets.REST
import com.arahant.utils.ArahantSession
import com.arahant.utils.DateUtils
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.jezhumble.javasysmon.CpuTimes
import com.jezhumble.javasysmon.JavaSysMon
import com.jezhumble.javasysmon.MemoryStats
import org.json.JSONObject
import org.kissweb.DateTime
import org.kissweb.FileUtils

import java.sql.DatabaseMetaData

/**
 * Author: Blake McBride
 * Date: 3/23/23
 *
 * This service is used to validate that the server is still functioning.
 */
class Ping {

    private final static String authToken = "c9d35fb6-7886-4636-82cc-c5b155775317";
    private static CpuTimes previousCpuTimes;

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String token = injson.getString("authToken");
        if (token == null || !token.equals(authToken))
            return;
        outjson.put("response", "up")
        outjson.put("CurrentDatetime", DateTime.currentDateTimeFormatted())

        //----------------------------------------------------------------

        final Runtime rt = Runtime.getRuntime();
        outjson.put("JavaMaxMemory", rt.maxMemory());
        outjson.put("JavaTotalMemory", rt.totalMemory());
        outjson.put("JavaFreeMemory", rt.freeMemory());

        //-----------------------------------------------------------------

        outjson.put("ApplicationPath", FileSystemUtils.getWorkingDirectory().getAbsolutePath())
        final DatabaseMetaData dmd = ArahantSession.getHSU().getConnection().getMetaData();
        outjson.put("DatabaseServer", dmd.getDatabaseProductName() + " " + dmd.getDatabaseProductVersion())
        outjson.put("DatabaseName", dmd.getURL())
        outjson.put("DatabaseUser", dmd.getUserName())
        outjson.put("Uptime", DateUtils.dateFormat("MM/dd/yyyy kk:mm:ss", ArahantSession.getUpTime()))
        outjson.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"))

        //----------------------------------------------------------------

        String host = "Unknown Host";
        String ip = "Unknown IP Address";
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            final String ipAddress = addr.getHostAddress();
            final String hostName = addr.getHostName();

            if (ipAddress != null && ipAddress.length() > 0)
                ip = ipAddress;

            if (!ip.equals(hostName))
                host = hostName;

        } catch (UnknownHostException e) {
            // ignore
        }

        outjson.put("Host", host);
        outjson.put("HostIP", ip);

        //----------------------------------------------------------------


        final InputStream is = new FileInputStream(FileSystemUtils.getWorkingDirectory().toString() + "/WEB-INF/classes/VersionData.txt")
        final InputStream bis = new FileInputStream(FileSystemUtils.getWorkingDirectory().toString() + "/WEB-INF/classes/buildConfig.properties")
        Properties props = new Properties();
        Properties bprops = new Properties();

        props.load(is);
        bprops.load(bis);
        is.close();
        bis.close();

        outjson.put("BuildDate", props.get("BuildDate"))
        outjson.put("SourceCodeRevision", props.get("SourceCodeRevisionNumber"))

        // ----------------------------------------------------------------

        final JavaSysMon sysmon = new JavaSysMon();
        final MemoryStats ms = sysmon.physical()
        outjson.put("OSFreeMemory", ms.getFreeBytes())
        outjson.put("OSTotalMemory", ms.getTotalBytes())
        outjson.put("OSUptime", sysmon.uptimeInSeconds())
        outjson.put("OSNumCpus", sysmon.numCpus())
        //outjson.put("OSCpuMhz", sysmon.cpuFrequencyInHz())  // this is failing on production systems.  I do not know why.  Need to investigate.

        final CpuTimes cpuTimes = sysmon.cpuTimes()
        outjson.put("OSCpuTotalTime", cpuTimes.getTotalMillis())
        outjson.put("OSCpuUserTime", cpuTimes.getUserMillis())
        outjson.put("OSCpuSystemTime", cpuTimes.getSystemMillis())
        outjson.put("OSCpuUserTime", cpuTimes.getUserMillis())
        outjson.put("OSCpuIdleTime", cpuTimes.getIdleMillis())
        outjson.put("OSCpuUsage", previousCpuTimes != null ? cpuTimes.getCpuUsage(previousCpuTimes) : 0)
        previousCpuTimes = cpuTimes;


        // ----------------------------------------------------------------


        String mi = FileUtils.readFile("/proc/meminfo")
        mi = "{" + mi.replaceAll("\\n", ", ") + "}"
        outjson.put("OSMemInfo", new JSONObject(mi))
    }
}
