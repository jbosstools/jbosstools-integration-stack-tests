package org.jboss.tools.runtime.reddeer.preference;

import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * 
 * @author apodhrad
 *
 */
public class InstalledJREs extends WorkbenchPreferencePage {

	public InstalledJREs() {
		super("Java", "Installed JREs");
	}

	/**
	 * Adds new jre with a given name and path. If the jre with such name already exists, nothing is added.
	 * 
	 * @param jrePath
	 *            Jre path
	 * @param jreName
	 *            Jre name
	 */
	public void addJre(String jrePath, String jreName) {
		if (containsJreWithName(jreName)) {
			return;
		}

		new PushButton("Add...").click();
		new DefaultShell("Add JRE");
		new DefaultList("Installed JRE Types:").select("Standard VM");
		new NextButton().click();
		new LabeledText("JRE home:").setText(jrePath);
		new LabeledText("JRE name:").setText(jreName);
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Add JRE"));
	}

	/**
	 * Returns whether the jre with a given name already exists.
	 * 
	 * @param jreName
	 *            Jre name
	 * @return whether the jre with a gicen name already exists
	 */
	public boolean containsJreWithName(String jreName) {
		List<TableItem> jreItems = new DefaultTable().getItems();
		for (TableItem jreItem : jreItems) {
			if (jreItem.getText().replace(" (default)", "").equals(jreName)) {
				return true;
			}
		}
		return false;
	}
	
	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}

	public void ok() {
		String title = new DefaultShell().getText();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
