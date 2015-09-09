package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class OrgUnitProperties extends Properties {
	
	public void setOwner(String owner) {
		new LabeledText("Owner").setText(owner);
	}

	public void setDefaultGroupId(String defaultGroupId) {
		new LabeledText("Default Group ID").setText(defaultGroupId);
	}
}
