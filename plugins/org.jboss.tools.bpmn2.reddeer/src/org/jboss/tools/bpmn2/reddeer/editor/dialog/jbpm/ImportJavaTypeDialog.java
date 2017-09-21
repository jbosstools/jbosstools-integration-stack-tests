package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 */
public class ImportJavaTypeDialog {

	private boolean confirmWithOk;
	
	public ImportJavaTypeDialog() {
		this.confirmWithOk = true;
	}
	
	public ImportJavaTypeDialog(boolean confirmWithOk) {
		this.confirmWithOk = confirmWithOk;
	}
	
	/**
	 * The data type name must be typed - otherwise the lookup listener will not be activated.
	 * 
	 * @param dataType
	 */
	public void add(String dataType) {
		String typeName = dataType;
		String typeLabel = typeName.substring(typeName.lastIndexOf(".") + 1) + " - " + typeName;

		new DefaultShell("Browse for a Java type to Import");
		new LabeledText("Type:").typeText(dataType);
		new DefaultTree().selectItems(new DefaultTreeItem(typeLabel));
		if(confirmWithOk) {
			new PushButton("OK").click();
		}
	}
}
