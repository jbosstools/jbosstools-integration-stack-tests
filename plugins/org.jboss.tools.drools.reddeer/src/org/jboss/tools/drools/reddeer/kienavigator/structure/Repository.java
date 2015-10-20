package org.jboss.tools.drools.reddeer.kienavigator.structure;

public class Repository {
	
	private String name;
	
	private String description;
	
	private String username;
	
	private String password;
	
	private String requestType;
	
	private String gitUrl;
	
	public Repository(String[] restResponse) {
		this.name = restResponse[0].split(":")[1];
		this.description = restResponse[1].split(":")[1];
		this.username = restResponse[2].split(":")[1];
		this.password = restResponse[3].split(":")[1];
		this.requestType = restResponse[4].split(":")[1];
		this.gitUrl = restResponse[5].replace("gitURL:", "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}
}
