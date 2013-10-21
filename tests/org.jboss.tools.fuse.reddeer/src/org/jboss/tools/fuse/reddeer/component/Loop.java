package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class Loop implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Loop";
	}

	@Override
	public String getLabel() {
		return "loop[simple{}]";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
