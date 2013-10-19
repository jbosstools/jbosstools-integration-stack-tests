package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.List;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Represents a properties page "Contract --> Transaction Policy".
 * 
 * @author tsedmik
 */
public class DefaultContractTransactionPage extends DefaultPage {

	private static final String TRANSACTION = "Transaction Policy:";
	
	public boolean isComboEnabled() {

		return isComboEnabled(TRANSACTION);
	}

	public int getComboSelectionIndex() {
		
		return getComboSelectionIndex(TRANSACTION);
	}
	
	public String getComboSelection() {
		
		return new DefaultCombo(TRANSACTION).getSelection();
	}
	
	public DefaultContractTransactionPage setTransactionPolicy(String policy) {
		
		new DefaultCombo(TRANSACTION).setText(policy);
		return this;
	}
	
	public List<String> getAllTransactionPolicies() {
		
		return getAllComboValues(0);
	}
}
