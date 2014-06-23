package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class RecipientList implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "RecipientList";
	}

	@Override
	public String getLabel() {
		return "recipientList[sim...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
