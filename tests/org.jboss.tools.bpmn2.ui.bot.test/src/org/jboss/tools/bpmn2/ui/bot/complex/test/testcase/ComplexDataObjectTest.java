package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects.DataObject;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-DataObject.bpmn2", saveAs = "BPMN2-DataObject.bpmn2", knownIssues = {"1319754"})
public class ComplexDataObjectTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {

		Process process = new Process("BaseBPMN2-DataObject");

		DataObject object = (DataObject) process.add("employee", ElementType.DATA_OBJECT);
		object.connectTo(new ScriptTask("Script"), ConnectionType.ASSOCIATION_ONE_WAY);
		object.setDataType("String");
		object.select();
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employee", "employeeOfTheYear");

		ProcessInstance processInstance = kSession.startProcess("BPMN2DataObject", params);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertEquals("EMPLOYEEOFTHEYEAR", ((WorkflowProcessInstance) processInstance).getVariable("employee"));
	}
}
