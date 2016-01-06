package org.jboss.tools.switchyard.reddeer.preference.implementation;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.preference.ResourcePage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractTransactionPage;

/**
 * Represents a properties pages of implementation (Bean, Camel, ...)
 * 
 * @author tsedmik
 */
public class ImplementationPropertiesPage {

	protected final Logger log = Logger.getLogger(this.getClass());

	public void openProperties(SwitchYardComponent component) {

		log.info("Open: " + component.getTooltip());
		component.getContextButton("Properties").click();
		new DefaultShell("Properties for " + component.getTooltip()).setFocus();
	}

	public ResourcePage getResourcePage() {
		log.info("Open the 'Resource' page.");
		new DefaultTreeItem("Resource").select();
		return new ResourcePage();
	}

	public ContractPage getContractPage() {
		log.info("Open the 'Contract' page.");
		new DefaultTreeItem("Contract").select();
		return new ContractPage();
	}

	public ContractSecurityPage getContractSecurityPage() {
		log.info("Open the 'Contract --> Security Policy' page.");
		new DefaultTreeItem("Contract", "Security Policy").select();
		return new ContractSecurityPage();
	}

	public ContractTransactionPage getContractTransactionPage() {

		log.info("Open the 'Contract --> Transaction Policy' page.");
		new DefaultTreeItem("Contract", "Transaction Policy").select();
		return new ContractTransactionPage();
	}

	public ComponentPage getComponentPage() {

		log.info("Open the 'Component' page.");
		new DefaultTreeItem("Component").select();
		return new ComponentPage();
	}

	public ComponentPropertiesPage getComponentPropertiesPage() {

		log.info("Open the 'Component --> Properties' page.");
		new DefaultTreeItem("Component", "Properties").select();
		return new ComponentPropertiesPage();
	}

	public ImplementationPage getImplementationPage() {

		log.info("Open the 'Implementation' page.");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationPage();
	}

	public ImplementationTransactionPage getImplementationTransactionPage() {

		log.info("Open the 'Implementation --> Transaction Policy' page.");
		new DefaultTreeItem("Implementation", "Transaction Policy").select();
		return new ImplementationTransactionPage();
	}

	public ImplementationSecurityPage getImplementationSecurityPage() {

		log.info("Open the 'Implementation --> Security Policy' page.");
		new DefaultTreeItem("Implementation", "Security Policy").select();
		return new ImplementationSecurityPage();
	}

	public ImplementationBPMNPage getBPMNImplementation() {

		log.info("Open the 'Implementation' page for BPMN.");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationBPMNPage();
	}

	public ImplementationKnowledgePage getRulesImplementation() {

		log.info("Open the 'Implementation' page for Rules (DRL).");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationKnowledgePage();
	}

	public void ok() {

		Button b = new PushButton("OK");
		log.info("Close Preferences dialog");
		b.click();
	}

	public void cancel() {

		Button b = new PushButton("Cancel");
		log.info("Cancel Preferences dialog");
		b.click();
	}

}
