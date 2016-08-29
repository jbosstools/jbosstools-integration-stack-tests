package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class SaveAsDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(SaveAsDialog.class);
	private final String modelNameLabelText;
	
	/** 
	 * @param schemaDialog - used to set dialog's texts, because XML Schema Save As dialog 
	 * has different texts unlike rest models Save As dialogs.
	 */
	public SaveAsDialog(boolean schemaDialog) {
		super((schemaDialog) ? "Save As" : "Save Model As");
		modelNameLabelText = (schemaDialog) ? "File name:" : "Enter the new model name";
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		
		AbstractWait.sleep(TimePeriod.SHORT);
		String importRefDialogName = title + " - Import References";
		if (new ShellWithTextIsActive(importRefDialogName).test()){
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsActive(importRefDialogName), TimePeriod.NORMAL);
		}
	}

	/**
	 * Sets location of new model.
	 */
	public SaveAsDialog setLocation(String... path) {
		log.info("Setting location to " + Arrays.toString(path));
		new DefaultTreeItem(path).select();
		return this;
	}
	
	/**
	 * Sets name of new model.
	 */
	public SaveAsDialog setModelName(String modelName) {
		log.info("Setting model name to '" + modelName + "'");
		new LabeledText(modelNameLabelText).setText(modelName);
		activate();
		return this;
	}	
}
