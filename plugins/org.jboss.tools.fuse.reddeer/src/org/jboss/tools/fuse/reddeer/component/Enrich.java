package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Enrich implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Enrich";
	}

	@Override
	public String getLabel() {
		return "enrich";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
