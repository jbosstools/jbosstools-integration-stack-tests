package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Wizard for import custom DDL
 * @author mkralik
 */
public class DDLCustomImportWizard extends TeiidImportWizard{
		
	public static final String DIALOG_TITLE = "Import DDL";
	
	public static final String Source_Type = "Source Model";
	public static final String View_Type = "View Model";
	
	// dialects
	public static final String TEIID = "TEIID";
	public static final String SQL92 = "SQL92";
	public static final String ORACLE = "ORACLE";
	public static final String POSTGRES = "POSTGRES";
	public static final String DERBY = "DERBY";
	
	private DDLCustomImportWizard() {
		super(DIALOG_TITLE, "DDL File (General) >> Source or View Model");
		log.info("DDL Custom import Wizard is opened");
	}
	
	public static DDLCustomImportWizard getInstance(){
		return new DDLCustomImportWizard();
	}
	
	public static DDLCustomImportWizard openWizard(){
		DDLCustomImportWizard wizard = new DDLCustomImportWizard();
		wizard.open();
		return wizard;
	}
	
	public DDLCustomImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		return this;
	}
	
	public DDLCustomImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLCustomImportWizard setPath(String path){
		log.info("Set import path: '" + path + "'");
		activate();
		new DefaultCombo(0).setText(path);
		return this;
	}
	
	public DDLCustomImportWizard autoSelect(boolean checked){
		log.info("Autoselect is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-select");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	/**
	 * @param dialect use one of dialects (DDLCustomImportWizard.TEIID, DDLCustomImportWizard.ORACLE ...)
	 */
	public DDLCustomImportWizard setDialect(String dialect){
		log.info("Set dialect to: '" + dialect + "'");
		activate();
		autoSelect(false);
		new DefaultCombo(1).setSelection(dialect);
		return this;
	}
	
	public DDLCustomImportWizard setFolder(String folder){
		log.info("Set folder to: '" + folder + "'");
		activate();
		new PushButton(2).click();
		new DefaultTreeItem(folder).select();
		new PushButton("OK").click();
		return this;
	}
	
	public DDLCustomImportWizard setName(String name){
		log.info("Set name to: '" + name + "'");
		activate();
		new DefaultText(1).setText(name);
		return this;
	}
	
	/**
	 * @param modelType - use DDLCustomImportWizard.Source_Type or DDLCustomImportWizard.View_Type
	 */
	public DDLCustomImportWizard setModelType(String modelType){
		log.info("Set model type to: '" + modelType + "'");
		activate();
		new DefaultCombo(2).setText(modelType);
		return this;
	}
	
	public DDLCustomImportWizard generateValidDefaultSQL(boolean checked){
		log.info("Generate valid default sql is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Generate valid default SQL (SELECT null AS column_name, etc....)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public DDLCustomImportWizard setDescriptionOfModel(boolean checked){
		log.info("Description of model is : '" + checked + "'");
		activate();
		new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Set description of model entities to corresponding DDL statement");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}

	public DDLCustomImportWizard createModelEntities(boolean checked){
		log.info("Create model entities is : '" + checked + "'");
		activate();
		new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Create model entities for DDL defined by unsupported DML (e.g., Views)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}
}
