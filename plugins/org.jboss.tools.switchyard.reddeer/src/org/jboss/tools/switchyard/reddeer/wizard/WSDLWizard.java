package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.Combo;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.impl.button.BackButton;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class WSDLWizard extends WizardDialog {

	public static final String TITLE = "New WSDL File";

	public WSDLWizard activate() {
		Shell shell = new DefaultShell(TITLE);
		setShell(shell);
		return this;
	}

	public WSDLWizard setFileName(String fileName) {
		new LabeledText("File name:").setText(fileName);
		return activate();
	}

	public WSDLWizard clickNext() {
		new NextButton().click();
		return this;
	}

	public WSDLWizard clickBack() {
		new BackButton().click();
		return this;
	}

	public Button getRpcEncoded() {
		return new RadioButton("rpc encoded");
	}

	public Button getRpcLiteral() {
		return new RadioButton("rpc literal");
	}

	public Button getDocumentLiteral() {
		return new RadioButton("document literal");
	}

	public Combo getProtocol() {
		return new LabeledCombo("Protocol:");
	}

	public Button getCreateWSDLSkeleton() {
		return new CheckBox("Create WSDL Skeleton");
	}

	public Text getPrefix() {
		return new LabeledText("Prefix:");
	}

	public Text getTargetnamespace() {
		return new LabeledText("Target namespace:");
	}

	public Text getFileName() {
		return new LabeledText("File name:");
	}

	public Text getEnterOrSelectTheParentFolder() {
		return new LabeledText("Enter or select the parent folder:");
	}

}
