package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-SubProcess.bpmn2", saveAs = "BPMN2-SubProcess.bpmn2", knownIssues = {
	"1263294" })
public class ComplexSubProcessTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");

		SubProcess subProcess = (SubProcess) start.append("Hello Subprocess", ElementType.SUB_PROCESS, Position.SOUTH);
		subProcess.addLocalVariable(VARIABLE1, "String");
		subProcess.connectTo(new ScriptTask("Goodbye"));

		// Now create the inner of the sub process.
		StartEvent subStart = (StartEvent) subProcess.addRelativeToElement("StartSubProcess", ElementType.START_EVENT,
				subProcess, new Point(-70, -35));

		ScriptTask script1 = (ScriptTask) subProcess.addRelativeToElement("Hello1", ElementType.SCRIPT_TASK, subStart,
				new Point(0, 50));
		script1.setScript("Java", "kcontext.setVariable(\"" + VARIABLE1 + "\",\" innerValue\");");
		subStart.connectTo(script1);

		ScriptTask script2 = (ScriptTask) subProcess.addRelativeToElement("Hello2", ElementType.SCRIPT_TASK, script1,
				new Point(130, 0));
		script2.setScript("Java", "System.out.println(kcontext.getVariable(\"" + VARIABLE1 + "\"));");
		script1.connectTo(script2);

		EndEvent subEnd = (EndEvent) subProcess.addRelativeToElement("EndSubProcess", ElementType.END_EVENT, script2,
				new Point(0, -50));
		script2.connectTo(subEnd);
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(Arrays.asList("StartProcess", "StartSubProcess",
				"Hello1", "Hello2", "EndSubProcess", "Goodbye", "EndProcess"), null);
		kSession.addEventListener(triggered);

		ProcessInstance processInstance = kSession.startProcess("BPMN2SubProcess");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertNull(((WorkflowProcessInstance) processInstance).getVariable(VARIABLE1));
	}
}
