package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class GenericProfilePage extends ConnectionProfileDatabasePage {

	public static final String LABEL_DATABASE = "Database:";
	public static final String LABEL_USER_NAME = "User name:";
	public static final String LABEL_PASSWORD = "Password:";
	public static final String LABEL_SAVE_PASSWORD = "Save password";
	public static final String URL = "URL:";
	
	private static final String HSQL_LOCK = ";hsqldb.lock_file=false";
	private static final String HSQL_PREFIX = "jdbc:hsqldb:";

	@Override
	public void setDatabase(String database) {
		new LabeledText(LABEL_DATABASE).setText(database);
	}

	@Override
	public void setHostname(String hostname) {
		if (hostname.contains("hsql")){//
			hostname = new File(hostname).getAbsolutePath();
			if (!(hostname.contains(";hsqldb.lock_file"))){
				hostname = hostname + HSQL_LOCK;
			}
			if (!(hostname.contains(HSQL_PREFIX))){
				hostname = HSQL_PREFIX + hostname;
			}
		} 
		new LabeledText(URL).setText(hostname);
	}

	@Override
	public void setPort(String port) {
		// there is no port
	}

	@Override
	public void setUsername(String username) {
		new LabeledText(LABEL_USER_NAME).setText(username);
	}

	@Override
	public void setPassword(String password) {
		new LabeledText(LABEL_PASSWORD).setText(password);
		new CheckBox(LABEL_SAVE_PASSWORD).click();
	}


}
