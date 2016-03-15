package org.jboss.tools.fuse.reddeer.component;

public class Salesforce implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Salesforce";
	}

	@Override
	public String getLabel() {
		return "salesforce:operationName:topicName";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
