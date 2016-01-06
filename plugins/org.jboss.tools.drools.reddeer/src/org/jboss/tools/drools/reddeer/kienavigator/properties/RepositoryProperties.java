package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class RepositoryProperties extends Properties {

	private static final String REPO_NAME = "Repository Name";
	private static final String ORG_UNIT = "Organizational Unit";
	private static final String REMOTE_GIT_URL = "Remote Git URL";
	private static final String LOCAL_GIT_URL = "Local Git Directory";
	private static final String DESCRIPTION = "Description";
	private static final String USER_NAME = "User Name";
	private static final String PASSWORD = "Password";

	public void setDescription(String description) {
		new LabeledText(DESCRIPTION).setText(description);
	}

	public void setUserName(String userName) {
		new LabeledText(USER_NAME).setText(userName);
	}

	public void setPassword(String password) {
		new LabeledText(PASSWORD).setText(password);
	}

	public String getRepositoryName() {
		return new LabeledText(REPO_NAME).getText();
	}

	public String getOrganizationalUnit() {
		return new LabeledText(ORG_UNIT).getText();
	}

	public String getRemoteUrl() {
		return new LabeledText(REMOTE_GIT_URL).getText();
	}

	public String getLocalUrl() {
		return new LabeledText(LOCAL_GIT_URL).getText();
	}

	public String getDescription() {
		return new LabeledText(DESCRIPTION).getText();
	}

	public String getUserName() {
		return new LabeledText(USER_NAME).getText();
	}
}
