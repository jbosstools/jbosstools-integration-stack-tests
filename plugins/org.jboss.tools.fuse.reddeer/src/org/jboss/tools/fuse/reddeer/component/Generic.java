package org.jboss.tools.fuse.reddeer.component;

public class Generic implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Generic";
	}

	@Override
	public String getLabel() {
		return "Endpoint";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
