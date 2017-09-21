package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

public class NewRuleResourceWizard extends NewMenuWizard {

	public NewRuleResourceWizard() {
		super("New Rule Pakcge...", "Drools", "Rule Resource");
	}

	public NewRuleResourceWizardPage getFirstPage() {
		return new NewRuleResourceWizardPage(this);
	}

}
