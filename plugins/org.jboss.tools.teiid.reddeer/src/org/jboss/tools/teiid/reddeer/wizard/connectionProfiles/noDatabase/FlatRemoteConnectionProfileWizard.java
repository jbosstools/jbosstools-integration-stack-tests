package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class FlatRemoteConnectionProfileWizard extends ConnectionProfileWizard {

    public static final String DIALOG_TITLE = "New connection profile";

	private FlatRemoteConnectionProfileWizard(String name) {
		super("Flat File URL Source",name);
		log.info("Remote flat file profile wizard is opened");
	}
	
	public static FlatRemoteConnectionProfileWizard getInstance(){
		return new FlatRemoteConnectionProfileWizard(null);
	}
	
	public static FlatRemoteConnectionProfileWizard openWizard(String name){
		FlatRemoteConnectionProfileWizard wizard = new FlatRemoteConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}
	
	public FlatRemoteConnectionProfileWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public FlatRemoteConnectionProfileWizard setUrl(String url) {
		log.info("Set url to : '" + url + "'");
		activateWizard();
		new LabeledText("Connection URL").setText(url);
		activateWizard();
		return this;
	}
	
	@Override
	public FlatRemoteConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activateWizard();
		return this;
	}
}
