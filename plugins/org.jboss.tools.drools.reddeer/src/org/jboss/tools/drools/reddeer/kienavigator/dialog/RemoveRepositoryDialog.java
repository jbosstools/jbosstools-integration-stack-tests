package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class RemoveRepositoryDialog {

	public void deleteRepoFromServer(boolean deleteRepo) {
		new LabeledCheckBox("Also delete the Repository complete from the Server.").toggle(deleteRepo);
	}

	public void yes() {
		new PushButton("Yes").click();
	}

	public void no() {
		new PushButton("No").click();
	}
}
