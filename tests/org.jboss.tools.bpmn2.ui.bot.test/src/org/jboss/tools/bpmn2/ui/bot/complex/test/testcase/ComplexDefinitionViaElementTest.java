package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ErrorBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.MessageEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.SignalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-DefinitionViaElement.bpmn2",
							saveAs="BPMN2-DefinitionViaElement.bpmn2",
							knownIssues={"1211647"})
public class ComplexDefinitionViaElementTest extends JBPM6ComplexTest {
	
	private Signal signalDef = new Signal("ComplexSignal");
	private ErrorRef errorRefDef = new ErrorRef("RuntimeException", "java.lang.RuntimeException", "java.lang.RuntimeException");
	private Escalation escalationDef = new Escalation("IllegalArgumentException", "java.lang.IllegalArgumentException");
	private Message messageDef = new Message("TextMessage", "String");

	/**
	 * Modeled process is very crazy an has no real meaning
	 * It is assimilated for testcase, which try to add definitions to process not directly
	 * in process properties view.
	 */
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		ErrorBoundaryEvent error = new ErrorBoundaryEvent("Error");
		error.setErrorEvent(errorRefDef, VARIABLE1);
		
		SignalStartEvent signal = new SignalStartEvent("Signal");
		signal.setSignal(signalDef, VARIABLE1);
		
		EscalationIntermediateThrowEvent escalation = new EscalationIntermediateThrowEvent("Escalation");
		escalation.setEscalation(escalationDef, VARIABLE1);
		
		MessageEndEvent message = new MessageEndEvent("Message");
		message.setMessage(messageDef, VARIABLE1);
		
	}
	
	@TestPhase(phase=Phase.VALIDATE)
	public void checkDefinitions(){
		Process process = new Process("BPMN2-DefinitionViaElement");
		List<Signal> signals = process.getSignals();
		assertEquals(1, signals.size());
		assertEquals(signalDef.getName(), signals.get(0).getName());
		
		List<ErrorRef> errors = process.getErrors();
		assertEquals(1, errors.size());
		assertEquals(errorRefDef.getName(), errors.get(0).getName());
		assertEquals(errorRefDef.getDataType(), errors.get(0).getDataType());
		assertEquals(errorRefDef.getCode(), errors.get(0).getCode());
		
		List<Escalation> escalations = process.getEscalations();
		assertEquals(1, escalations.size());
		assertEquals(escalationDef.getName(), escalations.get(0).getName());
		assertEquals(escalationDef.getCode(), escalations.get(0).getCode());
		
		List<Message> messages = process.getMessages();
		assertEquals(1, messages.size());
		assertEquals(messageDef.getName(), messages.get(0).getName());
		assertEquals(messageDef.getDataType(), messages.get(0).getDataType());
	}
	
	
}
