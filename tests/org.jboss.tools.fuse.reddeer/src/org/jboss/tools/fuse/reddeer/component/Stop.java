package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Stop implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Stop";
	}

	@Override
	public String getLabel() {
		return "stop";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
