package org.jboss.tools.switchyard.reddeer.preference.component;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Represents a properties page "Component".
 * 
 * @author tsedmik, apodhrad
 */
public class ComponentPage extends PreferencePage {

	public ComponentPage setName(String name) {
		new DefaultText(1).setText(name);
		return this;
	}

	public String getName() {
		return new DefaultText(1).getText();
	}
}
