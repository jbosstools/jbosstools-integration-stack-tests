package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class RemoveHeaders implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Remove Headers";
	}

	@Override
	public String getLabel() {
		return "removeHeaders";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
