package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;


import static org.junit.Assert.assertTrue;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-MetaDataNamespace.bpmn2", saveAs = "BPMN2-MetaDataNamespace.bpmn2", knownIssues = {"1293238", "1309950"})
public class ComplexMetaDataNamespaceTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		Process process = new Process("BPMN2-MetaDataNamespace");
		process.setMetaData("TRANSACTIONAL_SUPPORTED", "true");
		
		ScriptTask asyncTask = new ScriptTask("AsyncTask");
		asyncTask.setMetaData("customAsync", "true");
	}
	
	@TestPhase(phase = Phase.RUN)
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance instance = kSession.startProcess("BPMN2MetaDataNamespace");
		JbpmAssertions.assertProcessInstanceCompleted(instance, kSession);
	}
	
	@TestPhase(phase = Phase.VALIDATE)
	public void validate() {
		assertTrue(diagramSourceCode.contains("tns:metaData name=\"TRANSACTIONAL_SUPPORTED\""));
		assertTrue(diagramSourceCode.contains("tns:metaData name=\"customAsync\""));
	}
}
