package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ManualTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-ManualTask.bpmn2",
							saveAs="BPMN2-ManualTask.bpmn2")
public class ComplexManualTaskTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("NumberAssertion",ElementType.MANUAL_TASK);
		
		ManualTask manual = new ManualTask("NumberAssertion");
		manual.addParameterMapping(new ParameterMapping(new FromVariable(VARIABLE1), new ToDataInput("internalVariable", "Integer"), ParameterMapping.Type.INPUT));
		
		manual.connectTo(new EndEvent("EndProcess"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(VARIABLE1, 8);
		
		kSession.getWorkItemManager().registerWorkItemHandler("Manual Task", new NumberAssertionTaskHandler());
		
		ProcessInstance instance = kSession.startProcess("BPMN2ManualTask", args);
		JbpmAssertions.assertProcessInstanceCompleted(instance, kSession);
	}
	
	private class NumberAssertionTaskHandler implements WorkItemHandler {
		
		public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
			 System.out.println("Executing work item " + workItem);
			 
			 assertEquals(8, workItem.getParameter("internalVariable"));
			 manager.completeWorkItem(workItem.getId(), null);
		}

		public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
			// TODO Auto-generated method stub
			
		}

	}
}
