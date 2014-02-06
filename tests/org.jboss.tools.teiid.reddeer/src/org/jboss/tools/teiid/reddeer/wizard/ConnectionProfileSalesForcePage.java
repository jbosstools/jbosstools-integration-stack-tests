package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ConnectionProfileSalesForcePage extends
		ConnectionProfileDatabasePage {
	
	private static final String USERNAME = "User Name";
	private static final String PASSWORD = "Password";

	protected ConnectionProfileSalesForcePage(WizardDialog wizardDialog,
			int pageIndex) {
		super(wizardDialog, pageIndex);
	}

	@Override
	public void setDatabase(String database) {
		//not set
	}

	@Override
	public void setHostname(String hostname) {
		//not set
	}

	@Override
	public void setPort(String port) {
		//not set
	}

	@Override
	public void setUsername(String username) {
		new LabeledText(USERNAME).setText(username);
	}

	@Override
	public void setPassword(String password) {
		new LabeledText(PASSWORD).setText(password);
	}
	
	/*public void performAdditionalSteps(){
		
	}*/

}
