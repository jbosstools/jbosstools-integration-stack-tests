package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Wizard for importing an existing project.
 * 
 * @author apodhrad
 * 
 */
public class ImportProjectWizard extends ImportWizardDialog {

	public static final String ARCHIVE_LABEL = "Select archive file:";

	private String location;

	public ImportProjectWizard(String location) {
		super("General", "Existing Projects into Workspace");
		this.location = location;
	}

	public void execute() {
		open();

		if (location.toLowerCase().endsWith(".zip")) {
			new RadioButton(ARCHIVE_LABEL).click();
			new DefaultCombo(1).setText(location);
		} else {
			new DefaultCombo(0).setText(new File(location).getAbsolutePath());
			//TODO add option to choose
			if (! new CheckBox("Copy projects into workspace").isChecked()){
				new CheckBox("Copy projects into workspace").click();
			}
		}

		new PushButton("Refresh").click();
		finish();
	}

}
