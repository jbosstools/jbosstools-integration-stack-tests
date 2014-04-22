package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ScriptTaskTab;

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

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setScript(String language, String script) {
		properties.getTab("Script Task", ScriptTaskTab.class).setScript(language, script);
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Script Task", ScriptTaskTab.class).setIsForCompensation(value);
	}

}
