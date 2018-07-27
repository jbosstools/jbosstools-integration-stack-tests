package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class CreateRepositoryDialog extends Dialog {

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setDescription(String description) {
		new LabeledText("Description:").setText(description);
	}

	public void setGroupId(String groupId) {
		new LabeledText("Group ID:").setText(groupId);
	}

	public void setVersion(String version) {
		new LabeledText("Version:").setText(version);
	}
}
