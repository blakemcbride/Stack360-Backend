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

package com.arahant.rest;

import com.arahant.lisp.ABCL;
import com.arahant.servlets.REST;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import org.armedbear.lisp.LispObject;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;


import static com.arahant.servlets.REST.ExecutionReturn;

/**
 * Author: Blake McBride
 * Date: 5/5/18
 */
public class LispService extends RestBase {

    private static final transient ArahantLogger logger = new ArahantLogger(LispService.class);

    private static final Hashtable<String, LispPackageInfo> lispPackageCache = new Hashtable<>();

    private static class LispPackageInfo {
        static long cacheLastChecked = 0;   // last time cache unload checked
        String packageName;
        String fileName;
        long lastModified;
        long lastAccess;
        int executing;

        LispPackageInfo(String pname, String fname, long lm) {
            packageName = pname;
            fileName = fname;
            lastModified = lm;
            lastAccess = (new Date()).getTime() / 1000L;
            executing = 0;
        }
    }

    public ExecutionReturn tryLisp(REST ms, HttpServletResponse response, String _package, String _className, String _method, JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu) {
        final String _fullClassPath = _package + "." + _className;

        String lispFileName = _fullClassPath.replace(".", "/") + ".lisp";
        if (!(new File(FileSystemUtils.getSourcePath() + lispFileName)).exists())
            return ExecutionReturn.NotFound;

        if (FileSystemUtils.isUnderIDE())
            ABCL.reset();
        LispObject args = ABCL.executeLispFunction(ABCL.getMakeRestServiceArgs(), injson, outjson, hsu, ms);

        LispObject lispIn = args.NTH(0);
        LispObject lispOut = args.NTH(1);
        LispObject lispHSU = args.NTH(2);
        LispObject lispThis = args.NTH(3);

        LispPackageInfo res;
        try {
            res = loadLispFile(_package, lispFileName);
        } catch (Exception e) {
            ms.errorReturn(response, "Error loading Lisp " + lispFileName, e);
            res = null;
        }
        if (res == null) {
            ms.errorReturn(response, "Error loading Lisp " + lispFileName, null);
            return ExecutionReturn.Error;
        }

        try {
            res.executing++;
            ABCL.executeLisp(_package, _method, lispIn, lispOut, lispHSU, lispThis);
        } catch (Exception e) {
            ms.errorReturn(response, "Error executing Lisp " + lispFileName, e);
            return ExecutionReturn.Error;
        } finally {
            res.executing--;
        }

        return ExecutionReturn.Success;
    }

    private synchronized static LispPackageInfo loadLispFile(String packageName, String fileName) {
        LispPackageInfo ci;
        if (lispPackageCache.containsKey(fileName)) {
            ci = lispPackageCache.get(fileName);
            /* This must be done by checking the file date rather than a directory change watcher for two reasons:
                1) directory change watchers don't work on sub-directories
                2) there is no notification for file moves
             */
            long lastModified = (new File(FileSystemUtils.getSourcePath() + fileName)).lastModified();
            if (lastModified == 0L) {
                logger.error(fileName + " not found");
                lispPackageCache.remove(fileName);
                return null;
            }
            if (lastModified == ci.lastModified) {
                ci.lastAccess = (new Date()).getTime() / 1000L;
                cleanLispCache();
                return ci;
            }
            lispPackageCache.remove(fileName);
        }
        cleanLispCache();
        try {
            ABCL.deletePackage(packageName);  // get rid of any old version first so they don't get merged
            ABCL.load(fileName);
            lispPackageCache.put(fileName, ci = new LispPackageInfo(packageName, fileName, (new File(fileName)).lastModified()));
        } catch (Exception e) {
            logger.error("Error loading " + fileName, e);
            return null;
        }
        return ci;
    }

    private static void cleanLispCache() {
        long current = (new Date()).getTime() / 1000L;
        if (current - LispPackageInfo.cacheLastChecked > CheckCacheDelay) {
            ArrayList<String> keys = new ArrayList<>();
            for (Map.Entry<String, LispPackageInfo> itm : lispPackageCache.entrySet()) {
                LispPackageInfo lpi = itm.getValue();
                if (lpi.executing > 0)
                    lpi.lastAccess = current;
                else if (current - lpi.lastAccess > MaxHold)
                    keys.add(itm.getKey());
            }
            for (String key : keys) {
                LispPackageInfo lp = lispPackageCache.get(key);
                ABCL.deletePackage(lp.packageName);
                lispPackageCache.remove(key);
            }
            LispPackageInfo.cacheLastChecked = current;
        }
    }

}
