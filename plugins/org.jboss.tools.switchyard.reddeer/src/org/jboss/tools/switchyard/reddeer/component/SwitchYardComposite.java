package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;

public class SwitchYardComposite extends SwitchYardComponent {

	public SwitchYardComposite(String tooltip) {
		super(tooltip);
	}

	@Override
	public CompositePropertiesPage showProperties() {
		getContextButton("Properties").click();
		return new CompositePropertiesPage("").activate();
	}
}
