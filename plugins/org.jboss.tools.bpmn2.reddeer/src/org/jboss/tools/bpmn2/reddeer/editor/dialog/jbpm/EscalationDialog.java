package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;

/**
 * 
 */
public class EscalationDialog {

	/**
	 * 
	 * @param name
	 * @param code
	 */
	public void addEscalation(String name, String code) {
		add(new Escalation(name, code));
	}
	
	/**
	 * 
	 * @param escalation
	 */
	public void add(Escalation escalation) {
		new DefaultShell("Create New Escalation").setFocus();
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(escalation.getName());
		new DefaultTabItem("Escalation").activate();
		new LabeledText("Escalation Code").setText(escalation.getCode());
		new DefaultShell("Create New Escalation").setFocus();
		new PushButton("OK").click();
	}
	
}
