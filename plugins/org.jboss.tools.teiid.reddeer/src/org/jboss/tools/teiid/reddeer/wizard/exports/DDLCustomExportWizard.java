package org.jboss.tools.teiid.reddeer.wizard.exports;

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

	public DDLCustomExportWizard() {
		super("Teiid Designer", "Data Definition Language (DDL)");
	}
	
	public DDLCustomExportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLCustomExportWizard setNameInSource() {
		activate();
		new CheckBox("Generate Names from \'\'Name in Source\'\' (if applicable)").click();
		return this;
	}
	
	public DDLCustomExportWizard setNativeType() {
		activate();
		new CheckBox("Generate Types from \'\'Native Type\'\' (if applicable)").click(); 
		return this;
	}
	
	/**
	 * @param type - use types from DDLCustomExportWizard.<type>
	 */
	public DDLCustomExportWizard setType(String type) {
		activate();
		new LabeledCombo("Type:").setSelection(type);
		
		return this;
	}
	
	public DDLCustomExportWizard exportToSQLWorkbook() {
		activate();
		new RadioButton(new DefaultGroup("Export To:"),"SQL Workbook").click();
		return this;
	}
	
	public DDLCustomExportWizard exportToFile(String location) {
		activate();
		new RadioButton(new DefaultGroup("Export To:"),"File").click();
		new DefaultCombo(new DefaultGroup("Export To:"),0).setText(location);
		return this;
	}
	
	public DDLCustomExportWizard exportToWorkspace(String Name,String... location) {
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
		activate();
		new PushButton("...").click();
		new DefaultShell();
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		return this;
	}
	
}
