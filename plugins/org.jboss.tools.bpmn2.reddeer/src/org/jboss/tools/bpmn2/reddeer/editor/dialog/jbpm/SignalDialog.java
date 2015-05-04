package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;

/**
 * 
 */
public class SignalDialog {

	private static final String SHELL_LABEL = "Create New Signal";
	
	public void add(Signal signal) {
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(signal.getName());
		
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}
	
}
