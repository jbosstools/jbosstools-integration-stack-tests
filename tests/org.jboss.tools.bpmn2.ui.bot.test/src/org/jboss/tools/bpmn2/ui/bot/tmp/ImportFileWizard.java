package org.jboss.tools.bpmn2.ui.bot.tmp;

import java.io.File;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.handler.ControlHandler;
import org.eclipse.reddeer.eclipse.selectionwizard.ImportMenuWizard;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.table.DefaultTableItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;

/**
 * Temporary copy, while is not included in RedDeer
 * 
 * @author apodhrad
 * 
 */
public class ImportFileWizard extends ImportMenuWizard {

	public ImportFileWizard() {
		super("Import", "General", "File System");
	}

	public void importFile(String folder, String... fileNames) {

		File file = new File(folder);
		if (!file.exists()) {
			throw new RuntimeException("File '" + folder + "' not found!");
		}

		open();

		new DefaultShell("Import");
		new LabeledComboExt("From directory:").setText(file.getAbsolutePath());
		new DefaultTree().setFocus();
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.DEFAULT);
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

			ControlHandler.getInstance().setFocus(swtWidget);
		}

		public void typeText(String text) {

			setText("");
			setFocus();
			KeyboardFactory.getKeyboard().type(text);
		}
	}

}