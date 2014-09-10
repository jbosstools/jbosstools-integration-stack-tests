package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
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

	public static final String CONTEXT_PATH = "Context path:";
	public static final String WSDL_URI = "WSDL URI";
	public static final String ENDPOINT_ADDRESS = "Endpoint Address";
	public static final String SERVER_PORT = "Server Port";

	public SOAPBindingPage setContextPath(String contextPath) {
		new LabeledText(CONTEXT_PATH).setFocus();
		new LabeledText(CONTEXT_PATH).setText(contextPath);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getContextPath() {
		return new LabeledText(CONTEXT_PATH).getText();
	}

	public SOAPBindingPage setWsdlURI(String uri) {
		new DefaultText(1).setFocus();
		new DefaultText(1).setText(uri);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getWsdlURI() {
		throw new UnsupportedOperationException();
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

	public SOAPBindingPage setServerPort(String port) {
		new LabeledText(SERVER_PORT).setFocus();
		new LabeledText(SERVER_PORT).setText(port);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getServerPort() {
		return new LabeledText(SERVER_PORT).getText();
	}

}
