package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class InputSetEditorDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(InputSetEditorDialog.class);
	
	public InputSetEditorDialog() {
		super("Edit Input Set");
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
	}

	/**
	 * Creates new input parameter.
	 * i.e. pushes attribute from the right list to the left list.
	 */
	public void addNewInputParam(String... pathToParam) {
		log.info("Adding new input parameter " + Arrays.toString(pathToParam));
		new DefaultTree();
		new DefaultTreeItem(pathToParam).select();
		new PushButton("< New").click();
	}
}
