package org.jboss.tools.fuse.reddeer.component;

public class DirectVM implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Direct VM";
	}

	@Override
	public String getLabel() {
		return "direct-vm:name";
	}

	@Override
	public String getTooltip() {
		return "The Direct VM Component manages DirectVmEndpoint and holds the list of named direct-vm endpoints";
	}

}
