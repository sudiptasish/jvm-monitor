package com.sc.hm.monitor.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public interface ITransportSocketType {

	public void setHost(InetAddress host);
	
	public void setPort(int port);
	
	public void initializeSocket();
	
	public void closeTransportClient();
	
	public OutputStream getTransportOutputStream() throws IOException;
	
	public InputStream getTransportInputStream() throws IOException;
}
