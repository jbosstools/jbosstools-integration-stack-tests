package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ScriptTaskTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

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
//		properties.getTab("Script Task", ScriptTaskTab.class).setScript(language, script);
		graphitiProperties.setUpTabs(new ScriptSetUpCTab("Script Task", language, script));

	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Script Task", ScriptTaskTab.class).setIsForCompensation(value);
	}

}
