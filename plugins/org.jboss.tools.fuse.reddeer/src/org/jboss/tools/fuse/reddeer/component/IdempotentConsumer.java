package org.jboss.tools.fuse.reddeer.component;

public class IdempotentConsumer implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Idempotent Consumer";
	}

	@Override
	public String getLabel() {
		return "idempotentConsumer";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
