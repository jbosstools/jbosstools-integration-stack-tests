package org.jboss.tools.switchyard.reddeer.properties;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 *
 */
public class EditLibraryPage extends WizardDialog {

	public EditLibraryPage() {
		activate();
	}

	public EditLibraryPage activate() {
		new DefaultShell("Edit Library");
		return this;
	}

	public EditLibraryPage selectExecutionEnvironment(String env) {
		new RadioButton(new DefaultGroup("System library"), "Execution environment:").click();
		new DefaultCombo(new DefaultGroup("System library"), 0).setSelection(env);
		return this;
	}

	public EditLibraryPage selectExecutionEnvironmentWithPrefix(String prefix) {
		new RadioButton(new DefaultGroup("System library"), "Execution environment:").click();

		Combo combo = new DefaultCombo(new DefaultGroup("System library"), 0);
		selectItemWithPrefix(combo, prefix);
		return this;
	}

	private void selectItemWithPrefix(Combo combo, String prefix) {
		for (String item : combo.getItems()) {
			if (item.startsWith(prefix)) {
				combo.setSelection(item);
				return;
			}
		}
		throw new RuntimeException("Cannot find item with prefix '" + prefix + "'");
	}
}
