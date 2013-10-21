package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class InterceptSendToEndpoint implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "InterceptSendToEndpoint";
	}

	@Override
	public String getLabel() {
		return "intercept";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
