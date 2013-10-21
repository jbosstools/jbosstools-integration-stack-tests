package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Rollback implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Rollback";
	}

	@Override
	public String getLabel() {
		return "rollback";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
