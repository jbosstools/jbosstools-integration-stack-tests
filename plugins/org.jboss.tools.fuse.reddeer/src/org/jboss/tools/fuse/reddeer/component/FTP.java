package org.jboss.tools.fuse.reddeer.component;

public class FTP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "FTP";
	}

	@Override
	public String getLabel() {
		return "ftp:host:port/directoryName";
	}

	@Override
	public String getTooltip() {
		return "FTP Component";
	}

}
