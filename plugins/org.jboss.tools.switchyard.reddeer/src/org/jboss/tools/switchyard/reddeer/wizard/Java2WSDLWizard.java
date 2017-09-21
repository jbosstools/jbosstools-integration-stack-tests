package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating WSDL from Java.
 * 
 * @author apodhrad
 * 
 */
public class Java2WSDLWizard extends NewMenuWizard {

	public static final String DIALOG_TITLE = "Java2WSDL";

	public Java2WSDLWizard() {
		super(DIALOG_TITLE, "SwitchYard", "WSDL File from Java");
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
