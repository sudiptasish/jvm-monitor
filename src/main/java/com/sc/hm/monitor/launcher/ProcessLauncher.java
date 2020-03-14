package com.sc.hm.monitor.launcher;

import java.io.File;

import com.sc.hm.monitor.util.Logger;

public class ProcessLauncher {

	public static void launchProcess(ProcessArgs pArgs) {
		ProcessBuilder processBuilder = new ProcessBuilder(pArgs.generateProgramCommand());
		try {
			String currentDir = System.getProperty("user.dir");
			currentDir = currentDir.replace('\\', '/');
			Logger.log("Current Directory: " + currentDir);
			processBuilder.directory(new File(currentDir));
			
			Process process = Runtime.getRuntime().exec(pArgs.generateProgramCommand());
			Logger.log("Started Process.. [" + pArgs.generateProgramCommand() + "]");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
