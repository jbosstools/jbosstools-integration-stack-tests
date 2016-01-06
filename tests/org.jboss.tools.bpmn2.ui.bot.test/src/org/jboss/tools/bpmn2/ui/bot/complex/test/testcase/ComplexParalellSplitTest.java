package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-ParallelSplit.bpmn2", saveAs = "BPMN2-ParallelSplit.bpmn2")
public class ComplexParalellSplitTest extends JBPM6ComplexTest {

	private static final String N_END2 = "End2";
	private static final String N_END1 = "End1";
	private static final String N_SCRIPT2 = "Script2";
	private static final String N_SCRIPT1 = "Script1";
	private static final String N_SPLIT = "ParallelSplit";
	private static final String N_START = "StartProcess";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent(N_START);

		ParallelGateway gateway = (ParallelGateway) start.append(N_SPLIT, ElementType.PARALLEL_GATEWAY);
		gateway.setDirection(Direction.DIVERGING);
		gateway.connectTo(new ScriptTask(N_SCRIPT1));
		gateway.connectTo(new ScriptTask(N_SCRIPT2));
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
				Arrays.asList(N_START, N_SPLIT, N_SCRIPT1, N_SCRIPT2, N_END1, N_END2), null);
		kSession.addEventListener(triggeredNodes);

		ProcessInstance processInstance = kSession.startProcess("BPMN2ParallelSplit");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
