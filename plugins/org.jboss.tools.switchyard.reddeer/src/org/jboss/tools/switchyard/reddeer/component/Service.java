package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceTestClassWizard;

/**
 * A service component.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 * 
 */
public class Service extends Component {

	public Service(String tooltip) {
		super(tooltip, 0);
		System.out.println();
	}

	public Service(String tooltip, int index) {
		super(tooltip, index);
	}

	public PromoteServiceWizard promoteService() {
		contextButton("Promote Service").click();
		return new PromoteServiceWizard("Promote Component Service");
	}

	public void createNewServiceTestClass(String... mixin) {
		contextButton("New Service Test Class").click();
		new ServiceTestClassWizard().activate().selectMixin(mixin).finish();
	}
	
	public ServiceTestClassWizard newServiceTestClass() {
		contextButton("New Service Test Class").click();
		return new ServiceTestClassWizard().activate();
	}

	public WizardDialog addBinding(String binding) {
		new SwitchYardEditor().activateTool(binding);
		click();
		return new WizardDialog();
	}

	public void openTextEditor() {
		doubleClick();
	}

}
