package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
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

	public DatabaseConnectionProfile activateWizard() {
		switch (vendor){
		
		case "Sybase ASE":
			new DefaultShell("New Sybase ASE Connection Profile");
			break;
		case "PostgreSQL":
		case "Ingres":
			new DefaultShell("New JDBC Connection Profile");
			break;
		case "MongoDB Data Source":
			new DefaultShell("New MongoDB Data Source Profile");
			break;
		default:
			new DefaultShell(DIALOG_TITLE);
			break;
		}
		return this;
	}
	
	public CreateDriverDialog createNewDriver(){	
		log.info("Create new driver");
		activateWizard();
		new DefaultToolItem("New Driver Definition").click();
		return new CreateDriverDialog();
	}
	
	public DatabaseConnectionProfile setHostname(String hostname) {
		log.info("Set host name to: '" + hostname + "'");
		activateWizard();
		switch(vendor){
		
		case "Sybase ASE":
		case "PostgreSQL":
		case "Ingres":
		case "MySQL":
		case "Generic JDBC":
			new LabeledText("URL:").setText(hostname);
			break;
		case "MongoDB Data Source":
			new LabeledText("Server Host:").setText(hostname);
			break;
		default: 
			new LabeledText("Host:").setText(hostname);
			break;
		}		
		return this;
	}
	
	public DatabaseConnectionProfile setDatabase(String database) {
		log.info("Set database to: '" + database + "'");
		activateWizard();
		
		switch (vendor){
		
		case "Oracle":
			new LabeledText("Database instance:").setText(database);
			break;
		case "MongoDB Data Source":
			new LabeledText("Database Name:").setText(database);
			break;
		default:
			new LabeledText("Database:").setText(database);
			break;
		}		
		return this;
	}
	
	public DatabaseConnectionProfile setPort(String port) {
		log.info("Set port to: '" + port + "'");
		activateWizard();
		new LabeledText("Port number:").setText(port);
		return this;
	}
	
	public DatabaseConnectionProfile setServer(String server) {
		log.info("Set server to: '" + server + "'");
		activateWizard();
		new LabeledText("Server:").setText(server);
		return this;
	}
	
	public DatabaseConnectionProfile setUsername(String username) {
		log.info("Set user name to: '" + username + "'");
		activateWizard();
		if(vendor.equals("MongoDB Data Source")){
			new LabeledText("User Name:").setText(username);
		}else{
			new LabeledText("User name:").setText(username);
		}
		return this;
	}
	
	public DatabaseConnectionProfile setPassword(String password) {
		log.info("Set password to: '" + password + "'");
		activateWizard();
		new LabeledText("Password:").setText(password);
		return this;
	}
	
	public DatabaseConnectionProfile savePassword(boolean checked) {
		log.info("Save password : '" + checked + "'");
		activateWizard();
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
		activateWizard();
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
		activateWizard();
		return this;
	}
}
