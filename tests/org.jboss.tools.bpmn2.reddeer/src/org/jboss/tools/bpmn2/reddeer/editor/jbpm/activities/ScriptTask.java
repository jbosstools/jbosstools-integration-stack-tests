package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ScriptTaskTab;

/**
 * 
 */
public class ScriptTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ScriptTask(String name) {
		super(name, ConstructType.SCRIPT_TASK);
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
