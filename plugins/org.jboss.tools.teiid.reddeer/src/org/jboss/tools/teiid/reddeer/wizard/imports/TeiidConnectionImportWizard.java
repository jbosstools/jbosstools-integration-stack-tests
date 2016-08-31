package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.dialog.CreateDataSourceDialog;

public class TeiidConnectionImportWizard extends TeiidImportWizard{
		
	public static final String DIALOG_TITLE = "Import using a Teiid Connection";

	//import properties
	public static final String IMPORT_PROPERTY_SCHEMA_PATTERN = "Schema Pattern";
	public static final String IMPORT_PROPERTY_TABLE_NAME_PATTERN = "Table Name Pattern";
	public static final String IMPORT_PROPERTY_CATALOG = "catalog";
	public static final String IMPORT_PROPERTY_EXCEL_FILENAME = "Excel File";
	public static final String IMPORT_PROPERTY_HEADER_ROW_NUMBER = "Header Row Number";
	public static final String IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME = "Use Full Schema Name";
	public static final String IMPORT_PROPERTY_TABLE_TYPES = "Table Types";
	
	private TeiidConnectionImportWizard() {
		super("Teiid Connection >> Source Model");
		log.info("Wsdl import wizard is opened");
	}

	public static TeiidConnectionImportWizard getInstance(){
		return new TeiidConnectionImportWizard();
	}
	
	public static TeiidConnectionImportWizard openWizard(){
		TeiidConnectionImportWizard wizard = new TeiidConnectionImportWizard();
		wizard.open();
		return wizard;
	}
	
	public TeiidConnectionImportWizard nextPage(){
		log.info("Go to next wizard page");
		activate();
		new NextButton().click();
		return this;
	}
	
	public TeiidConnectionImportWizard nextPageWithWait(){
		return this.nextPageWithWait(TimePeriod.LONG);
	}
	
	public TeiidConnectionImportWizard nextPageWithWait(TimePeriod timePeriod){
		this.nextPage();		
		new WaitWhile(new IsInProgress(), timePeriod,false);
		return this;
	}
	
	public TeiidConnectionImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public TeiidConnectionImportWizard selectDataSource(String dataSourceName) {
		log.info("Select existing data source '" + dataSourceName + "'");
		activate();
		Table table = new DefaultTable(0);
		TableItem item = table.getItem(dataSourceName, 0);
		if (item != null) {
			item.select();
		}
		return this;
	}
	
	/**
	 * @param dataSourceProperties - as key use one of TeiidConnectionImportWizard.DATASOURCE_PROPERTY_
	 */
	public CreateDataSourceDialog createNewDataSource(){	
		log.info("Create new data source");
		activate();
		new PushButton("New...").click();
		return new CreateDataSourceDialog();
	}
	
	public TeiidConnectionImportWizard setTranslator(String translatorName){
		log.info("Change translator to '" + translatorName + "'");
		activate();
		new DefaultCombo(0).setSelection(translatorName);
		return this;
	}
	
	/**
	 * @param propertyName - use one of TeiidConnectionImportWizard.IMPORT_PROPERTY_
	 */
	public TeiidConnectionImportWizard setImportPropertie(String propertyName, String value){
		log.info("Set property " + propertyName + "'s value to: '"+value+"'");
		activate();
		try{
			Table table = new DefaultTable(new DefaultGroup("Import Properties"), 0);
			TableItem item = null;
			item = table.getItem(propertyName);
			item.click(1);
			if(value.equals("false") || value.equals("true")){
				new DefaultCCombo(new CellEditor(item),0).setSelection(value);
			}else{
				new DefaultText(new CellEditor(item),0).setText(value);
			}
			KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		}catch(Exception ex){
			log.warn("Property is not exist. It will be added.");
			this.addNewImportPropertie(propertyName, value);
		}
		return this;
	}
	
	public TeiidConnectionImportWizard addNewImportPropertie(String propertyName, String value){
		log.info("Add new property '" + propertyName + "' with value to: '"+value+"'");
		activate();
		new PushButton(new DefaultGroup("Optional Source Import Properties"), 0).click();
		new DefaultShell("Add New Property");
		new LabeledText("Name:").setText(propertyName);
		new LabeledText("Value:").setText(value);
		try{
			new PushButton("OK").click();
		}catch(Exception ex){
			log.warn("Property is already exist.");
			new PushButton("Cancel").click();
			activate();
			Table table = new DefaultTable(new DefaultGroup("Optional Source Import Properties"), 0);
			TableItem item = null;
			item = table.getItem(propertyName);
			item.click(1);
			new DefaultText(new CellEditor(item),0).setText(value);
			KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		}
		return this;
	}
	
	public TeiidConnectionImportWizard setProject(String projectName) {
		log.info("Set project name to: '" + projectName + "'");
		activate();
		new PushButton("...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
		return this;
	}
	
	public TeiidConnectionImportWizard setModelName(String modelName) {
		log.info("Set model name to: '" + modelName + "'");
		activate();
		new LabeledText("Name:").setText(modelName);
		return this;
	}
	
	public TeiidConnectionImportWizard setTablesToImport(String... tables) {
		log.info("Select tables to import: '" + Arrays.toString(tables) + "'");
		activate();
		DefaultTree tree = new DefaultTree();
		for (TreeItem treeItem : tree.getItems()) {
			treeItem.setChecked(false);
		}
		new DefaultTree();
		for (String path : tables) {
			String[] parts = path.split("/");
			new DefaultTreeItem(tree, parts).setChecked(true);
		}
		return this;
	}
}
