package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;

public class RemoveRepositoryDialog {

	public void deleteRepoFromServer(boolean deleteRepo) {
		new CheckBox("Also delete the Repository complete from the Server.").toggle(deleteRepo);
	}

	public void yes() {
		new PushButton("Yes").click();
	}

	public void no() {
		new PushButton("No").click();
	}
}
