package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.switchyard.reddeer.widget.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class BPELServiceWizard extends ServiceWizard<BPELServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public BPELServiceWizard() {
		super(DIALOG_TITLE);
	}

	@Override
	public BPELServiceWizard createWSDLInterface(String wsdlInterface) {
		if (!wsdlInterface.endsWith(".wsdl")) {
			wsdlInterface += ".wsdl";
		}
		checkInterfaceType("WSDL").clickInterface();
		new WSDLWizard().activate().setFileName(wsdlInterface).clickNext().finish();
		return activate();
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}
	
	public void setNamespace(String namespace) {
		new LabeledText("Namespace:").setText(namespace);
	}

}
