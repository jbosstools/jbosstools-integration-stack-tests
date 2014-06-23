package org.jboss.tools.switchyard.reddeer.preference;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.preference.implementation.BPMNImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultImplementationSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultResourcePage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.RulesImplementationPage;

/**
 * Represents a properties pages of implementation (Bean, Camel, ...)
 * 
 * @author tsedmik
 */
public class ImplementationPropertiesPage {
	
	protected final Logger log = Logger.getLogger(this.getClass());
	
	public void openProperties(Component component) {
		
		log.info("Open: " + component.getTooltip());
		component.contextButton("Properties").click();
		new DefaultShell("Properties for " + component.getTooltip()).setFocus();
	}
	
	public DefaultResourcePage getResourcePage() {
		
		log.info("Open the 'Resource' page.");
		new DefaultTreeItem("Resource").select();
		return new DefaultResourcePage();
	}
	
	public DefaultContractPage getContractPage() {
		
		log.info("Open the 'Contract' page.");
		new DefaultTreeItem("Contract").select();
		return new DefaultContractPage();
	}
	
	public DefaultContractSecurityPage getContractSecurityPage() {
		
		log.info("Open the 'Contract --> Security Policy' page.");
		new DefaultTreeItem("Contract", "Security Policy").select();
		return new DefaultContractSecurityPage();
	}
	
	public DefaultContractTransactionPage getContractTransactionPage() {
		
		log.info("Open the 'Contract --> Transaction Policy' page.");
		new DefaultTreeItem("Contract", "Transaction Policy").select();
		return new DefaultContractTransactionPage();
	}
	
	public DefaultComponentPage getComponentPage() {
	
		log.info("Open the 'Component' page.");
		new DefaultTreeItem("Component").select();
		return new DefaultComponentPage();
	}
	
	public DefaultComponentPropertiesPage getComponentPropertiesPage() {
		
		log.info("Open the 'Component --> Properties' page.");
		new DefaultTreeItem("Component", "Properties").select();
		return new DefaultComponentPropertiesPage();
	}
	
	public DefaultImplementationPage getImplementationPage() {
		
		log.info("Open the 'Implementation' page.");
		new DefaultTreeItem("Implementation").select();
		return new DefaultImplementationPage();
	}
	
	public DefaultTransactionPage getImplementationTransactionPage() {
		
		log.info("Open the 'Implementation --> Transaction Policy' page.");
		new DefaultTreeItem("Implementation", "Transaction Policy").select();
		return new DefaultTransactionPage();
	}
	
	public DefaultImplementationSecurityPage getImplementationSecurityPage() {
		
		log.info("Open the 'Implementation --> Security Policy' page.");
		new DefaultTreeItem("Implementation", "Security Policy").select();
		return new DefaultImplementationSecurityPage();
	}
	
	public BPMNImplementationPage getBPMNImplementation() {
		
		log.info("Open the 'Implementation' page for BPMN.");
		new DefaultTreeItem("Implementation").select();
		return new BPMNImplementationPage();
	}
	
	public RulesImplementationPage getRulesImplementation() {
		
		log.info("Open the 'Implementation' page for Rules (DRL).");
		new DefaultTreeItem("Implementation").select();
		return new RulesImplementationPage();
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
