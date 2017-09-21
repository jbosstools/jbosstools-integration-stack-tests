package org.jboss.tools.teiid.reddeer.preference;

import org.eclipse.reddeer.core.matcher.WithTooltipTextMatcher;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.spinner.DefaultSpinner;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidDesignerPreferencePage extends PreferencePage {

	public TeiidDesignerPreferencePage(ReferencedComposite ref) {
		super(ref, "Teiid Designer");

	}

	public void setDefaultTeiidInstanceTargetedVersion(String version) {
		select();
		new DefaultCombo().setSelection(version);
		close();
	}

	// TODO deploy preference for teiid import - seconds to wait
	public void setTeiidConnectionImporterTimeout(int secs) {
		select();
		new DefaultSpinner(new WithTooltipTextMatcher(
				"Set the timeout (in sec) for the Teiid Connection Importer. (use 0 for no timeout)")).setValue(secs);
		close();
	}

	public void setAutoToggleDataRoleChildren(boolean check) {
		select();
		new CheckBox(new DefaultGroup("Preview Data/VDB Execution"),
				"Enable auto-toggling children of the checked model object").toggle(check);
		close();

	}

	public void close() {
		new PushButton("Apply").click();
		if (/*new SWTWorkbenchBot().activeShell().getText().equals("Changing Teiid Instance version")*/
			new ShellIsActive("Changing Teiid Instance version").test()) {
			new PushButton("Yes").click();
		}
		new PushButton("Apply and Close").click();
	}

	public void select() {
		if (referencedComposite instanceof WorkbenchPreferenceDialog) {
			((WorkbenchPreferenceDialog) referencedComposite).select(this);
		}
	}

}
