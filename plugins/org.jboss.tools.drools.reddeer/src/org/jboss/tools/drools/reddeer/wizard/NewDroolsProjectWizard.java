package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

public class NewDroolsProjectWizard extends NewMenuWizard {

    public NewDroolsProjectWizard() {
        super("", "Drools", "Drools Project");
    }

    public NewDroolsInitialContentsWizardPage getFirstPage() {
        return new NewDroolsInitialContentsWizardPage(this);
    }

    public NewDroolsEmptyProjectWizardPage getEmptyProjectPage() {
        return new NewDroolsEmptyProjectWizardPage(this);
    }

    public NewDroolsProjectWithExamplesWizardPage getProjectWithExamplesPage() {
        return new NewDroolsProjectWithExamplesWizardPage(this);
    }
}
