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

import com.arahant.servlets.REST;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import net.openhft.compiler.CachedCompiler;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static com.arahant.servlets.REST.ExecutionReturn;

/**
 * Author: Blake McBride
 * Date: 5/5/18
 */
public class JavaService extends RestBase {

    private static final transient ArahantLogger logger = new ArahantLogger(JavaService.class);

    private static final Hashtable<String, JavaClassInfo> javaClassCache = new Hashtable<>();

    private static class JavaClassInfo {
        static long cacheLastChecked = 0;   // last time cache unload checked
        Class jclass;
        long lastModified;
        long lastAccess;
        int executing;

        JavaClassInfo(Class jc, long lm) {
            jclass = jc;
            lastModified = lm;
            lastAccess = (new Date()).getTime() / 1000L;
            executing = 0;
        }
    }

    public ExecutionReturn tryJava(REST ms, HttpServletResponse response, String _package, String _className, String _method, JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu) {
        JavaClassInfo ci;
        final String _fullClassPath = _package + "." + _className;
        String fileName = FileSystemUtils.getSourcePath() + _fullClassPath.replace(".", "/") + ".java";
        ci = loadJavaClass(_className, fileName);
        if (ci != null) {
            Class[] ca = {
                    JSONObject.class,
                    JSONObject.class,
                    HibernateSessionUtil.class,
                    REST.class
            };

            try {
                @SuppressWarnings("unchecked")
                Method methp = ci.jclass.getMethod(_method, ca);
                if (methp == null) {
                    ms.errorReturn(response, "Method " + _method + " not found in class " + this.getClass().getName(), null);
                    return ExecutionReturn.Error;
                }
                try {
                    ci.executing++;
                    methp.invoke(null, injson, outjson, hsu, ms);
                } finally {
                    ci.executing--;
                }
                return ExecutionReturn.Success;
            } catch (Exception e) {
                ms.errorReturn(response, "Error running method " + _method + " in class " + this.getClass().getName(), e);
                return ExecutionReturn.Error;
            }
        }
        return ExecutionReturn.NotFound;
    }

    private synchronized static JavaClassInfo loadJavaClass(String className, String fileName) {
        Class jclass;
        JavaClassInfo ci;
        if (javaClassCache.containsKey(fileName)) {
            ci = javaClassCache.get(fileName);
            /* This must be done by checking the file date rather than a directory change watcher for two reasons:
                1) directory change watchers don't work on sub-directories
                2) there is no notification for file moves
             */
            long lastModified = (new File(fileName)).lastModified();
            if (lastModified == 0L) {
                logger.error(fileName + " not found");
                javaClassCache.remove(fileName);
                return null;
            }
            if (lastModified == ci.lastModified) {
                ci.lastAccess = (new Date()).getTime() / 1000L;
                cleanJavaCache();
                return ci;
            }
            javaClassCache.remove(fileName);
        }
        cleanJavaCache();
        try {
            String code = new String(Files.readAllBytes(Paths.get(FileSystemUtils.getSourcePath() + fileName)), StandardCharsets.UTF_8);

            //jclass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, code);
            ClassLoader classLoader = new ClassLoader() {};
            CachedCompiler cc = new CachedCompiler(null, null);
            jclass = cc.loadFromJava(classLoader, className, code);

            javaClassCache.put(fileName, ci = new JavaClassInfo(jclass, (new File(fileName)).lastModified()));
        } catch (Exception e) {
            logger.error("Error loading " + fileName, e);
            return null;
        }
        return ci;
    }

    private static void cleanJavaCache() {
        long current = (new Date()).getTime() / 1000L;
        if (current - JavaClassInfo.cacheLastChecked > CheckCacheDelay) {
            ArrayList<String> keys = new ArrayList<>();
            for (Map.Entry<String, JavaClassInfo> itm : javaClassCache.entrySet()) {
                JavaClassInfo ci = itm.getValue();
                if (ci.executing > 0)
                    ci.lastAccess = current;
                else if (current - ci.lastAccess > MaxHold)
                    keys.add(itm.getKey());
            }
            for (String key : keys)
                javaClassCache.remove(key);
            JavaClassInfo.cacheLastChecked = current;
        }
    }

}
