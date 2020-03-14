/**
 * 
 */

package com.sc.hm.monitor.util.parser.factory;

import com.sc.hm.monitor.util.parser.XMLSAXParser;
import com.sc.hm.monitor.util.parser.factory.intf.ParserFactoryType;
import com.sc.hm.monitor.util.parser.factory.intf.XMLParser;


public class SAXParserFactory implements ParserFactoryType {
	
	public SAXParserFactory() {}

	public XMLParser newParser() {
		return new XMLSAXParser();
	}

}
