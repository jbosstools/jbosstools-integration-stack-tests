package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Camel binding page
 * 
 * @author apodhrad
 * 
 */
public class CamelBindingPage extends OperationOptionsPage<CamelBindingPage> {

	public static final String CONFIG_URI = "Config URI*";

	public LabeledText getConfigURI() {
		return new LabeledText("Config URI*");
	}
}
