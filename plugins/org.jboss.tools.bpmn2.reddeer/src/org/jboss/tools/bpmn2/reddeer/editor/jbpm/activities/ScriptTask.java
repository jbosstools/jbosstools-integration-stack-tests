package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class ScriptTask extends Task {

	private static final String SCRIPT_TASK = "Script Task";

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
		propertiesHandler.setUp(new ScriptSetUpCTab(SCRIPT_TASK, "Attributes", language, script));

	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(SCRIPT_TASK, "Is For Compensation", value));
	}

}
