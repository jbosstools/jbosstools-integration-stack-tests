package org.jboss.tools.fuse.reddeer.component;

public class Quartz implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Quartz";
	}

	@Override
	public String getLabel() {
		return "quartz:groupName/...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
