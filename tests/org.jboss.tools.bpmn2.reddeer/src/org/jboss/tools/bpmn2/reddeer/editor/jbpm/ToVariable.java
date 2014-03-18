package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping to a variable.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ToVariable implements MappingSide {

	private String variableName;
	
	/**
	 * Creates a new instance of ToVariable.
	 * 
	 * @param variableName
	 */
	public ToVariable(String variableName) {
		this.variableName = variableName;
	}
	
	/**
	 * 
	 */
	@Override
	public void add() {
		new LabeledCombo("Target").setSelection(variableName);
	}

	/**
	 * 
	 */
	@Override
	public String getValue() {
		return variableName;
	}
	
}
