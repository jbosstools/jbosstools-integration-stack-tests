package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

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

	public Text getNewWSDLFileTXT() {
		return new LabeledText("New WSDL File");
	}

	public CheckBox getUseImportsForGeneratedTypeSchemaCHB() {
		return new CheckBox("Use imports for generated type schema");
	}

	public CheckBox getUseWrappedMessagesCHB() {
		return new CheckBox("Use \"wrapped\" messages");
	}

	public Button getSelectTypeBTN() {
		return new PushButton("Select Type...");
	}

	public Text getFileNameTXT() {
		return new LabeledText("File name:");
	}

	public Text getEnterOrSelectTheParentFolderTXT() {
		return new LabeledText("Enter or select the parent folder:");
	}

	public Java2WSDLWizard openDialog() {
		open();
		return this;
	}

	public Java2WSDLWizard nextDialog() {
		next();
		return this;
	}

}
