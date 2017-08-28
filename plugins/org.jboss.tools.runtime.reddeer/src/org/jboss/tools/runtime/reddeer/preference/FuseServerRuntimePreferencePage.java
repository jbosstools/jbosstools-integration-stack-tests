package org.jboss.tools.runtime.reddeer.preference;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewRuntimeWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Represents the Fuse server runtime preference page
 * 
 * @author tsedmik
 */
public class FuseServerRuntimePreferencePage extends RuntimePreferencePage {

	private static final String FINISH_BUTTON = "Finish";
	private static final String EDIT_BUTTON = "Edit...";
	private static final String EDIT_WINDOW = "Edit Server Runtime Environment";
	private static final String INSTALL_DIR = "Home Directory";
	private static final String NAME = "Name";
	private static final String SERVER_SECTION = "Red Hat JBoss Middleware";

	/**
	 * Adds a new server runtime.
	 * 
	 * @param path
	 *            installation directory of a server
	 * @param type
	 *            server type (e.g. "JBoss Fuse", "JBoss Fuse 6.1")
	 */
	public void addServerRuntime(String type, String path) {
		NewRuntimeWizardDialog dialog = addRuntime();
		new DefaultTreeItem(SERVER_SECTION, type).select();
		dialog.next();
		new LabeledText(NAME).setText(type);
		new LabeledText(INSTALL_DIR).setText(path);
		dialog.finish(TimePeriod.NORMAL);
	}

	/**
	 * Edits the server runtime
	 * 
	 * @param name
	 *            name of the edited server runtime
	 * @param path
	 *            a new installation directory
	 */
	public void editServerRuntime(String name, String path) {
		new DefaultTable(0).select(name);
		new PushButton(EDIT_BUTTON).click();
		new DefaultShell(EDIT_WINDOW).setFocus();
		new LabeledText(INSTALL_DIR).setText(path);
		new PushButton(FINISH_BUTTON).click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
}
