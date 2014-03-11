package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;
import java.util.Properties;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for importing an archive file, existing project, file system, preferences.
 * 
 * @author lkrejcir (=lfabriko)
 * 
 * 
 * 
 */
public class ImportGeneralItemWizard extends ImportWizardDialog {

	private static final String ARCHIVE_LABEL = "Select archive file:";
	
	public static final String ARCHIVE_FILE = "Archive File";
	public static final String EXISTING_PROJECTS_INTO_WORKSPACE = "Existing Projects into Workspace";
	public static final String FILE_SYSTEM = "File System";
	public static final String PREFERENCES = "Preferences";

	private static final String INTO_FOLDER = "Into folder:";
	
	private String generalItemType;
	private Properties itemProps;

	public ImportGeneralItemWizard(String generalItemType, Properties itemProps) {
		super("General", generalItemType);
		this.itemProps = itemProps;
		this.generalItemType = generalItemType;
	}
	
	public void execute(){
		open();
		
		if (generalItemType.equals(EXISTING_PROJECTS_INTO_WORKSPACE)){
			executeExistingProjects();
		}
		else if (generalItemType.equals(FILE_SYSTEM)){
			executeFileSystem();
		}
		finish();
	}
	
	private void executeFileSystem() {
		String dirName = new File(itemProps.getProperty("dirName")).getAbsolutePath();//from where is imported
		String intoFolder = itemProps.getProperty("intoFolder");//project name
		String file = itemProps.getProperty("file");
		
		new DefaultCombo().setText(dirName);
		new LabeledText(INTO_FOLDER).setFocus();
		new DefaultTable().getItem(file).setChecked(true);
		new LabeledText(INTO_FOLDER).setText(intoFolder);
	}

	private void executeExistingProjects() {//existing projects into ws
		String location = itemProps.getProperty("location");
		if (location.toLowerCase().endsWith(".zip")) {
			new RadioButton(ARCHIVE_LABEL).click();
			new DefaultCombo(1).setText(location);
		} else {
			new DefaultCombo(0).setText(location);
		}

		new PushButton("Refresh").click();
		
	}

}
