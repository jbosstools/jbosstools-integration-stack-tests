package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SAP binding page
 * 
 * @author apodhrad
 * 
 */
public class SAPBindingPage extends OperationOptionsPage<SAPBindingPage> {

	public LabeledText getRFCName() {
		return new LabeledText("RFC Name*");
	}

	public LabeledText getServer() {
		return new LabeledText("Server*");
	}

	public CheckBox getTransacted() {
		return new CheckBox("Transacted");
	}

}
