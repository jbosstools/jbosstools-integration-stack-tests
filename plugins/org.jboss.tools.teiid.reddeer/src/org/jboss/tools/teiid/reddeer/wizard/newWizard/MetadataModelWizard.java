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

public class MetadataModelWizard extends NewWizardDialog {	
	
	public static final String DIALOG_TITLE = "New Model Wizard";
	
	public class ModelType { 
		public static final String SOURCE = "Source Model";
		public static final String VIEW = "View Model";
		public static final String DATATYPE = "Datatype Model";
		public static final String EXTENSION = "Model Class Extension";
		public static final String FUNCTION = "User Defined Function";
	}
	
	public class ModelClass {
		public static final String RELATIONAL = "Relational";
		public static final String XML = "XML (Deprecated)";
		public static final String XSD = "XML Schema (XSD) (Deprecated)";
		public static final String WEBSERVICE = "Web Service (Deprecated)";
		public static final String MODEL_EXTENSION = "Model Extension (Deprecated)";
		public static final String FUNCTION = "Function (Deprecated)";
	}
	
	public class ModelBuilder {
		public static final String TRANSFORM_EXISTING = "Transform from an existing model";
		public static final String COPY_EXISTING = "Copy from an existing model of the same model class";
		public static final String BUILD_FROM_XML_SCHEMA = "Build XML documents from XML schema";
		public static final String BUILD_FROM_WSDL_URL = "Build from existing WSDL file(s) or URL";
	}

	private MetadataModelWizard() {
		super("Teiid Designer", "Teiid Metadata Model");
		log.info("Metadata model wizard is opened");
	}
	
	public static MetadataModelWizard getInstance(){
		return new MetadataModelWizard();
	}
	
	public static MetadataModelWizard openWizard(){
		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		return wizard;
	}
	
	/**
	 * use nextPage()
	 */
	@Override
	@Deprecated
	public void next() {
		super.next();
	}
	
	public MetadataModelWizard nextPage() {
		log.info("Go to next wizard page");
		super.next();
		return this;
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
	
	/**
	 * @param modelClass MetadataModelWizard.ModelClass.*
	 */
	public MetadataModelWizard selectModelClass(String modelClass) {
		activate();
		log.info("Set model class to '" + modelClass + "'");
		new LabeledCombo("Model Class:").setSelection(modelClass);
		return this;
	}

	/**
	 * @param modelType MetadataModelWizard.ModelType.*
	 */
	public MetadataModelWizard selectModelType(String modelType) {
		activate();
		log.info("Set model type to '" + modelType+ "'");
		new LabeledCombo("Model Type:").setSelection(modelType);
		return this;
	}

	/**
	 * @param modelBuilder MetadataModelWizard.ModelBuilder.*
	 */
	public MetadataModelWizard selectModelBuilder(String modelBuilder) {
		activate();
		log.info("Set model builder to '" + modelBuilder + "'");
		new DefaultTable().select(modelBuilder);
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
}
