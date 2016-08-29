package org.jboss.tools.teiid.reddeer.wizard.exports;

import org.jboss.reddeer.jface.wizard.ExportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for export teiid DDL
 * @author mkralik
 */
public class DDLTeiidExportWizard extends ExportWizardDialog{
	public static final String DIALOG_TITLE = "Export Teiid DDL";

	
	public DDLTeiidExportWizard() {
		super("Teiid Designer", "Teiid DDL");
	}
	
	public DDLTeiidExportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLTeiidExportWizard setLocation(String... location) {
		activate();
		new PushButton("...").click();
		new DefaultShell();
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		return this;
	}
	
	public DDLTeiidExportWizard setNameInSource() {
		activate();
		new CheckBox("Add Name In Source values as OPTIONS").click();
		return this;
	}
	
	public DDLTeiidExportWizard setNativeType() {
		activate();
		new CheckBox("Add Native Type values as OPTIONS").click();
		return this;
	}
	
	public DDLTeiidExportWizard exportToWorkspace(String Name,String... location) {
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
}
