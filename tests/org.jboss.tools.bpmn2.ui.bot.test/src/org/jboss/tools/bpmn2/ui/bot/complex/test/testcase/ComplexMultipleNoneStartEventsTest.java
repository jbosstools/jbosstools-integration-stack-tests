package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-MultipleNoneStartEvents.bpmn2", saveAs = "BPMN2-MultipleNoneStartEvents.bpmn2", noErrorsInValidation=false)
public class ComplexMultipleNoneStartEventsTest extends JBPM6ComplexTest {
	
	private final String PROBLEM = "Only one untriggered Start Event is allowed";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		Process process = new Process("BPMN2-MultipleNoneStartEvents");
		StartEvent secondStart = (StartEvent) process.add("SecondStart", ElementType.START_EVENT);
		
		secondStart.connectTo(new ParallelGateway("Join"));
	}
	
	@TestPhase(phase = Phase.VALIDATE)
	public void validate() {
		String error = getErrorsFromProblemsView(PROBLEM);
		assertTrue(error.contains(PROBLEM));
	}
}
