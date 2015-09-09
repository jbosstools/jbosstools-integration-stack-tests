package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class RepositoryProperties extends Properties {
	
	public void setDescription(String description) {
		new LabeledText("Description").setText(description);
	}

	public void setUserName(String userName) {
		new LabeledText("User Name").setText(userName);
	}
	
	public void setPassword(String password) {
		new LabeledText("Password").setText(password);
	}
}
