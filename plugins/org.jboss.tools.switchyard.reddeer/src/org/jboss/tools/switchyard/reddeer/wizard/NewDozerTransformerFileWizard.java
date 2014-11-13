package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

public class NewDozerTransformerFileWizard extends WizardDialog {

	public NewDozerTransformerFileWizard() {
		activate();
	}

	public NewDozerTransformerFileWizard activate() {
		new DefaultShell("New Dozer Transformer File");
		return this;
	}

	public Button getFinishBTN() {
		return new PushButton("Finish");
	}

	public Button getCancelBTN() {
		return new PushButton("Cancel");
	}

	public Text getFilenameTXT() {
		return new LabeledText("File name:");
	}

	public void setFilename(String text) {
		getFilenameTXT().setText(text);
	}

	public void getFilename() {
		getFilenameTXT().getText();
	}

	public Tree getDefaultTRE() {
		return new DefaultTree(0);
	}

	public Text getEnterOrSelectTheParentFolderTXT() {
		return new LabeledText("Enter or select the parent folder:");
	}

	public void setEnterOrSelectTheParentFolder(String text) {
		getEnterOrSelectTheParentFolderTXT().setText(text);
	}

	public String getEnterOrSelectTheParentFolder() {
		return getEnterOrSelectTheParentFolderTXT().getText();
	}
}
