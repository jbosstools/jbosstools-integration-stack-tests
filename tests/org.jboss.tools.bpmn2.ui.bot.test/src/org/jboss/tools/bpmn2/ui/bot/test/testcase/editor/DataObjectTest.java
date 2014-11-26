package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects.DataObject;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

/**
 * ISSUE - Some users may use &quot; instead of ". The validator will not replace the entities.
 */
@ProcessDefinition(name="BPMN2-DataObject", project="EditorTestProject")
public class DataObjectTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Script", ElementType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Script");
		script.setScript("Java", "employee = employee.toUpperCase(); kcontext.setVariable(\"employee\",employee);");
		script.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
		Process process = new Process("BPMN2-DataObject");
		process.add("employee", ElementType.DATA_OBJECT, start, Position.SOUTH);
		
		DataObject object = new DataObject("employee");
		object.connectTo(script, ConnectionType.DATA_ASSOCIATION);
		object.setDataType("String");
		
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employee", "employeeOfTheYear");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2DataObject", params);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertEquals("EMPLOYEEOFTHEYEAR", ((WorkflowProcessInstance) processInstance).getVariable("employee"));
	}
	
}