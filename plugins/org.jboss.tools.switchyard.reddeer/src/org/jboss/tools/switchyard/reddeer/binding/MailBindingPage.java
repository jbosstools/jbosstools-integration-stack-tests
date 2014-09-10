package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * Mail binding page
 * 
 * @author apodhrad
 * 
 */
public class MailBindingPage extends OperationOptionsPage<MailBindingPage>  {

	public static final String HOST = "Host*";

	public MailBindingPage setHost(String host) {
		new LabeledText(HOST).setFocus();
		new LabeledText(HOST).setText(host);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getHost() {
		return new LabeledText(HOST).getText();
	}

}
