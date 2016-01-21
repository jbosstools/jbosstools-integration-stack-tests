package org.jboss.tools.fuse.reddeer.component;

public class SAPNetWeaver implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP NetWeaver";
	}

	@Override
	public String getLabel() {
		return "sap-netweaver:url";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
