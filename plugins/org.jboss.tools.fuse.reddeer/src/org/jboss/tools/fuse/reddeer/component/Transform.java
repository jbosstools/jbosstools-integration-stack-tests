package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Transform implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Transform";
	}

	@Override
	public String getLabel() {
		return "transform[simple{}]";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
