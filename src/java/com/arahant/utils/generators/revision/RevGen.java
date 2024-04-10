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


package com.arahant.utils.generators.revision;

import com.arahant.exec.SystemCommandExecutor;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RevGen {

    private static String revision;
    private static String location;

    public static void main(String[] args) {
        try {
            if ((new File(".svn").exists())) {
                SystemCommandExecutor commandExecutor = new SystemCommandExecutor("svn", "info");
                int result = commandExecutor.executeCommand();

                // get the stdout and stderr from the command that was run
//            StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
//            StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

                // print the stdout and stderr
//            System.out.println("The numeric result of the command was: " + result);
//            System.out.println("STDOUT:");
//            System.out.println(stdout);
//            System.out.println("STDERR:");
//            System.out.println(stderr);
                getRevAndLoc(new BufferedReader(new StringReader(commandExecutor.getStandardOutputFromCommand().toString())));
                createVersionData();
            } else {
                getRevAndLoc(new BufferedReader(new FileReader("svn-info.txt")));
                createVersionData();
            }
        } catch (Exception ex) {
            Logger.getLogger(RevGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void updateIndex() throws FileNotFoundException, IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        File ff = new File("build/web/index.html");
        File tf = new File("build/web/index.tmp");

        try {
            br = new BufferedReader(new FileReader(ff));
            bw = new BufferedWriter(new FileWriter(tf));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"src\", \"Main"))
                    line = "\t\t\t\t\t\t\"src\", \"Main?rev=" + revision + "\",";
                bw.write(line);
                bw.newLine();
            }
            br.close();
            bw.close();
            br = null;
            bw = null;
            ff.delete();
            tf.renameTo(ff);
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    private static void createVersionData() throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter("build/web/WEB-INF/classes/VersionData.txt");
            fw.write("Name=Arahant\nVersion=2.6.0\nBuildDate=" + DateUtils.getDateFormatted(DateUtils.now()) + "\nSourceCodeRevisionNumber=" + revision);
            fw.write("\nSourceCodeRevisionPath=" + location + "\n");
        } finally {
            if (fw != null)
                fw.close();
        }
    }

    private static void getRevAndLoc(BufferedReader br) throws FileNotFoundException, IOException {
        try {
            while (revision == null || location == null) {
                try {
                    String line = br.readLine();
                    if (line.startsWith("Revision:"))
                        revision = line.substring(10);
                    else if (line.startsWith("URL:")) {
                        location = line.substring(5);
                        location = location.substring(location.indexOf("//")+2);
                        location = location.substring(location.indexOf("/"));
                    }
                } catch (Exception e) {
                    break;
                }
            }
            if (location == null)
                location = "repo"; // default value
        } finally {
            if (br != null)
                br.close();
        }

    }
}
