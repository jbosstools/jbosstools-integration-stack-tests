package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewDroolsEmptyProjectWizardPage extends WizardPage {

	private static final String PROJECT_NAME_LABEL = "Project name:";
	private static final String LOCATION_LABEL = "Location";
	private static final String USE_RUNTIME_LABEL = "Use Runtime:";

	public void setProjectName(String projectName) {
		new LabeledText(PROJECT_NAME_LABEL).setText(projectName);
	}

	public void setUseDefaultLocation(boolean value) {
		new CheckBox(0).toggle(value);
	}
	
	public void setLocation(String location) {
		new LabeledText(LOCATION_LABEL).setText(location);
	}

	public void setUseDefaultRuntime(boolean value) {
		new CheckBox(1).toggle(value);
	}

	public void selectRuntime(String runtimeName) {
		setUseDefaultRuntime(false);
		new LabeledCombo(USE_RUNTIME_LABEL).setSelection(runtimeName);
	}
}
