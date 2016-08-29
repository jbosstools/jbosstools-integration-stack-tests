package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlImportWizard extends TeiidImportWizard {

	private static WsdlImportWizard INSTANCE;
	
	public static final String DIALOG_TITLE = "Create Relational Model from Web Service";

	private WsdlImportWizard() {
		super("Web Service Source >> Source and View Model (SOAP)");
		log.info("Wsdl import wizard is opened");
	}

	public static WsdlImportWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new WsdlImportWizard();
		}
		return INSTANCE;
	}
	
	public static WsdlImportWizard openWizard(){
		WsdlImportWizard wizard = getInstance();
		wizard.open();
		return wizard;
	}
	
	public WsdlImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public WsdlImportWizard setConnectionProfile(String connectionProfile) {
		log.info("Set connection profile: '" + connectionProfile + "'");
		activate();
		new DefaultCombo(0).setSelection(connectionProfile);
		new PushButton("Validate WSDL").click();
		new DefaultShell("WSDL Validation Results");
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlImportWizard selectOperations(String... operations) {
		log.info("Set select operations: '" + operations + "'");
		activate();
		for(String operation : operations){
			new DefaultTable(new DefaultGroup("Select the desired WSDL Operations"), 0).getItem(operation).setChecked(true);
		}
		return this;	
	}
	
	public WsdlImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activate();
		new DefaultCombo(0).setSelection(projectName);
		setProjectToModel("Source Model Definition",projectName);
		setProjectToModel("View Model Definition",projectName);
		return this;
	}
	
	public WsdlImportWizard setSourceModelName(String sourceModelName) {
		log.info("Set source model name to: '" + sourceModelName + "'");
		activate();
		new LabeledText(new DefaultGroup("Source Model Definition"), "Name").setText(sourceModelName);
		return this;
	}
	
	public WsdlImportWizard setViewModelName(String viewModelName) {
		log.info("Set view model name to: '" + viewModelName + "'");
		activate();
		new LabeledText(new DefaultGroup("View Model Definition"), "Name").setText(viewModelName);
		return this;
	}
	
	public WsdlImportWizard autoCreateDataSource(boolean check) {
		log.info("Auto-Create Data Source is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public WsdlImportWizard setJndiName(String JndiName) {
		log.info("Set JNDI name to: '" + JndiName + "'");
		activate();
		new LabeledText("JNDI Name").setText(JndiName);
		return this;
	}
	
	public WsdlImportWizard addRequestElement(String element) {
		String operation = element.split("/")[0];
		log.info("Set request element: '" + element + "' to operation:'"+ operation +"'");
		activate();
		new DefaultCombo(new DefaultGroup("Operations"), 0).setSelection(operation);
		addElement("Request", element);
		return this;
	}
	
	public WsdlImportWizard addResponseElement(String operation, String element) {
		log.info("Set response element: '" + element + "' to operation:'"+ operation +"'");
		activate();
		new DefaultCombo(new DefaultGroup("Operations"), 0).setSelection(operation);
		addElement("Response", element);
		return this;
	}
	
	private void setProjectToModel(String section, String projectName){
		new PushButton(new DefaultGroup(section), "...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
	}

	private void addElement(String tab, String path) {
		log.info("Add " + tab + " element '" + path + "'");
		new DefaultTabItem(tab).activate();
		try {
			new DefaultTreeItem(path.split("/")).select();
		} catch (Exception e) {
		}
		new PushButton("Add").click();
	}
	
	public WsdlImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		return this;
	}
}
