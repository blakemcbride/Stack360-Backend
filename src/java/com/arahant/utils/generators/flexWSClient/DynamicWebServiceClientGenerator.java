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


package com.arahant.utils.generators.flexWSClient;

import com.arahant.annotation.Validation;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.ABCL;
import com.arahant.utils.*;
import com.arahant.utils.generators.flexWSClient.wsdl.Binding;
import com.arahant.utils.generators.flexWSClient.wsdl.DOMReader;
import com.arahant.utils.generators.flexWSClient.wsdl.Operation;
import com.arahant.utils.generators.flexWSClient.wsdl.Port;
import com.arahant.utils.generators.flexWSClient.wsdl.Service;
import com.arahant.utils.generators.flexWSClient.wsdl.WebServiceDefinition;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DynamicWebServiceClientGenerator {

    private static String Frontend_Path;
    private static int iBaseFolderLength = 0;
    private String className;
    private String packName;
    private File outputDir;
    private String serviceName;
    private Map<String, HashMap> tables = new HashMap<String, HashMap>();
    private HashSet<String> standardTypes = new HashSet<String>();
    private String serviceImports = "";
    private boolean urlIsLocal;  //  false means url is servlet url, true means use local file system

    public DynamicWebServiceClientGenerator() {
        this.urlIsLocal = false;
    }

    private DynamicWebServiceClientGenerator(boolean urlIsLocal) {
        this.urlIsLocal = urlIsLocal;
    }

    public static void main(final String args[]) {
        Frontend_Path = "../Frontend_Flex/src";
        ABCL.init();
//        genOne("standard", "time", "timesheetEntry");
//        genOne("standard", "project", "projectCurrentStatus")
//        genOne("standard", "hr", "hrTrainingCategory");
//        genOne("custom", "waytogo", "timesheetEntry");
//        generateLocal("custom", "waytogo", "timesheetEntry");
//        generateLocal("standard", "billing", "createTimesheetInvoice");
//        generateLocal("standard", "site", "screen");
//        generateLocal("standard", "crm", "clientSummary");
//        generateLocal("standard", "time", "timesheetEntryWeeklySummary");
        generateLocal("standard", "hr", "hrEmployee");
//        genAll();
        HibernateUtil.close();
    }

    private static void genOne(String a, String b, String c) {
        generateLocal(a, b, c);
    }

    private static void genAll() {
        genAllForPath("standard");
        genAllForPath("custom");
    }

    private static void genAllForPath(String path) {
        String base = "src/java/com/arahant/services/" + path;

        File[] fl1 = (new File(base)).listFiles();
        if (fl1 != null)
            for (File d1 : fl1) {
                if (!d1.getName().equals("dynamicwebservices")) {
                    File[] fl2 = d1.getAbsoluteFile().listFiles();
                    if (fl2 != null)
                        for (File d2 : fl2) {
                            System.out.println(path + " - " + d1.getName() + " - " + d2.getName());
                            generateLocal(path, d1.getName(), d2.getName());
                        }
                }
            }
    }

    private static void generateLocal(String type, String module, String folder) {
        try {
            File output = new DynamicWebServiceClientGenerator(true).generate(type, module, "build/generated/wsgen/service/", folder);
            System.out.println("Output directory = " + output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * For com.arahant.service.custom.waytogo.timesheetEntry it is:
     *
     * @param type   "custom"
     * @param module "waytogo"
     * @param url    "http://localhost:8084/Arahant/"
     * @param folder "timesheetEntry"
     * @return
     * @throws Exception
     */
    public File generate(String type, String module, String url, String folder) throws Exception {
        //change screen name to ops class
        String op = upperCase(folder) + "Ops";
        String packName2 = "com.arahant.services." + type + "." + module + "." + folder;

        URL wsdlUrl = null;
        String wsdlPath = null;
        URL wsdlSchemaUrl = null;
        String wsdlSchemaPath = null;

        if (urlIsLocal) {
            wsdlPath = url + upperCase(type) + upperCase(module) + op + ".wsdl";
            wsdlSchemaPath = url + upperCase(type) + upperCase(module) + op + "_schema1.xsd";
        } else {
            //URL - http://prophet.arahant.com:80/arahant/StandardTimeTimesheetReviewOps?wsdl
            //need to query myself / packUrl+"Ops?wsdl"
            wsdlUrl = new URL(url + upperCase(type) + upperCase(module) + op + "?wsdl");
            wsdlSchemaUrl = new URL(url + upperCase(type) + upperCase(module) + op + "?xsd=1");
        }

        final DatabaseMetaData dmd = ArahantSession.getConnection().getMetaData();

        final ResultSet rs = dmd.getTables("public", null, null, new String[]{"TABLE"});

        while (rs.next()) {
            final String tableName = rs.getString(3);
            final HashMap<String, ColData> colMap = new HashMap<String, ColData>();
            tables.put(tableName, colMap);

            //System.out.println("******"+tableName);

            final ResultSet cols = dmd.getColumns("public", null, tableName, null);
            while (cols.next()) {
                final ColData cd = new ColData();

                cd.name = cols.getString("COLUMN_NAME");
                cd.columnSize = cols.getInt("COLUMN_SIZE");
                cd.dataType = cols.getInt("DATA_TYPE");
                cd.digits = cols.getInt("DECIMAL_DIGITS");

                colMap.put(cd.name, cd);
                //	System.out.println(cd.toString());
            }
        }


        String name;
        File rootDir = null;
        File serviceDir;
        if (urlIsLocal) {
            rootDir = new File(Frontend_Path);
            serviceDir = new File(Frontend_Path + "/com/arahant/app/screen/" + type + "/" + module + "/" + folder + "/service");
            if (serviceDir.exists())
                delete(serviceDir);
        } else {
            serviceDir = File.createTempFile("stubs", "tempdir");
            name = serviceDir.getAbsolutePath();
//        x.delete();
            if (serviceDir.exists())
                delete(serviceDir);
            rootDir = new File(name);
            rootDir.mkdirs();
        }


        if (!rootDir.exists())
            rootDir.mkdir();
        FileWriter fw1 = new FileWriter(new File(findBottom(rootDir), "info.txt"));
        fw1.write("Generated on " + DateUtils.getDateFormatted(DateUtils.now()));
        fw1.flush();
        fw1.close();


        //put the stubs in the directory
        new DynamicWebServiceClientGenerator(urlIsLocal).exec(op, packName2, rootDir.getAbsolutePath(), wsdlUrl, wsdlSchemaUrl, upperCase(type) + upperCase(module) + op, wsdlPath, wsdlSchemaPath);

        //put the validations in the directory

        System.out.println(packName2);
        for (Class c : getClasses(packName2))
            //looking for the web service class
            for (Annotation annot : c.getAnnotations())
                if (annot.annotationType().equals(javax.jws.WebService.class)) {
                    System.out.println("Found ops class " + c.getName());

                    //put in right directory
                    FileWriter fw;
                    if (urlIsLocal) {
                        fw = new FileWriter(new File(serviceDir, "ServiceValidation.as"));
                    } else {
                        fw = new FileWriter(new File(findBottom(rootDir), "ServiceValidation.as"));
                        System.out.println("File bottom = " + findBottom(rootDir));
                    }

                    copyInFile(packName2.replaceAll("com.arahant.services", "com.arahant.app.screen"), fw, "classStart.as");

                    //now I need to find all web methods

                    for (Method meth : c.getMethods())
                        for (Annotation methAnn : meth.getAnnotations())
                            if (methAnn.annotationType().equals(javax.jws.WebMethod.class)) {
                                //now I need to know the input class name
                                Class[] inputClasses = meth.getParameterTypes();

                                if (inputClasses.length == 1) {
                                    fw.write("\t\t\t////////////////////////////////////////////////////////////\n");
                                    fw.write("\t\t\t//" + meth.getName() + "\n");
                                    fw.write("\t\t\tvmap[\"" + meth.getName() + "\"] = new Dictionary();\n");

                                    //this should be an input method to process
                                    //				System.out.println("INput class "+inputClasses[0].getName());
                                    for (Field field : inputClasses[0].getDeclaredFields()) {
                                        Validation v = field.getAnnotation(com.arahant.annotation.Validation.class);

                                        if (v == null)
                                            continue;

                                        String fieldName = fixVariableName(field.getName());

                                        fw.write("\t\t\t(vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] = new ValidationData();\n");
                                        //access the field annotation
                                        //Field field = c.getField("greetingState");


                                        fw.write("\t\t\t(((vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] as ValidationData)).required = " + v.required() + ";\n");

                                        //	System.out.println(v);

                                        String max = null;
                                        String min = null;

                                        //if I have a table and column and field is a string then I need to hit database for size data
                                        if (field.getType().equals(String.class) && (v.table() != null && v.table().trim().length() > 0)
                                                && (v.column() != null && v.column().trim().length() > 0)) {
                                            if (tables == null)
                                                throw new Exception("Tables not found - could not connect to db?");
                                            if (tables.get(v.table()) == null)
                                                throw new Exception("Validation uses table " + v.table() + " which was not found in schema.");
                                            ColData cd = (ColData) tables.get(v.table()).get(v.column());
                                            if (cd != null && (cd.dataType == 12 || cd.dataType == -1 || cd.dataType == 1)) {
                                                min = "0";
                                                max = cd.columnSize + "";
                                            }
                                        }

                                        if (v.type().equals("date")) {
                                            min = "10000101";
                                            max = "30000101";
                                        }

                                        if (v.type().equals("time")) {
                                            min = "0";
                                            max = "235959999";
                                        }

                                        if (v.type().equals("ssn")) {
                                            min = "11";
                                            max = "11";
                                        }

                                        if (max != null && max.equals("-1"))
                                            max = "4000";

                                        if (min == null && field.getType().getSimpleName().toLowerCase().equals("string"))
                                            min = "0";

                                        if (max == null)
                                            max = v.max() + "";
                                        if (min == null)
                                            min = v.min() + "";


                                        //min and max
                                        fw.write("\t\t\t(((vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] as ValidationData)).min = " + min + ";\n");
                                        fw.write("\t\t\t(((vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] as ValidationData)).max = " + max + ";\n");


                                        if (v.type() != null && v.type().trim().length() > 0)
                                            fw.write("\t\t\t(((vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] as ValidationData)).type = \"" + v.type() + "\";\n");
                                        else
                                            fw.write("\t\t\t(((vmap[\"" + meth.getName() + "\"] as Dictionary)[\"" + fieldName + "\"] as ValidationData)).type = \"" + field.getType().getSimpleName().toLowerCase() + "\";\n");
                                    }
                                }
                            }
                    copyInFile(packName2, fw, "classEnd.as");
                    fw.flush();
                    fw.close();
                } /*
		 * //Object o=Class.forName(c.getName()).newInstance(); for (Field field
		 * : c.getDeclaredFields()) {
		 *
		 * //access the field annotation //Field field =
		 * c.getField("greetingState"); for (Annotation a :
		 * field.getAnnotations()) { System.out.println(c.getName());
		 * System.out.println(field.getName());
		 * System.out.println(a.toString());
		 * }//System.out.println(field.getAnnotation(Validation.class));
		}
		 */

        //now I should have my stuff in directory

        System.out.println("Files stored in " + rootDir.getAbsolutePath());

        //need to jar up this folder

        if (!urlIsLocal) {
            File jarFile = FileSystemUtils.createTempFile("tempfile", ".jar");

            test(rootDir, jarFile);

            // delete the directory
            delete(rootDir);

            System.out.println(jarFile.getAbsolutePath());

            return jarFile;
        } else {
            return serviceDir;
        }

    }

    private static String upperCase(String x) {
        return x.substring(0, 1).toUpperCase() + x.substring(1);
    }

    private static Class[] getClasses(String pckgname) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null)
                throw new ClassNotFoundException("Can't get class loader.");

            //May not want first '/' here
            String path = pckgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null)
                throw new ClassNotFoundException("No resource for " + path);
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
        }
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++)
                // we are only interested in .class files
                if (files[i].endsWith(".class"))
                    // removes the .class extension
                    classes.add(Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6)));
        } else
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
        Class[] classesA = new Class[classes.size()];
        classes.toArray(classesA);
        return classesA;
    }

    private static void checkDirectory(File dirobject, JarOutputStream jos) {
        if (dirobject.exists())
            if (dirobject.isDirectory()) {
                File[] fileList = dirobject.listFiles();
                // Loop through the files
                for (int i = 0; i < fileList.length; i++)
                    if (fileList[i].isDirectory())
                        checkDirectory(fileList[i], jos);
                    else if (fileList[i].isFile())
                        // Call the zipFunc function
                        jarFile(fileList[i].getPath(), jos);
            } else
                System.out.println(dirobject.getAbsolutePath() + " is not a directory.");
        else
            System.out.println("Directory " + dirobject.getAbsolutePath() + " does not exist.");
    }

    private static void test(File checkDir, File outputPath) {
        try {
            // Create the file output streams for both the file and the zip.

            String strBaseFolder = checkDir + File.separator;
            iBaseFolderLength = strBaseFolder.length();
            FileOutputStream fos = new FileOutputStream(outputPath);

            JarOutputStream jos = new JarOutputStream(fos);

            System.out.println(strBaseFolder);

            checkDirectory(checkDir, jos);
            // Close the file output streams
            jos.flush();
            jos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // a Jar method.
    private static void jarFile(String filePath, JarOutputStream jos) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            JarEntry fileEntry = new JarEntry(filePath.substring(iBaseFolderLength));
            jos.putNextEntry(fileEntry);
            byte[] data = new byte[1024];
            int byteCount;
            while ((byteCount = bis.read(data, 0, 1024)) > -1)
                jos.write(data, 0, byteCount);
        } catch (IOException e) {
        }
    }

    private void copyInFile(String packName, FileWriter fw, String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));

        String inputLine;
        String ret = "";
        while ((inputLine = br.readLine()) != null)
            ret += inputLine + "\n";
        br.close();

        ret = ret.replaceAll("%1", packName);

        fw.write(ret);

        //now I need to find all webmethods
    }

    private void delete(File f) {
        File bottom;
        do {
            bottom = findBottom(f);
            for (File x : bottom.listFiles())
                x.delete();
            bottom.delete();
        } while (!bottom.equals(f));
    }

    private void exec(final String op, String packageName, String frontendDir, URL wsdlURL, URL schemaURL, String servName, String wsdlPath, String wsdlSchemaPath) {
        try {
            packageName = packageName.replaceFirst("services", "app.screen");

            if (packageName.equals("com.arahant.app.screen.main"))
                packageName = "com.arahant.app.main";

            if (op.toUpperCase().contains("AQDEV"))
                return;

            final Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("ServiceScreenMap.properties"));
            } catch (final Exception e) {
            }

            String unitName = op.substring(0, 1).toLowerCase() + op.substring(1, op.length() - 3);

            unitName = props.getProperty(unitName, unitName);

            if (unitName.startsWith("hR"))
                unitName = "hr" + unitName.substring(2);

            if (unitName.equals("peachtreeApp"))
                return;
            if (unitName.equals("quickbooksApp"))
                return;
            if (unitName.equals("scanner"))
                return;

            packName = packageName + ".service";

            if (!frontendDir.endsWith("/") && !frontendDir.endsWith("\\"))
                frontendDir += "/";

            outputDir = new File(frontendDir);
            if (!outputDir.exists() || !outputDir.isDirectory())
                throw new RuntimeException(outputDir + " directory doesn't exist.");

            className = op + "Service";

            serviceName = servName;

            generateService(wsdlURL, schemaURL, wsdlPath, wsdlSchemaPath);

        } catch (final Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private void reportStatus(final String msg) {
        System.out.println(msg);
    }

    private String buildActionScriptClass(final WebServiceDefinition def, final String packageName, final String destDir, URL schemaURL, String schemaPath) {
        final Service svc = def.getService(this.serviceName);
        if (svc == null)
            reportStatus("Invalid service name " + this.serviceName);

        makeUsedClasses(schemaURL, destDir, schemaPath);

        makeServiceClassInclude(destDir);

        final StringBuffer serviceOutput = new StringBuffer();

        writeClassName(def, serviceOutput);

        writeCommonFunctions(serviceOutput);

        final StringBuffer methodBuffer = new StringBuffer();
        for (final Iterator ports = svc.ports(); ports.hasNext(); ) {
            final Port port = (Port) ports.next();
            final Binding binding = port.getBinding();

            handleBindings(methodBuffer, port, binding);
        }

        String ret = serviceOutput.toString();
        ret = ret.replaceAll("%1", methodBuffer.toString());

        return ret;
    }

    /**
     * Searches down a directory structure finding a leaf directory endpoint
     *
     * @param f
     * @return A File representing the directory found
     */
    private File findBottom(File f) {
        for (File fy : f.listFiles())
            if (fy.isDirectory())
                return findBottom(fy);

        return f; //I must not have had any directories, so I'm the bottom
    }

    private boolean isArray(Node n2) {
        String typ = DOMUtils.getAttribute((Element) n2, "type");
        typ = upcase(removeColon(typ));
        return (typ.startsWith("ArrayOf") || "unbounded".equals(DOMUtils.getAttribute((Element) n2, "maxOccurs")));
    }

    private void writeClassName(final WebServiceDefinition def, final StringBuffer serviceOutput) {
        serviceOutput.append("package " + this.packName + "\n{\n");

        serviceOutput.append("\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t// Imports\n");
        serviceOutput.append("\timport com.arahant.app.common.Utils;\n");
        serviceOutput.append(serviceImports);
        serviceOutput.append("\timport flash.events.*;\n");
        serviceOutput.append("\timport flash.net.*;\n\n\n");


        serviceOutput.append("\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t// Events\n");

        final Service svc = def.getService(serviceName);
        if (svc == null)
            throw new ArahantException(serviceName + " service not found!");
        for (final Iterator ports = svc.ports(); ports.hasNext(); ) {
            final Port port = (Port) ports.next();
            final Binding binding = port.getBinding();

            for (final Iterator operations = binding.operations(); operations.hasNext(); ) {
                final Operation operation = (Operation) operations.next();
                final String methodName = operation.getName();
                serviceOutput.append("\t[Event(name=\"" + methodName + "Complete\", type=\"" + packName + "." + startCap(methodName) + "Return\")]\n");
            }
        }
        serviceOutput.append("\n\n");

        serviceOutput.append("\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t// Class Definition\n");
        serviceOutput.append("\tpublic class Service extends EventDispatcher\n");
        serviceOutput.append("\t{\n");

        serviceOutput.append("\t\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t\t// Instance Vars\n");
        serviceOutput.append("\t\tpublic var debug:Boolean = false;\n");
        serviceOutput.append("\t\tpublic var myLoader:URLLoader;\n\n\n");
    }

    private String startCap(final String x) {
        return x.substring(0, 1).toUpperCase() + x.substring(1);
    }

    private void handleBindings(final StringBuffer methodBuffer, final Port port, final Binding binding) {
        boolean isFirst = true;

        for (final Iterator operations = binding.operations(); operations.hasNext(); ) {
            final Operation operation = (Operation) operations.next();
            final String methodName = operation.getName();
            final StringBuilder asyncMethodBuffer = new StringBuilder();
            final String inputType = methodName.substring(0, 1).toUpperCase() + methodName.substring(1) + "Input";
            final String outputType = methodName.substring(0, 1).toUpperCase() + methodName.substring(1) + "Return";

            if (isFirst)
                isFirst = false;
            else
                asyncMethodBuffer.append("\n");

            // write out event constant
            asyncMethodBuffer.append("\t\tpublic static const EVENT_" + methodName.toUpperCase() + "_COMPLETE:String = \"" + methodName + "Complete\";\n\n");


            // write out service operation
            asyncMethodBuffer.append("\t\tpublic function " + methodName + "(inParam:" + inputType + "):void\n");
            asyncMethodBuffer.append("\t\t{\n");

            asyncMethodBuffer.append("\t\t\tvar reqHeaders:Array = [];\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"SOAPAction\", \"\"));\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"Content-Type\", \"text/xml;charset=\\\\\"utf-8\\\\\"\"));\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"Cache-control\", \"no-store, no-cache, must-revalidate, post-check=0, pre-check=0, no-transform, private\"));\n\n");

            asyncMethodBuffer.append("\t\t\tvar domcode:String = \"<ns2:" + methodName + " xmlns:ns2=\\\\\"http://operations.arahant.com\\\\\" ><arg0>\"\n");
            asyncMethodBuffer.append("\t\t\t\t+ inParam.toXMLString()\n");
            asyncMethodBuffer.append("\t\t\t\t+ \"</arg0></ns2:" + methodName + ">\";\n");
            asyncMethodBuffer.append("\t\t\tvar req:XML = this.buildDocRequestXML(domcode, \"" + methodName + "\");\n\n");

            asyncMethodBuffer.append("\t\t\tvar myXMLURL:URLRequest = new URLRequest(this.getURLBase() + \"" + this.serviceName + "?reqtime=\"+ (new Date().valueOf()));\n");
            asyncMethodBuffer.append("\t\t\tmyXMLURL.requestHeaders = reqHeaders;\n");
            asyncMethodBuffer.append("\t\t\tmyXMLURL.method = URLRequestMethod.POST;\n");
            asyncMethodBuffer.append("\t\t\tmyXMLURL.data = req.toXMLString();\n\n");

            asyncMethodBuffer.append("\t\t\tthis.myLoader = new URLLoader(myXMLURL);\n");
            asyncMethodBuffer.append("\t\t\tthis.myLoader.addEventListener(\"complete\", " + methodName + "XmlLoaded);\n");
            asyncMethodBuffer.append("\t\t\tthis.myLoader.addEventListener(\"ioError\", " + methodName + "XmlLoaded);\n\n");

            asyncMethodBuffer.append("\t\t\tif (this.debug) trace(\"**** " + methodName + " WS Transmitting ****\");\n");
            asyncMethodBuffer.append("\t\t\tif (this.debug) trace(inParam.toXMLString());\n\n");

            asyncMethodBuffer.append("\t\t\tthis.myLoader.load(myXMLURL);\n");

            asyncMethodBuffer.append("\t\t}\n\n");


            // write out service operation call back
            asyncMethodBuffer.append("\t\tprivate function " + methodName + "XmlLoaded(evtObj:Event):void\n");
            asyncMethodBuffer.append("\t\t{\n");

            asyncMethodBuffer.append("\t\t\tvar retEvt:" + outputType + " = new " + outputType + "(EVENT_" + methodName.toUpperCase() + "_COMPLETE);\n\n");

            asyncMethodBuffer.append("\t\t\tthis.myLoader.removeEventListener(\"complete\", " + methodName + "XmlLoaded);\n");
            asyncMethodBuffer.append("\t\t\tthis.myLoader.removeEventListener(\"ioError\", " + methodName + "XmlLoaded);\n\n");

            asyncMethodBuffer.append("\t\t\tif (evtObj.type == IOErrorEvent.IO_ERROR)\n");
            asyncMethodBuffer.append("\t\t\t{\n");
            asyncMethodBuffer.append("\t\t\t\tif (this.debug) trace(\"**** " + methodName + " IO Error ****\");\n\n");
            asyncMethodBuffer.append("\t\t\t\tretEvt.wsStatus = -1000;\n");
            asyncMethodBuffer.append("\t\t\t\tretEvt.wsMessage = \"Error communicating with server.  \" + (evtObj as IOErrorEvent).text;\n");
            asyncMethodBuffer.append("\t\t\t}\n");
            asyncMethodBuffer.append("\t\t\telse\n");
            asyncMethodBuffer.append("\t\t\t{\n");
            asyncMethodBuffer.append("\t\t\t\tif (this.debug) trace(\"**** " + methodName + " WS Complete ****\");\n\n");
            asyncMethodBuffer.append("\t\t\t\t// turn off XML parser ignoring white space\n");
            asyncMethodBuffer.append("\t\t\t\tXML.ignoreWhitespace = false;\n\n");
            asyncMethodBuffer.append("\t\t\t\tvar myXML:XML = XML(myLoader.data);\n");
            asyncMethodBuffer.append("\t\t\t\tretEvt.populate(myXML);\n\n");
            asyncMethodBuffer.append("\t\t\t\t// turn on XML parser ignoring white space\n");
            asyncMethodBuffer.append("\t\t\t\tXML.ignoreWhitespace = true;\n");
            asyncMethodBuffer.append("\t\t\t}\n\n");

            asyncMethodBuffer.append("\t\t\tif (this.debug) trace(retEvt.toXMLString());\n\n");
            asyncMethodBuffer.append("\t\t\tdispatchEvent(retEvt);\n");
            asyncMethodBuffer.append("\t\t}\n");


            methodBuffer.append(asyncMethodBuffer);
        }
    }

    private void writeCommonFunctions(final StringBuffer serviceOutput) {
        serviceOutput.append("\t\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t\t// Properties\n");
        serviceOutput.append("\t\tpublic function get serviceValidation():ServiceValidation\n");
        serviceOutput.append("\t\t{\n");
        serviceOutput.append("\t\t\treturn new ServiceValidation();\n");
        serviceOutput.append("\t\t}\n\n\n");


        serviceOutput.append("\t\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t\t// Internal Helpers\n");
        serviceOutput.append("\t\tprivate function getURLBase():String\n");
        serviceOutput.append("\t\t{\n\t\t\treturn Utils.getBaseURL();\n");
        serviceOutput.append("\t\t}\n\n");


        serviceOutput.append("\t\tprivate function buildDocRequestXML(params:String, methodName:String):XML\n");
        serviceOutput.append("\t\t{\n");
        serviceOutput.append("\t\t\tvar requestXML:String = \"<?xml version=\\\"1.0\\\" ?>\";\n\n");
        serviceOutput.append("\t\t\trequestXML += \"<S:Envelope xmlns:S=\\\"http://schemas.xmlsoap.org/soap/envelope/\\\">\";\n");
        serviceOutput.append("\t\t\trequestXML += \"<S:Body>\";\n");
        serviceOutput.append("\t\t\trequestXML += params;\n");
        serviceOutput.append("\t\t\trequestXML += \"</S:Body>\";\n");
        serviceOutput.append("\t\t\trequestXML += \"</S:Envelope>\";\n\n");
        serviceOutput.append("\t\t\treturn new XML(requestXML);\n");
        serviceOutput.append("\t\t}\n\n\n");

        serviceOutput.append("\t\t////////////////////////////////////////////////////////////\n");
        serviceOutput.append("\t\t// Service Operations and Call Backs\n");
        serviceOutput.append("%1");
        serviceOutput.append("\t}" + "\n");
        serviceOutput.append("}" + "\n");
    }

    private String fixVariableName(final String name) {
        if ("type".equals(name)) //we need this because return classes inherit from event
            return "typex";
        return name;
    }

    private void makeUsedClasses(final URL schemaURL, final String destDir, String schemaPath) {

        standardTypes.add("String");
        standardTypes.add("string");
        standardTypes.add("boolean");
        standardTypes.add("Boolean");
        standardTypes.add("int");
        standardTypes.add("Int");
        standardTypes.add("Long");
        standardTypes.add("long");
        standardTypes.add("Number");
        standardTypes.add("number");
        standardTypes.add("Double");
        standardTypes.add("double");
        standardTypes.add("Float");
        standardTypes.add("float");
        standardTypes.add("char");
        standardTypes.add("Char");
        standardTypes.add("byte");
        standardTypes.add("Byte");
        standardTypes.add("short");
        standardTypes.add("Short");

        try {

            File temp = File.createTempFile("schema", ".");

            FileWriter fwx = new FileWriter(temp);

            BufferedReader br;
            if (!urlIsLocal)
                br = new BufferedReader(new InputStreamReader(schemaURL.openStream()));
            else
                br = new BufferedReader(new FileReader(schemaPath));

            String line;

            while ((line = br.readLine()) != null) {
                line = line.replaceAll("xs:", "");

                fwx.write(line);
            }
            fwx.flush();
            fwx.close();

            br.close();

            Document d = DOMUtils.createDocument(temp);

            final HashSet<String> used = new HashSet<String>();

            //The package name for each class is the schema target namespace backwards

            //<schema elementFormDefault="qualified" targetNamespace="http://login.services.arahant.com"

            final NodeList schemas = DOMUtils.getNodes(d, "//schema");
            final HashSet<String> imported = new HashSet<String>();

            for (int sloop = 0; sloop < schemas.getLength(); sloop++) {

                final Node schema = d;//schemas.item(sloop);


                final NodeList specTypes = DOMUtils.getNodes(schema, "//complexType[@name]");
                for (int loop = 0; loop < specTypes.getLength(); loop++) {
                    String pType = convertXMLTypeToActionScript(DOMUtils.getAttribute((Element) specTypes.item(loop), "type"));
                    String cType = DOMUtils.getAttribute((Element) specTypes.item(loop), "name");
                    int loopCounter = 0;
                    int itemCounter = 0;
                    cType = removeColon(cType);
                    if (cType.endsWith("Response"))
                        continue;

                    imported.clear();

                    //force non-import of built in classes
                    imported.addAll(standardTypes);

                    if (pType.equals("XMLNode") && !used.contains(cType)) {
                        if (!cType.startsWith("Array"))
                            serviceImports += "\timport " + packName + "." + upcase(cType) + ";" + "\n";

                        //I have an array, so I need to generate an object for the elements of it
                        final String nam2 = cType;

                        if (isArray((Element) specTypes.item(loop)))
                            continue;

                        final String dirPath = makeSureDirectoryExists(destDir, packName);
                        final BufferedWriter fw = new BufferedWriter(new FileWriter(dirPath + "/" + upcase(nam2) + ".as"));
                        final String className2 = nam2.substring(0, 1).toUpperCase() + nam2.substring(1);
                        String imports = "\timport com.arahant.app.common.Utils;\n";
                        String body;
                        String constructorBody = "";
                        boolean inherited = false;

                        final Node n = DOMUtils.getNode((Element) specTypes.item(loop), "complexContent/extension");
                        if (n != null) {
                            String base = DOMUtils.getAttribute((Element) n, "base");
                            final String namspc = preColon(base);
                            base = removeColon(base);

//							String bpackn = "http://operations.arahant.com";
//							bpackn = fixNamespace(bpackn);
                            if (!imported.contains(base))
                                imports += "\timport " + packName + "." + upcase(base) + ";\n";
                            imported.add(base);
                            body = "\tpublic class " + className2 + " extends " + upcase(base) + "\n";
                            inherited = true;
                        } else if (className2.equals("TransmitReturnBase")) {
                            imports += "\timport flash.events.Event;\n";
                            body = "\tpublic class " + className2 + " extends Event\n";
                            inherited = false;
                        } else
                            body = "\tpublic class " + className2 + "\n";

                        body += "\t{\n";
                        body += "\t\t////////////////////////////////////////////////////////////\n";
                        body += "\t\t// Instance Vars\n";

                        final NodeList nodeList2 = DOMUtils.getNodes(d, "//complexType[@name=\"" + nam2 + "\"]//element");
                        final List<Node> nl2 = new LinkedList<Node>();

                        for (int nl2Loop = 0; nl2Loop < nodeList2.getLength(); nl2Loop++)
                            nl2.add(nodeList2.item(nl2Loop));
                        for (int loop2 = 0; loop2 < nl2.size(); loop2++) {
                            final Node n2 = nl2.get(loop2);

                            String sName = DOMUtils.getAttribute((Element) n2, "name");
                            String typ = DOMUtils.getAttribute((Element) n2, "type");
                            pType = convertXMLTypeToActionScript(typ);
                            typ = upcase(removeColon(typ));
                            if (typ.endsWith("Response"))
                                continue;


                            if (isArray(n2) && !imported.contains(typ)) {
                                if (typ.startsWith("ArrayOf"))
                                    typ = typ.substring(7);

                                if (typ.indexOf('_') != -1)
                                    typ = typ.substring(typ.lastIndexOf('_') + 1);
                                if (!imported.contains(typ))
                                    imports += "\timport " + packName + "." + upcase(typ) + ";\n";
                                imported.add(typ);
                                body += "\t\tpublic var " + sName + "Array:Array;\n";
                                constructorBody += "\t\t\tthis." + sName + "Array = [];\n";
                            } else if (isArray(n2)) {

                                body += "\t\tpublic var " + sName + "Array:Array;\n";
                                constructorBody += "\t\t\tthis." + sName + "Array = [];\n";
                            } else if (pType.equals("XMLNode")) {
                                if (!isArray(n2) && !imported.contains(typ)) {
                                    imported.add(typ);
                                    imports += "\timport " + packName + "." + upcase(typ) + ";\n";
                                }
                                body += "\t\tpublic var " + fixVariableName(sName) + ":" + typ + ";\n";
                                constructorBody += "\t\t\tthis." + fixVariableName(sName) + " = null;\n";
                            } else {
                                String val = "";
                                if (pType.equals("String"))
                                    val = " = \"\"";
                                if (pType.equals("Number"))
                                    val = " = 0";
                                if (pType.equals("Boolean"))
                                    val = " = false";
                                body += "\t\tpublic var " + fixVariableName(sName) + ":" + pType + ";\n";
                                constructorBody += "\t\t\tthis." + fixVariableName(sName) + val + ";\n";
                            }
                        }

                        body += "\n\n";
                        body += "\t\t////////////////////////////////////////////////////////////\n";
                        body += "\t\t// Constructor\n";
                        if (className2.equals("TransmitReturnBase") || className2.endsWith("Return")) {
                            body += "\t\tpublic function " + className2 + "(eventType:String)\n";
                            body += "\t\t{\n";
                            body += "\t\t\tsuper(eventType);\n";
                            body += constructorBody;
                            body += "\t\t}\n\n\n";
                        } else {

                            body += "\t\tpublic function " + className2 + "()\n";
                            body += "\t\t{\n";
                            body += constructorBody;
                            body += "\t\t}\n\n\n";
                        }

                        body += "\t\t////////////////////////////////////////////////////////////\n";
                        body += "\t\t// Public Methods\n";
                        body += "\t\t" + ((inherited) ? "override " : "") + "public function toXMLString():String\n";
                        body += "\t\t{\n";
                        body += "\t\t\tvar loop" + (++loopCounter) + ":int = 0;\n";
                        if (inherited)
                            body += "\t\t\tvar ret:String = super.toXMLString();\n";
                        else
                            body += "\t\t\tvar ret:String = \"\";\n";

                        for (int loop2 = 0; loop2 < nl2.size(); loop2++) {
                            final Node n2 = nl2.get(loop2);

                            final String subName = DOMUtils.getAttribute((Element) n2, "name");
                            String typ = DOMUtils.getAttribute((Element) n2, "type");
                            String origType = typ;
                            typ = upcase(removeColon(typ));
                            if (typ.length() == 0)
                                continue;

                            body += "\n";

                            if (isArray(n2))
                                if (origType.equals("string")) {
                                    body += "\t\t\tfor (loop" + loopCounter + " = 0; loop" + loopCounter + " < " + subName + "Array.length; loop" + loopCounter + "++)\n";
                                    //body += "\t\t\t{\n";
                                    body += "\t\t\t\tret += \"<" + subName + ">\" + " + subName + "Array[loop" + loopCounter + "] + \"</" + subName + ">\";\n";
                                    //body += "\t\t\t}\n";
                                } else if (origType.equals("int")) {
                                    body += "\t\t\tfor (loop" + loopCounter + " = 0; loop" + loopCounter + " < " + subName + "Array.length; loop" + loopCounter + "++)\n";
                                    //body += "\t\t\t{\n";
                                    body += "\t\t\t\tret += \"<" + subName + ">\" + " + subName + "Array[loop" + loopCounter + "] + \"</" + subName + ">\";\n";
                                    //body += "\t\t\t}\n";
                                } else {
                                    body += "\t\t\tfor (loop" + loopCounter + " = 0; loop" + loopCounter + " < " + subName + "Array.length; loop" + loopCounter + "++)\n";
                                    //body += "\t\t\t{\n";
                                    body += "\t\t\t\tret += \"<" + subName + ">\" + " + subName + "Array[loop" + loopCounter + "].toXMLString() + \"</" + subName + ">\";\n";
                                    //body += "\t\t\t}\n";
                                }
                            else
                                //TODO: short term fix, relying on name length - should go through and check types instead
                                if (typ.length() > 9)
                                    body += "\t\t\tret += \"<" + subName + ">\" + ((" + fixVariableName(subName) + " == null) ? \"\" : " + fixVariableName(subName) + ".toXMLString()) + \"</" + subName + ">\";\n";
                                else
                                    body += "\t\t\tret += \"<" + subName + ">\" + Utils.escapeXML(" + fixVariableName(subName) + ") + \"</" + subName + ">\";\n";
                        }

                        body += "\n";
                        body += "\t\t\treturn ret;\n";
                        body += "\t\t}\n\n\n";


                        body += "\t\t////////////////////////////////////////////////////////////\n";
                        body += "\t\t// Internal Helpers\n";
                        body += ((inherited) ? "\t\toverride " : "\t\t") + "internal function populate(data:XML):void\n";
                        body += "\t\t{\n";
                        if (inherited)
                            body += "\t\t\tsuper.populate(data);\n";


                        boolean first = true;
                        for (int loop2 = 0; loop2 < nl2.size(); loop2++) {
                            final Node n2 = nl2.get(loop2);

                            if (first) {
                                first = false;

                                if (inherited)
                                    body += "\n";
                            } else
                                body += "\n";

                            String subName = DOMUtils.getAttribute((Element) n2, "name");
                            String typ = DOMUtils.getAttribute((Element) n2, "type");
                            String origType = typ;
                            typ = upcase(removeColon(typ));

                            if (isArray(n2)) {
                                String pathSubName = subName;

                                subName = subName + "Array";

                                if (inherited || className2.equals("TransmitReturnBase"))
                                    body += "\t\t\tvar " + subName + "Data:XMLList = data.*.*.*.*::" + pathSubName + ";\n";
                                else
                                    body += "\t\t\tvar " + subName + "Data:XMLList = data.*::" + pathSubName + ";\n";
                                body += "\t\t\tvar loop" + (++loopCounter) + ":int = 0;\n";
                                body += "\t\t\tfor each (var item" + (++itemCounter) + ":XML in " + subName + "Data)\n";
                                if (origType.equals("string"))
                                    body += "\t\t\t\tthis." + subName + "[loop" + loopCounter + "++] = item" + itemCounter + ";\n";
                                else if (origType.equals("int") || origType.equals("integer"))
                                    body += "\t\t\t\tthis." + subName + "[loop" + loopCounter + "++] = item" + itemCounter + ";\n";
                                else {
                                    body += "\t\t\t{\n";
                                    body += "\t\t\t\tthis." + subName + "[loop" + loopCounter + "] = new " + typ + "();\n";
                                    body += "\t\t\t\tthis." + subName + "[loop" + loopCounter + "++].populate(item" + itemCounter + ");\n";
                                    body += "\t\t\t}\n";
                                }

                            } else if (inherited || className2.equals("TransmitReturnBase"))
                                if (origType.equals("boolean"))
                                    body += "\t\t\tthis." + fixVariableName(subName) + " = data.*.*.*.*::" + subName + " == \"true\";\n";
                                else if (standardTypes.contains(typ))
                                    body += "\t\t\tthis." + fixVariableName(subName) + " = data.*.*.*.*::" + subName + ";\n";
                                else {
                                    body += "\t\t\tvar xml" + fixVariableName(subName) + ":XML = (data.*.*.*.*::" + subName + ")[0];\n";
                                    body += "\t\t\tif (xml" + fixVariableName(subName) + " != null)\n";
                                    body += "\t\t\t{\n";
                                    body += "\t\t\t\tthis." + fixVariableName(subName) + " = new " + typ + "();\n";
                                    body += "\t\t\t\tthis." + fixVariableName(subName) + ".populate(xml" + fixVariableName(subName) + ");";
                                    body += "\t\t\t}\n";

                                }
                            else if (origType.equals("boolean"))
                                body += "\t\t\tthis." + fixVariableName(subName) + " = data.*::" + subName + " == \"true\";\n";
                            else if (standardTypes.contains(typ))
                                body += "\t\t\tthis." + fixVariableName(subName) + " = data.*::" + subName + ";\n";
                            else {
                                body += "\t\t\tvar xml" + fixVariableName(subName) + ":XML = (data.*.*.*.*::" + subName + ")[0];\n";
                                body += "\t\t\tif (xml" + fixVariableName(subName) + " != null)\n";
                                body += "\t\t\t{\n";
                                body += "\t\t\t\tthis." + fixVariableName(subName) + " = new " + typ + "();\n";
                                body += "\t\t\t\tthis." + fixVariableName(subName) + ".populate(xml" + fixVariableName(subName) + ");\n";
                                body += "\t\t\t}\n";
                            }
                        }

                        body += "\t\t}\n";

                        body += "\t}\n";
                        body += "}\n";

                        fw.write("package " + packName + "\n");
                        fw.write("{\n");
                        fw.write("\t////////////////////////////////////////////////////////////\n");
                        fw.write("\t// Imports\n");
                        fw.write(imports);
                        fw.write("\n\n");
                        fw.write("\t////////////////////////////////////////////////////////////\n");
                        fw.write("\t// Class Definition\n");
                        fw.write(body);
                        fw.flush();
                        fw.close();
                    }
                    used.add(cType);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace(); //keep going
        }
    }

    private void makeServiceClassInclude(final String destDir) {
        try {
            final String dirPath = makeSureDirectoryExists(destDir, packName);
            final BufferedWriter fw = new BufferedWriter(new FileWriter(dirPath + "/ServiceClassRef.as"));

            fw.write("////////////////////////////////////////////////////////////\n");
            fw.write("// Imports\n");
            fw.write("import " + packName + ".Service;\n\n\n");
            fw.write("////////////////////////////////////////////////////////////\n");
            fw.write("// This reference is created to guarantee the generated Service class is included in the output SWF.\n");
            fw.write("// The MXML screen and pop-up files should include this file.\n");
            fw.write("private var _serviceClassRef:Service;\n");
            fw.flush();
            fw.close();
        } catch (final Exception e) {
            e.printStackTrace(); //keep going
        }
    }

    private String upcase(String x) {
        if (x.length() < 2)
            return x.toUpperCase();
        return x.substring(0, 1).toUpperCase() + x.substring(1);
    }

    /**
     * @param destDir
     * @param packName
     */
    private String makeSureDirectoryExists(final String destDir, final String packName) {

        //make sure directory exists
        final StringTokenizer stok = new StringTokenizer(packName, ".");

        File curDir = new File(destDir);

        while (stok.hasMoreTokens()) {

            final String directory = stok.nextToken();

            final File newDir = new File(curDir, directory);

            if (!newDir.exists())
                newDir.mkdir();

            curDir = newDir;
        }
        return curDir.getAbsolutePath();
    }

    private String fixNamespace(String ns) {
        //	http://login.services.arahant.com
        //first rip off the http stuff
        ns = ns.substring("http://".length());

        String ret = "";

        //now parse the package
        final StringTokenizer stok = new StringTokenizer(ns, ".");
        while (stok.hasMoreTokens())
            ret = stok.nextToken() + "." + ret;
        return ret;
    }

    private String removeColon(String typ) {
        if (!typ.contains(":"))
            return typ;
        typ = typ.substring(typ.indexOf(":") + 1);
        return typ;
    }

    private String preColon(final String typ) {
        return typ.substring(0, typ.indexOf(":"));
    }

    private String convertXMLTypeToActionScript(String xmlType) {
        xmlType = DOMUtils.getNCName(xmlType).toLowerCase();

        if (xmlType.equalsIgnoreCase("string"))
            return "String";

        if (xmlType.equalsIgnoreCase("boolean"))
            return "Boolean";

        if (xmlType.equalsIgnoreCase("date"))
            return "Date";

        if (xmlType.equalsIgnoreCase("datetime"))
            return "Date";

        if (xmlType.equalsIgnoreCase("decimal"))
            return "Number";

        if (xmlType.equalsIgnoreCase("double"))
            return "Number";

        if (xmlType.equalsIgnoreCase("byte"))
            return "Number";

        if (xmlType.equalsIgnoreCase("float"))
            return "Number";

        if (xmlType.equalsIgnoreCase("int"))
            return "Number";

        if (xmlType.contains("integer"))
            return "Number";

        if (xmlType.equalsIgnoreCase("long"))
            return "Number";

        if (xmlType.equalsIgnoreCase("short"))
            return "Number";

        if (xmlType.contains("unsigned"))
            return "Number";

        return "XMLNode";
    }

    private void generateService(URL wsdlUrl, URL schemaUrl, String wsdlPath, String schemaPath) throws Throwable {
        String results = "";
        BufferedReader br;

        if (!urlIsLocal) {
            System.out.println("Trying to open wsdl '" + wsdlUrl.toString() + "'");
            br = new BufferedReader(new InputStreamReader(wsdlUrl.openStream()));
        } else {
            System.out.println("Trying to open wsdl file '" + wsdlPath + "'");
            br = new BufferedReader(new BufferedReader(new FileReader(wsdlPath)));
        }
        String inputLine;

        while ((inputLine = br.readLine()) != null)
            results += inputLine + "\n";
        br.close();

        final Element definitionsNode = DOMUtils.createDocument(results).getDocumentElement();
        final WebServiceDefinition def = new DOMReader().load(definitionsNode);

        String code;
        String fileName = outputDir.getAbsolutePath();

        String dirName = makeSureDirectoryExists(fileName, this.packName);

        File outDir = new File(dirName);

        File[] f = outDir.listFiles();

        for (int fl = 0; fl < f.length; fl++)
            if (f[fl].isFile() && f[fl].getName().endsWith(".as") && !f[fl].getName().equals("ServiceValidation.as"))
                f[fl].delete();

        code = buildActionScriptClass(def, this.packName, outputDir.getAbsolutePath(), schemaUrl, schemaPath);

        fileName = dirName + "/Service.as";

        final File outputFile = new File(fileName);

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(code);
            writer.close();
        } catch (final Exception e) {
            reportStatus("Error writing output file.");
            e.printStackTrace();
        }

        reportStatus("File " + outputFile.getAbsolutePath() + " for Service " + className + " successfully generated.");
    }

    private static class ColData {

        public String name;
        public int dataType;
        public int columnSize;
        public int digits;

        @Override
        public String toString() {
            return name;

        }
    }
}
