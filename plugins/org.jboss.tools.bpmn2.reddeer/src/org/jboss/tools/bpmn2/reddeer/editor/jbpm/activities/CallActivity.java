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
		//properties.getTab("Call Activity", CallActivityTab.class).setIndependent(value);
		graphitiProperties.setUpTabs(new CheckBoxSetUpCTab("Call Activity", "Independent", value));
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setWaitForCompletion(boolean value) {
		//properties.getTab("Call Activity", CallActivityTab.class).setWaitForCompletion(value);
		graphitiProperties.setUpTabs(new CheckBoxSetUpCTab("Call Activity", "Independent", value));
	}
	
	/**
	 * 
	 * @param activityName
	 */
	public void setCalledActivity(String activityName) {
		//properties.getTab("Call Activity", CallActivityTab.class).setCalledActivity(activityName);
		graphitiProperties.setUpTabs(new CalledActivitySetUpCTab(activityName));
	}

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		//properties.getTab("Call Activity", CallActivityTab.class).setIsForCompensation(value);
		graphitiProperties.setUpTabs(new CheckBoxSetUpCTab("Call Activity", "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		//properties.getTab("Call Activity", CallActivityTab.class).setOnEntryScript(new Expression(language, script));
		graphitiProperties.setUpTabs(new ScriptSetUpCTab("Call Activity", "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		//properties.getTab("Call Activity", CallActivityTab.class).setOnExitScript(new Expression(language, script));
		graphitiProperties.setUpTabs(new ScriptSetUpCTab("Call Activity", "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		//properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
		graphitiProperties.setUpTabs(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
}
