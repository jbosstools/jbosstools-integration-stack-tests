package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;

/**
 * Wizard for creating WSDL from Java.
 * 
 * @author apodhrad
 * 
 */
public class Java2WSDLWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "Java2WSDL";

	public Java2WSDLWizard() {
		super("SwitchYard", "WSDL File from Java");
	}

	public Java2WSDLWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		AbstractWait.sleep(TimePeriod.SHORT);
		return this;
	}

	public Java2WSDLWizard openDialog() {
		open();
		return this;
	}
	
	public Java2WSDLWizard nextDialog() {
		next();
		return this;
	}

	@Override
	public void finish() {
		if (!new PushButton("Finish").isEnabled()) {
			System.out.println("Finish is not enabled!!!");
		}
		super.finish();
	}

}
