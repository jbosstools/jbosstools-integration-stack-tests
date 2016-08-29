package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles;

import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.swt.impl.spinner.LabeledSpinner;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ConnectionProfileLdapPage extends ConnectionProfileDatabasePage {

	public static final String HOSTNAME = "Hostname";
	public static final String LABEL_USER_NAME = "Bind DN or user";
	public static final String LABEL_PASSWORD = "Bind password";
	public static final String LABEL_PRINCIPAL_DN_SUFFIX = "Principal DN Suffix";
	public static final String LABEL_CONTEXT_FACTORY_NAME = "Context Factory Name";

	@Override
	public void setDatabase(String database) {
		// not set

	}

	@Override
	public void setHostname(String hostname) {
		new LabeledText(HOSTNAME).setText(hostname);

	}

	@Override
	public void setPort(String port) {
		new LabeledSpinner("Port").setValue(Integer.valueOf(port));
	}

	@Override
	public void setUsername(String username) {
		new LabeledText(LABEL_USER_NAME).setText(username);
	}

	@Override
	public void setPassword(String password) {
		new LabeledText(LABEL_PASSWORD).setText(password);
	}

	public void setPrincipalDnSuffix(String principalDnSuffix) {
		new LabeledText(LABEL_PRINCIPAL_DN_SUFFIX).setText(principalDnSuffix);

	}

	public void setContextFactoryName(String contextFactoryName) {
		new LabeledText(LABEL_CONTEXT_FACTORY_NAME).setText(contextFactoryName);

	}
}
