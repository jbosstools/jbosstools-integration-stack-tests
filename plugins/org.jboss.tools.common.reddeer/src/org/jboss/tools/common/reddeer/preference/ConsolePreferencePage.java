package org.jboss.tools.common.reddeer.preference;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * Represents <i>Console</i> preference page
 * 
 * @author tsedmik
 */
public class ConsolePreferencePage extends PreferencePage {

	public ConsolePreferencePage() {
		super("Run/Debug", "Console");
	}

	public void toggleShowConsoleStandardWrite(boolean checked) {
		new CheckBox(2).toggle(checked);
	}

	public void toggleShowConsoleErrorWrite(boolean checked) {
		new CheckBox(3).toggle(checked);
	}
}