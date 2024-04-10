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

import com.arahant.groovy.GroovyClass;
import com.arahant.utils.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import com.arahant.servlets.REST;

import static com.arahant.servlets.REST.ExecutionReturn;


/**
 * Author: Blake McBride
 * Date: 5/5/18
 */
public class GroovyService extends RestBase {

    private static final ArahantLogger logger = new ArahantLogger(GroovyService.class);

    private static final Hashtable<String, GroovyClassInfo> groovyClassCache = new Hashtable<>();

    public static class GroovyClassInfo {
        static long cacheLastChecked = 0;   // last time cache unload checked
        public GroovyClass gclass;
        long lastModified;
        long lastAccess;
        int executing;

        GroovyClassInfo(GroovyClass gc, long lm) {
            gclass = gc;
            lastModified = lm;
            lastAccess = (new Date()).getTime() / 1000L;
            executing = 0;
        }
    }

    /**
     * This method is used to obtain a class or instance method from a groovy class.
     * The Groovy file is treated as a microservice.
     * This means you will always get the most current definition of the method.
     * Once the method is obtained, it may be evoked any number of times.
     * <br><br>
     * System property "CustomPath" is used to determine the root of the custom code.
     * It defaults to the root of the Java code.  Under the IDE this directory is where
     * the source root is.  If not under an IDE, it is the root of where the Java class files are.
     * <br><br>
     * The "CustomPath" is checked first.  If not found, the SourcePath is checked.
     * <br><br>
     * The "CustomPath" system property can contain the "~" character.  This is replaced with the system path
     * as described above.
     * <br><br>
     * if ignoreMissing is true and the file, class, or method are missing a NULL is returned.
     * If ignoreMissing is false and the file, class, or method are missing an exception is thrown.
     *
     * @param ignoreMissing
     * @param relativePath relative to the custom root or package name
     * @param className
     * @param methodName
     * @param args the actual arguments or the argument types (classes)
     * @return the method found
     * @throws Exception
     * 
     * @see #run(Method, Object, Object...) 
     */
    public static Method getMethod(boolean ignoreMissing, String relativePath, String className, String methodName, Object ... args) throws Exception {
        return getMethod2(ignoreMissing, relativePath, className, methodName, args);
    }

