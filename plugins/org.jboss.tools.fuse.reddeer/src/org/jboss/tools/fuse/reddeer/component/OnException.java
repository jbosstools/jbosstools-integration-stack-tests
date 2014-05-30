package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class OnException implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "OnException";
	}

	@Override
	public String getLabel() {
		return "on exception []";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
