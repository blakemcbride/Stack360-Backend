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

package com.arahant.utils.cobraguard;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.FileSystemUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;

public class HTTPSSender {

	public static final String AUTHORIZATION="670a65dac44b049884ab4de61e7cd8d4";
	public static final String ORG_ID="5266";

	private static final char quote=34;
	private static final String newline="\r\n";

	public static String EMFILE = "EMFile";
	public static String ELFILE = "ELFile";
	public static String DPFILE = "DPFile";
	public static String TERMFILE = "TERMFile";
        public static String UTF8="UTF-8";

	public void send(String fname, File emFile, File dpFile, File elFile, File termFile) throws Exception
	{
        /* No longer functions.  Needs to be updated.
        
        
		File []fyles=new File[4];
		fyles[0]=emFile;
		fyles[1]=dpFile;
		fyles[2]=elFile;
		fyles[3]=termFile;

		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		URL url = new URL("https://www.cobraguard.net/api/1.1/loader.html");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);

		String boundry = FileSystemUtils.getHTTPPath(FileSystemUtils.createTempFile("bound", ""));

		con.setRequestMethod("POST");
		HttpsURLConnection.setFollowRedirects(true);
		con.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundry);

		//build the post
		String data="--"+boundry+newline;
		data+="Content-Disposition: form-data; name=" + quote + "AuthString" + quote+newline;
		data+="\n";
		data+=AUTHORIZATION+newline;
		data+="--"+boundry+newline;
		data+="Content-Disposition: form-data; name=" + quote + "OrgID" +quote +newline;
	//do I need this?	data+="\n";
		data+=ORG_ID+newline;



		DataOutputStream out = new DataOutputStream(con.getOutputStream());

		out.write(data.getBytes(Charset.forName(UTF8)));

		writeFile(out,emFile, EMFILE, boundry);
		writeFile(out,elFile, ELFILE, boundry);
		writeFile(out,dpFile, DPFILE, boundry);
		writeFile(out,termFile, TERMFILE, boundry);

		out.flush();

		if (con.getResponseCode()!=HttpsURLConnection.HTTP_OK)
			throw new ArahantException("HTTPS Transmit Failed with "+con.getResponseCode()+" "+con.getResponseMessage());


		InputStream is=con.getInputStream();

		InputStreamReader isr=new InputStreamReader(is);

		BufferedReader br=new BufferedReader(isr);
		String line;
		while ((line=br.readLine())!=null)
			System.out.println(line);

		br.close();
		out.close();
		con.disconnect();
      */
	}

	private void writeFile(OutputStream out, File fyle, String fyleType, String boundry) throws Exception {
		FileInputStream fis=new FileInputStream(fyle);

		String header="--"+boundry+newline;
		header+="Content-Disposition: form-data; name="+ quote + fyleType + quote +";filename="+quote+fyle.getName()+quote+newline;
		header+=newline;
		header+="Content-Type: application/octet-stream"+newline;
		header+=newline;

		out.write(header.getBytes(Charset.forName(UTF8)));


		//TODO: what if it's longer than maxint?  need to read in chunks in that case
		byte[] fdat=new byte[(int)fyle.length()];
		fis.read(fdat);
		out.write(fdat);
		String marker=newline+"--"+boundry+"--"+newline;
		out.write(marker.getBytes(Charset.forName(UTF8)));
		out.flush();

		fis.close();
	}

}
