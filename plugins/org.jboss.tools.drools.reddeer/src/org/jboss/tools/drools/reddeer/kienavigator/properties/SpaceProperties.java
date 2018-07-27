package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class SpaceProperties extends Properties {

	private static final String SPACE_NAME = "Space Name";
	private static final String DEFAULT_GID = "defaultGroupId";
	private static final String OWNER = "owner";

	public void setOwner(String owner) {
		new LabeledText(OWNER).setText(owner);
	}

	public void setDefaultGroupId(String defaultGroupId) {
		new LabeledText(DEFAULT_GID).setText(defaultGroupId);
	}

	public String getSpaceName() {
		return new LabeledText(SPACE_NAME).getText();
	}

	public String getOwner() {
		return new LabeledText(OWNER).getText();
	}

	public String getDefaultGroupId() {
		return new LabeledText(DEFAULT_GID).getText();
	}
}
