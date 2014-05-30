package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class RemoveHeader implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "RemoveHeader";
	}

	@Override
	public String getLabel() {
		return "remove header";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
