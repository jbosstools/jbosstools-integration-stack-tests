package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.List;

import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating a service test class.
 * 
 * @author apodhrad
 * 
 */
public class ServiceTestClassWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "Service Test Class";
	public static final String NAME = "Name:";
	public static final String PACKAGE = "Package:";

	public ServiceTestClassWizard activate() {
		Shell shell = new DefaultShell(DIALOG_TITLE);
		setShell(shell);
		return this;
	}

	public ServiceTestClassWizard setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public ServiceTestClassWizard setPackage(String pkg) {
		new LabeledText(PACKAGE).setFocus();
		new LabeledText(PACKAGE).setText(pkg);
		return this;
	}

	public String getPackage() {
		return new LabeledText(PACKAGE).getText();
	}

	public ServiceTestClassWizard selectMixin(String... mixin) {
		Table table = new DefaultTable();
		List<TableItem> items = table.getItems();
		for (TableItem item : items) {
			String text = item.getText();
			for (int i = 0; i < mixin.length; i++) {
				if (mixin[i] != null && mixin[i].equals(text)) {
					item.setChecked(true);
				}
			}
		}
		return this;
	}

}
