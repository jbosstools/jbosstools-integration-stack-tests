package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-AsyncLongScript.bpmn2", saveAs = "BPMN2-AsyncLongScript.bpmn2", knownIssues={"1321332", "1324041"})
public class ComplexAsyncLongScriptTest extends JBPM6ComplexTest {

	private ScriptTask longScriptTask;
	private String longScript;
	
	@TestPhase(phase = Phase.MODEL)
	public void model() {
		longScript = "System.out.println(\"";
		
		for (int i = 0; i< 300; i++) {
			longScript += "x";
		}
		
		longScript += "\");";
		
		longScriptTask = new ScriptTask("Long Script");
		longScriptTask.setScript("Java", longScript);
		longScriptTask.setIsAsync(true);
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateScriptLength() {
		String actualScript = longScriptTask.getScript();
		assertEquals(255, actualScript.length());
		assertNotEquals(longScript, actualScript);
	}
	
	@TestPhase(phase = Phase.VALIDATE)
	public void validateIsAsync() {
		assertTrue(isInSourceCode("<tns:metaData name=\"customAsync\">"));
		assertTrue(isInSourceCode("<tns:metaValue><![CDATA[true]]></tns:metaValue>"));
	}
	
	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2AsyncLongScript");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
