package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public abstract class NewResourceWizardPage extends WizardPage {

	public NewResourceWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setParentFolder(String parent) {
		new LabeledText("Enter or select the parent folder:").setText(parent);
	}

	public void setFileName(String fileName) {
		new LabeledText("File name:").setText(fileName);
	}

}
