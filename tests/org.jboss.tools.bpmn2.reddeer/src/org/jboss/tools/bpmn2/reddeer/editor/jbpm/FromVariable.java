package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping from a variable.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class FromVariable implements MappingSide {

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
	public String getValue() {
		return variableName;
	}
	
}
