package org.jboss.tools.teiid.reddeer.wizard.exports;

import java.util.Arrays;

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
	
	private DDLTeiidExportWizard() {
		super("Teiid Designer", "Teiid DDL");
		log.info("DDL teiid export Wizard is opened");
	}
	
	public static DDLTeiidExportWizard getInstance(){
		return new DDLTeiidExportWizard();
	}
	
	public static DDLTeiidExportWizard openWizard(){
		DDLTeiidExportWizard wizard = new DDLTeiidExportWizard();
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
	
	public DDLTeiidExportWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}
	
	public DDLTeiidExportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public DDLTeiidExportWizard setLocation(String... location) {
		log.info("Set location: '" + Arrays.toString(location) + "'");
		activate();
		new PushButton("...").click();
		new DefaultShell();
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		return this;
	}
	
	public DDLTeiidExportWizard setNameInSource(boolean checked) {
		log.info("Set name in source : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Add Name In Source values as OPTIONS");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public DDLTeiidExportWizard setNativeType(boolean checked) {
		log.info("Set native type : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Add Native Type values as OPTIONS");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public DDLTeiidExportWizard exportToWorkspace(String Name,String... location) {
		log.info("Export to workspace : '" + Arrays.toString(location) + "'");
		activate();
		new PushButton("Export to Workspace...").click();
		new DefaultShell("Export DDL To Workspace");
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		new DefaultShell("Export DDL To Workspace");
		new LabeledText("Name:").setText(Name);
		new PushButton("OK").click();
		return this;
	}
}
