package com.sc.hm.monitor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;

public class FileHandler {

	public static void writeFileData(String filename, String data) throws Exception {
		File file = new File(filename);
		if (!file.exists()) {
			URL fileURL = FileHandler.class.getClassLoader().getResource(filename);
			String fname = fileURL.getFile();
			fname = fname.replace('\\', '/');
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(fname))));
			pw.write(data);
			pw.flush();
			pw.close();
		}
		else {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename))));
			pw.write(data);
			pw.flush();
			pw.close();
		}
	}
}
