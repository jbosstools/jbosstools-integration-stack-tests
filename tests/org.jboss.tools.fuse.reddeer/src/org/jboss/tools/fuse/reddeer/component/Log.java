package org.jboss.tools.fuse.reddeer.component;

public class Log implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Log";
	}

	@Override
	public String getLabel() {
		return "log";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
