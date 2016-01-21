package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class PollEnrich implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Poll Enrich";
	}

	@Override
	public String getLabel() {
		return "pollEnrich";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
