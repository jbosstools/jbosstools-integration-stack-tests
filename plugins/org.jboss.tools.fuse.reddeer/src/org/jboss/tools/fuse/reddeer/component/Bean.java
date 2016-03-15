package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Bean implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Bean";
	}

	@Override
	public String getLabel() {
		return "Bean _bean1";
	}

	@Override
	public String getTooltip() {
		return "Calls a java bean";
	}

}
