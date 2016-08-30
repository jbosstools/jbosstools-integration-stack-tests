package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class FlatRemoteConnectionProfileWizard extends ConnectionProfileWizard {

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
	
	public FlatRemoteConnectionProfileWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public FlatRemoteConnectionProfileWizard setUrl(String url) {
		log.info("Set url to : '" + url + "'");
		activate();
		new LabeledText("Connection URL").setText(url);
		activate();
		return this;
	}
	
	@Override
	public FlatRemoteConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activate();
		return this;
	}
}
