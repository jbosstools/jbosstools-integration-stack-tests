package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class CreateProjectDialog extends Dialog {
	
	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}
	
	public void setDescription(String description) {
		new LabeledText("Description:").setText(description);
	}
	
	public void setGroupId(String groupId) {
		new LabeledText("Group ID:").setText(groupId);
	}
	
	public void setArtifactId(String artifactId) {
		new LabeledText("Artifact ID:").setText(artifactId);
	}
	
	public void setVersion(String version) {
		new LabeledText("Version:").setText(version);
	}
	
	public void importProjectToWorkspace(boolean importToWorkspace) {
		new LabeledCheckBox("Import the Project into my Workspace when done").toggle(importToWorkspace);
	}
}
