package org.jboss.tools.fuse.reddeer.component;

public class NettyHTTP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Netty HTTP";
	}

	@Override
	public String getLabel() {
		return "netty-http:host:p...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
