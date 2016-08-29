package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating REST connection profile
 * 
 * @author mmakovy
 *
 */

public class RestProfileWizard extends TeiidProfileWizard {

	public static final String AUTH_TYPE_BASIC = "HTTPBasic";
	public static final String AUTH_TYPE_DIGEST = "Digest";
	
	private String connectionUrl;
	private String type;
	private String authType;
	private String username;
	private String password;

	public RestProfileWizard() {
		super("Web Services Data Source (REST)");
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void execute() {
		open();

		new LabeledText(new DefaultGroup("Properties"), "Connection URL").setText(connectionUrl);

		if (type == null)
			throw new NullPointerException("type is null");

		new DefaultShell("New connection profile");

		if (type.toLowerCase().equals("xml")) {
			new DefaultCombo().setSelection("XML");
		} else if (type.toLowerCase().equals("json")) {
			new DefaultCombo().setSelection("JSON");
		}
		
		
		if (authType != null){
			new DefaultShell("New connection profile");
			new DefaultCombo(1).setSelection(authType);
			new LabeledText(new DefaultGroup("Properties"),"User Name").setText(username);
			new LabeledText(new DefaultGroup("Properties"),"Password").setText(password);
		}

		finish();

	}

}
