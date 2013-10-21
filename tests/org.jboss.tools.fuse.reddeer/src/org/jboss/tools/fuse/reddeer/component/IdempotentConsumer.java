package org.jboss.tools.fuse.reddeer.component;

public class IdempotentConsumer implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "IdempotentConsumer";
	}

	@Override
	public String getLabel() {
		return "idempotentConsume...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
