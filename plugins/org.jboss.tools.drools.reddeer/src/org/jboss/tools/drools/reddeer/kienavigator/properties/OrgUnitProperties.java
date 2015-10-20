package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class OrgUnitProperties extends Properties {
	
	private static final String ORG_UNIT = "Organization Name";
	private static final String DEFAULT_GID = "Default Group ID";
	private static final String OWNER = "Owner";
	
	public void setOwner(String owner) {
		new LabeledText(OWNER).setText(owner);
	}

	public void setDefaultGroupId(String defaultGroupId) {
		new LabeledText(DEFAULT_GID).setText(defaultGroupId);
	}
	
	public String getOrganizationName() {
		return new LabeledText(ORG_UNIT).getText();
	}
	
	public String getOwner() {
		return new LabeledText(OWNER).getText();
	}
	
	public String getDefaultGroupId() {
		return new LabeledText(DEFAULT_GID).getText();
	}
}
