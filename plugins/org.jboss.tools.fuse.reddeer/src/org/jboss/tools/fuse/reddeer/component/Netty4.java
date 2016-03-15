package org.jboss.tools.fuse.reddeer.component;

public class Netty4 implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Netty4";
	}

	@Override
	public String getLabel() {
		return "netty4:protocol:host:port";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
