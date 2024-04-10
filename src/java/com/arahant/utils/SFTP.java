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

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileHandle;
import org.kissweb.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SFTP {

	public boolean sendSCP(String hostname, String username, String password, String filename, String remoteDir) {
		// Create a connection instance

		Connection conn = new Connection(hostname);

		// Now connect

		try {
			conn.connect();

			//Authenticate.

			boolean isAuthenticated = conn.authenticateWithPassword(username, password);

			if (isAuthenticated) {
				SCPClient scp = new SCPClient(conn);

				System.out.println("Uploading file...");

                long length = (new File(filename)).length();
				if (!isEmpty(remoteDir))
					scp.put(filename, length, remoteDir, "0666");
				else
					scp.put(filename, length, ".", "0666");

				System.out.println("File Uploaded.");
			} else
				throw new IOException("Authentication failed.");
		} catch (IOException iOException) {
			conn.close();
			return false;
		}
		conn.close();
		return true;
	}

	public boolean sendSCP(String hostname, String username, String password, String[] filenames, String remoteDir) {
		//Create a connection instance

		Connection conn = new Connection(hostname);

		//Now connect

		try {
			conn.connect();

			// Authenticate.

			boolean isAuthenticated = conn.authenticateWithPassword(username, password);

			if (isAuthenticated) {
				SCPClient scp = new SCPClient(conn);

				System.out.println("Uploading files...");

                for (String filename : filenames)
                    if (!isEmpty(remoteDir))
                        scp.put(filename, (new File(filename)).length(), remoteDir, "0666");
                    else
                        scp.put(filename, (new File(filename)).length(), ".", "0666");

				System.out.println("Files Uploaded.");

			} else
				throw new IOException("Authentication failed.");
		} catch (IOException iOException) {
			conn.close();
			return false;
		}
		conn.close();
		return true;
	}

	public boolean sendSFTP(String hostname, String username, String password, String inFilename, String outFilename) {
		// Create a connection instance

		Connection conn = new Connection(hostname);
		BufferedInputStream in = null;
		SFTPv3Client scp = null;

		try {
			// Now connect
			conn.connect();

			//Authenticate.

			boolean isAuthenticated = conn.authenticateWithPassword(username, password);

			if (isAuthenticated) {
				scp = new SFTPv3Client(conn);
				in = new BufferedInputStream(new FileInputStream(inFilename));
				byte[] buf = new byte[1024];
				int len;
				SFTPv3FileHandle out = scp.createFileTruncate(outFilename);
				long offset = 0;

				System.out.println("Uploading file...");

				while ((len = in.read(buf)) > 0) {
					scp.write(out, offset, buf, 0, len);
					offset += len;
				}

				in.close();
				in = null;
				scp.close();
				scp = null;

				System.out.println("File Uploaded.");
			}
		} catch (IOException iOException) {
			try {
				if (in != null)
					in.close();
			} catch (IOException iOException1) {
			}
			if (scp != null)
				scp.close();
			conn.close();
			return false;
		}

		conn.close();
		return true;
	}

	public boolean sendSFTP(String hostname, String username, String password, String[] inFilenames, String outDirname) {
		return sendSFTP(hostname, 22, username, password, inFilenames, outDirname);
	}

	public boolean sendSFTP(String hostname, int port, String username, String password, String outDirname, String ... inFilenames) {
		return sendSFTP(hostname, port, username, password, inFilenames, outDirname);
	}

	public boolean sendSFTP(String hostname, int port, String username, String password, String outDirname, List<File> inFiles) {
		int sz = inFiles.size();
		String [] fileNames = new String[sz];
		for (int i=0 ; i < sz ; i++)
			fileNames[i] = inFiles.get(i).getPath();
		return sendSFTP(hostname, port, username, password, fileNames, outDirname);
	}

	public boolean sendSFTP(String hostname, int port, String username, String password, String[] inFilenames, String outDirname) {
		// Create a connection instance

		Connection conn = new Connection(hostname, port);
		BufferedInputStream in = null;
		SFTPv3Client scp = null;

		try {
			// Now connect
			conn.connect();

			//Authenticate.

			boolean isAuthenticated = conn.authenticateWithPassword(username, password);

			if (isAuthenticated) {
				scp = new SFTPv3Client(conn);
				byte[] buf = new byte[1024];

				for (String inFile : inFilenames) {
					in = new BufferedInputStream(new FileInputStream(inFile));
					long offset = 0;
					int len;
					String[] splitFilename = inFile.split("/");
					String filename = splitFilename[splitFilename.length - 1];
					if (outDirname != null && !outDirname.isEmpty())
						filename = StringUtils.isEmpty(outDirname) ? filename : outDirname + "/" + filename;
					SFTPv3FileHandle out = scp.createFileTruncate(filename);

					System.out.println("Uploading file..." + filename);

					while ((len = in.read(buf)) > 0) {
						scp.write(out, offset, buf, 0, len);
						offset += len;
					}

					scp.closeFile(out);

					in.close();
				}

				scp.close();

				System.out.println("File Uploaded.");
			} else
				throw new IOException("Authentication failed.");
		} catch (IOException iOException) {
			iOException.printStackTrace();
			try {
				if (in != null)
					in.close();
			} catch (IOException iOException1) {

				iOException1.printStackTrace();
			}
			if (scp != null)
				scp.close();
			conn.close();
			return false;
		}
		conn.close();
		return true;
	}

	public static void main(String[] args) {
		//new SFTP().sendSFTP("test-edi.humana.com", 5630, "ArahantGroup", "", new String[]{"/Users/arahant/Desktop/test_file.rtf"}, "ArahantNewPassInbEnroll");
	}


	private boolean isEmpty(String x) {
		return x == null || x.equals("");
	}
}
