package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Throttle implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Throttle";
	}

	@Override
	public String getLabel() {
		return "throttle";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
