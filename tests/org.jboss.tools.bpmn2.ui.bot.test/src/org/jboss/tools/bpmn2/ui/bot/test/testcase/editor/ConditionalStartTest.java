package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-ConditionalStart", project="EditorTestProject", needPerson=true)
public class ConditionalStartTest extends JBPM6BaseTest {

	private static final String N_END = "EndProcess";
	private static final String N_START = "StartProcess";
	private static final String N_HELLO = "Hello";

	@Override
	public void buildProcessModel() {
		new StartEvent(N_START).delete();
		Process process = new Process("BPMN2-ConditionalStart");
		process.addImport("org.jbpm.bpmn2.objects.Person");
		
		process.add(N_START, ElementType.CONDITIONAL_START_EVENT);
		
		
		ConditionalStartEvent startEvent = new ConditionalStartEvent(N_START);
		startEvent.setCondition("", "org.jbpm.bpmn2.objects.Person(name == \"john\")"); // causes NPE
		startEvent.append(N_HELLO, ElementType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask(N_HELLO);
		scriptTask.setScript("", "System.out.println(\"Hello World\");");
		scriptTask.append(N_END, ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList(N_START,
				N_HELLO, N_END), null);
		kSession.addEventListener(triggeredNodes);
		
		org.jbpm.bpmn2.objects.Person person = new org.jbpm.bpmn2.objects.Person();
		person.setName("john");
		kSession.insert(person);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2ConditionalStart");
		kSession.fireAllRules();
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}