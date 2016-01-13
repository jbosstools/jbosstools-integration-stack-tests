package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;

/**
 * Wizard page for adding and removing Fuse modules on the Fuse server.
 * 
 * @author tsedmik
 */
public class FuseModifyModulesPage extends ModifyModulesPage {

	/**
	 * Sets option 'If server is started, publish changes immediately'
	 * 
	 * @param value
	 *            true - option is checked, false - option is not checked
	 */
	public void setImmeadiatelyPublishing(boolean value) {

		new CheckBox().toggle(value);
	}

	/**
	 * Closes the page via 'Finish' button
	 */
	public void close() {

		new PushButton("Finish").click();
	}
}
