package org.jboss.tools.switchyard.reddeer.preference;

import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.switchyard.reddeer.wizard.ValidatorWizard;

public class ValidatorsPage extends CompositePropertiesPage {

	public ValidatorWizard add() {
		activate();
		new PushButton("Add").click();
		return new ValidatorWizard();
	}

	public ValidatorsPage edit() {
		activate();
		new PushButton("Edit").click();
		return this;
	}

	public ValidatorsPage remove() {
		activate();
		new PushButton("Remove").click();
		return this;
	}

	public ValidatorsPage removeAll() {
		List<TableItem> items = getValidators();
		for (TableItem item : items) {
			item.select();
			remove();
		}
		return this;
	}

	public ValidatorsPage selectByName(String name) {
		List<TableItem> items = getValidators();
		for (TableItem item : items) {
			if (item.getText(0).equals(name)) {
				item.select();
				break;
			}
		}
		return this;
	}

	public List<TableItem> getValidators() {
		return new DefaultTable().getItems();
	}
}
