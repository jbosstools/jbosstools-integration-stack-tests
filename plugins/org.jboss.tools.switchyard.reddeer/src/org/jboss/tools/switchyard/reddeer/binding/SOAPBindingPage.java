package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * SOAP binding page
 * 
 * @author apodhrad
 * 
 */
public class SOAPBindingPage extends OperationOptionsPage<SOAPBindingPage> {

	public static final String SOAP_HEADERS_TYPE_VALUE = "VALUE";
	public static final String SOAP_HEADERS_TYPE_CONFIG = "CONFIG";
	public static final String SOAP_HEADERS_TYPE_DOM = "DOM";
	public static final String SOAP_HEADERS_TYPE_XML = "XML";
	
	public static final String ENDPOINT_ADDRESS = "Endpoint Address";

	public SOAPBindingPage setWsdlURI(String uri) {
		new DefaultText(1).setFocus();
		new DefaultText(1).setText(uri);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public SOAPBindingPage setEndpointAddress(String address) {
		new LabeledText(ENDPOINT_ADDRESS).setFocus();
		new LabeledText(ENDPOINT_ADDRESS).setText(address);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getEndpointAddress() {
		return new LabeledText(ENDPOINT_ADDRESS).getText();
	}

	public LabeledText getThreshold() {
		return new LabeledText(new DefaultGroup("MTom"), "Threshold");
	}

	public LabeledText getConfigName() {
		return new LabeledText(new DefaultGroup("Endpoint Configuration"), "Config Name");
	}

	public LabeledText getConfigFile() {
		return new LabeledText(new DefaultGroup("Endpoint Configuration"), "Config File");
	}

	public LabeledText getServerPort() {
		return new LabeledText("Server Port");
	}

	public LabeledText getContextPath() {
		return new LabeledText("Context path:");
	}

	public LabeledText getWSDLPort() {
		return new LabeledText("WSDL Port");
	}

	public LabeledCombo getSOAPHeadersType() {
		return new LabeledCombo("SOAP Headers Type");
	}

	public Combo getxopExpand() {
		return new LabeledCombo(new DefaultGroup("MTom"), "xopExpand");
	}

	public Combo getTemporarilyDisable() {
		return new LabeledCombo(new DefaultGroup("MTom"), "Temporarily Disable");
	}

	public CheckBox getEnable() {
		return new CheckBox(new DefaultGroup("MTom"), "Enable");
	}

	public CheckBox getUnwrappedPayload() {
		return new CheckBox("Unwrapped Payload");
	}

}
