package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class HTTPBindingPage extends OperationOptionsPage<HTTPBindingPage> {

	public static final String CONTEXT_PATH = "Context path:";

	public HTTPBindingPage setContextPath(String contextPath) {
		new LabeledText(CONTEXT_PATH).setFocus();
		new LabeledText(CONTEXT_PATH).setText(contextPath);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}
	
	public String getContextPath() {
		return new LabeledText(CONTEXT_PATH).getText();
	}

}
