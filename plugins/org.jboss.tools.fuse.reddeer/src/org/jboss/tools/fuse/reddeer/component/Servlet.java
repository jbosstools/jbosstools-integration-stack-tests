package org.jboss.tools.fuse.reddeer.component;

public class Servlet implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Servlet";
	}

	@Override
	public String getLabel() {
		return "servlet:contextPath";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
