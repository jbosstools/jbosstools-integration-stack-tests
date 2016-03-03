package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;

/**
 * 
 */
public class StartEvent extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		this(name, ElementType.START_EVENT);
	}

	public StartEvent(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	StartEvent(String name, ElementType type) {
		super(name, type);
	}
	
	/**
	 * This method works only if is start event part of "Event SubProcess"
	 * @param value
	 */
	public void setIsInterrupting(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.EVENT_TAB, "Is Interrupting", value));
	}

}