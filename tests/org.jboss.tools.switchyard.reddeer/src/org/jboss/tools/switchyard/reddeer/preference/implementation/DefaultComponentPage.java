package org.jboss.tools.switchyard.reddeer.preference.implementation;

import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Represents a properties page "Component".
 * 
 * @author tsedmik
 */
public class DefaultComponentPage {
	
	public DefaultComponentPage setName(String name) {
		
		new DefaultText(1).setText(name);
		return this;
	}
	
	public String getName() {
		
		return new DefaultText(1).getText();
	}
}
