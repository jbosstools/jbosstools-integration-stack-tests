package org.jboss.tools.runtime.reddeer;

public class Remote {

	protected String host;
	private String remoteHome;
	private String username;
	private String password;
	private boolean isExternallyManaged;
	private boolean useManagementOperations;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRemoteHome() {
		return remoteHome;
	}

	public void setRemoteHome(String remoteHome) {
		this.remoteHome = remoteHome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isExternallyManaged() {
		return isExternallyManaged;
	}

	public void setExternallyManaged(boolean isExternallyManaged) {
		this.isExternallyManaged = isExternallyManaged;
	}

	public boolean isUseManagementOperations() {
		return useManagementOperations;
	}

	public void setUseManagementOperations(boolean useManagementOperations) {
		this.useManagementOperations = useManagementOperations;
	}

}
