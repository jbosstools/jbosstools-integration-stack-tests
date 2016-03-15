package org.jboss.tools.fuse.reddeer.component;

public class Netty4HTTP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Netty4 HTTP";
	}

	@Override
	public String getLabel() {
		return "netty4-http:protocol:host:port/path";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
