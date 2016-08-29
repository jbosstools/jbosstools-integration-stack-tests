package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.io.File;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for importing a file from file system
 * 
 * @author lkrejcir (=lfabriko)
 */
public class ImportFromFileSystemWizard extends ImportWizardDialog {

	public ImportFromFileSystemWizard() {
		super("General", "File System");
		log.info("Import item from file wizard is opened");
	}

	public ImportFromFileSystemWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public ImportFromFileSystemWizard setPath(String path){
		log.info("Set import path: '" + path + "'");
		activate();
		new DefaultCombo(0).setText(new File(path).getAbsolutePath());
		return this;
	}
	
	public ImportFromFileSystemWizard setFolder(String folder){
		log.info("Set folder to: '" + folder + "'");
		activate();
		new LabeledText("Into folder:").setText(folder);
		return this;
	}
	
	public ImportFromFileSystemWizard selectFile(String file){
		log.info("Select file: '" + file + "'");
		activate();
		new LabeledText("Into folder:").setFocus();
		new DefaultTable().getItem(file).setChecked(true);
		return this;
	}
	
	public ImportFromFileSystemWizard setOverwriteResources(boolean check){
		log.info("Overwrite resources is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Overwrite existing resources without warning");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportFromFileSystemWizard setCreteTopLevelFolder(boolean check){
		log.info("Create top-level folder is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Create top-level folder");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
}