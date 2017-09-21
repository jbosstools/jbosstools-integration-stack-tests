package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class GenerateRestProcedureDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(GenerateRestProcedureDialog.class);
	
	public GenerateRestProcedureDialog() {
		super("Generate REST Virtual Procedures");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Sets location of procedure's model.
	 */
	public GenerateRestProcedureDialog setTargetModelLocation(String... path) {
		log.info("Setting target model location to " + Arrays.toString(path));
		new PushButton(new DefaultGroup("View Model Definition"), 0, new WithMnemonicTextMatcher("...")).click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(path).select();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
		return this;
	}
	
	/**
	 * Sets existing model as procedure's model.
	 * @param path - path to model (<PROJECT>, ..., <MODEL>)
	 */
	public GenerateRestProcedureDialog setExistingTargetModel(String... path) {
		log.info("Setting existing target model to " + Arrays.toString(path));
		new PushButton(new DefaultGroup("View Model Definition"), 1, new WithMnemonicTextMatcher("...")).click();
		new DefaultShell("Select View Model");
		new DefaultTreeItem(path).select();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
		return this;
	}
	
	/**
	 * Sets name of new procedure's model.
	 */
	public GenerateRestProcedureDialog setNewTargetModel(String name) {
		log.info("Setting new target model name to " + name);
		new LabeledText("Name:").setText(name);
		activate();
		return this;
	}
	
	/**
	 * Selects specified tables.
	 */
	public GenerateRestProcedureDialog selectTables(String... tables) {
		log.info("Selecting tables: " + Arrays.toString(tables));
		DefaultTable table = new DefaultTable(new DefaultGroup("Tables Selection"), 0);
		for (String t : tables) {
			table.getItem(t).setChecked(true);
		}
		activate();
		return this;
	}
	
}
