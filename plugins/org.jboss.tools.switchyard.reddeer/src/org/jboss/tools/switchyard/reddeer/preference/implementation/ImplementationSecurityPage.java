package org.jboss.tools.switchyard.reddeer.preference.implementation;

import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * Represents a properties page "Implementation --> Security Policy".
 * 
 * @author apodhrad
 */
public class ImplementationSecurityPage {

	private static final String AUTHORIZATION = "Requires Authorization";

	public boolean isAuthorizationChecked() {
		return new CheckBox(AUTHORIZATION).isChecked();
	}

	public boolean isAuthorizationEnabled() {
		return new CheckBox(AUTHORIZATION).isEnabled();
	}

	public ImplementationSecurityPage setAuthorization(boolean value) {
		new CheckBox(AUTHORIZATION).toggle(value);
		return this;
	}

}
