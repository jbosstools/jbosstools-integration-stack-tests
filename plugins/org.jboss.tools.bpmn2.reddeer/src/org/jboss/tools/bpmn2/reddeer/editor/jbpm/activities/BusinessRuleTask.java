package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.LabeledTextSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

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

	public BusinessRuleTask(Element element) {
		super(element);
	}

	/**
	 * 
	 * @param group
	 */
	public void setRuleFlowGroup(String group) {
		propertiesHandler.setUp(new LabeledTextSetUp(PropertiesTabs.B_RULE_TASK_TAB, "Rule Flow Group", group));
	}

	/**
	 * 
	 * @param b
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.B_RULE_TASK_TAB, "Is For Compensation", value));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.B_RULE_TASK_TAB, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.B_RULE_TASK_TAB, "On Exit Script", language, script));
	}

}
