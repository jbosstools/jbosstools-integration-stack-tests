package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUES:
 * 	1) engine does not validate the presence of the rules.
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-RuleTask", project="EditorTestProject")
public class RuleTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Rule", ConstructType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask rule = new BusinessRuleTask("Rule");
		rule.setRuleFlowGroup("myRules");
		rule.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}