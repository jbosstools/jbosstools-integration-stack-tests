package org.jboss.tools.switchyard.reddeer.preference.implementation;


/**
 * Represents a properties page "Implementation --> Security Policy".
 * 
 * @author apodhrad
 */
public class DefaultImplementationSecurityPage extends DefaultPage {
	
	private static final String AUTHORIZATION = "Requires Authorization";
		
	public boolean isAuthorizationChecked() {
		
		return isCheckBoxChecked(AUTHORIZATION);
	}
	
	public boolean isAuthorizationEnabled() {
		
		return isCheckBoxEnabled(AUTHORIZATION);
	}
	
	public DefaultImplementationSecurityPage setAuthorization(boolean value) {
		
		setCheckBox(AUTHORIZATION, value);
		return this;
	}
	
}
