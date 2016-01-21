package org.jboss.tools.fuse.reddeer.component;

public class Direct implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Direct";
	}

	@Override
	public String getLabel() {
		return "direct:name";
	}

	@Override
	public String getTooltip() {
		return "The Direct Component manages DirectEndpoint and holds the list of named direct endpoints";
	}

}
