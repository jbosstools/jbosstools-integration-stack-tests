package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class InterceptSendToEndpoint implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Intercept Send To Endpoint";
	}

	@Override
	public String getLabel() {
		return "interceptSendToEn...";
	}

	@Override
	public String getTooltip() {
		return null;
	}

}
