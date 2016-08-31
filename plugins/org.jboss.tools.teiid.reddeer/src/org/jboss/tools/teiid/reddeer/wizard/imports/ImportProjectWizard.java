package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * Wizard for importing an existing project.
 * 
 * @author apodhrad
 * 
 */
public class ImportProjectWizard extends ImportWizardDialog {
	
	private ImportProjectWizard() {
		super("General", "Existing Projects into Workspace");
		log.info("Project import wizard is opened");
	}
	
	public static ImportProjectWizard getInstance(){
		return new ImportProjectWizard();
	}
	
	public static ImportProjectWizard openWizard(){
		ImportProjectWizard wizard = new ImportProjectWizard();
		wizard.open();
		return wizard;
	}
	
	public ImportProjectWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public ImportProjectWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public ImportProjectWizard setPath(String path){
		log.info("Set import path: '" + path + "'");
		activate();
		
		if (path.toLowerCase().endsWith(".zip")) {
			new RadioButton("Select archive file:").click();
			new DefaultCombo(1).setText(path);
		} else {
			new DefaultCombo(0).setText(path);
			if (!new CheckBox("Copy projects into workspace").isChecked()) {
				new CheckBox("Copy projects into workspace").click();
			}
		}
		new PushButton("Refresh").click();
		return this;
	}
}
