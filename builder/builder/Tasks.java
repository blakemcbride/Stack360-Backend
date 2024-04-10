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
 * Author: Blake McBride
 * Date: 2/16/20
 *
 * I've found that I spend more time messing with build programs (such as Maven, Gradle, and others) than
 * the underlying application I am trying to build.  They all do the normal things very, very easily.
 * But when you try to go off their beaten path it gets real difficult real fast.  Being sick and
 * tired of this, and having easily built a shell script to build what I want, I needed a more portable
 * solution.  The files in this directory are that solution.
 *
 * There are two classes as follows:
 *
 *     BuildUtils -  the generic utilities needed to build
 *     Tasks      -  the application-specific build procedures (or tasks)
 *
 *    Non-private instance methods with no parameters are considered tasks.
 */
package builder;

import static builder.BuildUtils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Tasks {

    final LocalDependencies localLibs = buildLocalDependencies();
    final String tomcatTarFile = "apache-tomcat-9.0.65.tar.gz";
    final String tomcatZipFile = "apache-tomcat-9.0.65.zip";
    final String debugPort = "9090";

    void configWayToGo() {
        final String config = "WayToGoStaffing";
        clean();
        copyTreeForce("Configurations/" + config, "src/java");
        rmTree("src/java/web");
        copyTreeForce("Configurations/" + config + "/web", "web");
        copyTreeForce("Configurations/" + config + "/keys/ADP", "build/web/WEB-INF/keys/ADP");
        touch("dist/config." + config);
    }

    void configAdmin() {
        final String config = "Admin";
        clean();
        copyTreeForce("Configurations/" + config, "src/java");
        rmTree("src/java/web");
        copyTreeForce("Configurations/" + config + "/web", "web");
        touch("dist/config." + config);
    }

    void configStack360() {
        final String config = "Stack360";
        clean();
        copyTreeForce("Configurations/" + config, "src/java");
        rmTree("src/java/web");
        copyTreeForce("Configurations/" + config + "/web", "web");
        touch("dist/config." + config);
    }

    void configDemo() {
        final String config = "Demo";
        clean();
        copyTreeForce("Configurations/" + config, "src/java");
        rmTree("src/java/web");
        copyTreeForce("Configurations/" + config + "/web", "web");
        touch("dist/config." + config);
    }

    /**
     * This builds all the backend
     */
    void build() {
        copyRegex("lib", "build/web/WEB-INF/lib", ".*\\.jar", null, false);
        copyTreeRegex("src/java", "build/web/WEB-INF/classes", null, ".*\\.java");
        buildJava("src/java", "build/web/WEB-INF/classes", localLibs, null);
        runWait(true, ".", "java -cp build/web/WEB-INF/classes com/arahant/utils/generators/wsconfiggen/WsConfigGen");
        runWait(true, ".", "java -cp build/web/WEB-INF/classes com/arahant/utils/generators/revision/RevGen noindex");
        copyTree("web", "build/web");
        if ((new File("src/java/keys").exists()))
            copyTree("src/java/keys", "build/web/WEB-INF/keys");
        copyRegex("src/java", "build/web/WEB-INF/classes", null, ".*\\.java", false);
        copy("Documentation/StandardProspectImport_Doc.pdf", "build/web/docs");
        copy("Documentation/StandardProspectImport_Template.csv", "build/web/docs");
        copyTree("bin", "build/web/bin");
        if (isWindows) {
            final String link = "tomcat/webapps/ROOT";
            rmTree(link);
            copyTree("build/web", link);
        }
    }

    /**
     * This wraps it up into a single WAR file.
     */
    void war() {
        build();
        copyTree("src/commandline", "build/web/WEB-INF/commandline");
        rmRegex("build/web/temporary", ".+");
        rmRegex("build/web/reports", ".+");
        rm("build/web/overrun.txt");
        copyForce("build/web/WEB-INF/web.xml.production", "build/web/WEB-INF/web.xml");  // disallow CORS (safe for production)
        createJar("build/web", "dist/Stack360Backend.war");
    }

    /**
     * This breaks away the libraries that rarely change for easier
     * system-to-system copies
     */
    void warWithoutLibs() {
        war();
        unJar("dist/Stack360", "dist/Stack360Backend.war");
        move("dist/Stack360/WEB-INF/lib/abcl.jar", "dist/abcl.jar");
        move("dist/Stack360/WEB-INF/lib/Kiss.jar", "dist/Kiss.jar");
        move("dist/Stack360/WEB-INF/lib/json.jar", "dist/json.jar");
        createJar("dist/Stack360/WEB-INF/lib", "dist/Stack360SharedLibs.war");
        rmTree("dist/Stack360/WEB-INF/lib");
        move("dist/abcl.jar", "dist/Stack360/WEB-INF/lib/abcl.jar");
        move("dist/Kiss.jar", "dist/Stack360/WEB-INF/lib/Kiss.jar");
        move("dist/json.jar", "dist/Stack360/WEB-INF/lib/json.jar");
        createJar("dist/Stack360", "dist/Stack360WithoutLibs.war");
        rmTree("dist/Stack360");
    }

    void clean() {
        rmTree("src/java/timedTasks");
        rmTree("build");
        rmTree("web/com");
        rmTree("web/iVo");
        rmTree("web/site");
        rmTree("web/videos");
        rmRegex("web", ".*\\.png");
        rm("web/Arrow.png");
        rm("web/report.xml");
        rm("web/WebXml.txt");
        rm("web/WEB-INF/sun-jaxws.xml");
        rm("web/WEB-INF/web.xml");
        rmTree("dist");
        rmTree("tomcat");
    }

    void realclean() {
        clean();
        rmRegex("builder/builder", ".*\\.class");
    }
    
    /**
     * This task runs the back-end only.  The front-end must be started independently.
     * It assumes the system is built.
     * 
     * @throws IOException 
     */
    void run() throws IOException {
        setupTomcat();
        copyForce("build/web/WEB-INF/web.xml.development", "build/web/WEB-INF/web.xml");  // allow CORS (unsafe for production!)
        if (isWindows) {
            copyForce("build/web/WEB-INF/web.xml.development", "tomcat/webapps/ROOT/WEB-INF/web.xml");  // allow CORS (unsafe for production!)
            runWait(true, "tomcat\\bin\\debug.cmd");
        } else
            runWait(true, "tomcat/bin/debug");
 
        println("Server log can be viewed at " + cwd() + "/tomcat/logs/catalina.out");
        println("The back-end can be debugged at port " + debugPort);
        println("The front-end must be started independently.");
    }

    void stop() {
        println("shutting down tomcat");
        if (isWindows)
            runWait(true, "tomcat\\bin\\stopdebug.cmd");
        else
            runWait(true, "tomcat/bin/shutdown.sh");
    }

    void setupTomcat() throws IOException {
        if (!exists("tomcat/bin/startup.sh")) {
            if (!isWindows) {
                download(tomcatTarFile, ".", "https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.65/bin/apache-tomcat-9.0.65.tar.gz");
                gunzip(tomcatTarFile, "tomcat", 1);
                final String link = "tomcat/webapps/ROOT";
                rmTree(link);
                Files.createSymbolicLink(Paths.get("", link), Paths.get("", "../../build/web"));
            } else {
                download(tomcatZipFile, ".", "https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.65/bin/apache-tomcat-9.0.65.zip");
                runWait(true, "bin\\unzip.exe -qq -o " + tomcatZipFile);
                File tf = new File(tomcatZipFile.substring(0, tomcatZipFile.length() - 4));
                tf.renameTo(new File("tomcat"));
            }
        }
        if (isWindows) {
            rm("tomcat\\conf\\tomcat-users.xml");
            // The following is needed by NetBeans
            writeToFile("tomcat\\conf\\tomcat-users.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<tomcat-users xmlns=\"http://tomcat.apache.org/xml\"\n"
                    + "              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "              xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\"\n"
                    + "              version=\"1.0\">\n"
                    + "  <user username=\"admin\" password=\"admin\" roles=\"tomcat,manager-script\" />\n"
                    + "</tomcat-users>\n");
            writeToFile("tomcat\\bin\\debug.cmd", "@echo off\n"
                    + "cd " + getcwd() + "\\tomcat\\bin\n"
                    + "set JAVA_HOME=" + getJavaPathOnWindows() + "\n"
                    + "set CATALINA_HOME=" + getTomcatPath() + "\n"
                    + "set JPDA_ADDRESS=" + debugPort +"\n"
                    + "set JPDA_TRANSPORT=dt_socket\n"
                    + "catalina.bat jpda start\n");
            writeToFile("tomcat\\bin\\stopdebug.cmd", "@echo off\n"
                    + "cd " + getcwd() + "\\tomcat\\bin\n"
                    + "set JAVA_HOME=" + getJavaPathOnWindows() + "\n"
                    + "set CATALINA_HOME=" + getTomcatPath() + "\n"
                    + "shutdown.bat\n");
        } else {
            rm("tomcat/conf/tomcat-users.xml");
            // The following is needed by NetBeans
            writeToFile("tomcat/conf/tomcat-users.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<tomcat-users xmlns=\"http://tomcat.apache.org/xml\"\n"
                    + "              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "              xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\"\n"
                    + "              version=\"1.0\">\n"
                    + "  <user username=\"admin\" password=\"admin\" roles=\"tomcat,manager-script\" />\n"
                    + "</tomcat-users>\n");
            writeToFile("tomcat/bin/debug", "#\n"
                    + "cd " + getcwd() + "/tomcat/bin\n"
                    + "export JPDA_ADDRESS=" + debugPort + "\n"
                    + "export JPDA_TRANSPORT=dt_socket\n"
                    + "./catalina.sh jpda start\n");
            makeExecutable("tomcat/bin/debug");
        }
    }
    
    private LocalDependencies buildLocalDependencies() {
        final LocalDependencies dep = new LocalDependencies();
        dep.add("build/web/WEB-INF/lib/abcl.jar");
        dep.add("build/web/WEB-INF/lib/ant-1.10.12.jar");
        dep.add("build/web/WEB-INF/lib/antlr-2.7.7.jar");
        dep.add("build/web/WEB-INF/lib/asm-4.0.jar");
        dep.add("build/web/WEB-INF/lib/asm-analysis-4.0.jar");
        dep.add("build/web/WEB-INF/lib/asm-commons-4.0.jar");
        dep.add("build/web/WEB-INF/lib/asm-tree-4.0.jar");
        dep.add("build/web/WEB-INF/lib/asm-util-4.0.jar");
        dep.add("build/web/WEB-INF/lib/bcpg-jdk15-145.jar");
        dep.add("build/web/WEB-INF/lib/bcprov-jdk15-145.jar");
        dep.add("build/web/WEB-INF/lib/c3p0-0.9.5.5.jar");
        dep.add("build/web/WEB-INF/lib/cglib-2.2.jar");
        dep.add("build/web/WEB-INF/lib/checkstyle-all.jar");
        dep.add("build/web/WEB-INF/lib/ClassEncryptor.jar");
        dep.add("build/web/WEB-INF/lib/classmate-1.3.0.jar");
        dep.add("build/web/WEB-INF/lib/cleanimports.jar");
        dep.add("build/web/WEB-INF/lib/commons-beanutils-1.9.4.jar");
        dep.add("build/web/WEB-INF/lib/commons-codec-1.11.jar");
        dep.add("build/web/WEB-INF/lib/commons-collections4-4.1.jar");
        dep.add("build/web/WEB-INF/lib/commons-compress-1.20.jar");
        dep.add("build/web/WEB-INF/lib/commons-digester3-3.2.jar");
        dep.add("build/web/WEB-INF/lib/commons-digester-2.1.jar");  //  Needed by JasperReports
        dep.add("build/web/WEB-INF/lib/commons-discovery-0.5.jar");
        dep.add("build/web/WEB-INF/lib/commons-fileupload-1.4.jar");
        dep.add("build/web/WEB-INF/lib/commons-httpclient-3.1.jar");
        dep.add("build/web/WEB-INF/lib/commons-io-2.11.jar");
        dep.add("build/web/WEB-INF/lib/commons-logging-1.2.jar");
        dep.add("build/web/WEB-INF/lib/compiler-2.3.1-SNAPSHOT.jar");
        dep.add("build/web/WEB-INF/lib/concurrent-1.3.2.jar");
        dep.add("build/web/WEB-INF/lib/connector.jar");
        dep.add("build/web/WEB-INF/lib/dom4j-2.1.1.jar");
        dep.add("build/web/WEB-INF/lib/ehcache-2.10.1.jar");
        dep.add("build/web/WEB-INF/lib/fontbox-2.0.14.jar");
        dep.add("build/web/WEB-INF/lib/ganymed-ssh-2-build260.jar");
        dep.add("build/web/WEB-INF/lib/geronimo-jta_1.1_spec-1.1.1.jar");
        dep.add("build/web/WEB-INF/lib/groovy-4.0.11.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-c3p0-5.1.17.Final.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-commons-annotations-5.0.1.Final.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-core-5.1.17.Final.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-ehcache-5.1.17.Final.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-java8-5.1.17.Final.jar");
        dep.add("build/web/WEB-INF/lib/hibernate-jpa-2.1-api-1.0.0.Final.jar");
        dep.add("build/web/WEB-INF/lib/iText-2.1.7.jar");  // Needed by JasperReports
        dep.add("build/web/WEB-INF/lib/iText-5.0.5.jar");
        dep.add("build/web/WEB-INF/lib/jaas.jar");
        dep.add("build/web/WEB-INF/lib/jacc-1_0-fr.jar");
        dep.add("build/web/WEB-INF/lib/jackson-annotations-2.13.3.jar");
        dep.add("build/web/WEB-INF/lib/jackson-core-2.13.3.jar");
        dep.add("build/web/WEB-INF/lib/jackson-databind-2.13.3.jar");
        dep.add("build/web/WEB-INF/lib/jakarta.activation-1.2.2.1-jre17.jar");
        dep.add("build/web/WEB-INF/lib/jakarta.xml.ws-api-4.0.0.jar");
        dep.add("build/web/WEB-INF/lib/jandex-2.0.3.Final.jar");
        dep.add("build/web/WEB-INF/lib/jasperreports-6.18.1.jar");
        dep.add("build/web/WEB-INF/lib/javassist-3.20.0-GA.jar");
        dep.add("build/web/WEB-INF/lib/javasysmon.jar");
        dep.add("build/web/WEB-INF/lib/javax.annotation-api-1.3.2.jar");
        dep.add("build/web/WEB-INF/lib/javax.jws-api-1.1.jar");
        dep.add("build/web/WEB-INF/lib/javax.mail-1.6.2.jar");
        dep.add("build/web/WEB-INF/lib/jaxb-impl.jar");
        dep.add("build/web/WEB-INF/lib/jaxen-1.1-beta-7.jar");
        dep.add("build/web/WEB-INF/lib/jaxrpc.jar");
        dep.add("build/web/WEB-INF/lib/jaxws-api-2.3.1.jar");
        dep.add("build/web/WEB-INF/lib/jbosscache-core-3.2.1.GA.jar");
        dep.add("build/web/WEB-INF/lib/jboss-logging-3.3.0.Final.jar");
        dep.add("build/web/WEB-INF/lib/jce.jar");
        dep.add("build/web/WEB-INF/lib/jcr181-api-1.0-MR1.jar");
        dep.add("build/web/WEB-INF/lib/jdbc2_0-stdext.jar");
        dep.add("build/web/WEB-INF/lib/jess.jar");
        dep.add("build/web/WEB-INF/lib/jgroups-2.2.8.jar");
        dep.add("build/web/WEB-INF/lib/json.jar");
        dep.add("build/web/WEB-INF/lib/jta-1.1.jar");
        dep.add("build/web/WEB-INF/lib/jtds-1.2.5.jar");
        dep.add("build/web/WEB-INF/lib/junit-4.12.jar");
        dep.add("build/web/WEB-INF/lib/Kiss.jar");
        dep.add("build/web/WEB-INF/lib/log4j-1.2-api-2.17.2.jar");
        dep.add("build/web/WEB-INF/lib/log4j-api-2.17.2.jar");
        dep.add("build/web/WEB-INF/lib/log4j-core-2.17.2.jar");
        dep.add("build/web/WEB-INF/lib/mchange-commons-java-0.2.20.jar");
        dep.add("build/web/WEB-INF/lib/ojdbc14_g.jar");
        dep.add("build/web/WEB-INF/lib/ojdbc14.jar");
        dep.add("build/web/WEB-INF/lib/oscache-2.1.jar");
        dep.add("build/web/WEB-INF/lib/pdfbox-2.0.14.jar");
        dep.add("build/web/WEB-INF/lib/postgresql-42.5.4.jar");
        dep.add("build/web/WEB-INF/lib/resolver.jar");
        dep.add("build/web/WEB-INF/lib/resolver-20050927.jar");
        dep.add("build/web/WEB-INF/lib/saaj-api.jar");
        dep.add("build/web/WEB-INF/lib/saaj-impl.jar");
        dep.add("build/web/WEB-INF/lib/sendgrid-java-4.9.3-jar-with-dependencies.jar");
        dep.add("build/web/WEB-INF/lib/serializer.jar");
        dep.add("build/web/WEB-INF/lib/slf4j-api-1.7.30.jar");
        dep.add("build/web/WEB-INF/lib/slf4j-simple-1.7.30.jar");
        dep.add("build/web/WEB-INF/lib/sqljdbc.jar");
        dep.add("build/web/WEB-INF/lib/swarmcache-1.0rc2.jar");
        dep.add("build/web/WEB-INF/lib/syndiag2.jar");
        dep.add("build/web/WEB-INF/lib/thread-scope-1.3.jar");
        dep.add("build/web/WEB-INF/lib/toolbox.jar");
        dep.add("build/web/WEB-INF/lib/twilio-8.20.0-jar-with-dependencies.jar");
        dep.add("build/web/WEB-INF/lib/versioncheck.jar");
        dep.add("build/web/WEB-INF/lib/xercesImpl-2.11.0.jar");
        dep.add("build/web/WEB-INF/lib/xml-apis.jar");

        dep.add("lib-extra/javax.servlet-api-3.1.0.jar");
        dep.add("lib-extra/servlet-api.jar");
        return dep;
    }
    
    void updateDemo() {
        configDemo();
        build();
        war();
        runWait(true, "scp dist/Stack360Backend.war root@admin.stack360.io:DemoBackend.war");
    }
    
    void updateAdmin() {
        configAdmin();
        build();
        war();
        runWait(true, "scp dist/Stack360Backend.war root@admin.stack360.io:AdminBackend.war");
    }
    
    void updateDemoAdmin() {
        updateDemo();
        updateAdmin();
    }
    
        
    void updateWayToGo() {
        if (!exists("dist/config.WayToGoStaffing") || !exists("dist/Stack360WithoutLibs.war")) {
            configWayToGo();
            build();
            war();
            warWithoutLibs();
        }
        runWait(true, "scp dist/Stack360WithoutLibs.war root@waytogo.arahant.com:");
    }
    
    void updateWayToGoLibs() {
        if (!exists("dist/config.WayToGoStaffing") || !exists("dist/Stack360SharedLibs.war")) {
            configWayToGo();
            build();
            war();
            warWithoutLibs();
        }
        runWait(true, "scp dist/Stack360SharedLibs.war root@waytogo.arahant.com:");
    }


}
