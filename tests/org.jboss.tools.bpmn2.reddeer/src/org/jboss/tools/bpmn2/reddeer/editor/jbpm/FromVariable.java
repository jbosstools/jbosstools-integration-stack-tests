package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.IMappingSide;

/**
 * Mapping from a variable.
 * 
 * @author mbaluch
 */
public class FromVariable implements IMappingSide {

	private String variableName;
	
	/**
	 * Creates a new instance of FromVariable.
	 * 
	 * @param variableName
	 */
	public FromVariable(String variableName) {
		this.variableName = variableName;
	}
	
	@Override
	public void add() {
		new DefaultCombo("Source").setSelection(variableName);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return variableName;
	}
	
}
