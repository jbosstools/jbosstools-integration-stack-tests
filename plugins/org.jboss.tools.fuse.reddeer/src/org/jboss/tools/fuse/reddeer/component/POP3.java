package org.jboss.tools.fuse.reddeer.component;

public class POP3 implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "POP3";
	}

	@Override
	public String getLabel() {
		return "pop3:host:port";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
