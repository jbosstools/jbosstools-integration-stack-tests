package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * @author skaleta
 */
public class SaveAsDialog {
	protected final Logger log = Logger.getLogger(this.getClass());
	public final String DIALOG_TITLE;
	public final String MODEL_NAME_LABEL;
	
	/** 
	 * @param schemaDialog - used to set dialog's texts, because XML Schema Save As dialog 
	 * has different texts unlike rest models Save As dialogs.
	 */
	public SaveAsDialog(boolean schemaDialog) {
		if (schemaDialog){
			DIALOG_TITLE = "Save As";
			MODEL_NAME_LABEL = "File name:";
		} else {
			DIALOG_TITLE = "Save Model As";
			MODEL_NAME_LABEL = "Enter the new model name";
		}
	}
	
	public SaveAsDialog activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public void open(){
		log.info("Opening Save As dialog using top menu ");
		new ShellMenu("File","Save As...").select();
		new DefaultShell(DIALOG_TITLE);
	}

	public SaveAsDialog setLocation(String... path) {
		activate();
		log.info("Set location to '" + pathToString(path) + "'");
		new DefaultTreeItem(path).select();
		return this;
	}
	
	public SaveAsDialog setModelName(String modelName) {
		activate();
		log.info("Set model name to '" + modelName + "'");
		new LabeledText(MODEL_NAME_LABEL).setText(modelName);
		// activate to refresh focus
		activate();
		return this;
	}
	
	public boolean isOkEnabled() {
		return new OkButton().isEnabled();
	}
	
	public void ok() {
		log.info("Ok Save As Dialog");
		
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(DIALOG_TITLE), TimePeriod.NORMAL);
		
		// sometimes can't find dialog	
		AbstractWait.sleep(TimePeriod.SHORT);
		String importRefDialogName = DIALOG_TITLE + " - Import References";
		if (new ShellWithTextIsActive(importRefDialogName).test()){
			// close import references dialog too
			// TODO action in dialog
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsActive(importRefDialogName), TimePeriod.NORMAL);
		}
	}
	
	public void cancel() {
		log.info("Cancel Save As Dialog");

		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsActive(DIALOG_TITLE), TimePeriod.NORMAL);
	}
	
	private static String pathToString(String... path) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < path.length; i++) {
			result.append(path[i]);
			if (path.length < i - 1) {
				result.append(" > ");
			}
		}
		return result.toString();
	}
}
