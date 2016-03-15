package org.jboss.tools.fuse.reddeer.component;

public class Language implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Language";
	}

	@Override
	public String getLabel() {
		return "language:languageName";
	}

	@Override
	public String getTooltip() {
		return "The Language component enables sending org.apache.camel.Exchanges to a given language in order to have a script executed";
	}

}
