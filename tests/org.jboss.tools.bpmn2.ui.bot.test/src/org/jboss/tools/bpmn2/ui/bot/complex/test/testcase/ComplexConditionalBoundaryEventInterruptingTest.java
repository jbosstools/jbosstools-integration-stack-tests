package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-ConditionalBoundaryEventInterrupting.bpmn2",
							saveAs="BPMN2-ConditionalBoundaryEventInterrupting.bpmn2")
public class ComplexConditionalBoundaryEventInterruptingTest extends JBPM6ComplexTest {
	
	private static final String END_PROCESS2 = "EndProcess2";
	private static final String HANDLER = "Handler";
	private static final String END_SUB = "EndSubProcess";
	private static final String INCREMENTOR = "Incrementor";
	private static final String START_SUB = "StartSubProcess";
	private static final String END_PROCESS = "EndProcess";
	private static final String SUB_PROCESS = "IncrementorSubProcess";
	private static final String START_PROCESS = "StartProcess";
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		
		SubProcess subProcess = new SubProcess(SUB_PROCESS);
		
		ConditionalBoundaryEvent conditionalBoundaryEvent = 
			(ConditionalBoundaryEvent) subProcess.add("Conditional Boundary Event Process", ElementType.CONDITIONAL_BOUNDARY_EVENT);
		conditionalBoundaryEvent.setScript("Java", "return " + VARIABLE1 + " > 5;");
		conditionalBoundaryEvent.connectTo(new ScriptTask(HANDLER));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2ConditionalBoundaryEventInterrupting");
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList(START_PROCESS, SUB_PROCESS, START_SUB, INCREMENTOR, 
				END_SUB, HANDLER, END_PROCESS2), 
				Arrays.asList(END_PROCESS));
		kSession.addEventListener(triggered);
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
