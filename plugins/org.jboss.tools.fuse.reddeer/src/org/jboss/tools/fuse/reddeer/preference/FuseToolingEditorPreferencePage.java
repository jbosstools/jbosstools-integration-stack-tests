package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Represents the "Fuse Tooling --> Editor" preference page
 * 
 * @author tsedmik
 */
public class FuseToolingEditorPreferencePage extends WorkbenchPreferencePage {

	public FuseToolingEditorPreferencePage() {
		super("Fuse Tooling", "Editor");
	}

	/**
	 * Sets checkbox "If enabled the ID values will be used for labels if existing"
	 * 
	 * @param value true - check the checkbox, false - uncheck the checkbox
	 */
	public void setShowIDinEditor(boolean value) {

		new CheckBox("If enabled the ID values will be used for labels if existing").toggle(value);
	}
}
