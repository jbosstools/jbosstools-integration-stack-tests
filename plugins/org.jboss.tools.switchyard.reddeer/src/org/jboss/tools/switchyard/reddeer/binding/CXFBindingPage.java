package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * CXF binding page
 * 
 * @author apodhrad
 * 
 */
public class CXFBindingPage extends OperationOptionsPage<CXFBindingPage> {

	public static final String DATA_FORMAT_POJO = "POJO";
	public static final String DATA_FORMAT_PAYLOAD = "PAYLOAD";
	public static final String DATA_FORMAT_MESSAGE = "MESSAGE";

	public LabeledText getPortName() {
		return new LabeledText("Port Name");
	}

	public LabeledText getServiceName() {
		return new LabeledText("Service Name");
	}

	public LabeledText getServiceClass() {
		return new LabeledText("Service Class");
	}

	public LabeledText getWSDLURL() {
		return new LabeledText("WSDL URL");
	}

	public LabeledText getCXFURI() {
		return new LabeledText("CXF URI*");
	}

	public LabeledCombo getWrappedStyle() {
		return new LabeledCombo("Wrapped Style");
	}

	public LabeledCombo getDataFormat() {
		return new LabeledCombo("Data Format");
	}

	public CheckBox getWrapped() {
		return new CheckBox("Wrapped");
	}

	public CheckBox getRelayHeaders() {
		return new CheckBox("Relay Headers");
	}

}
