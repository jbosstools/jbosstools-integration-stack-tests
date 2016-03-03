package org.jboss.tools.fuse.reddeer.component;

public class CXF implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "CXF";
	}

	@Override
	public String getLabel() {
		return "cxf:beanId:address";
	}

	@Override
	public String getTooltip() {
		return "Defines the CXF Component";
	}

}
