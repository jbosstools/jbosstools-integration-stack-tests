package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.InclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-InclusiveSplit.bpmn2", saveAs = "BPMN2-InclusiveSplit.bpmn2")
public class ComplexInclusiveSplitTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");

		InclusiveGateway gateway = (InclusiveGateway) start.append("Gateway", ElementType.INCLUSIVE_GATEWAY);
		gateway.connectTo(new ScriptTask("Script1"));
		gateway.connectTo(new ScriptTask("Script2"));
		gateway.connectTo(new ScriptTask("Script3"));

		gateway.setDirection(Direction.DIVERGING);
		gateway.setCondition("Gateway -> Script1", "Java", "return " + VARIABLE1 + " > 0;");
		gateway.setCondition("Gateway -> Script2", "Java", "return " + VARIABLE1 + " > 10;");
		gateway.setCondition("Gateway -> Script3", "Java", "return " + VARIABLE1 + " > 20;");
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Gateway", "Script1", "EndProcess1", "Script2", "EndProcess2"),
				Arrays.asList("Script3", "EndProcess3"));
		kSession.addEventListener(triggered);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(VARIABLE1, 15);

		ProcessInstance processInstance = kSession.startProcess("BPMN2InclusiveSplit", params);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
