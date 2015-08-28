package org.jboss.tools.switchyard.reddeer.preference.contract;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

/**
 * Represents a properties page "Contract --> Security Policy".
 * 
 * @author tsedmik, apodhrad
 */
public class ContractSecurityPage {

	private static final String CKCK_AUTHENTICATION = "Client Authentication";
	private static final String CONFIDENTALITY = "Confidentiality";
	private static final String COMBOBOX_SECURITY = "Security Configuration";

	public boolean isAuthenticationChecked() {
		return new CheckBox(CKCK_AUTHENTICATION).isChecked();
	}

	public boolean isAuthenticationEnabled() {
		return new CheckBox(CKCK_AUTHENTICATION).isEnabled();
	}

	public ContractSecurityPage setAuthentication(boolean value) {
		new CheckBox(CKCK_AUTHENTICATION).toggle(value);
		return this;
	}

	public boolean isConfidentalityChecked() {
		return new CheckBox(CONFIDENTALITY).isChecked();
	}

	public boolean isConfidentalityEnabled() {
		return new CheckBox(CONFIDENTALITY).isEnabled();
	}

	public ContractSecurityPage setConfidentality(boolean value) {
		new CheckBox(CONFIDENTALITY).toggle(value);
		return this;
	}

	public boolean isSecurityConfComboEnabled() {
		return new LabeledCombo(COMBOBOX_SECURITY).isEnabled();
	}

	public ContractSecurityPage setSecurityConf(String conf) {
		new LabeledCombo(COMBOBOX_SECURITY).setText(conf);
		return this;
	}
}
