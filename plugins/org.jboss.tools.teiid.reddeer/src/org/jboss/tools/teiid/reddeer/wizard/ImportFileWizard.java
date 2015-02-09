package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;

public class ImportFileWizard extends ImportWizardDialog {

	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String path, String importFolder) {
		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("File '" + path + "' not found!");
		}

		open();

		new SWTWorkbenchBot().comboBoxWithLabel("From directory:").typeText(file.getAbsolutePath());
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultText(0).setFocus();
		new SWTWorkbenchBot().tree().setFocus();
		new SWTWorkbenchBot().tree().getTreeItem(importFolder).check();

		finish();
	}
}
