package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-ParallelSplit", project="EditorTestProject")
public class ParallelSplitTest extends JBPM6BaseTest {

	private static final String N_END2 = "End2";
	private static final String N_END1 = "End1";
	private static final String N_SCRIPT2 = "Script2";
	private static final String N_SCRIPT1 = "Script1";
	private static final String N_SPLIT = "ParallelSplit";
	private static final String N_START = "StartProcess";

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent(N_START);
		start.append(N_SPLIT, ElementType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway(N_SPLIT);
		gateway.setDirection(Direction.DIVERGING);
		gateway.append(N_SCRIPT1, ElementType.SCRIPT_TASK, Position.NORTH_EAST);
		gateway.append(N_SCRIPT2, ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		ScriptTask script1 = new ScriptTask(N_SCRIPT1);
		script1.setScript("Java", "System.out.println(\"1\");");
		script1.append(N_END1, ElementType.END_EVENT);
		
		// Fails on setScript and I don't know why!
		ScriptTask script2 = new ScriptTask(N_SCRIPT2);
		script2.select();
		script2.setScript("Java", "System.out.println(\"2\");");
		script2.append(N_END2, ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
			Arrays.asList(N_START, N_SPLIT, N_SCRIPT1, N_SCRIPT2, N_END1, N_END2), null);
		kSession.addEventListener(triggeredNodes);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2ParallelSplit");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}