package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BusinessRuleTask", project="EditorTestProject")
public class BusinessRuleTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Business Rule Task", ConstructType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask task = new BusinessRuleTask("Business Rule Task");
		task.setRuleFlowGroup("MyRuleFlowGroup");
		task.append("EndProcess", ConstructType.END_EVENT);
	}
	
}