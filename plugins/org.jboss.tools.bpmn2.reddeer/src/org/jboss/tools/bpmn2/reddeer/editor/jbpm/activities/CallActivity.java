package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CalledActivitySetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class CallActivity extends Task {
	
	private static final String CALL_ACTIVITY = "Call Activity";

	/**
	 * 
	 * @param name
	 */
	public CallActivity(String name) {
		super(name, ElementType.CALL_ACTIVITY);
	}
	
	public CallActivity(org.jboss.tools.bpmn2.reddeer.editor.Element element){
		super(element);
	}

	/**
	 * 
	 * @param value
	 */
	public void setIndependent(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(CALL_ACTIVITY, "Independent", value));
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setWaitForCompletion(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(CALL_ACTIVITY, "Independent", value));
	}
	
	/**
	 * 
	 * @param activityName
	 */
	public void setCalledActivity(String activityName) {
		propertiesHandler.setUp(new CalledActivitySetUpCTab(activityName));
	}

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(CALL_ACTIVITY, "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(CALL_ACTIVITY, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(CALL_ACTIVITY, "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
}
