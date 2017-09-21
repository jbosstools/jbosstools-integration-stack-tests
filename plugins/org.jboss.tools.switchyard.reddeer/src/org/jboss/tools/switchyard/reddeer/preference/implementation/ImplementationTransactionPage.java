package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.List;

import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Represents a properties page "Transaction Policy".
 * 
 * @author tsedmik, apodhrad
 */
public class ImplementationTransactionPage {

	private static final int TRANSACTION_POLICY = 0;

	public String getTransactionPolicy() {
		return new DefaultCombo(TRANSACTION_POLICY).getText();
	}

	public List<String> getAllTransactionPolicies() {
		return new DefaultCombo(TRANSACTION_POLICY).getItems();
	}

	public ImplementationTransactionPage setTransactionPolicy(String policy) {
		new DefaultCombo(TRANSACTION_POLICY).setText(policy);
		return this;
	}

	public boolean isTransactionPolicyComboEnabled() {
		return new DefaultCombo(TRANSACTION_POLICY).isEnabled();
	}

	public int getComboSelectionIndex() {
		return new DefaultCombo(TRANSACTION_POLICY).getSelectionIndex();
	}
}
