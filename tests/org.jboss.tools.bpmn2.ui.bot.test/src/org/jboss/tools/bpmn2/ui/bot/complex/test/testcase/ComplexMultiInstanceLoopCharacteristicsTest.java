package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.drools.core.event.DefaultProcessEventListener;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.MultipleInstancesSubProcess;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-MultiInstanceLoopCharacteristics.bpmn2", saveAs = "BPMN2-MultiInstanceLoopCharacteristics.bpmn2")
public class ComplexMultiInstanceLoopCharacteristicsTest extends JBPM6ComplexTest {

	private static final String ITERATOR = "iterator";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		MultipleInstancesSubProcess repeater = new MultipleInstancesSubProcess("Repeater");
		repeater.setInputCollection(VARIABLE2);
		repeater.setIteratorTroughCollection(ITERATOR);
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		HashMap<String, Object> args = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		args.put(VARIABLE2, numbers);
		kSession.addEventListener(new RepeaterListener());
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession
				.startProcess("BPMN2MultiInstanceLoopCharacteristics", args);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

	private class RepeaterListener extends DefaultProcessEventListener {

		private int counter;

		@Override
		public void beforeVariableChanged(ProcessVariableChangedEvent event) {
			if (VARIABLE1.compareTo(event.getVariableInstanceId()) == 0) {
				counter++;
			}
		}

		@Override
		public void beforeProcessCompleted(ProcessCompletedEvent event) {
			assertEquals(3, counter);
		}
	}
}
