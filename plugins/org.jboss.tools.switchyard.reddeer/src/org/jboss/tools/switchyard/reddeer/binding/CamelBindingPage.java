package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * Camel binding page
 * 
 * @author apodhrad
 * 
 */
public class CamelBindingPage extends OperationOptionsPage<CamelBindingPage> {

	public static final String CONFIG_URI = "Config URI*";

	public CamelBindingPage setConfigURI(String configURI) {
		new LabeledText(CONFIG_URI).setFocus();
		new LabeledText(CONFIG_URI).setText(configURI);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getConfigURI() {
		return new LabeledText(CONFIG_URI).getText();
	}

}
