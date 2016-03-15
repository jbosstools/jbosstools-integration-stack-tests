package org.jboss.tools.fuse.reddeer.component;

public class JDBC implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "JDBC";
	}

	@Override
	public String getLabel() {
		return "jdbc:dataSourceName";
	}

	@Override
	public String getTooltip() {
		return "Camel JDBC support";
	}

}
