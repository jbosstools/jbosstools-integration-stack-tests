package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.AddParameterMappingSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CalledActivitySetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

/**
 * 
 */
public class CallActivity extends Task {
	
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
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.CALL_ACTIVITY_TAB, "Independent", value));
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setWaitForCompletion(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.CALL_ACTIVITY_TAB, "Independent", value));
	}
	
	/**
	 * 
	 * @param activityName
	 */
	public void setCalledActivity(String activityName) {
		propertiesHandler.setUp(new CalledActivitySetUp(activityName));
	}

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.CALL_ACTIVITY_TAB, "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.CALL_ACTIVITY_TAB, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.CALL_ACTIVITY_TAB, "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUp(parameterMapping));
	}
	
}
