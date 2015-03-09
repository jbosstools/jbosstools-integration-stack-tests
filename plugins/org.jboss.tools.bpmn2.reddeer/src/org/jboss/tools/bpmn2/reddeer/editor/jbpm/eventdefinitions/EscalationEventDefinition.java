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
	private MappingVariableType mappingType;
	private String variable;

	/**
	 * 
	 * @param escalation
	 */
	public EscalationEventDefinition(Escalation escalation, String variable, MappingVariableType mappingType) {
		this.escalation = escalation;
		this.variable = variable;
		this.mappingType = mappingType;
	}
	
	@Override
	public void setUp() {
		Combo c = new LabeledCombo("Escalation");
		if (!c.getItems().contains(escalation.getName())) {
			new PushButton(0).click();
			new EscalationDialog().add(escalation);
		}
		c.setSelection(escalation.getName());
		new LabeledCombo(mappingType.label()).setSelection(variable);
		new DefaultSection("Escalation Event Definition Details").getToolbarButton("Close").click();
	}
	
}
