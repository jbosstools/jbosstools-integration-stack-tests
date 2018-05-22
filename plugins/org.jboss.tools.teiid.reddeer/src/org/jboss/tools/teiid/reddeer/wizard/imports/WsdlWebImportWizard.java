package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlWebImportWizard extends TeiidImportWizard {
	
	public static final String DIALOG_TITLE = "Create Web Service from WSDL File";
	
	public WsdlWebImportWizard() {
		super(DIALOG_TITLE, "WSDL File or URL >> Web Service Model");
		log.info("Wsdl web import wizard is opened");
	}

	public static WsdlWebImportWizard getInstance(){
		return new WsdlWebImportWizard();
	}
	
	public static WsdlWebImportWizard openWizard(){
		WsdlWebImportWizard wizard = new WsdlWebImportWizard();
		wizard.open();
		return wizard;
	}
	
	public WsdlWebImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public WsdlWebImportWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public WsdlWebImportWizard setModelName(String modelName) {
		log.info("Set model name: '" + modelName + "'");
		activateWizard();
		new LabeledText("Web Service Model Name").setText(modelName);
		return this;	
	}

	public WsdlWebImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activateWizard();
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlWebImportWizard importFromWorkspace(String... pathToWsdl){
		log.info("Import from workspace");
		activateWizard();
		new PushButton("Workspace...").click();
		new DefaultShell("WSDL File Selection");
		new DefaultTreeItem(pathToWsdl).select();
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlWebImportWizard importFromURL(String url, String userName, String password, boolean httpBasic) {
		log.info("Set wsdlURL: '" + url + "', username: '" + userName +"', password: '" + password +"'");
		activateWizard();
		new PushButton("URL...").click();
		new LabeledText("Enter WSDL URL:").setText(url);
		if(httpBasic){
			new DefaultCombo("Security Type").setSelection("HTTPBasic");
			new LabeledText("User Name").setText(userName);
			new LabeledText("Password").setText(password);
		}
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlWebImportWizard setXmlModel(String xmlModelName, boolean generateVirtualXML) {
		log.info("Set xml model name to: '" + xmlModelName + "' and set general virtual xml to: "+generateVirtualXML);
		activateWizard();
		new LabeledText("XML Model:").setText(xmlModelName);
		CheckBox checkBox = new CheckBox();
		if(generateVirtualXML != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
}
