package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * 
 */
public class DataTypeDialog {

	private static final String SHELL_LABEL = "Create New Data Type";

	/**
	 * 
	 * @param type
	 */
	public void add(String qualifiedType) {
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("Data Type").activate();
		new PushButton(0).click();
		new DefaultShell("Browse for a Java type to Import");
		new LabeledText("Type:").typeText(qualifiedType);
		String type = qualifiedType.substring(qualifiedType.lastIndexOf(".") + 1);
		new DefaultTree(0).selectItems(new DefaultTreeItem(type + " - " + qualifiedType));
		new PushButton("OK").click();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}

}
