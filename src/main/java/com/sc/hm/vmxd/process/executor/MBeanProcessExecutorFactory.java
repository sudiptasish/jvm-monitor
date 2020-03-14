package com.sc.hm.vmxd.process.executor;

public class MBeanProcessExecutorFactory {

	private static final MBeanProcessExecutorFactory executorFactory = new MBeanProcessExecutorFactory();
	
	private MBeanProcessExecutorFactory() {}
	
	public static synchronized MBeanProcessExecutorFactory getExecutorFactory() {
		return executorFactory;
	}
	
	public AbstractMBeanProcessExecutor getMBeanExecutor(String executor_name) {
		return new MBeanProcessExecutor(executor_name);
	}
	
	public AbstractMBeanProcessExecutor getMBeanExecutor(String executor_name, int executor_count) {
		return new MBeanProcessExecutor(executor_name, executor_count);
	}
}
