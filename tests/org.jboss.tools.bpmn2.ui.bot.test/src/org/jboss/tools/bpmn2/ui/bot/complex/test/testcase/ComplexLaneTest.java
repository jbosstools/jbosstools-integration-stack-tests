package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-Lane.bpmn2",
							saveAs="BPMN2-Lane.bpmn2")
public class ComplexLaneTest extends JBPM6ComplexTest {

	private static final String EXPECTED_VALUE = "123456"; 
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		
		Process process = new Process("BPMN2-Lane");
		
		process.add("Manager", ElementType.LANE);
		Lane lane = new Lane("Manager");
		lane.add("StartProcess", ElementType.START_EVENT);
		StartEvent start = new StartEvent("StartProcess");
		
		ScriptTask script = (ScriptTask) start.append("LaneScript", ElementType.SCRIPT_TASK);
		script.setScript("Java", "kcontext.setVariable(\""+VARIABLE1+"\", \""+EXPECTED_VALUE+"\");");
		script.append("EndProcess", ElementType.END_EVENT);
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		kSession.addEventListener(new BeforeEndEventListener());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(VARIABLE1, "empty");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2Lane", map);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	private class BeforeEndEventListener extends DefaultProcessEventListener  {
		@Override
		public void beforeProcessCompleted(ProcessCompletedEvent event) {
			assertEquals("Process variable "+VARIABLE1+" didn't changed to expected value.", EXPECTED_VALUE, ((WorkflowProcessInstance) event.getProcessInstance()).getVariable(VARIABLE1));
		}
		
	}
}
