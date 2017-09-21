package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;

public class NewDslSamplesWizardPage extends WizardPage {

	public NewDslSamplesWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setAddSampleDsl(boolean value) {
		new CheckBox("Create the DSL file with some sample DSL statements").toggle(value);
	}

}
