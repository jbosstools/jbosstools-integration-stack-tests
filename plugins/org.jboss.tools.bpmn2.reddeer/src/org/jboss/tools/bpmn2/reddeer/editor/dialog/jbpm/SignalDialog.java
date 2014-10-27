package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;

/**
 * 
 */
public class SignalDialog {

	/**
	 * 
	 * @param signalName
	 */
	public void add(String signalName) {
		new SWTBot().shell("Create New Signal").activate();
		new LabeledText("Name").setText(signalName);
		new PushButton("OK").click();
	}
	
	public void add(Signal signal) {
		new DefaultShell("Create New Signal").setFocus();
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(signal.getName());
		
		new PushButton("OK").click();
	}
	
}
