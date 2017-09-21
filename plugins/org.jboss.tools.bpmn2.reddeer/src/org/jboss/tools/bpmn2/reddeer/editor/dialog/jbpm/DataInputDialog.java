package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class DataInputDialog {

	public void add(String varName, String varType) {
		new DefaultShell("Edit Data Input");
		new DefaultTabItem("Data Input").activate();
		new LabeledText("Name").setText(varName);
		new LabeledCombo("Data Type").setSelection(varType);
		new PushButton("OK").click();
	}

}
