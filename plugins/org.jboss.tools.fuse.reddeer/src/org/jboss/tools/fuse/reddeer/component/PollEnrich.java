package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class PollEnrich implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "PollEnrich";
	}

	@Override
	public String getLabel() {
		return "poll enrich";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
