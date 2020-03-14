package com.sc.hm.monitor.util.parser.factory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sc.hm.monitor.util.parser.factory.intf.ParserFactoryType;

public abstract class AbstractXMLParserFactory {

	private static String PARSER_FACTORY = "";
	
	private static AbstractXMLParserFactory _FACTORY_ = null;
	
	private static Lock mLock = new ReentrantLock();
	
	public static AbstractXMLParserFactory getXMLParserFactory() throws Exception {
		if (_FACTORY_ == null) {
			try {
				mLock.lock();
				_FACTORY_ = new XMLParserFactory();
			}
			finally {
				mLock.unlock();
			}
		}
		return _FACTORY_;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static String getParserFactory() {
		return PARSER_FACTORY;
	}

	public static void setParserFactory(String parser_factory) {
		PARSER_FACTORY = parser_factory;
	}
	
	public abstract ParserFactoryType getParserFactory(String FACTORY_TYPE);
}
