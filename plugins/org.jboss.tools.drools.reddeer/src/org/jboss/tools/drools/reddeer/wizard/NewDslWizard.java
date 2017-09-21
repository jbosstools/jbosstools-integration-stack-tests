package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

public class NewDslWizard extends NewMenuWizard {

	public NewDslWizard() {
		super("New Domain Specific Language configuration", "Drools", "Domain Specific Language");
	}

	public void createDefaultDsl(String path, String name) {
		open();
		NewDslWizardPage page = new NewDslWizardPage(this);
		page.setParentFolder(path);
		page.setFileName(name);

		NewDslSamplesWizardPage samplePage = new NewDslSamplesWizardPage(this);
		samplePage.setAddSampleDsl(true);
		finish();
	}
}
