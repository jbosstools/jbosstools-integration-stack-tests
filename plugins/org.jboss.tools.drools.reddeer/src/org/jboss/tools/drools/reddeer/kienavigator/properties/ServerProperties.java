package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ServerProperties extends Properties {
	
	public void setUsername(String name) {
		new LabeledText("Username:").setText(name);
	}
	
	public void setPassword(String name) {
		new LabeledText("Password:").setText(name);
	}
	
	public void useTrustedConnection(boolean checked) {
		new LabeledCheckBox("Trust connections to this Server").toggle(checked);
	}
	
	public void setApplicationName(String name) {
		new LabeledText("KIE Application Name:").setText(name);
	}
	
	public void setHttpPort(String portNumber) {
		new LabeledText("HTTP Port:").setText(portNumber);
	}
	
	public void setGitPort(String gitPortNumber) {
		new LabeledText("Git Port:").setText(gitPortNumber);
	}
	
	public void useDefaultRepoPath(boolean useDefaultPath) {
		new LabeledCheckBox("Use default Git Repository Path").toggle(useDefaultPath);
	}
	
	public void setGitRepoPath(String path) {
		new LabeledText("Git Repository Path").setText(path);
	}
}
