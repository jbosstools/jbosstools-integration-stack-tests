package org.jboss.tools.fuse.reddeer.component;

public class SEDA implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SEDA";
	}

	@Override
	public String getLabel() {
		return "seda:name";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
