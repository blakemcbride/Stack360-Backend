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


package com.arahant.remote;


import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.XML;
import com.arahant.utils.dynamicwebservices.DataObjectMap;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author Blake McBride
 */
public class Server {

	private boolean listening = true;
	private ServerSocket serverSocket;
	private SocketServer listenThread;

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void close() {
		try {
			listening = false;
			if (listenThread == null)
				serverSocket.close();  // closing the socket interrupts the accept() in the thread.  This is the only way to interrupt the accept.
			else
				listenThread.stopListening();
		} catch (Exception e) {
			
		}
	}

	public void listen() {
		try {
			while (listening)
				new SocketHandler(serverSocket.accept()).start();
		} catch (SocketException se) {
			/*  This error will occur if we are attempting to stop the server or if there is a connection problem.
			 *  It is only an "error" if we weren't attempting to stop the server
			 */
			if (listening)
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, se);
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				serverSocket.close();
				serverSocket = null;
			} catch (IOException ex) {
			}
		}
	}

	public void listenSeperateThread() {
		listenThread = new SocketServer(serverSocket);
		listenThread.start();
	}

	private static class SocketServer extends Thread {

		private boolean listening = true;
		private ServerSocket serverSocket;

		public SocketServer(ServerSocket s) {
			serverSocket = s;
		}

		@Override
		public void run() {
			try {
				while (listening)
					new SocketHandler(serverSocket.accept()).start();
			} catch (SocketException se) {
				/*  This error will occur if we are attempting to stop the server or if there is a connection problem.
				 *  It is only an "error" if we weren't attempting to stop the server
				 */
				if (listening)
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, se);
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				try {
					serverSocket.close();
				} catch (IOException ex) {
				}
			}
		}
		
		void stopListening() {
			listening = false;
			try {
				serverSocket.close();  // closing the socket interrupts the accept() in the thread.  This is the only way to interrupt the accept.
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private static class SocketHandler extends Thread {

		private Socket socket;

		public SocketHandler(Socket s) {
			socket = s;
		}

		@Override
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				InputStream is = socket.getInputStream();
				Document doc = XML.receive(is);
				DataObjectMap inDOM = new DataObjectMap(doc);
				DataObjectMap outDOM = new DataObjectMap();

				(new DynamicWebServiceOps()).handleRequest(inDOM, outDOM, socket.getRemoteSocketAddress().toString());
				
				XML.send(outDOM.toXML(), out);
				out.close();
				is.close();
			} catch (Throwable ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				try {
					socket.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	public static void main(String [] args) {
		Server GWTServer;
		GWTServer = new Server(2000);
		GWTServer.listenSeperateThread();  //  start the socket listner for socket communications with GWT
		GWTServer.close();
		
		GWTServer = new Server(2000);
		GWTServer.listenSeperateThread();  //  start the socket listner for socket communications with GWT
		GWTServer.close();
	}

}
