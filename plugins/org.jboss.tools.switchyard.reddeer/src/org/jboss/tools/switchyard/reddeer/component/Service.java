package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceTestClassWizard;

/**
 * A service component.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 * 
 */
public class Service extends SwitchYardComponent {

	public Service(String tooltip) {
		super(tooltip, 0);
	}

	public Service(String tooltip, int index) {
		super(tooltip, index);
	}

	public PromoteServiceWizard promoteService() {
		getContextButton("Promote Service").click();
		return new PromoteServiceWizard("Promote Component Service").activate();
	}

	public void createNewServiceTestClass(String... mixin) {
		getContextButton("New Service Test Class").click();
		new ServiceTestClassWizard().activate().selectMixin(mixin).finish();
	}
	
	public ServiceTestClassWizard newServiceTestClass() {
		getContextButton("New Service Test Class").click();
		return new ServiceTestClassWizard().activate();
	}

	public WizardDialog addBinding(String binding) {
		new SwitchYardEditor().getPalette().activateTool(binding);
		click();
		new WaitWhile(new JobIsRunning());
		return new WizardDialog();
	}

	public void openTextEditor() {
		doubleClick();
	}

}
