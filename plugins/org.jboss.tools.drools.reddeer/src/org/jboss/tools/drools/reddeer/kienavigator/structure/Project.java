package org.jboss.tools.drools.reddeer.kienavigator.structure;

public class Project {

	private String name;

	private String description;

	private String groupId;

	private String version;

	public Project(String[] restResponse) {
		this.name = restResponse[0].split(":")[1];
		this.description = restResponse[1].split(":")[1];
		this.groupId = restResponse[2].split(":")[1];
		this.version = restResponse[3].split(":")[1];
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
