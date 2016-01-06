package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping to a variable.
 */
public class ToVariable implements MappingSide {

	private String variableName;

	/**
	 * 
	 * @param variableName
	 */
	public ToVariable(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public void setUp() {
		new LabeledCombo("Target").setSelection(variableName);
	}

	@Override
	public String getName() {
		return variableName;
	}

}
