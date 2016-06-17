package org.jboss.tools.fuse.reddeer;

public enum ProjectTemplate {

	CBR("Content Based Router");

	private String name;

	private ProjectTemplate(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
