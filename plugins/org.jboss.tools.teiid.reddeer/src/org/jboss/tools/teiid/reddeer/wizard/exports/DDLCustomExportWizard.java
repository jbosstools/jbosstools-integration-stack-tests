package org.jboss.tools.teiid.reddeer.wizard.exports;

import java.util.Arrays;

import org.jboss.reddeer.jface.wizard.ExportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for export custom DDL
 * @author mkralik
 */
public class DDLCustomExportWizard extends ExportWizardDialog{
		
	public static final String DIALOG_TITLE = "Export DDL";
	
	public static final String TYPE_IBM = "IBM DB2 DDL";
	public static final String TYPE_MSSQL = "MS SQL Server DDL";
	public static final String TYPE_MYSQL = "MySQL DDL";
	public static final String TYPE_ORACLE = "Oracle DDL";
	public static final String TYPE_PASSTHROUGH = "Passthrough (intermediate XML form)";
	public static final String TYPE_POSTGRESQL = "PostgreSQL DDL";
	public static final String TYPE_SYBASE = "Sybase DDL";

	private DDLCustomExportWizard() {
		super("Teiid Designer", "Data Definition Language (DDL)");
		log.info("DDL custom export Wizard is opened");
	}
	
	public static DDLCustomExportWizard getInstance(){
		return new DDLCustomExportWizard();
	}
	
	public static DDLCustomExportWizard openWizard(){
		DDLCustomExportWizard wizard = new DDLCustomExportWizard();
		wizard.open();
		return wizard;
	}
	
	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
	
	public DDLCustomExportWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}
	
	public DDLCustomExportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLCustomExportWizard setNameInSource(boolean checked) {
		log.info("Set name in source : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Generate Names from \'\'Name in Source\'\' (if applicable)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public DDLCustomExportWizard setNativeType(boolean checked) {
		log.info("Set native type : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Generate Types from \'\'Native Type\'\' (if applicable)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	/**
	 * @param type - use types from DDLCustomExportWizard.<type>
	 */
	public DDLCustomExportWizard setType(String type) {
		log.info("Set type : '" + type + "'");
		activate();
		new LabeledCombo("Type:").setSelection(type);
		return this;
	}
	
	public DDLCustomExportWizard exportToSQLWorkbook() {
		log.info("Export to sql workbook");
		activate();
		new RadioButton(new DefaultGroup("Export To:"),"SQL Workbook").click();
		return this;
	}
	
	public DDLCustomExportWizard exportToFile(String location) {
		log.info("Export to file");
		activate();
		new RadioButton(new DefaultGroup("Export To:"),"File").click();
		new DefaultCombo(new DefaultGroup("Export To:"),0).setText(location);
		return this;
	}
	
	public DDLCustomExportWizard exportToWorkspace(String Name,String... location) {
		log.info("Export to workspace");
		activate();
		new PushButton("Export to Workspace...").click();
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		new LabeledText("Name:").setText(Name);
		new PushButton("OK").click();
		return this;
	}
	
	public DDLCustomExportWizard setLocation(String... location) {
		log.info("Set location to : '" + Arrays.toString(location) + "'");
		activate();
		new PushButton("...").click();
		new DefaultShell();
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		return this;
	}
}
