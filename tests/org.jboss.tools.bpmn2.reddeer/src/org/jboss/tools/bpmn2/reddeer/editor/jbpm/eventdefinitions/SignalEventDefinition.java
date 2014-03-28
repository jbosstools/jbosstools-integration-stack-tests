package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.SignalDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.AbstractEventDefinition;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class SignalEventDefinition extends AbstractEventDefinition {

	public enum Type {
		SOURCE, TARGET;
		
		public String label() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
	private String signalName;
	private String variableName;
	private Type type;

	/**
	 * 
	 * @param signalName
	 */
	public SignalEventDefinition(String signalName, Type type) {
		this(signalName, null, type);
	}
	
	/**
	 * 
	 * @param signalName
	 * @param variableName
	 */
	public SignalEventDefinition(String signalName, String variableName, Type type) {
		this.signalName = signalName;
		this.variableName = variableName;
		this.type = type;
	}
	
	@Override
	public void setUp() {
		DefaultCombo combo = new DefaultCombo("Signal");
		if (!combo.contains(signalName)) {
			new PushButton(0).click();
			new SignalDialog().add(signalName);
		}
		combo.setSelection(signalName);
		
		String processName = new ProcessEditorView().getProcess().getName();
		new DefaultCombo(type.label()).setSelection(processName + "/" + variableName);
		new DefaultSection("Signal Event Definition Details").getToolbarButton("Close").click();
	}
	
}
