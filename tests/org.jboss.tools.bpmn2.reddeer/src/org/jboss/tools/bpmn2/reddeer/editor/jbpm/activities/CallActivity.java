package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.CallActivityTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;

/**
 * 
 */
public class CallActivity extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public CallActivity(String name) {
		super(name, ConstructType.CALL_ACTIVITY);
	}

	/**
	 * 
	 * @param value
	 */
	public void setIndependent(boolean value) {
		properties.getTab("Call Activity", CallActivityTab.class).setIndependent(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setWaitForCompletion(boolean value) {
		properties.getTab("Call Activity", CallActivityTab.class).setWaitForCompletion(value);
	}
	
	/**
	 * 
	 * @param activityName
	 */
	public void setCalledActivity(String activityName) {
		properties.getTab("Call Activity", CallActivityTab.class).setCalledActivity(activityName);
	}

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Call Activity", CallActivityTab.class).setIsForCompensation(value);
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab("Call Activity", CallActivityTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		properties.getTab("Call Activity", CallActivityTab.class).setOnExitScript(new Expression(language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}
	
}
