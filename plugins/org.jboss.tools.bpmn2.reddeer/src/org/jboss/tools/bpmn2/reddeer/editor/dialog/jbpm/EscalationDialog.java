package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

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
		escalation.setUpViaDialog();
	}
	
}
