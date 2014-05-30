package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating a service test class.
 * 
 * @author apodhrad
 * 
 */
public class ServiceTestClassWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "Service Test Class";
	public static final String PACKAGE = "Package:";

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public ServiceTestClassWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
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
