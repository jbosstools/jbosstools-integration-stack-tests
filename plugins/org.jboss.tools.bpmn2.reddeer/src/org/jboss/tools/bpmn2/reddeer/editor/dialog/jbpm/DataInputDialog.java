package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class DataInputDialog {
	
	public void add(String varName, String varType) {
		new DefaultShell("Edit Data Input");
		new DefaultTabItem("Data Input").activate();
		new LabeledText("Name").setText(varName);
		new LabeledCombo("Data Type").setSelection(varType);
		new PushButton("OK").click();
	}

}
