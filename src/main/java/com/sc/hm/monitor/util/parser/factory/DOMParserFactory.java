/**
 * 
 */

package com.sc.hm.monitor.util.parser.factory;

import com.sc.hm.monitor.util.parser.XMLDOMParser;
import com.sc.hm.monitor.util.parser.factory.intf.ParserFactoryType;
import com.sc.hm.monitor.util.parser.factory.intf.XMLParser;


public class DOMParserFactory implements ParserFactoryType {

	public DOMParserFactory() {}

	public XMLParser newParser() {
		return new XMLDOMParser();
	}	
	
}
