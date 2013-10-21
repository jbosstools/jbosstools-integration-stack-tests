package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Sampling implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Sampling";
	}

	@Override
	public String getLabel() {
		return "sample[1 Exchange...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
