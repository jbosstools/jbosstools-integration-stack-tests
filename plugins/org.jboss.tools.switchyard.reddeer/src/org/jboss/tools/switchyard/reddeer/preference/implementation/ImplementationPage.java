package org.jboss.tools.switchyard.reddeer.preference.implementation;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Represents a properties page "Implementation".
 * 
 * @author tsedmik
 */
public class ImplementationPage {

	private static final String BUTTON_BROWSE = "Browse...";

	public String getBeanClass() {
		return new DefaultText(1).getText();
	}

	public boolean isBrowseButtonEnabled() {
		return new PushButton(BUTTON_BROWSE).isEnabled();
	}
}