    private static Method getMethod2(boolean ignoreMissing, String relativePath, String className, String methodName, Object [] args) throws Exception {
        if (relativePath == null)
            relativePath = "";
        else
            relativePath = relativePath.replaceAll("\\.", "/");
        final String customRoot = FileSystemUtils.getCustomRoot();
        String fileName = customRoot + "/" + (!relativePath.isEmpty() ? relativePath + "/" : "") + className + ".groovy";
        if (!(new File(fileName)).exists()) {
            final String systemRoot = FileSystemUtils.getSourcePath();
            fileName = systemRoot + (!relativePath.isEmpty() ? relativePath + "/" : "") + className + ".groovy";
            if (ignoreMissing && !(new File(fileName)).exists())
                return null;
        }
        final GroovyClassInfo ci = loadGroovyClass(fileName);
        Method methp;
        if (ci == null) {
            if (ignoreMissing)
                return null;
            throw new Exception("Groovy file " + new File(fileName).getAbsolutePath() + " not found.");
        }
        Class<?> [] ca = new Class<?>[args.length];
        for (int i=0 ; i < args.length ; i++) {
            if (args[i] == null)
                ca[i] = Object.class;
            else if (args[i] instanceof Class) {
                // The user is passing a class indicating the class of a null argument
                ca[i] = (Class) args[i];
                args[i] = null;
            } else
                ca[i] = args[i].getClass();
        }
        try {
            methp = ci.gclass.getMethod(methodName, ca);
            if (methp == null) {
                if (ignoreMissing)
                    return null;
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Method " + methodName + " not found in Groovy file " + new File(fileName).getAbsolutePath(), e);
        }
        return methp;
    }

    public static Method getMethod(String filePath, String className, String methodName, Object... args) throws Exception {
        return getMethod2(false, filePath, className, methodName, args);
    }

    /**
     * Run a method.  This is mainly used when you want to run the same method multiple times.
     * <br><br>
     * The calling method may use boxed or unboxed arguments, but a boxed type will always be returned.
     * <br><br>
     * All arguments to the method being executed are received in boxed form.  It
     * must also return a boxed object.
     * 
     * @param methp
     * @param inst instance or null if class method
     * @param args
     * @return
     * @throws Exception
     * 
     * @see #getMethod(String, String, String, Object...) 
     * @see #run(String, String, String, Object, Object...) 
     */
    public static Object run(Method methp, Object inst, Object... args) throws Exception {
        return run2(methp, inst, args);
    }

    private static Object run2(Method methp, Object inst, Object [] args) throws Exception {
        try {
            if (args == null) {
                args = new Object[1];
                args[0] = null;
            }
            return methp.invoke(inst, args);
        } catch (Exception e) {
            throw new Exception("Error executing method " + methp.getName() + " of class " + methp.getClass().getName(), e);
        }
    }

    public static Object run(boolean ignoreMissing, String filePath, String className, String methodName, Object inst, Object... args) throws Exception {
        return run2(ignoreMissing, filePath, className, methodName, inst, args);
    }

    private static Object run2(boolean ignoreMissing, String filePath, String className, String methodName, Object inst, Object [] args) throws Exception {
        Method meth = getMethod2(ignoreMissing, filePath, className, methodName, args);
        return run2(meth, inst, args);
    }

    /**
     * This is the main method used to execute Groovy microservices.  Use <code>getMethod</code> instead
     * if you are calling the method multiple times.
     * <br><br>
     * System property "CustomPath" is used to determine the root of the custom code.
     * It defaults to the root of the Java code.  Under the IDE this directory is where
     * the source root is.  If not under an IDE, it is the root of where the Java class files are.
     * <br><br>
     * The "CustomPath" is checked first.  If not found, the SourcePath is checked.
     * <br><br>
     * The "CustomPath" system property can contain the "~" character.  This is replaced with the system path
     * as described above.
     * <br><br>
     * The calling method may use boxed or unboxed arguments, but a boxed type will always be returned.
     * <br><br>
     * All arguments to the method being executed are received in boxed form.  It
     * must also return a boxed object.
     *
     * @param relativePath relative to the custom root or package name
     * @param className
     * @param methodName
     * @param inst instance or null if a class method
     * @param args boxed or unboxed arguments (variable number)
     * @return always boxed
     * @throws Exception
     *
     * @see #run(boolean, String, String, String, Object, Object...)
     * @see #getMethod(String, String, String, Object...)
     */
    public static Object run(String relativePath, String className, String methodName, Object inst, Object ... args) throws Exception {
        return run2(false, relativePath, className, methodName, inst, args);
    }

    /**
     * Execute a Groovy constructor.
     *
     * @param relativePath
     * @param className
     * @param args
     * @return
     * @throws Exception
     */
    public static Object constructor(String relativePath, String className, Object ... args) throws Exception {
        if (relativePath == null)
            relativePath = "";
        else
            relativePath = relativePath.replaceAll("\\.", "/");
        String rootPath = FileSystemUtils.getSourcePath();
        final String fileName = rootPath + (relativePath != null  &&  !relativePath.isEmpty() ? relativePath + "/" : "") + className + ".groovy";
        final GroovyClassInfo ci = loadGroovyClass(fileName);
        if (ci == null)
            throw new Exception("Groovy file " + fileName + " not found.");
        if (args == null) {
            args = new Object[1];
            args[0] = null;
        }
        return ci.gclass.invokeConstructor(args);
    }

    /**
     * This is the main entry point for REST services.  It can only execute static methods.
     *
     * @param ms
     * @param response
     * @param _package
     * @param _className
     * @param _method
     * @param injson
     * @param outjson
     * @param hsu
     * @return
     */
    public ExecutionReturn tryGroovy(REST ms, HttpServletResponse response, String _package, String _className, String _method, JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu) {
        final String _fullClassPath = _package + "." + _className;
        final String fileName = FileSystemUtils.getSourcePath() + _fullClassPath.replace(".", "/") + ".groovy";
        final GroovyClassInfo ci = loadGroovyClass(fileName);
        if (ci != null) {
            Class<?>[] ca = {
                    JSONObject.class,
                    JSONObject.class,
                    HibernateSessionUtil.class,
                    REST.class
            };

            try {
                @SuppressWarnings("unchecked")
                Method methp = ci.gclass.getMethod(_method, ca);
                if (methp == null) {
                    ms.errorReturn(response, "Method " + _method + " not found in class " + ci.gclass.getClass().getName(), null);
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
                ms.errorReturn(response, "Error running method " + _method + " in class " + ci.gclass.getClass().getName(), e);
                return ExecutionReturn.Error;
            }
        }
        return ExecutionReturn.NotFound;
    }

    public synchronized static GroovyClassInfo loadGroovyClass(String fileName) {
        GroovyClass gclass;
        GroovyClassInfo ci;
        if (groovyClassCache.containsKey(fileName)) {
            ci = groovyClassCache.get(fileName);
            /* This must be done by checking the file date rather than a directory change watcher for two reasons:
                1) directory change watchers don't work on sub-directories
                2) there is no notification for file moves
             */
            long lastModified = (new File(fileName)).lastModified();
            if (lastModified == 0L) {
                logger.error("File " + new File(fileName).getAbsolutePath() + " not found");
                groovyClassCache.remove(fileName);
                return null;
            }
            if (lastModified == ci.lastModified) {
                ci.lastAccess = (new Date()).getTime() / 1000L;
                cleanGroovyCache();
                return ci;
            }
            groovyClassCache.remove(fileName);
        }
        cleanGroovyCache();
        try {
            GroovyClass.reset();
            gclass = new GroovyClass(false, fileName);
            groovyClassCache.put(fileName, ci = new GroovyClassInfo(gclass, (new File(fileName)).lastModified()));
        } catch (Exception e) {
            logger.error("Error loading " + new File(fileName).getAbsolutePath(), e);
            return null;
        }
        return ci;
    }

    private static void cleanGroovyCache() {
        long current = (new Date()).getTime() / 1000L;
        if (current - GroovyClassInfo.cacheLastChecked > CheckCacheDelay) {
            ArrayList<String> keys = new ArrayList<>();
            for (Map.Entry<String, GroovyClassInfo> itm : groovyClassCache.entrySet()) {
                GroovyClassInfo ci = itm.getValue();
                if (ci.executing > 0)
                    ci.lastAccess = current;
                else if (current - ci.lastAccess > MaxHold)
                    keys.add(itm.getKey());
            }
            for (String key : keys)
                groovyClassCache.remove(key);
            GroovyClassInfo.cacheLastChecked = current;
        }
    }

}
