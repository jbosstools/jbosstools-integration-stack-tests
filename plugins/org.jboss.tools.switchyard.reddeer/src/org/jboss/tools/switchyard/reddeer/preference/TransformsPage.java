package org.jboss.tools.switchyard.reddeer.preference;

import java.util.List;

import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.switchyard.reddeer.wizard.ValidatorWizard;

public class TransformsPage extends CompositePropertiesDialog {

	public static final int COLUMN_FROM = 0;
	public static final int COLUMN_TO = 1;
	public static final int COLUMN_TYPE = 2;

	public TransformsPage() {
		super("");
	}

	public TransformsPage(String title) {
		super(title);
	}

	public ValidatorWizard add() {
		activate();
		new PushButton("Add").click();
		return new ValidatorWizard();
	}

	public TransformsPage remove() {
		activate();
		new PushButton("Remove").click();
		return this;
	}

	public TransformsPage removeAll() {
		List<TableItem> items = getTransforms();
		while (!items.isEmpty()) {
			items.get(0).select();
			remove();
			items = getTransforms();
		}
		return this;
	}

	public List<TableItem> getTransforms() {
		return new DefaultTable().getItems();
	}
}
