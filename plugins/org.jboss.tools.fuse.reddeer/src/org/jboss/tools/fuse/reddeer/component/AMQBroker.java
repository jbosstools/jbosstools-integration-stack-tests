package org.jboss.tools.fuse.reddeer.component;

public class AMQBroker implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "AMQ Broker";
	}

	@Override
	public String getLabel() {
		return "broker:queue:foo";
	}

	@Override
	public String getTooltip() {
		return "Creates an ActiveMQ broker endpoint...";
	}

}
