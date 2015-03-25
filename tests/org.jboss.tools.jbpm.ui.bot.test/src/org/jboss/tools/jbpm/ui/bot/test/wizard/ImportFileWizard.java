package org.jboss.tools.jbpm.ui.bot.test.wizard;

import java.io.File;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * 
 * @author apodhrad
 *
 */
public class ImportFileWizard extends ImportWizardDialog {

	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String folder, String... fileNames) {
		File file = new File(folder);
		if (!file.exists()) {
			throw new RuntimeException("File '" + folder + "' not found!");
		}

		open();

		new DefaultShell("Import");
		new LabeledComboExt("From directory:").typeText(file.getAbsolutePath());
		new LabeledComboExt("From directory:").setText(file.getAbsolutePath());
		new DefaultTree().setFocus();
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.NORMAL);
		for (String fileName : fileNames) {
			new DefaultTableItem(fileName).setChecked(true);
		}
		if (fileNames.length == 0) {
			for (TreeItem item : new DefaultTree().getItems()) {
				item.setChecked(true);
			}
		}

		finish();
	}
	
	private class LabeledComboExt extends LabeledCombo {

		public LabeledComboExt(String label) {
			super(label);
		}

		public void setFocus() {
			log.debug("Set focus to Combo");
			WidgetHandler.getInstance().setFocus(swtWidget);
		}
		
		public void typeText(String text) {
			log.info("Type text " + text);
			setText("");
			setFocus();
			KeyboardFactory.getKeyboard().type(text);
		}
	}

}
