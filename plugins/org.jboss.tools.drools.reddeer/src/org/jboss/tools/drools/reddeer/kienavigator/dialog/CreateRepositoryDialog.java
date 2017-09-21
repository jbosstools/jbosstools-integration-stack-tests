package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class CreateRepositoryDialog extends Dialog {

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setDescription(String description) {
		new LabeledText("Description:").setText(description);
	}

	public void setUsername(String username) {
		new LabeledText("Username:").setText(username);
	}

	public void setPassword(String password) {
		new LabeledText("Password:").setText(password);
	}

	public void createNewRepository() {
		new RadioButton("Create New Repository").click();
	}

	public void cloneAnExistingRepository() {
		new RadioButton("Clone an existing Repository").click();
	}

	public void setRepositoryUrl(String url) {
		new LabeledText("URL of a Repository to clone:").setText(url);
	}
}
