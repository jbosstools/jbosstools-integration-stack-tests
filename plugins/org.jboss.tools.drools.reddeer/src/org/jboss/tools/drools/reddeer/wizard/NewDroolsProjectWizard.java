package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

public class NewDroolsProjectWizard extends NewWizardDialog {

    public NewDroolsProjectWizard() {
        super("Drools", "Drools Project");
    }

    public NewDroolsInitialContentsWizardPage getFirstPage() {
        return new NewDroolsInitialContentsWizardPage();
    }

    public NewDroolsEmptyProjectWizardPage getEmptyProjectPage() {
        return new NewDroolsEmptyProjectWizardPage();
    }

    public NewDroolsProjectWithExamplesWizardPage getProjectWithExamplesPage() {
        return new NewDroolsProjectWithExamplesWizardPage();
    }
}
