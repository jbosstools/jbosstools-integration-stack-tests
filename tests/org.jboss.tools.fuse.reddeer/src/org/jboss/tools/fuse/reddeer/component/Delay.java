package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Delay implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Delay";
	}

	@Override
	public String getLabel() {
		return "delay[simple{}]";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
