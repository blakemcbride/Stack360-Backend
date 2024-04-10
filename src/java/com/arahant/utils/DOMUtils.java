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

import com.arahant.exceptions.ArahantException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 *
 *
 * Arahant
 */
public class DOMUtils {

	public static final String CDATA_END = "]]>";
	public static final String CDATA_START = "<![CDATA[";
	public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final XPathFactory factory = XPathFactory.newInstance();

	public static Element setValue(final Node node, final String xpath, final String value) {
		try {
			if (node == null || xpath == null)
				return null;

			final Node tmp = (Node) factory.newXPath().compile(xpath).evaluate(node, XPathConstants.NODE);

			//final Node tmp = XPathAPI.selectSingleNode(node, xpath);

			if (tmp == null)
				return appendNode(node, xpath, value);

			if (value == null) {
				node.removeChild(tmp);
				return null;
			}

			tmp.setNodeValue(value);
			return (Element) tmp;
		} catch (final Exception e) {
			return null;
		}
	}

	public static void setNodeValue(final Node node, final String value) {
		if (node == null)
			return;

		switch (node.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			case Node.TEXT_NODE:
				if (value == null)
					node.getParentNode().removeChild(node);
				else
					node.setNodeValue(value);
				break;
			case Node.ELEMENT_NODE:
				node.appendChild(node.getOwnerDocument().createTextNode(value));
				break;
			default: // don;t know what to do. But, at least we'll separate those out.
				node.setNodeValue(value);
				break;
		}
	}

	public static boolean getBoolean(final Node node, final String xpath)
			throws TransformerException {
		return Conversions.toBoolean(getString(node, xpath));
	}

	public static short getShort(final Node node, final String xpath)
			throws TransformerException {
		return Conversions.toShort(getString(node, xpath));
	}

	public static int getInt(final Node node, final String xpath)
			throws TransformerException {
		return Conversions.toInt(getString(node, xpath));
	}

	public static long getLong(final Node node, final String xpath)
			throws TransformerException {
		return Conversions.toLong(getString(node, xpath));
	}

	public static float getFloat(final Node node, final String xpath)
			throws TransformerException {
		return Conversions.toFloat(getString(node, xpath));
	}

	public static double getDouble(final Node node, final String xpath)
			throws TransformerException {
		String val = getString(node, xpath);
		if (val == null || val.equals(""))
			return 0;
		return Double.parseDouble(val);
	}

	public static java.util.Date getDate(final Node node, final String xpath)
			throws TransformerException {
		try {
			return DateFormat.getDateTimeInstance().parse(getString(node, xpath));
		} catch (Exception e) {
			return null;
		}
	}

	public static String getString(final Node node, final String xpath)
			throws TransformerException {
		if (node == null || xpath == null)
			return "";
		return getNodeValue(getNode(node, xpath));
	}

	public static Element appendNode(final Node node, final String name, final String value) {
		final Element element = DOMUtils.createElement(node, name);

		if (value != null) {
			final Text text = createTextNode(node, value);
			element.appendChild(text);
		}
		node.appendChild(element);
		return element;
	}

	public static Element appendNodeNS(final Node node, final String namespace, final String name, final String value) {
		final Element element = DOMUtils.createElementNS(node, namespace, name);

		if (value != null) {
			final Text text = createTextNode(node, value);
			element.appendChild(text);
		}

		node.appendChild(element);
		return element;
	}

	public static Element createElement(final Node parent, final String name) {
		Document doc;

		if (parent.getNodeType() == Node.DOCUMENT_NODE)
			doc = (Document) parent;
		else
			doc = parent.getOwnerDocument();

		return doc.createElement(name);
	}

	public static Element createElementNS(final Node parent, final String namespace, final String name) {
		Document doc;

		if (parent.getNodeType() == Node.DOCUMENT_NODE)
			doc = (Document) parent;
		else
			doc = parent.getOwnerDocument();

		return doc.createElementNS(namespace, name);
	}

