package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.DefaultOutlineView;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

import org.kie.api.runtime.process.WorkflowProcessInstance;




@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	DefaultOutlineView outlineView = new DefaultOutlineView();
	private static final String EXPECTED_VALUE = "123456"; 
	
	@Override
	public void buildProcessModel() {
		new StartEvent("StartProcess").delete();
		Process process = new Process("BPMN2-Lane");
		process.addLocalVariable(VARIABLE1, "String");
		process.add("Manager", ElementType.LANE);
		Lane lane = new Lane("Manager");
		lane.add("StartProcess", ElementType.START_EVENT);
		StartEvent start = new StartEvent("StartProcess");
		start.append("LaneScript", ElementType.SCRIPT_TASK);
		ScriptTask script = new ScriptTask("LaneScript");
		script.setScript("Java", "kcontext.setVariable(\""+VARIABLE1+"\", \""+EXPECTED_VALUE+"\");");
		script.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
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