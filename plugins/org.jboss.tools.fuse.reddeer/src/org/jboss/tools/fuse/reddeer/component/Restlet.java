package org.jboss.tools.fuse.reddeer.component;

public class Restlet implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Restlet";
	}

	@Override
	public String getLabel() {
		return "restlet:protocol:...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
