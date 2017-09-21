package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.PushButton;

public class NewDroolsInitialContentsWizardPage extends WizardPage {

	public NewDroolsInitialContentsWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void selectEmptyProject() {
		new PushButton(0).click();
	}
	
	public void selectProjectWithExamples() {
		new PushButton(1).click();
	}
	
	public void selectInternetExamples() {
		new PushButton(2).click();
	}
}
