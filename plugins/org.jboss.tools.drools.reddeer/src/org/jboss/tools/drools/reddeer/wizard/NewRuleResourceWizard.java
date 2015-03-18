package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

public class NewRuleResourceWizard extends NewWizardDialog {

    public NewRuleResourceWizard() {
        super("Drools", "Rule Resource");
    }

    public NewRuleResourceWizardPage getFirstPage() {
        return new NewRuleResourceWizardPage();
    }

}
