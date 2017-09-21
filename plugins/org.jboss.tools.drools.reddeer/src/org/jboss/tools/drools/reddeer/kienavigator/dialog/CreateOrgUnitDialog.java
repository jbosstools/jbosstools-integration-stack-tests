package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class CreateOrgUnitDialog extends Dialog {

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setDescription(String description) {
		new LabeledText("Description:").setText(description);
	}

	public void setOwner(String owner) {
		new LabeledText("Owner:").setText(owner);
	}

	public void setDefaultGroupId(String groupId) {
		new LabeledText("Default Group ID:").setText(groupId);
	}
}
