package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-ConditionalStart.bpmn2",
							saveAs="BPMN2-ConditionalStart.bpmn2")
public class ComplexConditionalStartTest extends JBPM6ComplexTest {
	
	private static final String N_START = "Start";
	private static final String N_HELLO = "Hello";
	private static final String N_END = "EndProcess";
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		
		Process process = new Process("BPMN2-ConditionalStart");
		
		process.add(N_START, ElementType.CONDITIONAL_START_EVENT);
		
		ConditionalStartEvent startEvent = new ConditionalStartEvent(N_START);
		startEvent.setCondition("Rule", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		startEvent.connectTo(new ScriptTask(N_HELLO));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList(N_START,
				N_HELLO, N_END), null);
		kSession.addEventListener(triggeredNodes);
		
		org.jbpm.bpmn2.objects.Person person = new org.jbpm.bpmn2.objects.Person();
		person.setName("john");
		kSession.insert(person);
		
		kSession.fireAllRules();
		
		assertEquals("There shouldn't be a active process instance", 0, kSession.getProcessInstances().size());
	}

}
