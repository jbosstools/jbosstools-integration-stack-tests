package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlWebImportWizard extends TeiidImportWizard {

	private static WsdlWebImportWizard INSTANCE;
	
	public static final String DIALOG_TITLE = "Create Web Service from WSDL File";
	
	public WsdlWebImportWizard() {
		super("WSDL File or URL >> Web Service Model");
		log.info("Wsdl web import wizard is opened");
	}

	public static WsdlWebImportWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new WsdlWebImportWizard();
		}
		return INSTANCE;
	}
	
	public static WsdlWebImportWizard openWizard(){
		WsdlWebImportWizard wizard = getInstance();
		wizard.open();
		return wizard;
	}
	
	public WsdlWebImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public WsdlWebImportWizard setModelName(String modelName) {
		log.info("Set model name: '" + modelName + "'");
		activate();
		new LabeledText("Web Service Model Name").setText(modelName);
		return this;	
	}

	public WsdlWebImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activate();
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlWebImportWizard importFromWorkspace(String... pathToWsdl){
		log.info("Import from workspace");
		activate();
		new PushButton("Workspace...").click();
		new DefaultShell("WSDL File Selection");
		new DefaultTreeItem(pathToWsdl).select();
		new PushButton("OK").click();
		return this;
	}
	
	public WsdlWebImportWizard importFromURL(String url, String userName, String password, boolean httpBasic) {
		log.info("Set wsdlURL: '" + url + "', username: '" + userName +"', password: '" + password +"'");
		activate();
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
		activate();
		new LabeledText("XML Model:").setText(xmlModelName);
		CheckBox checkBox = new CheckBox();
		if(generateVirtualXML != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public WsdlWebImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
}
