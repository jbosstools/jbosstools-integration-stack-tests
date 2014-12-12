package org.jboss.tools.bpmn2.reddder.editor.tests;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.EventSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.CompensationStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ErrorStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.EscalationStartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ImportResourceRequirement.ImportResource;
import org.junit.Test;

/**
 * @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1173140
 * @author jomarko
 *
 */

@ImportResource(projectName="BpmnTestProject", folderToImportName="resources/bpmn2/model/base", baseDiagramFileName="StartEventSubprocessTest.bpmn2")
public class StartEventSubprocessTest extends ImportResourceTest{
	
private static final String BASE_FILE = "StartEventSubprocessTest.bpmn2";
	
	/**
	 * *******
	 * BUGZILA https://bugzilla.redhat.com/show_bug.cgi?id=1162174
	 * *******
	 * 
	 * From the documentation:
	 * The Compensation Start Event MAY NOT be used for a top-level Process.
	 * The Compensation Start Event MAY be used for an Event Sub-Process.
	 */
	@Test
	public void compensationStartEventTest() {
		EventSubProcess subProcess = new EventSubProcess("EventSubProcess");
		subProcess.add("CompensationStart", ElementType.COMPENSATION_START_EVENT);
		
		CompensationStartEvent compensationStart = new CompensationStartEvent("CompensationStart");
		compensationStart.connectTo(new ScriptTask("SubScript"));
		
		saveAsAndValidate("Compensation" + BASE_FILE);
	}
	
	
	/**
	 * *******
	 * BUGZILA https://bugzilla.redhat.com/show_bug.cgi?id=1162174
	 * *******
	 * 
	 * An Error Start Event can only occur in Event Sub-Processes.
	 */
	@Test
	public void errorStartEventTest() {
		EventSubProcess subProcess = new EventSubProcess("EventSubProcess");
		subProcess.add("ErrorStart", ElementType.ERROR_START_EVENT);
		
		ErrorStartEvent errorStart = new ErrorStartEvent("ErrorStart");
		errorStart.connectTo(new ScriptTask("SubScript"));
		
		saveAsAndValidate("Error" + BASE_FILE);
	}
	
	
	/**
	 * *******
	 * BUGZILA https://bugzilla.redhat.com/show_bug.cgi?id=1162174
	 * *******
	 * 
	 * The Escalation Start Event is only allowed for triggering an in-line Event Sub-Process.
	 */
	@Test
	public void escalationStartEventTest() {
		EventSubProcess subProcess = new EventSubProcess("EventSubProcess");
		subProcess.add("EscalationStart", ElementType.ESCALATION_START_EVENT);
		
		EscalationStartEvent escalationStart = new EscalationStartEvent("EscalationStart");
		escalationStart.setEscalation(new Escalation("BpmnEscalation", "java.lang.RuntimeException"), OBJECT_VAR);
		escalationStart.connectTo(new ScriptTask("SubScript"));
		
		saveAsAndValidate("Escalation" + BASE_FILE);
	}
}
