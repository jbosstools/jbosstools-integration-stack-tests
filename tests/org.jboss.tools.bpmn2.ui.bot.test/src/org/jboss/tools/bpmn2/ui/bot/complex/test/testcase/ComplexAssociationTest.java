package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.artifacts.TextAnnotation;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-Association.bpmn2",
							saveAs="BPMN2-Association.bpmn2")
public class ComplexAssociationTest extends JBPM6ComplexTest{

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		ScriptTask script = new ScriptTask("Log");
		TextAnnotation annotation = new TextAnnotation();
		annotation.connectTo(script, ConnectionType.ASSOCIATION_UNDIRECTED);
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2Association");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
