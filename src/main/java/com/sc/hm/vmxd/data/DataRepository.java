package com.sc.hm.vmxd.data;

import java.util.HashMap;
import java.util.Map;

import com.sc.hm.vmxd.data.listener.DataInitNotificationListener;
import com.sc.hm.vmxd.data.listener.DataNotificationInfo;

/**
 * Abstract repository class where all the monitoring data will be kept.
 * 
 * The JMX connector thread will periodically query specific resource data from 
 * the remote JVM and add them to specific repository.
 * Repository is a in-memory store. Care must be taken while running monitoring
 * tool for a longer period of time.
 * 
 * A repository can have one or more listener(s) registered to it. All repositories
 * come with a default listener which emits notification post initialization. This
 * notificationn is important, because only then the GUI will know that the resource
 * utilization data is available and thus can be shownn on the UI.
 * 
 * @author Sudiptasish Chanda
 */
public abstract class DataRepository {
	
	private String repository_name = "";
	
	private boolean initializedRepository = false;
	
	protected final Map<DataInitNotificationListener, Object> registeredListener = new HashMap<>();
	
	protected DataRepository (String name) {
		repository_name = name;
	}
	
	/**
	 * Return the name of the repository.
	 * @return
	 */
	public String getRepositoryName() {
		return repository_name;
	}
	
	/**
	 * Register the data notification listener.
	 * 
	 * @param listener
	 * @param handback
	 */
	public void registerListener(DataInitNotificationListener listener, Object handback) {
		registeredListener.put(listener, handback);
	}
	
	/**
	 * Invoke the underlying notification listener indicating that this
	 * repository has been initialized.
	 */
	private void invokeListener() {
		for (Map.Entry<DataInitNotificationListener, Object> me : registeredListener.entrySet()) {
			me.getKey().dataInitialized(
			        new DataNotificationInfo(DataNotificationInfo.DATA_INITIALIZED)
			        , me.getValue());
		}
	}
	
	/**
	 * Check to see if this repository has been initialized.
	 * @return boolean     True, if initialized, False otherwise.
	 */
	public boolean isInitializedRepository() {
		return initializedRepository;
	}

	/**
	 * Mark this repository as initialized.
	 * @param initializedRepository
	 */
	public void setInitializedRepository(boolean initializedRepository) {
		if (this.initializedRepository = initializedRepository) {
			invokeListener();
		}
	}

	/**
	 * Initialize the repository.
	 * User can optionally pass an argument (data) that may be required
	 * at the initialization phase.
	 * 
	 * @param data
	 * @throws Exception
	 */
	public abstract void initializeRepository(Object data) throws Exception;
	
	/**
	 * Add the continuous metrics data to this repository.
	 * The underlying sync mbean process will periodically call this API
	 * to push the latest metrics info.
	 * 
	 * @param name
	 * @param data
	 * @throws Exception
	 */
	public abstract void addData(String name, Object data) throws Exception;
}
