package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

/**
 * 
 */
public class ScriptTask extends Task {

	/**
	 * 
	 * @param name
	 */
	public ScriptTask(String name) {
		super(name, ElementType.SCRIPT_TASK);
	}
	
	public ScriptTask(String name, Element parent) {
		super(name, ElementType.SCRIPT_TASK, parent);
	}
	
	public ScriptTask(Element element) {
		super(element);
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.SCRIPT_TASK_TAB, "Attributes", language, script));

	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.SCRIPT_TASK_TAB, "Is For Compensation", value));
	}

}
