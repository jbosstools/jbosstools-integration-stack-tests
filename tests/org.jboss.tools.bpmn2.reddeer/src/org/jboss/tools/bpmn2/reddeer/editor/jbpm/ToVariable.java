package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.IMappingSide;

/**
 * Mapping to a variable.
 * 
 * @author mbaluch
 */
public class ToVariable implements IMappingSide {

	private String variableName;
	
	/**
	 * Creates a new instance of ToVariable.
	 * 
	 * @param variableName
	 */
	public ToVariable(String variableName) {
		this.variableName = variableName;
	}
	
	@Override
	public void add() {
		new DefaultCombo("Target").setSelection(variableName);
	}

	@Override
	public String getName() {
		return variableName;
	}
	
}
