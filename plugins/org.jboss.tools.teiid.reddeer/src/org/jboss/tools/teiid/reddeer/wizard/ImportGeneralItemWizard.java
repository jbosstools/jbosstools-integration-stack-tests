package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;
import java.util.Properties;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;

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
	

	public static class Type{
		public static final String ARCHIVE_FILE = "Archive File";
		public static final String EXISTING_PROJECTS_INTO_WORKSPACE = "Existing Projects into Workspace";
		public static final String FILE_SYSTEM = "File System";
		public static final String PREFERENCES = "Preferences";
	}
	
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
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
		
		if (generalItemType.equals(Type.EXISTING_PROJECTS_INTO_WORKSPACE)){
			executeExistingProjects();
		}
		else if (generalItemType.equals(Type.FILE_SYSTEM)){
			executeFileSystem();
		}
		//TODO if needed - archive file and preferences
		finish();
	}
	
	private void executeFileSystem() {
		String dirName = new File(itemProps.getProperty("dirName")).getAbsolutePath();//from where is imported
		String intoFolder = itemProps.getProperty("intoFolder");//project name
		String file = itemProps.getProperty("file");
		String createTopLevel = itemProps.getProperty("createTopLevel");//ie. import lib/abc and create folder lib
		
		new DefaultCombo().setText(dirName);
		new LabeledText(INTO_FOLDER).setFocus();
		new DefaultTable().getItem(file).setChecked(true);
		new LabeledText(INTO_FOLDER).setText(intoFolder);
		if (createTopLevel != null){
			new CheckBox("Create top-level folder").toggle(Boolean.valueOf(createTopLevel));
		}
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
