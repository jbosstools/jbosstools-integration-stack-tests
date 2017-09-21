package org.jboss.tools.common.reddeer.preference;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

/**
 * Represents "Maven --> User Settings" preference page
 * 
 * @author tsedmik
 */
public class MavenUserSettingsPreferencePage extends PreferencePage {

	public MavenUserSettingsPreferencePage(ReferencedComposite ref) {
		super(ref, "Maven", "User Settings");
	}

	public String getGlobalSettings() {
		return new DefaultText(1).getText();
	}

	public String getUserSettings() {
		return new DefaultText(2).getText();
	}

	public void setGlobalSettings(String path) {
		new DefaultText(1).setText(path);
	}

	public void setUserSettings(String path) {
		new DefaultText(2).setText(path);
	}

	public void updateSettings() {
		new PushButton("Update Settings").click();
	}

	public void reindex() {
		new PushButton("Reindex").click();
	}

	public String getRepositoryLocation() {
		return new DefaultText(3).getText();
	}
}
