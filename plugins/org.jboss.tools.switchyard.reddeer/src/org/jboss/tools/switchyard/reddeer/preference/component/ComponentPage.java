package org.jboss.tools.switchyard.reddeer.preference.component;

import org.eclipse.reddeer.swt.impl.text.DefaultText;

/**
 * Represents a properties page "Component".
 * 
 * @author tsedmik, apodhrad
 */
public class ComponentPage {

	public ComponentPage setName(String name) {
		new DefaultText(1).setText(name);
		return this;
	}

	public String getName() {
		return new DefaultText(1).getText();
	}
}
