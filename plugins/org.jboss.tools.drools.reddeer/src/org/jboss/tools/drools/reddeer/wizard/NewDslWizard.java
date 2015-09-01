package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

public class NewDslWizard extends NewWizardDialog {

    public NewDslWizard() {
        super("Drools", "Domain Specific Language");
    }

    public void createDefaultDsl(String path, String name) {
        open();
        NewDslWizardPage page = new NewDslWizardPage();
        page.setParentFolder(path);
        page.setFileName(name);
        
        NewDslSamplesWizardPage samplePage = new NewDslSamplesWizardPage();
        samplePage.setAddSampleDsl(true);
        finish();
    }
}
