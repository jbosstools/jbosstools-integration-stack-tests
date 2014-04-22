package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.BusinessRuleTaskTab;

/**
 * 
 */
public class BusinessRuleTask extends Task {
	
	/**
	 * 
	 * @param name
	 */
	public BusinessRuleTask(String name) {
		super(name, ElementType.BUSINESS_RULE_TASK);
	}

	/**
	 * 
	 * @param group
	 */
	public void setRuleFlowGroup(String group) {
		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setRuleFlowGroup(group);
	}

	/**
	 * 
	 * @param b
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setIsForCompensation(value);
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		properties.getTab("Business Rule Task", BusinessRuleTaskTab.class).setOnExitScript(new Expression(language, script));
	}
	
}
