package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Represents the <i>Deploy Folders</i> preference page in the <i>Fuse
 * Tooling</i> section.
 * 
 * @author tsedmik
 */
public class DeployFolderPreferencePage extends WorkbenchPreferencePage {

	private static final String EDIT_NAME = "Name";
	private static final String EDIT_DEPLOY_FOLDER = "Deploy Folder";
	private static final String EDIT_DESCRIPTION = "Description";
	private static final String BUTTON_ADD = "Add";

	public DeployFolderPreferencePage() {
		super("Fuse Tooling", "Deploy Folders");
	}

	/**
	 * Adds a new deploy folder. <b>Note: Be sure that another record with the
	 * same <i>name</i> not exists.</b>
	 * 
	 * @param name
	 *            Unique deploy folder's name
	 * @param folder
	 *            target destination of deploy folder within a Fuse server
	 *            folder
	 * @param description
	 */
	public void addDeployFolder(String name, String folder, String description) {

		new LabeledText(EDIT_NAME).setText(name);
		new LabeledText(EDIT_DEPLOY_FOLDER).setText(folder);
		new LabeledText(EDIT_DESCRIPTION).setText(description);
		new PushButton(BUTTON_ADD).click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Returns the count of defined deploy folders
	 * 
	 * @return count of defined deploy folders
	 */
	public int getDeployFoldersCount() {

		return new DefaultTable().rowCount();
	}
}