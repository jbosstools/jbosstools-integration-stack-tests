package org.jboss.tools.fuse.reddeer.component;

public class SQL implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SQL";
	}

	@Override
	public String getLabel() {
		return "sql:query";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
