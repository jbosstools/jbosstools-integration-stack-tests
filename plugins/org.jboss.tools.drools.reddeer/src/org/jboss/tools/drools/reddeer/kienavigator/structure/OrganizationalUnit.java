package org.jboss.tools.drools.reddeer.kienavigator.structure;

import java.util.ArrayList;
import java.util.List;

public class OrganizationalUnit {

	private String name;

	private String description;

	private String owner;

	private String groupId;

	private List<String> repositories;

	public OrganizationalUnit(String[] restResponse) {
		this.name = restResponse[0].split(":")[1];
		this.description = restResponse[1].split(":")[1];
		this.owner = restResponse[2].split(":")[1];
		this.groupId = restResponse[3].split(":")[1];

		this.repositories = new ArrayList<String>();
		if (!restResponse[4].equals("repositories:")) {
			for (int i = 4; i < restResponse.length; i++) {
				repositories.add(restResponse[i].replace("repositories:", ""));
			}
		}
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<String> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<String> repositories) {
		this.repositories = repositories;
	}
}
