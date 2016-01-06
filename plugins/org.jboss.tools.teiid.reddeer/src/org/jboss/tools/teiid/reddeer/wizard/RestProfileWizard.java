package org.jboss.tools.teiid.reddeer.wizard;

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

	private String connectionUrl;
	private String type;

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

		finish();

	}

}
