package org.jboss.tools.teiid.reddeer.wizard.imports;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

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
	
	private String modelName;

	private MetadataImportWizard() {
		super("Designer Text File >> Source or View Models");
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
	
	public MetadataImportWizard setPathToFile(String path) {
		log.info("Set path to file: '" + path + "'");
		activate();
		// Workaround due to TEIIDDES-417
		addSelection("Select Source Text File", path);
		new DefaultCombo().setSelection(path);
		return this;
	}
	
	/**
	 * setName must be set (MetadataImportWizard.setName)
	 */
	public MetadataImportWizard setProject(String project){
		new PushButton(1).click();
		new DefaultTreeItem(project).select();
		new LabeledText("New Model Name: ").setText(modelName);
		new PushButton("OK").click();
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	/**
	 * name will be filled in the MetadataImportWizard.setProject()
	 */
	public MetadataImportWizard setName(String name){
		modelName = name;
		return this;
	}
	
	// Workaround due to TEIIDDES-417
	private void addSelection(String label, final String selection) {
		SWTBotCombo botCombo = new SWTWorkbenchBot().comboBoxWithLabel("Select Source Text File");
		final Combo combo = botCombo.widget;
		syncExec(new VoidResult() {

			@Override
			public void run() {
				combo.add(selection);
			}
		});
	}
}
