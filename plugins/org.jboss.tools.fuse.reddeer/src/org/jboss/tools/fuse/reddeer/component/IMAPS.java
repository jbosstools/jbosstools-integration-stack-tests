package org.jboss.tools.fuse.reddeer.component;

public class IMAPS implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "IMAPS";
	}

	@Override
	public String getLabel() {
		return "imaps:host:port";
	}

	@Override
	public String getTooltip() {
		return "Component for JavaMail.";
	}

}
