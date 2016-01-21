package org.jboss.tools.fuse.reddeer.component;

public class CXFRS implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "CXF-RS";
	}

	@Override
	public String getLabel() {
		return "cxfrs:beanId:address";
	}

	@Override
	public String getTooltip() {
		return "Defines the CXF RS Component";
	}

}
