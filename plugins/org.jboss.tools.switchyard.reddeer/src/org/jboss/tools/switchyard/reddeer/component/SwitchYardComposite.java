package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesDialog;

public class SwitchYardComposite extends SwitchYardComponent {

	public SwitchYardComposite(String tooltip) {
		super(tooltip);
	}

	@Override
	public CompositePropertiesDialog showProperties() {
		getContextButton("Properties").click();
		return new CompositePropertiesDialog("").activate();
	}
}
