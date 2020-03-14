package com.sc.hm.vmxd.data.listener;

/**
 * Notification listener that will emit evennt once a repository is initialized.
 * 
 * @author Sudiptasish Chanda
 */
public interface DataInitNotificationListener {

    /**
     * A listener which is used to emit notification upon
     * successful initialization of a repository.
     * 
     * @param notification
     * @param handback
     */
	public void dataInitialized(DataNotification notification, Object handback);
}
