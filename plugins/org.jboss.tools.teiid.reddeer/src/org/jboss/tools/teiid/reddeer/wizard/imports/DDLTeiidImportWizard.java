package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Wizard for import teiid DDL
 * @author mkralik
 */
public class DDLTeiidImportWizard  extends ImportWizardDialog{

	private static DDLTeiidImportWizard INSTANCE;
	
	public static final String DIALOG_TITLE = "Import Teiid DDL";

	public static final String Source_Type = "Source Model";
	public static final String View_Type = "View Model";
	
	private DDLTeiidImportWizard() {
		super("Teiid Designer","DDL File (Teiid) >> Source or View Model");
		log.info("DDL teiid import Wizard is opened");
	}
	
	public static DDLTeiidImportWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new DDLTeiidImportWizard();
		}
		return INSTANCE;
	}
	
	public static DDLTeiidImportWizard openWizard(){
		DDLTeiidImportWizard wizard = getInstance();
		wizard.open();
		return wizard;
	}
	
	public DDLTeiidImportWizard activate(){
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLTeiidImportWizard setPath(String path){
		log.info("Set import path: '" + path + "'");
		activate();
		new DefaultCombo(0).setText(path);
		return this;
	}
	
	public DDLTeiidImportWizard setFolder(String... folder){
		log.info("Set import folder: '" + folder + "'");
		activate();
		new PushButton(2).click();
		new DefaultTreeItem(folder).select();
		new PushButton("OK").click();
		return this;
	}
	
	public DDLTeiidImportWizard setName(String name){
		log.info("Set name: '" + name + "'");
		activate();
		new DefaultText(1).setText(name);
		return this;
	}
	/**
	 * @param modelType - use DDLTeiidImportWizard.Source_Type or DDLTeiidImportWizard.View_Type
	 */
	public DDLTeiidImportWizard setModelType(String modelType){
		log.info("Set model type: '" + modelType + "'");
		activate();
		new DefaultCombo(1).setText(modelType);
		return this;
	}
	
	public DDLTeiidImportWizard generateValidDefaultSQL(boolean check){
		log.info("Generate valid default SQL is: '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Generate valid default SQL (SELECT null AS column_name, etc....)");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public DDLTeiidImportWizard setDescriptionOfModel(boolean check){
		log.info("Set description of model is: '" + check + "'");
		activate();
		new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Set description of model entities to corresponding DDL statement");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}

	public DDLTeiidImportWizard createModelEntities(boolean check){
		log.info("Create model entities is: '" + check + "'");
		activate();
		new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Create model entities for DDL defined by unsupported DML (e.g., Views)");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}	

	public DDLTeiidImportWizard filterUniqueConstraints(boolean check){
		log.info("Filter unique constraints is: '" + check + "'");
		activate();
		new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Filter unique constraints already defined as a primary key (Teiid Only)");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}

	public DDLTeiidImportWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
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
	
	@Override
	public void finish(){
		log.info("Finish wizard");
		FinishButton button = new FinishButton();
		button.click();
		if(new ShellWithTextIsActive("Table 'Supports Update' Property Changed").test()){
			new PushButton("OK").click();
		}
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}
}
