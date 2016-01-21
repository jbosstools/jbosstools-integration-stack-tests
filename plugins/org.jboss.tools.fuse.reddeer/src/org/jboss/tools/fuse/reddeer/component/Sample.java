package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Sample implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Sample";
	}

	@Override
	public String getLabel() {
		return "sample";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
