package org.jboss.tools.fuse.reddeer.component;

public class RSS implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "RSS";
	}

	@Override
	public String getLabel() {
		return "rss:feedUri";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
