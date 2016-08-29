package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

public class SalesforceImportWizard extends ImportWizardDialog {

	public static final String DIALOG_TITLE = "Create Relational Model from SalesForce Data Model";

	public SalesforceImportWizard() {
		super("Teiid Designer", "Salesforce >> Source Model");
		log.info("Salesforce import wizard is opened");
	}

	public SalesforceImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public SalesforceImportWizard setConnectionProfile(String connectionProfile) {
		log.info("Set connection profile: '" + connectionProfile + "'");
		activate();
		new DefaultCombo(0).setSelection(connectionProfile);
		new PushButton("Validate Connection").click();
		return this;
	}
	
	public SalesforceImportWizard selectObjects(String... objects){
		log.info("Select objects: '" + objects + "'");
		activate();
		new PushButton("Deselect All").click();
		for (String object : objects) {
			new DefaultTable().getItem(object.trim()).setChecked(true);
		}
		return this;
	}
	
	public SalesforceImportWizard deselectObjects(String... objects){
		log.info("Deselect objects: '" + objects + "'");
		activate();
		new PushButton("Select All").click();
		for (String object : objects) {
			new DefaultTable().getItem(object.trim()).setChecked(false);
		}
		return this;
	}
	
	public SalesforceImportWizard setModelName(String modelName) {
		log.info("Set model name to: '" + modelName + "'");
		activate();
		new LabeledText("Model Name:").setText(modelName);
		return this;
	}
	
	public SalesforceImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activate();
		new LabeledText("Location:").setText(projectName);
		return this;
	}
	
	public SalesforceImportWizard autoCreateDataSource(boolean check) {
		log.info("Auto-Create Data Source is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public SalesforceImportWizard setJndiName(String JndiName) {
		log.info("Set JNDI name to: '" + JndiName + "'");
		activate();
		new LabeledText("JNDI Name").setText(JndiName);
		return this;
	}
	
	@Override
	public void next(){
		super.next();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}
	
	@Override
	public void finish(){
		super.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}
}
