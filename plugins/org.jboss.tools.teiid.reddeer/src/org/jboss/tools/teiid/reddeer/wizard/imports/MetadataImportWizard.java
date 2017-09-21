package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from designer text file
 * 
 * @author apodhrad
 * 
 */
public class MetadataImportWizard extends TeiidImportWizard {
	
	public static final String DIALOG_TITLE = "Import Metadata From Text File";
	
	public static final String TYPE_RELATIONAL_MODEL = "Relational Model (XML Format)";
	public static final String TYPE_RELATIONAL_TABLE = "Relational Tables (CSV Format)";
	public static final String TYPE_RELATIONAL_VIRTUAL_TABLE = "Relational Virtual Tables (CSV Format)";

	private MetadataImportWizard() {
		super(DIALOG_TITLE, "Designer Text File >> Source or View Models");
		log.info("Import metadata wizard is opened");
	}
	
	public static MetadataImportWizard getInstance(){
		return new MetadataImportWizard();
	}
	
	public static MetadataImportWizard openWizard(){
		MetadataImportWizard wizard = new MetadataImportWizard();
		wizard.open();
		return wizard;
	}
	
	public MetadataImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public MetadataImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	/**
	 * @param importType - use types from MetadataImportWizard.<type>
	 */
	public MetadataImportWizard setImportType(String importType) {
		log.info("Set import type to: '" + importType + "'");
		activate();
		new DefaultCombo().setSelection(importType);
		return this;
	}
	
	public MetadataImportWizard setPathToFile(final String path) {
		log.info("Set path to file: '" + path + "'");
		activate();
		// Workaround due to TEIIDDES-417
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				LabeledCombo combo = new LabeledCombo("Select Source Text File");
				combo.getSWTWidget().add(path);
				combo.setSelection(path); 
			}
		});
		return this;
	}
	
	public MetadataImportWizard setProject(String project, String modelName){
		new PushButton(1).click();
		new DefaultTreeItem(project).select();
		new LabeledText("New Model Name: ").setText(modelName);
		new PushButton("OK").click();
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
}
