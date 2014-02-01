package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping;

public class Task extends AbstractTask {

	public Task(String name) {
		super(name, ConstructType.TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping)
	 */
	@Override
	public void addParameterMapping(ParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
