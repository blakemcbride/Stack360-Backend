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


package com.arahant.utils;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * This is the old FTP code that worked fine in the past.
 * A new, cleaner FTP class is being created to replace this one.
 * 
 */
public class FTP {

    private String check(BufferedReader reader, String code, String code2) throws Exception {
        boolean responseMatched = false;
        final int maxTimeoutSec = 30;
        String response = "";
        int elapsedTime = 0;
        while (!reader.ready()) {
            if (elapsedTime >= maxTimeoutSec)
                throw new Exception("FTP response timed out.");
            elapsedTime++;
            Thread.sleep(1000);
        }
        String nextResponse = reader.readLine();
        while (nextResponse != null) {
            response = nextResponse + "\n" + response;
//            System.out.println(nextResponse);
            if (nextResponse.startsWith(code) || (!isEmpty(code2) && (nextResponse.startsWith(code2))))// || response.startsWith("230-"))
            {
                responseMatched = true;
                break;
            }
            elapsedTime = 0;
            while (!reader.ready()) {
                if (elapsedTime >= maxTimeoutSec)
                    throw new Exception("FTP response timed out.");
                elapsedTime++;
                Thread.sleep(1000);
            }
            nextResponse = reader.readLine();
        }
        if (!responseMatched)
            throw new Exception("Error : " + response);
        return response;
    }
 
	private String check(BufferedReader reader, String code) throws Exception {
		return check(reader, code, null);
	}
	
    private String cmd(BufferedWriter writer, BufferedReader reader, String cmd, String code) throws Exception {
        cmd += "\r\n";
//        System.out.println(cmd);
        writer.write(cmd);
        writer.flush();
        if (code != null)
            return check(reader, code);
        return "";
    }

    public void send(String comUrl, String comPassword, String comDirectory, File fyle, boolean binary) throws MalformedURLException, Exception {
        URL url = new URL(comUrl);
        send(url.getHost(), url.getPort(), url.getUserInfo(), comPassword, comDirectory, fyle, binary);
    }

    private boolean isEmpty(String x) {
        return x == null || x.equals("");
    }

    public void send(String host, int port, String user, String pass, String dir, File fyle, boolean binary) throws Exception {

        Socket socket = new Socket(host, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        check(reader, "220 ");

        cmd(writer, reader, "USER " + user, "331 ");
        cmd(writer, reader, "PASS " + pass, "230 ");
        if (!isEmpty(dir))
            cmd(writer, reader, "CWD " + dir, "250 ");

        if (binary)
            cmd(writer, reader, "TYPE I", "200 ");

        String ip = host;
        int port2 = -1;
        boolean pasv = false;

        /*		try
		{
			String response=cmd(writer,reader,"EPSV","229 ");
			
			//229 Entering Extended Passive Mode (|||13118|)
			
			//find the port
			
			int endPos=response.lastIndexOf('|');
			int startPos=response.lastIndexOf('|', endPos-1);
			
			String sport=response.substring(startPos+1, endPos);
			
			port2=Integer.parseInt(sport);	
		
			
		}
		catch (Exception e)
         */ 
        {
            String response = cmd(writer, reader, "PASV", "227 ");
            pasv = true;

            System.out.println(response);

            int oparen = response.indexOf('(');
            int cparen = response.indexOf(')', oparen + 1);
            if (cparen > 0) {
                StringTokenizer tokenizer = new StringTokenizer(response.substring(oparen + 1, cparen), ",");
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port2 = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());

                System.out.println(ip + " " + port2);
            }
        }

        //UploadThread ut=new UploadThread(fyle,ip,port);
        //	ut.start();
        cmd(writer, reader, "STOR " + fyle.getName(), null);

        System.out.println("did store");

        Socket dataSocket = new Socket();
        SocketAddress addr = new InetSocketAddress(ip, port2);
        //dataSocket.setSoTimeout(1000*60*10);  //0 infinite
        dataSocket.connect(addr, 1000000000);

        //check for 150
        check(reader, "150 ", "125 ");

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fyle));

        BufferedOutputStream bos = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1)
            bos.write(buffer, 0, bytesRead);
        bos.flush();
        bos.close();
        bis.close();
        dataSocket.close();

        //ut.run();
        check(reader, "226");

        cmd(writer, reader, "QUIT", null);

        reader.close();
        writer.close();
        socket.close();

    }
    /*
	public class UploadThread extends Thread
	{
		
		File fyle;
		String ip;
		int port2;
		
		public UploadThread(File f, String host, int port)
		{
			fyle=f;
			ip=host;
			port2=port;
		}
		
		public void run()
		{
			try {
				Socket dataSocket = new Socket(ip, port2);
				dataSocket.

				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fyle));

				BufferedOutputStream bos = new BufferedOutputStream(dataSocket.getOutputStream());
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.flush();
				bos.close();
				bis.close();
				dataSocket.close();
				System.out.println("wrote data");
			} catch (UnknownHostException ex) {
				Logger.getLogger(FTP.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FTP.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
*/
    public static void main(String args[]) {
        FTP ftp = new FTP();
        try {

            //ftp.send("abc.com", 21, "test", "test286", ".", new File("/Users/Arahant/testfile.txt"), false);
            //ftp.send("poweredbyhamsters.com", 21, "arahant", "H@mst3rs", "david", new File("/Users/Arahant/crypt/EDI371351918_24_2.edi.pgp"), false);
            ftp.send("ftp.consociategroup.com", 21, "wcounty", "yAF6qKiCrY", ".", new File("/Users/Arahant/crypt/EDI371351918_24_3.edi.pgp"), false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
}
