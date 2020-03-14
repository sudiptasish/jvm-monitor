package com.sc.hm.monitor.util.parser;

import java.util.Stack;

import javax.swing.text.Element;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.sc.hm.monitor.util.parser.cobject.CustomizedXMLObject;
import com.sc.hm.monitor.util.parser.factory.ParserAdapter;

public class XMLSAXParser extends ParserAdapter {
	
	private XMLReader xmlReader = null;
	
	public void parse(String xmlFile, CustomizedXMLObject cObject) throws Exception {
		try {
			xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			xmlReader.setContentHandler(new ParserHandler());
			xmlReader.parse(xmlFile);
			//toDo
		}
		catch (Exception e) {}
	}

	public void parse(String xmlFile) throws Exception {
		try {
			xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			xmlReader.setContentHandler(new ParserHandler());
			xmlReader.parse(xmlFile);
			//toDo
		}
		catch (Exception e) {}
	}
	
	private class ParserHandler extends DefaultHandler {
		private Stack<Element> stackElement = new Stack<Element>();
		
		public void startElement (String uri, String localName, String qName, Attributes attributes) {
			//toDo
		}
		
		public void endElement (String uri, String localName, String qName) throws SAXException {
			//toDo
		}
	}
}
