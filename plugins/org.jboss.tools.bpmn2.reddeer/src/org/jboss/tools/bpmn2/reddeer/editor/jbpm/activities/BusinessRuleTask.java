package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.LabeledTextSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class BusinessRuleTask extends Task {
	
	private static final String B_RULE_TASK = "Business Rule Task";

	/**
	 * 
	 * @param name
	 */
	public BusinessRuleTask(String name) {
		super(name, ElementType.BUSINESS_RULE_TASK);
	}
	
	public BusinessRuleTask(Element element) {
		super(element);
	}

	/**
	 * 
	 * @param group
	 */
	public void setRuleFlowGroup(String group) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(B_RULE_TASK, "Rule Flow Group" , group));
	}

	/**
	 * 
	 * @param b
	 */
	public void setIsForCompensation(boolean value) {
		//properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setIsForCompensation(value);
		propertiesHandler.setUp(new CheckBoxSetUpCTab(B_RULE_TASK, "Is For Compensation", value));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
//		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setOnEntryScript(new Expression(language, script));
		propertiesHandler.setUp(new ScriptSetUpCTab(B_RULE_TASK, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
//		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setOnExitScript(new Expression(language, script));
		propertiesHandler.setUp(new ScriptSetUpCTab(B_RULE_TASK, "On Exit Script", language, script));
	}
	
}
