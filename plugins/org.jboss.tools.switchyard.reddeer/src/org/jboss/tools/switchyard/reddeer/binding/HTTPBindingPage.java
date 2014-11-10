package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class HTTPBindingPage extends OperationOptionsPage<HTTPBindingPage> {

	public LabeledText getContextPath() {
		return new LabeledText("Context path:");
	}
}
