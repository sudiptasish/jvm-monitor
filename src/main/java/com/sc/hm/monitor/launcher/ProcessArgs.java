package com.sc.hm.monitor.launcher;

import java.text.MessageFormat;

import com.sc.hm.monitor.common.VMConstants;
import com.sc.hm.monitor.config.manager.VMConfigurationUtil;

public class ProcessArgs {

    //private final String START_COMMAND = "cmd.exe /c start {1} -Xms{2} -Xmx{3} -classpath .;{4} {5}";
    private final String START_COMMAND = "{0} {1} -Xms{2} -Xmx{3} -classpath {4} {5}";
    private final String DEBUG_COMMAND = "{0} {1} -Xms{2} -Xmx{3} -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address={4},server=y,suspend=n -classpath {5} {6}";
    
	private String className = "";
	private String[] memoryArgs = null;
	private String[] args = null;
	
	public ProcessArgs() {}
	
	public ProcessArgs(String cName, String[] memoryArgs, String[] args) {
		this.className = cName;
		this.memoryArgs = memoryArgs;
		this.args = args;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

    public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public String generateProgramCommand() {
		String command = "";
		if (Boolean.valueOf(VMConfigurationUtil.getPrimeProperty(VMConstants.XML_ENABLE_PROCESS_DEBUG.getValue()))) {
			try {
				command = MessageFormat.format(DEBUG_COMMAND
				        , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_CMD_LAUNCHER.getValue())
                        , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_COMMAND_OPT.getValue())
		                , memoryArgs[0]
		                , memoryArgs[1]
		                , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_DEBUG_PORT.getValue())
		                , System.getProperty("java.class.path")
		                , className);
			}
			catch (Exception e) {
				e.printStackTrace();
				command = MessageFormat.format(START_COMMAND
				        , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_CMD_LAUNCHER.getValue())
                        , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_COMMAND_OPT.getValue())
                        , memoryArgs[0]
		                , memoryArgs[1]
		                , System.getProperty("java.class.path")
		                , className);
			}
		}
		else {
			command = MessageFormat.format(START_COMMAND
			        , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_CMD_LAUNCHER.getValue())
                    , VMConfigurationUtil.getPrimeProperty(VMConstants.XML_COMMAND_OPT.getValue())
                    , memoryArgs[0]
	                , memoryArgs[1]
	                , System.getProperty("java.class.path")
	                , className);
		}
		StringBuilder sBuilder = new StringBuilder(command.trim());
		
		for (int i = 0; i < this.args.length; i ++) {
			sBuilder.append(" {").append(i).append("}"); 
		}
		command = MessageFormat.format(sBuilder.toString(), args);
		
		return command;
	}
}
