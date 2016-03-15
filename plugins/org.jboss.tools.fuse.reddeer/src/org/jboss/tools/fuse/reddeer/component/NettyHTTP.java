package org.jboss.tools.fuse.reddeer.component;

public class NettyHTTP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Netty HTTP";
	}

	@Override
	public String getLabel() {
		return "netty-http:protocol:host:port/path";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
