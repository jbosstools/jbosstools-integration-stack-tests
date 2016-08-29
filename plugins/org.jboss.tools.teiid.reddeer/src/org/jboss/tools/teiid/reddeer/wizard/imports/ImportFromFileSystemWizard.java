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

	private ImportFromFileSystemWizard() {
		super("General", "File System");
		log.info("Import item from file wizard is opened");
	}

	public static ImportFromFileSystemWizard getInstance(){
		return new ImportFromFileSystemWizard();
	}
	
	public static ImportFromFileSystemWizard openWizard(){
		ImportFromFileSystemWizard wizard = new ImportFromFileSystemWizard();
		wizard.open();
		return wizard;
	}
	
	public ImportFromFileSystemWizard nextPage(){
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
	
	public ImportFromFileSystemWizard setOverwriteResources(boolean checked){
		log.info("Overwrite resources is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Overwrite existing resources without warning");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportFromFileSystemWizard setCreteTopLevelFolder(boolean checked){
		log.info("Create top-level folder is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Create top-level folder");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
}