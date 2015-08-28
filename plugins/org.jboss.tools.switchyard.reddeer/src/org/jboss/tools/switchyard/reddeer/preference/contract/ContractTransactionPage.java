package org.jboss.tools.switchyard.reddeer.preference.contract;

import java.util.List;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

/**
 * Represents a properties page "Contract --> Transaction Policy".
 * 
 * @author tsedmik, apodhrad
 */
public class ContractTransactionPage {

	private static final String TRANSACTION = "Transaction Policy:";

	public boolean isComboEnabled() {
		return new LabeledCombo(TRANSACTION).isEnabled();
	}

	public int getComboSelectionIndex() {
		return new LabeledCombo(TRANSACTION).getSelectionIndex();
	}

	public String getComboSelection() {
		return new LabeledCombo(TRANSACTION).getSelection();
	}

	public ContractTransactionPage setTransactionPolicy(String policy) {
		new LabeledCombo(TRANSACTION).setText(policy);
		return this;
	}

	public List<String> getAllTransactionPolicies() {
		return new DefaultCombo().getItems();
	}
}
