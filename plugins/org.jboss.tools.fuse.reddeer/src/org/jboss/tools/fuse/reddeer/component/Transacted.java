package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Transacted implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Transacted";
	}

	@Override
	public String getLabel() {
		return "transacted[]";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
