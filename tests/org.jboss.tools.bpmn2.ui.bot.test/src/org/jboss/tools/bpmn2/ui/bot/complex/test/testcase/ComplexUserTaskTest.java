package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.drools.core.process.instance.WorkItem;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-UserTask.bpmn2",
							saveAs="BPMN2-UserTask.bpmn2",
							knownIssues={"1175772"})
public class ComplexUserTaskTest extends JBPM6ComplexTest {
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		
		UserTask task = (UserTask) start.append("User Task", ElementType.USER_TASK);
		task.addActor("john");
		task.connectTo(new TerminateEndEvent("EndProcess"));
		
//		/*EXAMPLE WHAT TO TEST*/ 
//		UserTask task = new UserTask("Email");
//		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "This is an urgent email #{x}"), new ToDataInput("Body", "String"), ParameterMapping.Type.INPUT));
//		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "Urgent email !"), new ToDataInput("Subject", "String"), ParameterMapping.Type.INPUT));
//		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "you@mail.com"), new ToDataInput("To", "String"), ParameterMapping.Type.INPUT));
//		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "ne@mail.com"), new ToDataInput("From", "String"), ParameterMapping.Type.INPUT));
//		task.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "User Task", "EndProcess"), null);
		kSession.addEventListener(triggered);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2UserTask");
		WorkItem item = (WorkItem) handler.getWorkItem("User Task");
		assertEquals("This user task should be assigned to john", "john",(String) item.getParameter("ActorId"));
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
