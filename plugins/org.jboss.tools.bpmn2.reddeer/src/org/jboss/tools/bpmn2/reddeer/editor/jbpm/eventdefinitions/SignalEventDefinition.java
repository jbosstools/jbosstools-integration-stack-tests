package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.SignalDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class SignalEventDefinition extends EventDefinition {

	public enum Type {
		SOURCE, TARGET;

		public String label() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

	private Signal signal;
	private String variableName;
	private Type type;

	/**
	 * 
	 * @param signalName
	 */
	public SignalEventDefinition(Signal signal, Type type) {
		this(signal, null, type);
	}

	/**
	 * 
	 * @param signalName
	 * @param variableName
	 */
	public SignalEventDefinition(Signal signal, String variableName, Type type) {
		this.signal = signal;
		this.variableName = variableName;
		this.type = type;
	}

	@Override
	public void setUp() {
		DefaultCombo combo = new DefaultCombo("Signal");
		if (!combo.contains(signal.getName())) {
			new PushButton(0).click();
			new SignalDialog().add(signal);
		}
		combo.setSelection(signal.getName());

		new DefaultCombo(type.label()).setSelection(variableName);
		new SectionToolItem("Signal Event Definition Details", "Close").click();
	}

}
