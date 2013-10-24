package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping;

public class Task extends AbstractTask {

	public Task(String name) {
		super(name, ConstructType.TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
