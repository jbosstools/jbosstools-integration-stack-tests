package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 */
public class ImportJavaTypeDialog {

	/**
	 * The data type name must be typed - otherwise the lookup listener
	 * will not be activated.
	 * 
	 * @param dataType
	 */
	public void add(String dataType) {
		String typeName = dataType;
		String typeLabel = typeName.substring(typeName.lastIndexOf(".") + 1) + " - " + typeName;
		
		new DefaultShell("Browse for a Java type to Import");
		new LabeledText("Type:").typeText(dataType);
		new DefaultTree().selectItems(new DefaultTreeItem(typeLabel));
		new PushButton("OK").click();
	}
	
}
