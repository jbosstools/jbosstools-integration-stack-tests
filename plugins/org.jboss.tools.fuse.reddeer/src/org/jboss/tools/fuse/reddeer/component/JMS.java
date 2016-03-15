package org.jboss.tools.fuse.reddeer.component;

public class JMS implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "JMS";
	}

	@Override
	public String getLabel() {
		return "jms:destinationType:destinationName";
	}

	@Override
	public String getTooltip() {
		return "A JMS Component";
	}

}
