package org.jboss.tools.fuse.reddeer.component;

public class SMTPS implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SMTPS";
	}

	@Override
	public String getLabel() {
		return "smtps:host:port";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