	public static Text createTextNode(final Node parent, final String value) {
		Document doc;

		if (parent.getNodeType() == Node.DOCUMENT_NODE)
			doc = (Document) parent;
		else
			doc = parent.getOwnerDocument();

		return doc.createTextNode(value);
	}

	public static void removeAllChildNodes(final Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			final Element element = (Element) node;
			while (element.getChildNodes().getLength() > 0)
				element.removeChild(element.getChildNodes().item(0));
		}
	}

	public static String convertToHTML(String input) {
		input = escapeText(input);
		input = input.replaceAll("\n", "<br/>");
		input = input.replaceAll(" ", "&nbsp;");
		input = input.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		return input;
	}

	public static String doubleEscapeText(String input) {
		if (input == null)
			return null;
		if (input.indexOf("&") > -1) {
			input = input.replaceAll("&", "&amp;");
			return input;
		}

		return input.replaceAll("&", "&amp;");
	}

	public static String unescapeText(String input) {
		if (input == null)
			return null;

		input = input.replaceAll("&amp;", "&");
		input = input.replaceAll("&apos;", "'");
		input = input.replaceAll("&lt;", "<");
		input = input.replaceAll("&gt;", ">");
		input = input.replaceAll("&quot;", "\"");

		return input;
	}

	public static String escapeText(String input) {
		if (input == null)
			return "";

		// replaces & -> &amp;
		// 			' -> &apos;
		// 			< -> &lt;
		//			> -> &gt;
		//			" -> &quot;
		int current = 0;
		while (current < input.length()) {
			current = input.indexOf("&", current);
			if (current == -1)
				break;
			final String tmp = input.substring(current);
			if (tmp.startsWith("&nbsp;") || tmp.startsWith("&amp;")
					|| tmp.startsWith("&lt;") || tmp.startsWith("&gt;")
					|| tmp.startsWith("&quot")
					|| tmp.startsWith("&pos;"))
				current += 1;
			else
				input = input.substring(0, current) + "&amp;"
						+ input.substring(current + 1);
		}

		input = aposPattern.reset(input).replaceAll("&apos;");
		input = lessPattern.reset(input).replaceAll("&lt;");
		input = greaterPattern.reset(input).replaceAll("&gt;");
		input = quotePattern.reset(input).replaceAll("&quot;");
		return input;
	}
	private static final Matcher aposPattern = Pattern.compile("\\'").matcher("");
	private static final Matcher lessPattern = Pattern.compile("\\<").matcher("");
	private static final Matcher greaterPattern = Pattern.compile("\\>").matcher("");
	private static final Matcher quotePattern = Pattern.compile("\\\"").matcher("");

	public static void convertToString(final Node node, final StringBuffer out) {
		convertToString(node, out, null);
	}

	public static void convertToString(final Node node, final StringBuffer out, Map<String, String> namespaceMap) {
		if (node == null)
			return;

		if (node.getNodeType() == Node.COMMENT_NODE) {
			out.append("<!--");
			out.append(escapeText(node.getNodeValue()));
			out.append("-->");
			return;
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			String value = node.getNodeValue();
			if (value != null) {
				value = escapeText(value.trim());
				out.append(value);
			}

			return;
		} else if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
			String value = node.getNodeValue();
			if (value != null) {
				value = toCDATA(value);
				out.append(value);
			}

			return;
		}

		// 
		final NamedNodeMap attrs = node.getAttributes();
		if (attrs != null)
			if (namespaceMap != null) {
				Map<String, String> newNamespaceMap = null;
				for (int i = 0; i < attrs.getLength(); i++) {
					// load namespaces
					final Node attr = attrs.item(i);
					if ("xmlns".equals(attr.getPrefix())) {
						final String prefix = attr.getLocalName();
						final String value = attr.getNodeValue();
						final String currentValue = (String) namespaceMap.get(value);

						if (prefix.equals(currentValue) == false) {
							if (newNamespaceMap == null)
								newNamespaceMap = new HashMap<String, String>(namespaceMap);

							newNamespaceMap.put(value, prefix);
						}
					}
				}

				if (newNamespaceMap != null)
					namespaceMap = newNamespaceMap;
			}

		out.append("<").append(getQName(node, namespaceMap));
		if (attrs != null)
			for (int i = 0; i < attrs.getLength(); i++) {
				final Node attr = attrs.item(i);
//				if (attr.getNodeName().startsWith("" + XML_NAMESPACE_ID + ":xs"))
//					continue;

				out.append(" ").append(getQName(attr, namespaceMap)).append("=" + "\"").append(escapeText(attr.getNodeValue())).append("\"");
			}

		out.append(">");
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			convertToString(child, out, namespaceMap);

		// 
		out.append("</").append(getQName(node, namespaceMap)).append(">\n");
	}

	public static String convertToString(final Node node) {
		return convertToString(node, (Map<String, String>) null);
	}

	public static String convertToString(final Node node, final Map<String, String> namespaceMap) {
		final StringBuffer out = new StringBuffer();
		convertToString(node, out, namespaceMap);
		return out.toString();
	}

	public static String format(final String xml) {
		try {
			final Document doc = DOMUtils.createDocument(xml);
			if (doc == null)
				return "";

			final Node node = doc.getDocumentElement();
			return format(node);
		} catch (final Exception e) {
			return xml;
		}
	}

	public static String format(final Node node) {
		return format(node, false, 0);
	}

	public static String format(final Node node, final Map<String, String> namespaceMap) {
		return format(node, false, 0, namespaceMap);
	}

	public static String format(final Node node, final boolean asSourceCode) {
		return format(node, asSourceCode, 0);
	}

	public static String format(final Node node, final boolean asSourceCode, final Map<String, String> namespaceMap) {
		return format(node, asSourceCode, 0, namespaceMap);
	}

	public static String format(final Node node, final boolean asSourceCode, final int tabLevel) {
		final StringBuffer out = new StringBuffer();
		format(out, node, asSourceCode, tabLevel);
		return out.toString();
	}

	public static String format(final Node node, final boolean asSourceCode, final int tabLevel, final Map<String, String> namespaceMap) {
		final StringBuffer out = new StringBuffer();
		format(out, node, asSourceCode, tabLevel, namespaceMap);
		return out.toString();
	}

	public static void format(final StringBuffer out, final Node node,
			final boolean asSourceCode, final int tabLevel) {
		format(out, node, asSourceCode, tabLevel, null);
	}

	public static void format(final StringBuffer out, final Node node, final boolean asSourceCode, final int tabLevel, Map<String, String> namespaceMap) {
		out.append("\n");
		for (int i = 0; i < tabLevel; i++)
			out.append("\t");

		if (asSourceCode) {
			if (tabLevel > 0)
				out.append("+");

			out.append("\"");
		}

		if (node.getNodeType() == Node.COMMENT_NODE) {
			out.append("<!-- ");
			out.append(node.getNodeValue());
			out.append(" -->");
			return;
		} else if (node.getNodeType() != Node.TEXT_NODE && node.getNodeType() != Node.CDATA_SECTION_NODE && node.getNodeType() != Node.ELEMENT_NODE
				&& node.getNodeType() != Node.DOCUMENT_NODE && node.getNodeType() != Node.DOCUMENT_FRAGMENT_NODE)
			return;

		// 


		final NamedNodeMap attrs = node.getAttributes();
		if (attrs != null)
			if (namespaceMap != null) {
				Map<String, String> newNamespaceMap = null;
				for (int i = 0; i < attrs.getLength(); i++) {
					// load namespaces
					final Node attr = attrs.item(i);
					final String nodeName = attr.getNodeName();
					if (nodeName.startsWith("xmlns:")) {
						final String prefix = nodeName.substring("xmlns:".length());
						final String value = attr.getNodeValue();
						final String currentValue = namespaceMap.get(value);

						if (!prefix.equals(currentValue)) {
							if (newNamespaceMap == null)
								newNamespaceMap = new HashMap<>(namespaceMap);

							newNamespaceMap.put(value, prefix);
						}
					}
				}

				if (newNamespaceMap != null)
					namespaceMap = newNamespaceMap;
			}

		out.append("<").append(getQName(node, namespaceMap));
		if (attrs != null)
			for (int i = 0; i < attrs.getLength(); i++) {
				final Node attr = attrs.item(i);
//				if (attr.getNodeName().startsWith("xmlns:xs") || attr.getNodeName().startsWith("xml:"))
//					continue;

				if (asSourceCode)
					// 
					out.append(" ").append(getQName(attr, namespaceMap)).append("='").append(attr.getNodeValue()).append("'");
				else
					// 
					out.append(" ").append(getQName(attr, namespaceMap)).append("=\"").append(attr.getNodeValue()).append("\"");
			}

		Node child = node.getFirstChild();
		if (child == null)
			out.append("/");

		if (asSourceCode)
			out.append(">\"");
		else
			out.append(">");

		if (child == null)
			return;

		boolean useTabs = false;
		for (; child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.TEXT_NODE) {
				String value = child.getNodeValue().trim();
				if (!(value == null || value.length() == 0 || value.trim().length() == 0)) {
					if (asSourceCode)
						out.append("+\"");

					value = escapeText(value);
					out.append(value);
					if (asSourceCode)
						out.append("\"");
				}
			} else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
				String value = child.getNodeValue().trim();
				if (!(value == null || value.length() == 0 || value.trim().length() == 0)) {
					if (asSourceCode)
						out.append("+\"");

					value = toCDATA(value);
					out.append(value);
					if (asSourceCode)
						out.append("\"");
				}
			} else {
				useTabs = true;
				format(out, child, asSourceCode, tabLevel + 1, namespaceMap);
			}

		if (useTabs) {
			out.append("\n");
			for (int i = 0; i < tabLevel; i++)
				out.append("\t");
		}

		if (asSourceCode)
			out.append("+\"</").append(getQName(node, namespaceMap)).append(">\"");
		else
			out.append("</").append(getQName(node, namespaceMap)).append(">");
	}

	public static void sortElements(final Element parent, final boolean sortAttributes) {
		sortElements(parent, null, sortAttributes, true);
	}

	public static void sortElements(final Element parent, Comparator<Node> comparator, final boolean sortAttributes, final boolean recurse) {
		final ArrayList<Node> nodes = new ArrayList<Node>();
		final NodeList subNodes = parent.getChildNodes();
		for (int i = 0; i < subNodes.getLength(); i++) {
			final Node node = subNodes.item(i);
			final int type = node.getNodeType();

			if (type == Node.ELEMENT_NODE && recurse == true)
				sortElements((Element) node, comparator, sortAttributes, recurse);

			if (type == Node.ELEMENT_NODE || (sortAttributes == true && type == Node.ATTRIBUTE_NODE))
				nodes.add(node);
		}

		for (final Node node : nodes)
			parent.removeChild(node);

		class NodeNameComparator implements Comparator<Node> {

			@Override
			public int compare(final Node obj0, final Node obj1) {
				final Node node0 = obj0;
				final Node node1 = obj1;

				final String name0 = node0.getNodeName();
				final String name1 = node1.getNodeName();

				return Comparisons.compare(name0, name1, false);
			}
		}

		if (comparator == null)
			comparator = new NodeNameComparator();

		java.util.Collections.sort(nodes, comparator);

		for (final Node node : nodes) {
			parent.appendChild(node);
		}

		if (sortAttributes == true) {
			nodes.clear();
			final NamedNodeMap attributes = parent.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++)
				nodes.add(attributes.item(i));

			for (final Node node : nodes)
				parent.removeAttribute(node.getNodeName());

			java.util.Collections.sort(nodes, comparator);

			for (final Node node : nodes)
				parent.setAttribute(node.getNodeName(), node.getNodeValue());
		}
	}

	private static String replaceQuotes(final String value) {
		return value.replaceAll("\"", "'");
	}

	private static String replaceSpaces(final String txt) {
		final StringBuilder buf = new StringBuilder();
		for (int j = 0; j < txt.length(); j++)
			if (txt.charAt(j) == 0xA0)
				buf.append("&nbsp;");
			else
				buf.append(txt.charAt(j));
		return buf.toString();
	}

	private static String getQName(final Node node, final Map<String, String> namespaceMap) {
		String prefix = node.getPrefix();
		if (prefix == null) {
			final String namespaceURI = node.getNamespaceURI();
			if (namespaceURI != null) {
				prefix = (String) namespaceMap.get(namespaceURI);
				if (prefix == null) {
					Integer i = Integer.parseInt(namespaceMap.get("_count"));
//					prefix = "ns" + i.intValue();	
				}
			}
		}

		return node.getNodeName();
	}

	private static void _DOMToString(final Node node, final StringBuffer buffer, Map<String, String> namespaceMap) {
		final NamedNodeMap attrs = node.getAttributes();
		if (attrs != null)
			if (namespaceMap != null) {
				Map<String, String> newNamespaceMap = null;
				for (int i = 0; i < attrs.getLength(); i++) {
					// load namespaces
					final Node attr = attrs.item(i);
					if ("xmlns".equals(attr.getPrefix())) {
						final String prefix = attr.getLocalName();
						final String value = attr.getNodeValue();
						final String currentValue = (String) namespaceMap.get(value);

						if (!prefix.equals(currentValue)) {
							if (newNamespaceMap == null)
								newNamespaceMap = new HashMap<String, String>(namespaceMap);

							newNamespaceMap.put(value, prefix);
						}
					}
				}

				if (newNamespaceMap != null)
					namespaceMap = newNamespaceMap;
			}

		switch (node.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
				// 
				buffer.append(getQName(node, namespaceMap)).append("=\"").append(replaceQuotes(node.getNodeValue())).append("\"");
				return;
			case Node.COMMENT_NODE:
				final Comment comment = (Comment) node;
				buffer.append("<!--").append(comment.getNodeValue()).append("-->");
				break;
			case Node.DOCUMENT_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.ELEMENT_NODE:
				buffer.append("<").append(getQName(node, namespaceMap));
				break;
			case Node.TEXT_NODE:
				buffer.append(escapeText(replaceSpaces(node.getNodeValue())));
				break;
			case Node.CDATA_SECTION_NODE:
				buffer.append(toCDATA(node.getNodeValue()));
				break;
		}

		if (attrs != null)
			for (int i = 0; i < attrs.getLength(); i++) {
				final Node attribute = attrs.item(i);
				buffer.append(" ");
				_DOMToString(attribute, buffer, namespaceMap);
			}

		switch (node.getNodeType()) {
			case Node.DOCUMENT_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.ELEMENT_NODE:
				buffer.append(">");
				break;
		}

		final NodeList childNodes = node.getChildNodes();
		if (childNodes != null)
			for (int i = 0; i < childNodes.getLength(); i++) {
				final Node childNode = childNodes.item(i);
				_DOMToString(childNode, buffer, namespaceMap);
			}

		switch (node.getNodeType()) {
			case Node.DOCUMENT_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.ELEMENT_NODE:
				// 
				buffer.append("</").append(getQName(node, namespaceMap)).append(">");
				break;
		}
	}

	public static String DOMToString(final Node node) {
		return DOMToString(node, null);
	}

	public static String DOMToString(final Node node, final Map<String, String> namespaceMap) {
		final StringBuffer buffer = new StringBuffer();
		_DOMToString(node, buffer, namespaceMap);
		return buffer.toString();
	}

	public static String HTMLDOMToString(final Node node) {
		return HTMLDOMToString(node, null);
	}

	public static String HTMLDOMToString(Node node, final Map<String, String> namespaceMap) {
		final StringBuffer buffer = new StringBuffer();
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			buffer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
					+ "\n");
			buffer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+ "\n");
			final Document doc = (Document) node;
			node = doc.getDocumentElement();
		}

		_DOMToString(node, buffer, namespaceMap);
		return buffer.toString();
	}

	public static Node buildDOM(Document owner, Node root, Throwable t)
			throws ParserConfigurationException {
		if (owner == null && root == null)
			owner = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		else if (owner == null)
			owner = root.getOwnerDocument();

		if (root == null)
			if (t instanceof Exception)
				root = owner.createElement("exception");
			else
				root = owner.createElement("throwable");

		final Element element = (Element) root;
		element.setAttribute("className", DOMUtils.escapeText(t.getClass().getName()));
		final String str = t.getMessage();
		if (!(str == null || str.length() == 0 || str.trim().length() == 0)) {
			final Element msg = owner.createElement("message");
			DOMUtils.setNodeValue(msg, DOMUtils.escapeText(t.getMessage()));
			element.appendChild(msg);
		}
		final String str1 = t.getLocalizedMessage();

		if (!(str1 == null || str1.length() == 0 || str1.trim().length() == 0)) {
			final Element msg = owner.createElement("localized-message");
			DOMUtils.setNodeValue(msg, DOMUtils.escapeText(t.getLocalizedMessage()));
			element.appendChild(msg);
		}

		final Element trace = owner.createElement("stacktrace");
		element.appendChild(trace);

		final StackTraceElement[] elements = t.getStackTrace();
		for (final StackTraceElement traceElement : elements) {
			final Element method = owner.createElement("method");
			method.setAttribute("className", DOMUtils.escapeText(traceElement.getClassName()));
			method.setAttribute("name", DOMUtils.escapeText(traceElement.getMethodName()));

			final String fileName = traceElement.getFileName();
			if (!(fileName == null || fileName.length() == 0 || fileName.trim().length() == 0)) {
				method.setAttribute("filename", DOMUtils.escapeText(fileName));
				final int lineNumber = traceElement.getLineNumber();
				if (lineNumber > 0)
					method.setAttribute("line", "" + lineNumber);
			}
		}

		t = t.getCause();
		if (t != null) {
			final Element cause = owner.createElement("cause");
			cause.appendChild(buildDOM(owner, null, t));
			root.appendChild(cause);
		}

		return root;
	}

	public static String buildXML(final Throwable t) {
		final StringBuffer buffer = new StringBuffer(512);
		buildXML(buffer, t);
		return buffer.toString();
	}

	public static void buildXML(final StringBuffer buffer, final Throwable t) {
		if (t instanceof Exception)
			buffer.append("<exception");
		else
			buffer.append("<throwable");

		buffer.append(" className=\"");
		buffer.append(DOMUtils.escapeText(t.getClass().getName()));
		buffer.append("\"");
		buffer.append(">");
		final String str = t.getMessage();

		if (!(str == null || str.length() == 0 || str.trim().length() == 0)) {
			buffer.append("<message>");
			buffer.append(DOMUtils.escapeText(t.getMessage()));
			buffer.append("</message>");
		}
		final String str1 = t.getLocalizedMessage();

		if (!(str1 == null || str1.length() == 0 || str1.trim().length() == 0)) {
			buffer.append("<localized-message>");
			buffer.append(DOMUtils.escapeText(t.getLocalizedMessage()));
			buffer.append("</localized-message>");
		}

		buffer.append("<stacktrace>");
		final StackTraceElement[] elements = t.getStackTrace();
		for (final StackTraceElement traceElement : elements) {
			buffer.append("<method");
			buffer.append(" className=\"");
			buffer.append(DOMUtils.escapeText(traceElement.getClassName()));
			buffer.append("\"");

			buffer.append(" name=\"");
			buffer.append(DOMUtils.escapeText(traceElement.getMethodName()));
			buffer.append("\"");

			final String fileName = traceElement.getFileName();
			if (!(fileName == null || fileName.length() == 0 || fileName.trim().length() == 0)) {
				buffer.append(" filename=\"");
				buffer.append(DOMUtils.escapeText(fileName));
				buffer.append("\"");
				final int lineNumber = traceElement.getLineNumber();
				if (lineNumber > 0) {
					buffer.append(" line=\"");
					buffer.append("").append(lineNumber);
					buffer.append("\"");
				}
			}

			buffer.append("/>");
		}

		buffer.append("</stacktrace>");

		final Throwable innerT = t.getCause();
		if (innerT != null) {
			buffer.append("<cause>");
			buildXML(buffer, innerT);
			buffer.append("</cause>");
		}

		if (t instanceof Exception)
			buffer.append("</exception>");
		else
			buffer.append("</throwable>");
	}

	public static Document createDocument() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (final Exception e) {
			return null;
		}
	}

	public static Document createDocument(final String xml) throws Exception {
		Document doc;
		//convert the result to a Document object
		doc = createDocumentBuilder().parse(
				new InputSource(new StringReader(xml)));
		return doc;
	}
	
	/**
	 * 
	 * The problem was that hibernate.cfg.xml has a DOCTYPE/dtd reference to an external resource.
	 * If you had no Internet connection or that site is down the system would fail to startup.
	 * This code causes the system to find the dtd in hibernate3.jar
	 * 
	 * @param is
	 * @return
	 * @throws Exception 
	 */
	public static Document createDocument(final InputStream is) throws Exception {
		Document doc;

		DocumentBuilder db = createDocumentBuilder();

		db.setEntityResolver(new org.xml.sax.EntityResolver() {

			@Override
			public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId)
					throws org.xml.sax.SAXException, java.io.IOException {
				if (systemId.equals("http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd")) {
					InputStream y = this.getClass().getClassLoader().getResourceAsStream("org/hibernate/hibernate-configuration-3.0.dtd");
					// return a special input source
					//logger.info("Using special entity resolver");
					return new InputSource(y);
				} else
					// use the default behaviour
					// logger.info("Using default entity resolver");
					return null;

			}
		});

 		doc = db.parse(new InputSource(new BufferedReader(
				new InputStreamReader(is))));
		return doc;
	}

	public static Document createDocument(final File file) throws Exception {
		Document doc;
		//convert the result to a Document object
		final FileReader reader = new FileReader(file);
		doc = createDocumentBuilder().parse(
				new InputSource(reader));
		reader.close();
		return doc;
	}

	private static DocumentBuilder createDocumentBuilder() throws Exception {
		final DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		//factory.setIgnoringElementContentWhitespace(true);
		return fact.newDocumentBuilder();
	}

	/**
	 * This method gets a node from a DOM
	 *
	 * @param node the root of the query
	 * @param xpath the query
	 * @return the node found by the query
	 */
	public static Node getNode(final Node node, final String xpath) throws TransformerException {
		try {
			return (Node) factory.newXPath().compile(xpath).evaluate(node, XPathConstants.NODE);
			//return XPathAPI.selectSingleNode(node, xpath);
		} catch (XPathExpressionException ex) {
			Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
			throw new ArahantException(ex);
		}
		//return XPathAPI.selectSingleNode(node, xpath);
	}

	public static Element getElement(Node node, final String xpath) throws TransformerException {
		node = getNode(node, xpath);
		if (node instanceof Element)
			return (Element) node;

		return null;
	}

	public static Element getFirstElement(final Node node) throws TransformerException {
		final NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node tmp = nodes.item(i);
			if (tmp.getNodeType() == Node.ELEMENT_NODE)
				return (Element) tmp;
		}

		return null;
	}

	public static String getValue(final Node node, final String xpath) throws TransformerException {

		try {
			if (xpath.endsWith("text()")) {
				final StringBuilder buffer = new StringBuilder();
				final NodeList nodes = (NodeList) factory.newXPath().compile(xpath).evaluate(node, XPathConstants.NODESET);
				//final NodeList nodes = XPathAPI.selectNodeList(node, xpath);
				for (int i = 0, j = nodes.getLength(); i < j; i++) {
					final Node newNode = nodes.item(i);
					buffer.append(newNode.getNodeValue());
				}

				return buffer.toString().trim();
			} else {
				final Node newNode = (Node) factory.newXPath().compile(xpath).evaluate(node, XPathConstants.NODE);
				//final Node newNode = XPathAPI.selectSingleNode(node, xpath);
				if (newNode == null)
					return "";

				return getNodeValue(newNode);
			}
		} catch (XPathExpressionException e) {
			throw new ArahantException(e);
		}
	}

	public static String getNodeValue(final Node node) {
		if (node == null)
			return "";

		final StringBuffer ret = new StringBuffer();
		switch (node.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
			case Node.TEXT_NODE:
			case Node.DOCUMENT_NODE:
				ret.append(node.getNodeValue());
				break;
			case Node.ELEMENT_NODE:
				final NodeList children = node.getChildNodes();
				for (int i = 0, j = children.getLength(); i < j; i++) {
					final Node tmpNode = children.item(i);
					DOMUtils.convertToString(tmpNode, ret);
				}
		}

		return ret.toString().replaceAll("amp;", "").replaceAll("&apos;", "'").trim();
	}

	public static int getNodeType(final Node node) {
		return node.getNodeType();
	}

	public static String getAttribute(final Element element, final String name) {
		if (element == null)
			return "";
		return element.getAttribute(name);
	}

	public static String getNCName(String name) {
		final int index = name.indexOf(":");
		if (index >= 0)
			name = name.substring(index + 1);

		return name;
	}

	public static String getNamespace(final String name) {
		final int index = name.indexOf(":");
		if (index >= 0)
			return name.substring(0, index);

		return null;
	}

	public static NodeList getNodes(final Node node, final String xpath) throws TransformerException {
		try {
			return (NodeList) factory.newXPath().compile(xpath).evaluate(node, XPathConstants.NODESET);
			//return XPathAPI.selectNodeList(node, xpath);
		} catch (XPathExpressionException ex) {
			Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
			throw new ArahantException(ex);
		}
		//return XPathAPI.selectNodeList(node, xpath);
	}

	public static NodeList getChildNodes(final Node node) {
		return node.getChildNodes();
	}

	public static Document makeDocument(final String xml) throws Exception {
		Document doc;

		//convert the result to a Document object
		doc =
				DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new StringReader(xml)));

		return doc;

	}

	public static String toCDATA(final String text) {
		return CDATA_START + text + CDATA_END;
	}

	public static boolean nodeNameEquals(final Node node, final String nodeNameTest) {
		if (node == null)
			return false;

		final String nodeName = node.getNodeName();
		if (nodeName == null)
			return false;

		if (nodeName.equalsIgnoreCase(nodeNameTest))
			return true;

		return nodeName.toLowerCase().endsWith(":" + nodeNameTest.toLowerCase());
	}

	public static void main(final String[] args) throws Exception {
		final String xml = "<envelope>\r\n"
				+ "	<message><![CDATA[some basic text]]></message>\r\n"
				+ "	<message><![CDATA[some &lt;escaped&gt; text]]></message>\r\n"
				+ "	<message><![CDATA[some <literalXML> and not text]]></message>\r\n"
				+ "</envelope>\r\n"
				+ "";

		final Document doc = DOMUtils.createDocument(xml);

		System.out.println("DOMUtils.format(doc)" + "\n" + "\n" + "\n" + DOMUtils.format(doc));
	}
}
