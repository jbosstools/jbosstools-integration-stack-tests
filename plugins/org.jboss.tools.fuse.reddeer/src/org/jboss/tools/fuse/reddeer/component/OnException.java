package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class OnException implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "On Exception";
	}

	@Override
	public String getLabel() {
		return "onException";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
