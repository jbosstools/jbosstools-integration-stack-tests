package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.EscalationDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class EscalationEventDefinition extends EventDefinition {

	private Escalation escalation;
	private String variableForMaping;
	private String comboForMappingLabel;
	
	/**
	 * 
	 * @param escalation
	 */
	public EscalationEventDefinition(Escalation escalation, String variableForMapping, String comboForMapingLabel) {
		this.escalation = escalation;
		this.variableForMaping = variableForMapping;
		this.comboForMappingLabel = comboForMapingLabel;
	}
	
	@Override
	public void setUp() {
		Combo c = new LabeledCombo("Escalation");
		if (!c.getItems().contains(escalation.getName())) {
			new PushButton(0).click();
			new EscalationDialog().add(escalation);
		}
		c.setSelection(escalation.getName());
		
		c = new LabeledCombo(comboForMappingLabel);
		if (!c.getItems().contains(variableForMaping)) {
			throw new IllegalArgumentException(variableForMaping + " wasn't in list of variables");
		}
		c.setSelection(variableForMaping);
		
		new DefaultSection("Escalation Event Definition Details").getToolbarButton("Close").click();
	}
	
}
