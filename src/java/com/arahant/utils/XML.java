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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 *
 * @author Blake McBride
 */
public class XML {

	public static Document create() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (Exception e) {
			return null;
		}
	}

	public static Document parse(String inp) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.parse(new InputSource(new StringReader(inp)));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Document parse(File inp) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.parse(inp);
		} catch (Exception e) {
			return null;
		}
	}

	public static String toString(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			return null;
		}
	}
	
	public static String toFormattedString(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			return null;
		}
	}

	private static class XMLOutputStream extends ByteArrayOutputStream {

		private DataOutputStream outchannel;

		public XMLOutputStream(OutputStream outchannel) {
			super();
			this.outchannel = new DataOutputStream(outchannel);
		}

		public void send() throws IOException {
			byte[] data = toByteArray();
			outchannel.writeInt(data.length);
			outchannel.write(data);
			reset();
		}
	}

	/**
	 * Send XML over a stream allowing for the fact that you need to EOF the XML
	 * without actually EOF'ing the stream (for Sockets)
	 */
	public static boolean send(Document tosend, OutputStream channel) {
		XMLOutputStream out = new XMLOutputStream(channel);
		try {
			StreamResult sr = new StreamResult(out);
			DOMSource ds = new DOMSource(tosend);
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.transform(ds, sr);
			out.send();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * Send XML over a stream allowing for the fact that you need to EOF the XML
	 * without actually EOF'ing the stream (for Sockets)
	 */
	public static boolean send(String tosend, OutputStream channel) {
		XMLOutputStream out = new XMLOutputStream(channel);
		try {
			out.write(tosend.getBytes());
			out.send();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private static class XMLInputStream extends ByteArrayInputStream {

		private DataInputStream inchannel;

		public XMLInputStream(InputStream inchannel) {
			super(new byte[2]);
			this.inchannel = new DataInputStream(inchannel);
		}

		public void recive() throws IOException {
			int i = inchannel.readInt();
			byte[] data = new byte[i];
			inchannel.read(data, 0, i);
			this.buf = data;
			this.count = i;
			this.mark = 0;
			this.pos = 0;
		}
	}

	/**
	 * Receive XML over a stream allowing for the fact that you need to EOF the
	 * XML without actually EOF'ing the stream (for Sockets)
	 */
	public static Document receive(InputStream channel) {
		try {
			DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
			Document request;

			XMLInputStream xmlin = new XMLInputStream(channel);

			xmlin.recive();

			request = docBuilder.parse(xmlin);

			return request;
		} catch (Exception e) {
			return null;
		}
	}
}
