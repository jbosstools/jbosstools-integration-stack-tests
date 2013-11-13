package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceTestClassWizard;

/**
 * A service component.
 * 
 * @author apodhrad
 * 
 */
public class Service extends Component {

	public Service(String tooltip) {
		super(tooltip, 0);
	}

	public Service(String tooltip, int index) {
		super(tooltip, index);
	}

	public PromoteServiceWizard promoteService() {
		clickContextButton("Promote Service");
		return new PromoteServiceWizard("Promote Component Service");

	}

	public void newServiceTestClass() {
		clickContextButton("New Service Test Class");
		new ServiceTestClassWizard().activate().finish();
	}

	public WizardDialog addBinding(String binding) {
		new SwitchYardEditor().activateTool(binding);
		click();
		return new WizardDialog();
	}
	
	public void openTextEditor(){
		doubleClick();
	}
}
