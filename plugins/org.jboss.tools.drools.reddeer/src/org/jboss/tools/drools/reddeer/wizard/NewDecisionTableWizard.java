package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

public class NewDecisionTableWizard extends NewMenuWizard {

	public NewDecisionTableWizard() {
		super("New Decision Table", "Drools", "Decision Table");
	}

	public NewDecisionTableWizardPage getFirstPage() {
		return new NewDecisionTableWizardPage(this);
	}
}
