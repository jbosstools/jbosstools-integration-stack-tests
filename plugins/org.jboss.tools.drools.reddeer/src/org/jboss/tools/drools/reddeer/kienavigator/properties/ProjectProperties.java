package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ProjectProperties extends Properties {
	
	private static final String PROJECT_NAME = "Project Name";
	private static final String REPOSITORY = "Repository";
	private static final String DESCRIPTION = "Description";
	private static final String GROUP_ID = "Group ID";
	private static final String VERSION = "Version";

	public void setDescription(String description) {
		new LabeledText(DESCRIPTION).setText(description);
	}
	
	public void setGroupId(String groupId) {
		new LabeledText(GROUP_ID).setText(groupId);
	}
	
	public void setVersion(String version) {
		new LabeledText(VERSION).setText(version);
	}	
	
	public String getProjectName() {
		return new LabeledText(PROJECT_NAME).getText();
	}
	
	public String getRepository() {
		return new LabeledText(REPOSITORY).getText();
	}
	
	public String getDescription() {
		return new LabeledText(DESCRIPTION).getText();
	}
	
	public String getGroupId() {
		return new LabeledText(GROUP_ID).getText();
	}
	
	public String getVersion() {
		return new LabeledText(VERSION).getText();
	}
}
