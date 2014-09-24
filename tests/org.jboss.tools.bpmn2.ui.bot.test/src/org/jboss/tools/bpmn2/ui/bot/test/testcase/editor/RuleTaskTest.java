package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * ISSUES - Engine does not validate the presence of the rules.
 */
@ProcessDefinition(name="BPMN2-RuleTask", project="EditorTestProject")
public class RuleTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Rule", ElementType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask rule = new BusinessRuleTask("Rule");
		rule.setRuleFlowGroup("myRules");
		rule.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2RuleTask");
		assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
	}
	
}