package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import java.util.Arrays;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;

/**
 * 
 * @author apodhrad
 * 
 */
public class MetadataModelWizard extends NewWizardDialog {

	private static MetadataModelWizard INSTANCE;
	
	public static final String DIALOG_TITLE = "New Model Wizard";

	private MetadataModelWizard() {
		super("Teiid Designer", "Teiid Metadata Model");
		log.info("Metadata model wizard is opened");
	}
	
	public static MetadataModelWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new MetadataModelWizard();
		}
		return INSTANCE;
	}
	
	public static MetadataModelWizard openWizard(){
		MetadataModelWizard wizard = getInstance();
		wizard.open();
		return wizard;
	}
	public MetadataModelWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public MetadataModelWizard setLocation(String... path) {
		activate();
		log.info("Set location to '" + Arrays.toString(path) + "'");
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

	public MetadataModelWizard setModelName(String modelName) {
		activate();
		log.info("Set model name to '" + modelName + "'");
		new LabeledText("Model Name:").setText(modelName);
		return this;
	}

	public MetadataModelWizard selectModelClass(ModelClass modelClass) {
		activate();
		log.info("Set model class to '" + modelClass.getText() + "'");
		new LabeledCombo("Model Class:").setSelection(modelClass.getText());
		return this;
	}

	public MetadataModelWizard selectModelType(ModelType modelType) {
		activate();
		log.info("Set model type to '" + modelType.getText() + "'");
		new LabeledCombo("Model Type:").setSelection(modelType.getText());
		return this;
	}

	public MetadataModelWizard selectModelBuilder(ModelBuilder modelBuilder) {
		activate();
		log.info("Set model builder to '" + modelBuilder.getText() + "'");
		new DefaultTable().select(modelBuilder.getText());
		return this;
	}

	public MetadataModelWizard selectXMLSchemaFile(String... path) {
		activate();
		log.info("Set xml schema file to '" + Arrays.toString(path) + "'");
		new PushButton("...").click();
		new DefaultShell("Select an XML Schema");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

	public MetadataModelWizard addElement(String... element) {
		activate();
		for (int i = 0; i < element.length; i++) {
			log.info("Add element '" + element[i] + "'");
			new DefaultTable().select(element[i]);
			new PushButton(1).click();
		}
		return this;
	}

	public MetadataModelWizard setExistingModel(String... path) {
		activate();
		log.info("Set existing model to '" + Arrays.toString(path) + "'");
		new PushButton("...").click();
		new DefaultShell("Select a Model File");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public MetadataModelWizard setWsdlFileFromWorkspace(String... path){
		activate();
		new PushButton("Workspace...").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell("WSDL File Selection");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public MetadataModelWizard setXmlModelNameForWebService(String name){
		activate();
		new LabeledText("XML Model:").setText(name);
		return this;
	}
	
	public MetadataModelWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}

	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
}
