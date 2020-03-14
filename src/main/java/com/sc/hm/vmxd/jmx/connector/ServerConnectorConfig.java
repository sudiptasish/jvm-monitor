package com.sc.hm.vmxd.jmx.connector;

/**
 * Connection configuration for Mbean server.
 * 
 * @author Sudiptasish Chanda
 */
public class ServerConnectorConfig {

	private String application = "";
	private String host = "";
	private String port = "";
	
	private String protocol = "t3";
	private String jndiRoot = "/jndi/";
	private String mserver = "";
	private String user = "";
	private String password = "";
	private String providerPackage = "weblogic.management.remote";
	private Long waitTime = 10000L;
	private String roleName = "";
	private String rolePassword = "";
	
	public ServerConnectorConfig(String application, String host, String port) {
		this.application = application;
		this.host = host;
		this.port = port;
	}

	public ServerConnectorConfig(String application, String host, String port,
            String mserver, String user, String password, String roleName, String rolePassword) {
        
        this.application = application;
        this.host = host;
        this.port = port;
        this.mserver = mserver;
        this.user = user;
        this.password = password;
        this.roleName = roleName;
        this.rolePassword = rolePassword;
    }

    public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	/**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the jndiRoot
     */
    public String getJndiRoot() {
        return jndiRoot;
    }

    /**
     * @param jndiRoot the jndiRoot to set
     */
    public void setJndiRoot(String jndiRoot) {
        this.jndiRoot = jndiRoot;
    }

    /**
     * @return the mserver
     */
    public String getMserver() {
        return mserver;
    }

    /**
     * @param mserver the mserver to set
     */
    public void setMserver(String mserver) {
        this.mserver = mserver;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the providerPackage
     */
    public String getProviderPackage() {
        return providerPackage;
    }

    /**
     * @param providerPackage the providerPackage to set
     */
    public void setProviderPackage(String providerPackage) {
        this.providerPackage = providerPackage;
    }

    /**
     * @return the waitTime
     */
    public Long getWaitTime() {
        return waitTime;
    }

    /**
     * @param waitTime the waitTime to set
     */
    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the rolePassword
     */
    public String getRolePassword() {
        return rolePassword;
    }

    /**
     * @param rolePassword the rolePassword to set
     */
    public void setRolePassword(String rolePassword) {
        this.rolePassword = rolePassword;
    }

    public String toString() {
		return new StringBuilder().append("[").append(application).append(" : ").append(host).append(" : ").append(port).append("]").toString();
	}
}
