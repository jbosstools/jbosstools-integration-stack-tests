package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.spinner.DefaultSpinner;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class LdapConnectionProfileWizard extends ConnectionProfileWizard{

    public static final String DIALOG_TITLE = "New connection Profile";

	private LdapConnectionProfileWizard(String name) {
		super("LDAP",name);
		log.info("Ldap connection profile wizard is opened");
	}
	
	public static LdapConnectionProfileWizard getInstance(){
		return new LdapConnectionProfileWizard(null);
	}
	
	public static LdapConnectionProfileWizard openWizard(String name){
		LdapConnectionProfileWizard wizard = new LdapConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}
	
	public LdapConnectionProfileWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public LdapConnectionProfileWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public LdapConnectionProfileWizard setHostname(String hostname) {
		log.info("Set host name to: '" + hostname + "'");
		activate();
		new LabeledText("Hostname").setText(hostname);
		return this;
	}

	public LdapConnectionProfileWizard setPort(String port) {
		log.info("Set port to: '" + port + "'");
		activate();
		new DefaultSpinner().setValue(Integer.parseInt(port));
		return this;
	}
	
	public LdapConnectionProfileWizard setSslEncryption() {
		log.info("Set ssl encryption");
		activate();
		new LabeledCombo("Encryption method").setSelection(1);
		return this;
	}
	
	public LdapConnectionProfileWizard connectAfter(boolean checked) {
		log.info("Connect when the wizard completes : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Connect when the wizard completes");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public LdapConnectionProfileWizard setUsername(String username) {
		log.info("Set user name to: '" + username + "'");
		activate();
		new LabeledText("Bind DN or user").setText(username);
		return this;
	}
	
	public LdapConnectionProfileWizard setPassword(String password) {
		log.info("Set password to: '" + password + "'");
		activate();
		new LabeledText("Bind password").setText(password);
		return this;
	}
	
	@Override
	public LdapConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activate();
		return this;
	}
}