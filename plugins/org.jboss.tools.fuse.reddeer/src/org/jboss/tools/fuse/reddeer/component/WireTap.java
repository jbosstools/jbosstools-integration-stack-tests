package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class WireTap implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Wire Tap";
	}

	@Override
	public String getLabel() {
		return "wireTap";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
