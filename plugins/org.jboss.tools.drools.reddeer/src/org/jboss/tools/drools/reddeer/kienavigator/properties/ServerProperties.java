package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ServerProperties extends Properties {
	
	private static final String SERVER_NAME = "Server Name:";
	private static final String HOST_NAME = "Host Name:";
	private static final String USERNAME = "Username:";
	private static final String PASSWORD = "Password:";
	private static final String TRUST_CONNECTION = "Trust connections to this Server";
	private static final String APP_NAME = "KIE Application Name:";
	private static final String HTTP_PORT = "HTTP Port:";
	private static final String GIT_PORT = "Git Port:";
	private static final String USE_DEFAULT_REPO = "Use default Git Repository Pat";
	private static final String GIT_REPO_PATH = "Git Repository Path";
	
	public void setUsername(String name) {
		new LabeledText(USERNAME).setText(name);
	}
	
	public void setPassword(String name) {
		new LabeledText(PASSWORD).setText(name);
	}
	
	public void useTrustedConnection(boolean checked) {
		new CheckBox(TRUST_CONNECTION).toggle(checked);
	}
	
	public void setApplicationName(String name) {
		new LabeledText(APP_NAME).setText(name);
	}
	
	public void setHttpPort(String portNumber) {
		new LabeledText(HTTP_PORT).setText(portNumber);
	}
	
	public void setGitPort(String gitPortNumber) {
		new LabeledText(GIT_PORT).setText(gitPortNumber);
	}
	
	public void useDefaultRepoPath(boolean useDefaultPath) {
		new CheckBox(USE_DEFAULT_REPO).toggle(useDefaultPath);
	}
	
	public void setGitRepoPath(String path) {
		new LabeledText(GIT_REPO_PATH).setText("");
		new LabeledText(GIT_REPO_PATH).typeText(path);
	}
	
	public String getServerName() {
		return new LabeledText(SERVER_NAME).getText();
	}
	
	public String getHostName() {
		return new LabeledText(HOST_NAME).getText();
	}
	
	public String getUsername() {
		return new LabeledText(USERNAME).getText();
	}
	
	public boolean isTrustConnectionUsed() {
		return new CheckBox(TRUST_CONNECTION).isChecked();
	}
	
	public String getKieAppName() {
		return new LabeledText(APP_NAME).getText();
	}
	
	public String getHttpPort() {
		return new LabeledText(HTTP_PORT).getText();
	}
	
	public String getGitPort() {
		return new LabeledText(GIT_PORT).getText();
	}
	
	public boolean isDefaultRepoPathUsed() {
		return new CheckBox(USE_DEFAULT_REPO).isChecked();
	}
	
	public String getGitRepoPath() {
		return new LabeledText(GIT_REPO_PATH).getText();
	}
}
