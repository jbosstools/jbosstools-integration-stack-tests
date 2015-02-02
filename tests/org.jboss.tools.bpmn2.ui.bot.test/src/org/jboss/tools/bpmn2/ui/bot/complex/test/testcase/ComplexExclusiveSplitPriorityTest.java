package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-ExclusiveSplitPriority.bpmn2",
							saveAs="BPMN2-ExclusiveSplitPriority.bpmn2")
public class ComplexExclusiveSplitPriorityTest extends JBPM6ComplexTest{
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		
		ExclusiveGateway gw = (ExclusiveGateway) start.append("Split", ElementType.EXCLUSIVE_GATEWAY);
		gw.setDirection(Direction.DIVERGING);
		gw.connectTo(new ScriptTask("Script1"));
		gw.connectTo(new ScriptTask("Script2"));
		
		gw.select();
		gw.setCondition("Split -> Script1", "Java", "return " + VARIABLE1 + " != null;");
		gw.setCondition("Split -> Script2", "Java", "return " + VARIABLE1 + " == null;");
		gw.setPriority("Split -> Script2", "1");
		gw.setPriority("Split -> Script1", "2");
		
		ScriptTask task1 = new ScriptTask("Script1");
		
		ExclusiveGateway gw2 = (ExclusiveGateway) task1.append("Join", ElementType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		gw2.setDirection(Direction.CONVERGING);
		gw2.connectTo(new EndEvent("EndProcess"));
		
		
		ScriptTask task2 = new ScriptTask("Script2");
		task2.connectTo(gw2);
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Split", "Script1", "EndProcess"), Arrays.asList("Script2"));
		kSession.addEventListener(triggered);
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(VARIABLE1, "nonNullValue");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2ExclusiveSplitPriority", args);
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
