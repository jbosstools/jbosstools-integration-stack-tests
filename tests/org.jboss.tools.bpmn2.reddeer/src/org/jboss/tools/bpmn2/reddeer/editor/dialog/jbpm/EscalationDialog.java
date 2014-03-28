package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;

/**
 * 
 */
public class EscalationDialog {

	/**
	 * 
	 * @param escalation
	 */
	public void add(Escalation escalation) {
		new SWTBot().shell("Create New Escalation").activate();
		new LabeledText("Name").setText(escalation.getName());
		new LabeledText("Escalation Code").setText(escalation.getCode());
		new PushButton("OK").click();
	}
	
}
