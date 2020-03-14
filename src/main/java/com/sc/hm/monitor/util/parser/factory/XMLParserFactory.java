package com.sc.hm.monitor.util.parser.factory;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.util.parser.factory.intf.ParserFactoryType;

public class XMLParserFactory extends AbstractXMLParserFactory {

	public XMLParserFactory() {
		super();
	}
	
	public ParserFactoryType getParserFactory(String FACTORY_TYPE) {
		if (FACTORY_TYPE.equalsIgnoreCase(VMConstants.PARSER_FACTORY_TYPE_DOM.getValue())) {
			return new DOMParserFactory();
		}
		else if (FACTORY_TYPE.equalsIgnoreCase(VMConstants.PARSER_FACTORY_TYPE_SAX.getValue())) {
			return new SAXParserFactory();
		}
		else {
			return null;
		}
	}
}
