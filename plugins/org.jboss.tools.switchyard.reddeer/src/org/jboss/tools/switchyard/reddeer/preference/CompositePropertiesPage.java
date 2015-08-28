package org.jboss.tools.switchyard.reddeer.preference;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.switchyard.reddeer.preference.binding.BindingsPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationBPMNPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationKnowledgePage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationTransactionPage;

/**
 * 
 * @author apodhrad
 *
 */
public class CompositePropertiesPage {

	private final Logger log = Logger.getLogger(CompositePropertiesPage.class);
	
	protected String title;

	public CompositePropertiesPage(String title) {
		this.title = title;
	}

	public CompositePropertiesPage activate() {
		new DefaultShell("Properties for " + title);
		return this;
	}

	public ValidatorsPage selectValidators() {
		activate();
		log.info("Select 'Validators' page");
		new DefaultTreeItem("Validators").select();
		return new ValidatorsPage();
	}
	
	public TransformsPage selectTransforms() {
		activate();
		log.info("Select 'Transforms' page");
		new DefaultTreeItem("Transforms").select();
		return new TransformsPage();
	}

	public ResourcePage selectResource() {
		log.info("Select 'Resource' page.");
		new DefaultTreeItem("Resource").select();
		return new ResourcePage();
	}

	public ContractPage selectContract() {
		log.info("Select 'Contract' page.");
		new DefaultTreeItem("Contract").select();
		return new ContractPage();
	}

	public ContractSecurityPage selectContractSecurity() {
		log.info("Select 'Contract --> Security Policy' page.");
		new DefaultTreeItem("Contract", "Security Policy").select();
		return new ContractSecurityPage();
	}

	public ContractTransactionPage selectContractTransaction() {
		log.info("Select 'Contract --> Transaction Policy' page.");
		new DefaultTreeItem("Contract", "Transaction Policy").select();
		return new ContractTransactionPage();
	}

	public ComponentPage selectComponent() {
		log.info("Select 'Component' page.");
		new DefaultTreeItem("Component").select();
		return new ComponentPage();
	}

	public ComponentPropertiesPage selectComponentProperties() {
		log.info("Select 'Component --> Properties' page.");
		new DefaultTreeItem("Component", "Properties").select();
		return new ComponentPropertiesPage();
	}

	public ImplementationPage selectImplementation() {
		log.info("Select 'Implementation' page.");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationPage();
	}

	public ImplementationTransactionPage selectImplementationTransaction() {
		log.info("Select 'Implementation --> Transaction Policy' page.");
		new DefaultTreeItem("Implementation", "Transaction Policy").select();
		return new ImplementationTransactionPage();
	}

	public ImplementationSecurityPage selectImplementationSecurity() {
		log.info("Select 'Implementation --> Security Policy' page.");
		new DefaultTreeItem("Implementation", "Security Policy").select();
		return new ImplementationSecurityPage();
	}

	public ImplementationBPMNPage selectBPMNImplementation() {
		log.info("Select 'Implementation' page for BPMN.");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationBPMNPage();
	}

	public ImplementationKnowledgePage selectRulesImplementation() {
		log.info("Select 'Implementation' page for Rules (DRL).");
		new DefaultTreeItem("Implementation").select();
		return new ImplementationKnowledgePage();
	}

	public ThrottlingPage selectThrottling() {
		log.info("Select 'Throttling' page.");
		new DefaultTreeItem("Throttling").select();
		return new ThrottlingPage();
	}

	public BindingsPage selectBindings() {
		log.info("Select 'Bindings' page.");
		new DefaultTreeItem("Bindings").select();
		new DefaultList().deselectAll();
		return new BindingsPage();
	}

	public void ok() {
		activate();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for " + title));
	}

}
