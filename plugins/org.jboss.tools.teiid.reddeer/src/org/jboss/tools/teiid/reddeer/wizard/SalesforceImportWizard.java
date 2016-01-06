package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

public class SalesforceImportWizard extends ImportWizardDialog {

	private String connectionProfile;
	private String projectName;
	private String modelName;
	private String[] deselectedObjects;
	private String[] selectedObjects;
	// private String[] importOptions

	private static final String VALIDATE_CONNECTION = "Validate Connection";
	private static final String CONNECTION_PROFILE = "Connection Profile";
	private static final String MODEL_NAME = "Model Name:";
	private static final String LOCATION = "Location:";
	private static final String TITLE = "Create Relational Model from SalesForce Data Model";

	public SalesforceImportWizard() {
		super("Teiid Designer", "Salesforce >> Source Model");
	}

	public void execute() {
		open();
		setFocus();
		fillFirstPage();
		next();// wait while shell Progress Information is active
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		setFocus();
		fillSecondPage();
		next();
		setFocus();
		fillThirdPage();
		finish();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}

	private void setFocus() {
		new DefaultShell(TITLE);

	}

	private void fillFirstPage() {
		new SWTWorkbenchBot().comboBoxInGroup(CONNECTION_PROFILE).setSelection(connectionProfile);
		new PushButton(VALIDATE_CONNECTION).click();
	}

	private void fillSecondPage() {
		// either deselect or select
		if (deselectedObjects != null) {
			for (int i = 0; i < deselectedObjects.length; i++) {
				new org.jboss.reddeer.swt.impl.table.DefaultTable().getItem(deselectedObjects[i].trim())
						.setChecked(false);
			}
		} else if (selectedObjects != null) {
			new PushButton("Deselect All").click();
			for (int i = 0; i < selectedObjects.length; i++) {
				// new SWTWorkbenchBot().tableInGroup(SF_OBJECTS).select(selectedObjects[i].trim());
				new org.jboss.reddeer.swt.impl.table.DefaultTable().getItem(selectedObjects[i].trim()).setChecked(true);
			}
		}
	}

	private void fillThirdPage() {
		new DefaultShell("Create Relational Model from SalesForce Data Model");
		new SWTWorkbenchBot().textWithLabel(MODEL_NAME).setText(modelName);
		new SWTWorkbenchBot().textWithLabel(LOCATION).setText(projectName);

		// if importOptions != null ...
	}

	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String[] getDeselectedObjects() {
		return deselectedObjects;
	}

	public void setDeselectedObjects(String[] deselectedObjects) {
		this.deselectedObjects = deselectedObjects;
	}

	public String[] getSelectedObjects() {
		return selectedObjects;
	}

	public void setSelectedObjects(String[] selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

}
