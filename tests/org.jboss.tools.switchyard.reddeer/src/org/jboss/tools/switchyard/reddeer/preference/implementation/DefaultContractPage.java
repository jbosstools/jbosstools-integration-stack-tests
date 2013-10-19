package org.jboss.tools.switchyard.reddeer.preference.implementation;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents a properties page "Contract".
 *  
 * @author tsedmik
 */
public class DefaultContractPage {
	
	private static final String INTERFACE = "Interface:";
	private static final String SERVICE_NAME = "Service Name:";
	
	public boolean isInterfaceTypeEnabled(String type) {
		
		return new RadioButton(type).isEnabled();
	}

	public String getInterface() {
		
		return new LabeledText(INTERFACE).getText();
	}
	
	public String getServiceName() {
		
		return new LabeledText(SERVICE_NAME).getText();
	}
	
	public DefaultContractPage setServiceName(String name) {
		
		new LabeledText(SERVICE_NAME).setText(name);
		return this;
	}
}