package org.jboss.tools.fuse.reddeer.component;

public class SFTP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SFTP";
	}

	@Override
	public String getLabel() {
		return "sftp:host:port/di...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
