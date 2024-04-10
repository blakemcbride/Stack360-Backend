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

import java.io.*;
import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arahant.utils.DOMUtils;
import com.arahant.utils.generators.flexWSClient.wsdl.*;
import org.w3c.dom.Document;

public class ArahantWebServiceClientGenerator {

    private String className;
    private String packName;
    private File outputDir;

    public ArahantWebServiceClientGenerator(final String[] args) {
    }

    public static void main(final String args[]) {
        try {
            if (args.length == 0) {
                new ArahantWebServiceClientGenerator(args).exec("OrgGroupProjectListOps",
                        "com.arahant.services.standard.project.orgGroupProjectList",
                        "StandardProjectOrgGroupProjectList",
                        "build/generated/wsgen/service/",
                        "/Users/Arahant/Prophet/Frontend_Flex");
            } else {
                new ArahantWebServiceClientGenerator(args).exec(args[0], args[1], args[2], args[3], args[4]);
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String upperCase(String x) {
        return x.substring(0, 1).toUpperCase() + x.substring(1);
    }

    public static void generate(String type, String module, String url, String folder) {
        //change screen name to ops class
        String op = upperCase(folder) + "Ops";
        String packName = "com.arahant.services." + module + "." + folder;
        String packUrl = upperCase(type) + upperCase(module) + upperCase(folder);


        //URL - http://prophet.arahant.com:80/arahant/StandardTimeTimesheetReviewOps?wsdl

        //need to query myself / packUrl+"Ops?wsdl"


        new ArahantWebServiceClientGenerator(null).exec(op, packName, packUrl, "", "./");

    }

    private void exec(final String op, String packageName, final String packUrl, String wsdlDir, String frontendDir) {
        try {
            packageName = packageName.replaceFirst("services", "app.screen");

            if (packageName.equals("com.arahant.app.screen.main")) {
                packageName = "com.arahant.app.main";
            }

            if (op.toUpperCase().contains("AQDEV")) {
                return;
            }

            final Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("ServiceScreenMap.properties"));
            } catch (final Exception e) {

            }

            final String screenName = op;

            String unitName = screenName.substring(0, 1).toLowerCase() + screenName.substring(1, screenName.length() - 3);

            unitName = props.getProperty(unitName, unitName);

            if (unitName.startsWith("hR")) {
                unitName = "hr" + unitName.substring(2);
            }

            if (unitName.equals("peachtreeApp")) {
                return;
            }
            if (unitName.equals("scanner")) {
                return;
            }

            packName = packageName + ".service";

            if (!frontendDir.endsWith("/") && !frontendDir.endsWith("\\"))
                frontendDir += "/";


            outputDir = new File(frontendDir + "src/");
            if (!outputDir.exists() || !outputDir.isDirectory()) {
                throw new RuntimeException(outputDir + " directory doesn't exist.");
            }


            className = op + "Service";

            serviceName = packUrl + "Ops";
            schemaName = wsdlDir + packUrl + "Ops" + "_schema1.xsd";
            generateService(wsdlDir + packUrl + "Ops" + ".wsdl");

        } catch (final Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private String serviceName, schemaName;

    private void reportStatus(final String msg) {
        System.out.println(msg);

    }

    private String buildActionScriptClass(final WebServiceDefinition def, final String packageName, final String serviceName, final String destDir) {
        final Service svc = def.getService(this.serviceName);
        if (svc == null) {
            reportStatus("Invalid service name " + this.serviceName);
        }

        makeUsedClasses(schemaName, destDir);

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

            if (isFirst) {
                isFirst = false;
            } else {
                asyncMethodBuffer.append("\n");
            }

            // write out event constant
            asyncMethodBuffer.append("\t\tpublic static const EVENT_" + methodName.toUpperCase() + "_COMPLETE:String = \"" + methodName + "Complete\";\n\n");


            // write out service operation
            asyncMethodBuffer.append("\t\tpublic function " + methodName + "(inParam:" + inputType + "):void\n");
            asyncMethodBuffer.append("\t\t{\n");

            asyncMethodBuffer.append("\t\t\tvar reqHeaders:Array = [];\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"SOAPAction\", \"\"));\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"Content-Type\", \"text/xml;charset=\\\\\"utf-8\\\\\"\"));\n");
            asyncMethodBuffer.append("\t\t\treqHeaders.push(new URLRequestHeader(\"Cache-control\", \"private\"));\n\n");

            asyncMethodBuffer.append("\t\t\tvar domcode:String = \"<ns2:" + methodName + " xmlns:ns2=\\\\\"http://operations.arahant.com\\\\\" ><arg0>\"\n");
            asyncMethodBuffer.append("\t\t\t\t+ inParam.toXMLString()\n");
            asyncMethodBuffer.append("\t\t\t\t+ \"</arg0></ns2:" + methodName + ">\";\n");
            asyncMethodBuffer.append("\t\t\tvar req:XML = this.buildDocRequestXML(domcode, \"" + methodName + "\");\n\n");

            asyncMethodBuffer.append("\t\t\tvar myXMLURL:URLRequest = new URLRequest(this.getURLBase() + \"" + this.serviceName + "\");\n");
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

    private String serviceImports = "";

    private String fixVariableName(final String name) {
        if ("type".equals(name)) //we need this because return classes inherit from event
        {
            return "typex";
        }
        return name;
    }

    private void makeUsedClasses(final String schemaName, final String destDir) {
        try {

            File temp = File.createTempFile("schema", ".");

            FileWriter fwx = new FileWriter(temp);

            BufferedReader br = new BufferedReader(new FileReader(schemaName));

            while (br.ready()) {
                String line = br.readLine();

                line = line.replaceAll("xs:", "");

                fwx.write(line);
            }
            fwx.flush();
            fwx.close();

            Document d = DOMUtils.createDocument(temp);

            final HashSet<String> used = new HashSet<>();

            //The package name for each class is the schema target namespace backwards

            //<schema elementFormDefault="qualified" targetNamespace="http://login.services.arahant.com"

            final NodeList schemas = DOMUtils.getNodes(d, "//schema");

            for (int sloop = 0; sloop < schemas.getLength(); sloop++) {

                final Node schema = d;//schemas.item(sloop);


                final NodeList specTypes = DOMUtils.getNodes(schema, "//complexType[@name]");
                for (int loop = 0; loop < specTypes.getLength(); loop++) {
                    String pType = convertXMLTypeToActionScript(DOMUtils.getAttribute((Element) specTypes.item(loop),
                            "type"));
                    String cType = DOMUtils.getAttribute((Element) specTypes.item(loop), "name");
                    int loopCounter = 0;
                    int itemCounter = 0;
                    cType = removeColon(cType);
                    if (cType.endsWith("Response")) {
                        continue;
                    }


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
                    standardTypes.add("Number");

                    //force non-import of built in classes
                    final HashSet<String> imported = new HashSet<>(standardTypes);

                    if (pType.equals("XMLNode") && !used.contains(cType)) {
                        if (!cType.startsWith("Array")) {
                            serviceImports += "\timport " + packName + "." + upcase(cType) + ";" + "\n";
                        }

                        //I have an array, so I need to generate an object for the elements of it
                        final String nam2 = cType;

                        if (isArray(specTypes.item(loop)))
                            continue;

                        final String dirPath = makeSureDirectoryExists(destDir, packName);

                        final BufferedWriter fw = new BufferedWriter(new FileWriter(dirPath + "/" + upcase(nam2) + ".as"));
                        final String className = nam2.substring(0, 1).toUpperCase() + nam2.substring(1);
                        StringBuilder imports = new StringBuilder("\timport com.arahant.app.common.Utils;\n");
                        StringBuilder body;
                        StringBuilder constructorBody = new StringBuilder();
                        boolean inherited = false;

                        final Node n = DOMUtils.getNode(specTypes.item(loop), "complexContent/extension");
                        if (n != null) {
                            String base = DOMUtils.getAttribute((Element) n, "base");
                            base = removeColon(base);

                            if (!imported.contains(base))
                                imports.append("\timport ").append(packName).append(".").append(upcase(base)).append(";\n");
                            imported.add(base);
                            body = new StringBuilder("\tpublic class " + className + " extends " + upcase(base) + "\n");
                            inherited = true;
                        } else if (className.equals("TransmitReturnBase")) {
                            imports.append("\timport flash.events.Event;\n");
                            body = new StringBuilder("\tpublic class " + className + " extends Event\n");
                            inherited = false;
                        } else
                            body = new StringBuilder("\tpublic class " + className + "\n");


                        body.append("\t{\n");
                        body.append("\t\t////////////////////////////////////////////////////////////\n");
                        body.append("\t\t// Instance Vars\n");

                        final NodeList nodeList2 = DOMUtils.getNodes(d, "//complexType[@name=\"" + nam2 + "\"]//element");
                        final List<Node> nl2 = new LinkedList<>();

                        for (int nl2Loop = 0; nl2Loop < nodeList2.getLength(); nl2Loop++)
                            nl2.add(nodeList2.item(nl2Loop));
                        for (final Node n2 : nl2) {
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
                                    imports.append("\timport ").append(packName).append(".").append(upcase(typ)).append(";\n");
                                imported.add(typ);
                                body.append("\t\tpublic var ").append(sName).append("Array:Array;\n");
                                constructorBody.append("\t\t\tthis.").append(sName).append("Array = [];\n");
                            } else if (isArray(n2)) {

                                body.append("\t\tpublic var ").append(sName).append("Array:Array;\n");
                                constructorBody.append("\t\t\tthis.").append(sName).append("Array = [];\n");
                            } else if (pType.equals("XMLNode")) {
                                if (!isArray(n2) && !imported.contains(typ)) {
                                    imported.add(typ);
                                    imports.append("\timport ").append(packName).append(".").append(upcase(typ)).append(";\n");
                                }
                                body.append("\t\tpublic var ").append(fixVariableName(sName)).append(":").append(typ).append(";\n");
                                constructorBody.append("\t\t\tthis.").append(fixVariableName(sName)).append(" = null;\n");
                            } else {
                                String val = "";
                                if (pType.equals("String")) {
                                    val = " = \"\"";
                                }
                                if (pType.equals("Number")) {
                                    val = " = 0";
                                }
                                if (pType.equals("Boolean")) {
                                    val = " = false";
                                }
                                body.append("\t\tpublic var ").append(fixVariableName(sName)).append(":").append(pType).append(";\n");
                                constructorBody.append("\t\t\tthis.").append(fixVariableName(sName)).append(val).append(";\n");
                            }
                        }

                        body.append("\n\n");
                        body.append("\t\t////////////////////////////////////////////////////////////\n");
                        body.append("\t\t// Constructor\n");
                        if (className.equals("TransmitReturnBase") || className.endsWith("Return")) {
                            body.append("\t\tpublic function ").append(className).append("(eventType:String)\n");
                            body.append("\t\t{\n");
                            body.append("\t\t\tsuper(eventType);\n");
                            body.append(constructorBody);
                            body.append("\t\t}\n\n\n");
                        } else {

                            body.append("\t\tpublic function ").append(className).append("()\n");
                            body.append("\t\t{\n");
                            body.append(constructorBody);
                            body.append("\t\t}\n\n\n");
                        }

                        body.append("\t\t////////////////////////////////////////////////////////////\n");
                        body.append("\t\t// Public Methods\n");
                        body.append("\t\t").append((inherited) ? "override " : "").append("public function toXMLString():String\n");
                        body.append("\t\t{\n");
                        body.append("\t\t\tvar loop").append(++loopCounter).append(":int = 0;\n");
                        if (inherited) {
                            body.append("\t\t\tvar ret:String = super.toXMLString();\n");
                        } else {
                            body.append("\t\t\tvar ret:String = \"\";\n");
                        }

                        for (final Node n2 : nl2) {
                            final String subName = DOMUtils.getAttribute((Element) n2, "name");
                            String typ = DOMUtils.getAttribute((Element) n2, "type");
                            typ = upcase(removeColon(typ));
                            if (typ.length() == 0) {
                                continue;
                            }

                            body.append("\n");

                            if (isArray(n2)) {
                                if (typ.contains("tring")) {
                                    body.append("\t\t\tfor (loop").append(loopCounter).append(" = 0; loop").append(loopCounter).append(" < ").append(subName).append("Array.length; loop").append(loopCounter).append("++)\n");
                                    //body += "\t\t\t{\n";
                                    body.append("\t\t\t\tret += \"<").append(subName).append(">\" + ").append(subName).append("Array[loop").append(loopCounter).append("] + \"</").append(subName).append(">\";\n");
                                    //body += "\t\t\t}\n";
                                } else if (typ.contains("Int")) {
                                    body.append("\t\t\tfor (loop").append(loopCounter).append(" = 0; loop").append(loopCounter).append(" < ").append(subName).append("Array.length; loop").append(loopCounter).append("++)\n");
                                    //body += "\t\t\t{\n";
                                    body.append("\t\t\t\tret += \"<").append(subName).append(">\" + ").append(subName).append("Array[loop").append(loopCounter).append("] + \"</").append(subName).append(">\";\n");
                                    //body += "\t\t\t}\n";
                                } else {
                                    body.append("\t\t\tfor (loop").append(loopCounter).append(" = 0; loop").append(loopCounter).append(" < ").append(subName).append("Array.length; loop").append(loopCounter).append("++)\n");
                                    //body += "\t\t\t{\n";
                                    body.append("\t\t\t\tret += \"<").append(subName).append(">\" + ").append(subName).append("Array[loop").append(loopCounter).append("].toXMLString() + \"</").append(subName).append(">\";\n");
                                    //body += "\t\t\t}\n";
                                }
                            } else {

                                //TODO: short term fix, relying on name length - should go through and check types instead
                                if (typ.length() > 9) {
                                    body.append("\t\t\tret += \"<").append(subName).append(">\" + ((").append(fixVariableName(subName)).append(" == null) ? \"\" : ").append(fixVariableName(subName)).append(".toXMLString()) + \"</").append(subName).append(">\";\n");
                                } else {
                                    body.append("\t\t\tret += \"<").append(subName).append(">\" + Utils.escapeXML(").append(fixVariableName(subName)).append(") + \"</").append(subName).append(">\";\n");
                                }
                            }
                        }

                        body.append("\n");
                        body.append("\t\t\treturn ret;\n");
                        body.append("\t\t}\n\n\n");


                        body.append("\t\t////////////////////////////////////////////////////////////\n");
                        body.append("\t\t// Internal Helpers\n");
                        body.append((inherited) ? "\t\toverride " : "\t\t").append("internal function populate(data:XML):void\n");
                        body.append("\t\t{\n");
                        if (inherited) {
                            body.append("\t\t\tsuper.populate(data);\n");
                        }


                        boolean first = true;
                        for (final Node n2 : nl2) {
                            if (first) {
                                first = false;

                                if (inherited)
                                    body.append("\n");
                            } else
                                body.append("\n");

                            String subName = DOMUtils.getAttribute((Element) n2, "name");
                            String typ = DOMUtils.getAttribute((Element) n2, "type");
                            typ = upcase(removeColon(typ));
                            if (isArray(n2)) {
                                String pathSubName = subName;

                                subName = subName + "Array";

                                if (inherited || className.equals("TransmitReturnBase")) {
                                    body.append("\t\t\tvar ").append(subName).append("Data:XMLList = data.*.*.*.*::").append(pathSubName).append(";\n");
                                } else {
                                    body.append("\t\t\tvar ").append(subName).append("Data:XMLList = data.*::").append(pathSubName).append(";\n");
                                }
                                body.append("\t\t\tvar loop").append(++loopCounter).append(":int = 0;\n");
                                body.append("\t\t\tfor each (var item").append(++itemCounter).append(":XML in ").append(subName).append("Data)\n");
                                if (typ.contains("tring")) {
                                    body.append("\t\t\t\tthis.").append(subName).append("[loop").append(loopCounter).append("++] = item").append(itemCounter).append(";\n");
                                } else if (typ.contains("Int")) {
                                    body.append("\t\t\t\tthis.").append(subName).append("[loop").append(loopCounter).append("++] = item").append(itemCounter).append(";\n");
                                } else {
                                    body.append("\t\t\t{\n");
                                    body.append("\t\t\t\tthis.").append(subName).append("[loop").append(loopCounter).append("] = new ").append(typ).append("();\n");
                                    body.append("\t\t\t\tthis.").append(subName).append("[loop").append(loopCounter).append("++].populate(item").append(itemCounter).append(");\n");
                                    body.append("\t\t\t}\n");
                                }

                            } else if (inherited || className.equals("TransmitReturnBase")) {
                                if (typ.contains("oolean")) {
                                    body.append("\t\t\tthis.").append(fixVariableName(subName)).append(" = data.*.*.*.*::").append(subName).append(" == \"true\";\n");
                                } else if (standardTypes.contains(typ)) {
                                    body.append("\t\t\tthis.").append(fixVariableName(subName)).append(" = data.*.*.*.*::").append(subName).append(";\n");

                                } else {
                                    body.append("\t\t\tvar xml").append(fixVariableName(subName)).append(":XML = (data.*.*.*.*::").append(subName).append(")[0];\n");
                                    body.append("\t\t\tif (xml").append(fixVariableName(subName)).append(" != null)\n");
                                    body.append("\t\t\t{\n");
                                    body.append("\t\t\t\tthis.").append(fixVariableName(subName)).append(" = new ").append(typ).append("();\n");
                                    body.append("\t\t\t\tthis.").append(fixVariableName(subName)).append(".populate(xml").append(fixVariableName(subName)).append(");");
                                    body.append("\t\t\t}\n");

                                }
                            } else if (typ.contains("oolean")) {
                                body.append("\t\t\tthis.").append(fixVariableName(subName)).append(" = data.*::").append(subName).append(" == \"true\";\n");
                            } else if (standardTypes.contains(typ)) {
                                body.append("\t\t\tthis.").append(fixVariableName(subName)).append(" = data.*::").append(subName).append(";\n");

                            } else {
                                body.append("\t\t\tvar xml").append(fixVariableName(subName)).append(":XML = (data.*.*.*.*::").append(subName).append(")[0];\n");
                                body.append("\t\t\tif (xml").append(fixVariableName(subName)).append(" != null)\n");
                                body.append("\t\t\t{\n");
                                body.append("\t\t\t\tthis.").append(fixVariableName(subName)).append(" = new ").append(typ).append("();\n");
                                body.append("\t\t\t\tthis.").append(fixVariableName(subName)).append(".populate(xml").append(fixVariableName(subName)).append(");\n");
                                body.append("\t\t\t}\n");
                            }
                        }


                        body.append("\t\t}\n");

                        body.append("\t}\n");
                        body.append("}\n");

                        fw.write("package " + packName + "\n");
                        fw.write("{\n");
                        fw.write("\t////////////////////////////////////////////////////////////\n");
                        fw.write("\t// Imports\n");
                        fw.write(imports.toString());
                        fw.write("\n\n");
                        fw.write("\t////////////////////////////////////////////////////////////\n");
                        fw.write("\t// Class Definition\n");
                        fw.write(body.toString());
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

    private HashSet<String> standardTypes = new HashSet<>();

    private String upcase(String x) {
        if (x.length() < 2) {
            return x.toUpperCase();
        }
        return x.substring(0, 1).toUpperCase() + x.substring(1);
    }

    private String makeSureDirectoryExists(final String destDir, final String packName) {

        //make sure directory exists
        final StringTokenizer stok = new StringTokenizer(packName, ".");

        File curDir = new File(destDir);

        while (stok.hasMoreTokens()) {

            final String directory = stok.nextToken();

            final File newDir = new File(curDir, directory);

            if (!newDir.exists()) {
                newDir.mkdir();
            }

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

        if (xmlType.equalsIgnoreCase("string")) {
            return "String";
        }

        if (xmlType.equalsIgnoreCase("boolean")) {
            return "Boolean";
        }

        if (xmlType.equalsIgnoreCase("date")) {
            return "Date";
        }

        if (xmlType.equalsIgnoreCase("datetime")) {
            return "Date";
        }

        if (xmlType.equalsIgnoreCase("decimal")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("double")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("byte")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("float")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("int")) {
            return "Number";
        }

        if (xmlType.contains("integer")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("long")) {
            return "Number";
        }

        if (xmlType.equalsIgnoreCase("short")) {
            return "Number";
        }

        if (xmlType.contains("unsigned")) {
            return "Number";
        }

        return "XMLNode";
    }

    private void generateService(final String wsdlFile) throws Throwable {
        StringBuilder results = new StringBuilder();

        final BufferedReader br = new BufferedReader(new FileReader(wsdlFile));

        while (br.ready())
            results.append(br.readLine()).append("\n");

        final Element definitionsNode = DOMUtils.createDocument(results.toString()).getDocumentElement();
        final WebServiceDefinition def = new DOMReader().load(definitionsNode);

        String code;
        String fileName = outputDir.getAbsolutePath();

        String dirName = makeSureDirectoryExists(fileName, this.packName);

        File outDir = new File(dirName);

        File[] f = outDir.listFiles();

        if (f != null)
            for (File aF : f)
                if (aF.isFile() && aF.getName().endsWith(".as") && !aF.getName().equals("ServiceValidation.as"))
                    aF.delete();

        code = buildActionScriptClass(def, this.packName, className, outputDir.getAbsolutePath());

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
}

	
