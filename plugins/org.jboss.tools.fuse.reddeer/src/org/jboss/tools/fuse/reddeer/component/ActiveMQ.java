package org.jboss.tools.fuse.reddeer.component;

public class ActiveMQ implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "ActiveMQ";
	}

	@Override
	public String getLabel() {
		return "activemq:queue:foo";
	}

	@Override
	public String getTooltip() {
		return "Creates an ActiveMQ endpoint...";
	}

}
