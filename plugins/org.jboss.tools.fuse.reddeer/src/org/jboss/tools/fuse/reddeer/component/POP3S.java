package org.jboss.tools.fuse.reddeer.component;

public class POP3S implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "POP3S";
	}

	@Override
	public String getLabel() {
		return "pop3s:host:port";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
