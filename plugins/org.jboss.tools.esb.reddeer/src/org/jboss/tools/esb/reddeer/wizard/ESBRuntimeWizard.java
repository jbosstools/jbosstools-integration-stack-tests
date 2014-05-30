package org.jboss.tools.esb.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBRuntimeWizard extends WizardDialog {


	public static final String JBOSS_ESB_RUNTIME = "JBoss ESB Runtime";
	public static final String HOME_FOLDER = "Home Folder:";

	public ESBRuntimeWizard() {
		super();
		new DefaultShell("New JBoss ESB Runtime");
	}

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public String getName() {
		return new LabeledText("Name:").getText();
	}

	public void setVersion(String version) {
		new LabeledCombo("Version:").setSelection(version);
	}

	public String getVersion() {
		return new LabeledCombo("Version:").getText();
	}

	public void setHomeFolder(String folder) {
		new LabeledText(HOME_FOLDER).setText(folder);
	}

	public String getHomeFolder() {
		return new LabeledText(HOME_FOLDER).getText();
	}
	
	public String getInfoText() {
		return new LabeledText(JBOSS_ESB_RUNTIME).getText().trim();
	}

}
