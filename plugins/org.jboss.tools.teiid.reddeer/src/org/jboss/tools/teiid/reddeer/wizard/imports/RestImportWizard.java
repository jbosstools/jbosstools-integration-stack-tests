package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.util.Arrays;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from REST WS
 * 
 * @author mmakovy
 * 
 */

public class RestImportWizard extends TeiidImportWizard {

	private static final String DIALOG_TITLE = "Import From REST Web Service Source";
	private String rootPath;
	
	private RestImportWizard() {
		super(DIALOG_TITLE, "Web Service Source >> Source and View Model (REST)");
		log.info("Rest import wizard is opened");
	}
	
	public static RestImportWizard getInstance(){
		return new RestImportWizard();
	}
	
	public static RestImportWizard openWizard(){
		RestImportWizard wizard = new RestImportWizard();
		wizard.open();
		return wizard;
	}
	
	public RestImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public RestImportWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public RestImportWizard setProfileName(String profileName) {
		log.info("Set connectionProfile to: '" + profileName + "'");
		activateWizard();
		new DefaultCombo().setSelection(profileName);
		return this;
	}
	
	public RestImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activateWizard();
		new DefaultCombo(0).setSelection(projectName);
		setProjectToModel("Source Model Definition",projectName);
		setProjectToModel("View Model Definition",projectName);
		return this;
	}
	
	public RestImportWizard setSourceModelName(String sourceModelName) {
		log.info("Set source model name to: '" + sourceModelName + "'");
		activateWizard();
		new LabeledText(new DefaultGroup("Source Model Definition"), "Name:").setText(sourceModelName);
		return this;
	}
	
	public RestImportWizard setViewModelName(String viewModelName) {
		log.info("Set view model name to: '" + viewModelName + "'");
		activateWizard();
		new LabeledText(new DefaultGroup("View Model Definition"), "Name:").setText(viewModelName);
		return this;
	}
	
	public RestImportWizard setProcedureName(String procedureName) {
		log.info("Set procedure name to: '" + procedureName + "'");
		activateWizard();
		new LabeledText(new DefaultGroup("View Model Definition"), "New View Procedure Name:").setText(procedureName);
		return this;
	}
	
	public RestImportWizard autoCreateDataSource(boolean checked) {
		log.info("Auto-Create Data Source is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
    public RestImportWizard setJndiName(String jndiName) {
        log.info("Set JNDI name to: '" + jndiName + "'");
		activateWizard();
        jndiName = (jndiName.contains("java:/")) ? jndiName : "java:/" + jndiName;
        new LabeledText("JNDI Name").setText(jndiName);
		return this;
	}
	
	public RestImportWizard setRootPath(String rootPath) {
		log.info("Set root path to: '" + rootPath + "'");
		activateWizard();
		new DefaultTreeItem(rootPath.split("/")).select();
		new ContextMenuItem("Set as root path").select();
		this.rootPath=rootPath;
		return this;
	}
	
	/**
	 * rootPath must be set (RestImportWizard.setRootPath(rootPath))
	 */
	public RestImportWizard setColumns(String... columns) {
		log.info("Set columns to: '" + Arrays.toString(columns) + "'");
		activateWizard();
		for (String column : columns) {
			new DefaultTreeItem((rootPath + "/" + column).split("/")).select();
			new PushButton("Add").click();
		}
		return this;
	}
	
	private void setProjectToModel(String section, String projectName){
		new PushButton(new DefaultGroup(section), "...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
	}
}
