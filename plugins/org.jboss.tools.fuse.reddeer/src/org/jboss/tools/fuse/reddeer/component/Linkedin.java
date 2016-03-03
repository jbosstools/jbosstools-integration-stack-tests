package org.jboss.tools.fuse.reddeer.component;

public class Linkedin implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Linkedin";
	}

	@Override
	public String getLabel() {
		return "linkedin:apiName/...";
	}

	@Override
	public String getTooltip() {
		return "Represents the component that manages LinkedInEndpoint";
	}

}
