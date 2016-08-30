package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class SalesforceConnectionProfileWizard extends ConnectionProfileWizard{
	
	private SalesforceConnectionProfileWizard(String name) {
		super("SalesForce",name);
		log.info("Sales force connection profile wizard is opened");
	}
	
	public static SalesforceConnectionProfileWizard getInstance(){
		return new SalesforceConnectionProfileWizard(null);
	}
	
	public static SalesforceConnectionProfileWizard openWizard(String name){
		SalesforceConnectionProfileWizard wizard = new SalesforceConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}
	
	public SalesforceConnectionProfileWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public SalesforceConnectionProfileWizard setUsername(String username) {
		log.info("Set user name to: '" + username + "'");
		activate();
		new LabeledText("User Name").setText(username);
		return this;
	}

	public SalesforceConnectionProfileWizard setPassword(String password) {
		log.info("Set password to: '" + password + "'");
		activate();
		new LabeledText("Password").setText(password);
		return this;
	}
	
	public SalesforceConnectionProfileWizard connectAfter(boolean checked) {
		log.info("Connect when the wizard completes : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Connect when the wizard completes");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	@Override
	public SalesforceConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activate();
		return this;
	}
}
