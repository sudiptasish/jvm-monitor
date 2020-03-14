package com.sc.hm.monitor.util.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sc.hm.monitor.util.Logger;
import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;
import com.sc.hm.monitor.util.parser.cobject.ParserNode;
import com.sc.hm.monitor.util.parser.factory.ParserAdapter;

public class XMLDOMParser extends ParserAdapter {

	public XMLDOMParser() {
		super();
	}
	
	public void parse(String xmlFile, CustomizedXMLObject cObject) throws Exception {
		Document document = null;
		
		File file = new File(xmlFile);
		InputStream iStream = null;
		if (!file.exists()) {
			iStream = getClass().getClassLoader().getResourceAsStream(xmlFile);
			if (iStream == null || iStream.available() == 0) {
				URL fileURL = getClass().getClassLoader().getResource(xmlFile);
				String fname = fileURL.getFile();
				fname = fname.replace('\\', '/');
				file = new File(fname);
				if (!file.exists()) {
					throw new Exception("Configuration File Not Found... Exiting from the System!!!!");
				}
				iStream = new FileInputStream(file);
			}
		}
		else {
			iStream = new FileInputStream(file);
		}
		if (iStream != null && iStream.available() > 0) {
			try {
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
				document = domBuilder.parse(iStream);
			}
			catch (ParserConfigurationException pce) {
				throw pce;
			}
			catch (IOException ie) {
				throw ie;
			}
			catch (SAXException sxe) {
				throw sxe;
			}
			finally {
				if (iStream != null) {
					iStream.close();
				}
			}
			loadCustomizedConfiguration(document, cObject);
			Logger.log("Done!!! XML Parsing....");
		}
	}
	
	public void loadCustomizedConfiguration(Document document, CustomizedXMLObject cObject) {
		Element rootElement = document.getDocumentElement();
		Node startNode = null;
		if (rootElement.getNodeName().equals(cObject.getNodeName())) {
			startNode = rootElement;
		}
		else {
			NodeList children = rootElement.getElementsByTagName(cObject.getNodeName());
			if (children == null || children.getLength() == 0) {
				return;
			}
			startNode = children.item(0);
		}
		if (startNode.hasAttributes()) {
			NamedNodeMap namedNodeMap = startNode.getAttributes();
			for (int j = 0; j < namedNodeMap.getLength(); j ++) {
				Node attributeNode = namedNodeMap.item(j);
				cObject.addAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
			}
		}
		if (startNode.hasChildNodes()) {
			_internalLoad((Element)startNode, cObject);
		}
	}
	
	public void _internalLoad(Element parent, CustomizedXMLObject parentObject) {
		NodeList childNodes = parent.getChildNodes();
		int size = childNodes.getLength();
		if (size > 0) {
			parentObject.hasChild(true);
		}
		for (int i = 0; i < size; i ++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				CustomizedXMLObject childObject = new ParserNode(node.getNodeName());
				if (node.hasAttributes()) {
					NamedNodeMap namedNodeMap = node.getAttributes();
					for (int j = 0; j < namedNodeMap.getLength(); j ++) {
						Node attributeNode = namedNodeMap.item(j);
						childObject.addAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
					}
				}
				parentObject.addChild(childObject);
				childObject.setNodeValue(getNodeValue(node));
				_internalLoad((Element)node, childObject);
			}
		}
	}
	
	private String getNodeValue(Node parent) {
		String val = "";
		Node firstChild = parent.getFirstChild();
		if (firstChild == null) {
			return val;
		}
		val = firstChild.getNodeType() == Node.TEXT_NODE && firstChild.getNodeValue() != null ? firstChild.getNodeValue() : "";
			
		return val;
	}
}
