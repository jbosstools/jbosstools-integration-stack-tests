package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.BoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;

/**
 * 
 */
public class Task extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 */
	public Task(String name) {
		super(name, ElementType.TASK);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public Task(String name, ElementType type) {
		super(name, type);
	}

	public Task(String name, ElementType type, org.jboss.tools.bpmn2.reddeer.editor.Element parent) {
		super(name, type, parent);
	}

	public Task(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}

	/**
	 * 
	 * @param name
	 * @param eventType
	 */
	public BoundaryEvent addEvent(String name, ElementType eventType) {
		return super.addEvent(name, eventType, eventType.getJavaClass());

	}

	public void setIsAsync(boolean isAsync) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.SCRIPT_TASK_TAB, "Is Async", isAsync));
	}
}
