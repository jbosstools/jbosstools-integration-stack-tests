package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping from a variable.
 */
public class FromVariable implements MappingSide {

	private String variableName;

	/**
	 * 
	 * @param variableName
	 */
	public FromVariable(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public void setUp() {
		new LabeledCombo("Source").setSelection(variableName);
	}

	@Override
	public String getName() {
		return variableName;
	}

}
