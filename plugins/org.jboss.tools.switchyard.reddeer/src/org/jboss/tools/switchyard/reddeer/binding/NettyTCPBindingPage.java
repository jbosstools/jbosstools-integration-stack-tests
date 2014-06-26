package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * Netty TCP binding page
 * 
 * @author apodhrad
 * 
 */
public class NettyTCPBindingPage extends OperationOptionsPage<NettyTCPBindingPage> {

	public static final String HOST = "Host*";
	public static final String PORT = "Port*";

	public NettyTCPBindingPage setHost(String host) {
		new LabeledText(HOST).setFocus();
		new LabeledText(HOST).setText(host);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getHost() {
		return new LabeledText(HOST).getText();
	}

	public NettyTCPBindingPage setPort(String port) {
		new LabeledText(PORT).setFocus();
		new LabeledText(PORT).setText(port);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getPort() {
		return new LabeledText(PORT).getText();
	}
}
