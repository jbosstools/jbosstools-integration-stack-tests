package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 *
 */
public class BPELServiceWizard extends ServiceWizard<BPELServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public BPELServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
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

}
