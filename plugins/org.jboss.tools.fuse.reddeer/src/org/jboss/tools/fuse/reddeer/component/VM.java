package org.jboss.tools.fuse.reddeer.component;

public class VM implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "VM";
	}

	@Override
	public String getLabel() {
		return "vm:name";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
