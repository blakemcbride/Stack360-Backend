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
 * SetAnnotations.java
 *
 * Created on November 30, 2007, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.arahant.utils.setAnnotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * Arahant
 */
public class SetAnnotations {
    
    /** Creates a new instance of SetAnnotations */
    public SetAnnotations() {
    }
    
    public static void main(String args [])
    {
     //   convert("src/java/com/arahant/services/custom/williamsonCounty/benefitWizard/BenefitWizardOps.java");
        
        File baseDir=new File("src/java/com/arahant/services");
        recurseDirs(baseDir);
        
    }
    
    public static void recurseDirs(File current)
    {
    //    System.out.println(current.getName());
        if (current.isDirectory())
        {
            for (File f : current.listFiles())
            {
                recurseDirs(f);
            }
        }
        else
        {
            if (current.getName().endsWith("Ops.java"))
            {
                System.out.println(current.getAbsolutePath());
                convert(current.getAbsolutePath());
            }
        }
    }
    
    public static void convert(String path)
    {
        try {
 
            String newPath=path.subSequence(0,path.lastIndexOf('.')+1)+"test";
     //       File fyle=new File("../../../../");
        //    System.out.println(fyle.getAbsolutePath());
            BufferedReader br=new BufferedReader(new FileReader(path));
            BufferedWriter bw=new BufferedWriter(new FileWriter(newPath));
            
            String line=br.readLine();
            while (line!=null)
            {
               // System.out.println(line);
                
                if (line.trim().startsWith("package"))
                {
                    bw.write(line+"\n");
                
                    line=br.readLine();
                    bw.write("\nimport javax.jws.WebMethod;\n" +
                            "import javax.jws.WebService;\n" +
                            "import javax.jws.soap.SOAPBinding;\n" +
                            "import javax.jws.soap.SOAPBinding.ParameterStyle;\n" +
                            "import javax.jws.soap.SOAPBinding.Style;\n" +
                            "import javax.jws.soap.SOAPBinding.Use;\n" +
                            "import javax.jws.WebParam;\n" +
                            "import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;\n");
                    
                    continue;
                }
                if (line.trim().startsWith("public class "))
                {
                    String remainder=path.substring(path.indexOf("src/java/com/arahant/services/")+"src/java/com/arahant/services/".length());;
                    StringTokenizer stok=new StringTokenizer(remainder, "/");
                    String name="";
                    
                    while (stok.hasMoreTokens())
                    {
                        String tok=stok.nextToken();
                        if (tok.endsWith("Ops.java"))
                            continue;
                        name+=tok.substring(0,1).toUpperCase()+tok.substring(1);
                    }
                    name=name+"Ops";
     
                    bw.write("@WebService(targetNamespace=\"http://operations.arahant.com\",serviceName=\""+name+"\")\n");
                    bw.write("@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) \n");
                    bw.write("@ThreadScope()\n");
                    

                }
                else
                    if (line.trim().startsWith("public"))
                    {
                        String k=line.trim();
                        StringTokenizer constTok=new StringTokenizer(k," ");
                        constTok.nextToken();
                        if (constTok.nextToken().indexOf("Return")!=-1)
                        {
                            bw.write("\t@WebMethod()\n");
                            bw.write(line.substring(0,line.indexOf('(')+1)+"@WebParam(name = \"in\")"+line.substring(line.indexOf('(')+1));
                            line=br.readLine();
                            continue;
                        }
                        
                    }
                bw.write(line+"\n");
                
                line=br.readLine();
            }
            bw.flush();
            bw.close();
            br.close();
            
            File old=new File(path);
            old.delete();
            File f=new File(newPath);
            f.renameTo(old);
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     
}
