package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.teiid.reddeer.dialog.CreateDriverDialog;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class DatabaseConnectionProfile extends ConnectionProfileWizard{
	
	public static final String DIALOG_TITLE = "New Connection Profile";
	private String vendor = null;
	
	public DatabaseConnectionProfile(String vendor, String name) {
		super(vendor, name);
		this.vendor = vendor;
		log.info(vendor+" connection profile wizard is opened");
	}
	
	public static DatabaseConnectionProfile openWizard(String vendor, String name){
		DatabaseConnectionProfile wizard = new DatabaseConnectionProfile(vendor,name);
		wizard.open();
		return wizard;
	}

	public DatabaseConnectionProfile activate() {
		if(vendor.equals("Sybase ASE")){
			new DefaultShell("New Sybase ASE Connection Profile");
		}else if(vendor.equals("PostgreSQL")||vendor.equals("Ingres")){
			new DefaultShell("New JDBC Connection Profile");
		}else{
			new DefaultShell(DIALOG_TITLE);
		}
		return this;
	}
	
	public CreateDriverDialog createNewDriver(){	
		log.info("Create new driver");
		activate();
		new DefaultToolItem("New Driver Definition").click();
		return new CreateDriverDialog();
	}
	
	public DatabaseConnectionProfile setHostname(String hostname) {
		log.info("Set host name to: '" + hostname + "'");
		activate();
		if(vendor.equals("Sybase ASE")||vendor.equals("PostgreSQL")||vendor.equals("Ingres")
				||vendor.equals("MySQL")||vendor.equals("Generic JDBC")){
			new LabeledText("URL:").setText(hostname);
		}else{
			new LabeledText("Host:").setText(hostname);
		}
		return this;
	}
	
	public DatabaseConnectionProfile setDatabase(String database) {
		log.info("Set database to: '" + database + "'");
		activate();
		if(vendor.equals("Oracle")){
			new LabeledText("SID:").setText(database);
		}else{
			new LabeledText("Database:").setText(database);
		}
		return this;
	}
	
	public DatabaseConnectionProfile setPort(String port) {
		log.info("Set port to: '" + port + "'");
		activate();
		new LabeledText("Port number:").setText(port);
		return this;
	}
	
	public DatabaseConnectionProfile setUsername(String username) {
		log.info("Set user name to: '" + username + "'");
		activate();
		new LabeledText("User name:").setText(username);
		return this;
	}
	
	public DatabaseConnectionProfile setPassword(String password) {
		log.info("Set password to: '" + password + "'");
		activate();
		new LabeledText("Password:").setText(password);
		return this;
	}
	
	public DatabaseConnectionProfile savePassword(boolean checked) {
		log.info("Save password : '" + checked + "'");
		activate();
		CheckBox checkBox = null;
		if(vendor.equals("HSQLDB")){
			checkBox = new CheckBox("Save Password");
		}else{
			checkBox = new CheckBox("Save password");
		}
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public DatabaseConnectionProfile connectAfter(boolean checked) {
		log.info("Connect when the wizard completes : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Connect when the wizard completes");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	@Override
	public DatabaseConnectionProfile testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activate();
		return this;
	}
}
