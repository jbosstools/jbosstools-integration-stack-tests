package org.jboss.tools.fuse.reddeer.component;

public class LoadBalance implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Load Balance";
	}

	@Override
	public String getLabel() {
		return "loadBalance";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
