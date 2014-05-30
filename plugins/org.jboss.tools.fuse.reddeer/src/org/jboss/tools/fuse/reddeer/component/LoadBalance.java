package org.jboss.tools.fuse.reddeer.component;

public class LoadBalance implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "LoadBalance";
	}

	@Override
	public String getLabel() {
		return "load balance";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
