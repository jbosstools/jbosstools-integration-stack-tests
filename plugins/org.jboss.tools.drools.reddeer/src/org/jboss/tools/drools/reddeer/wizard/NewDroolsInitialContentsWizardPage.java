package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class NewDroolsInitialContentsWizardPage extends WizardPage {

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
