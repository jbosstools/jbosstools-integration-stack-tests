package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.List;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Represents a properties page "Transaction Policy".
 * 
 * @author tsedmik
 */
public class DefaultTransactionPage extends DefaultPage {
	
	private static final int TRANSACTION_POLICY = 0;
	
	public String getTransactionPolicy() {
		
		return new DefaultCombo(TRANSACTION_POLICY).getText();
	}
	
	public List<String> getAllTransactionPolicies() {
		
		return getAllComboValues(TRANSACTION_POLICY);
	}
	
	public DefaultTransactionPage setTransactionPolicy(String policy) {
		
		new DefaultCombo(TRANSACTION_POLICY).setText(policy);
		return this;
	}
	
	public boolean isTransactionPolicyComboEnabled() {
		
		return isComboEnabled(TRANSACTION_POLICY);
	}

	public int getComboSelectionIndex() {
		
		return getComboSelectionIndex(TRANSACTION_POLICY);
	}
}
