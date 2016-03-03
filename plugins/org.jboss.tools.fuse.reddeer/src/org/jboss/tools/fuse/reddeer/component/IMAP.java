package org.jboss.tools.fuse.reddeer.component;

public class IMAP implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "IMAP";
	}

	@Override
	public String getLabel() {
		return "imap:host:port";
	}

	@Override
	public String getTooltip() {
		return "Component for JavaMail.";
	}

}
