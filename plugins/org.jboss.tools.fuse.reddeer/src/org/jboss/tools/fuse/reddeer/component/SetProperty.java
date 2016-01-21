package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SetProperty implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Set Property";
	}

	@Override
	public String getLabel() {
		return "setProperty";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
