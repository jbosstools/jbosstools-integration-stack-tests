package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.AdHocSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@ProcessDefinition(name="BPMN2-AdHocSubProcess",  project="EditorTestProject")
public class AdHocSubProcessTest extends JBPM6BaseTest {

	private static final String PROCESS_ID = "BPMN2AdHocSubProcess";

	/**
	 * ISSUE: - May contain another bug. When adding a connection from an element
	 *          to itself then 'y' is missing in the 'di' element.
	 *        - See ContainerConstruct.add(String, ConstructType) 
	 * 
	 * ISSUE: - Appending anything after a AdHocSubprocess or Subprocess does not
	 *          generate the connection until the subprocess is moved.
	 *         
	 */
	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ElementType.AD_HOC_SUB_PROCESS, Position.SOUTH);

		AdHocSubProcess subprocess = new AdHocSubProcess("Hello");
		subprocess.setCompletionCondition("Rule", "getActivityInstanceAttribute(\"numberOfActiveInstances\") == 0");
		subprocess.append("Goodbye", ElementType.SCRIPT_TASK);		

		ScriptTask task3 = new ScriptTask("Goodbye");
		task3.setScript("", "System.out.println(\"Goodbye World\");");
		task3.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
		/*
		 * Finish ad-hoc sub-process.
		 */
		subprocess.add("Hello1", ElementType.SCRIPT_TASK);
		
		ScriptTask task1 = new ScriptTask("Hello1");
		task1.setScript("", "System.out.println(\"Hello World 1\");");
		
		subprocess.addRelativeToElement("Hello2", ElementType.SCRIPT_TASK, task1, new Point(0,80));
		
		ScriptTask task2 = new ScriptTask("Hello2");
		task2.setScript("", "System.out.println(\"Hello World 2\");");
		task2.append("UserTask", ElementType.USER_TASK);
 	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList("StartProcess" ,
				"Hello", "Goodbye", "EndProcess", "Hello1", "Hello2", "UserTask"), null);
		kSession.addEventListener(triggeredNodes);
	    
		
		ProcessInstance processInstance = kSession.startProcess(PROCESS_ID);
		kSession.signalEvent("Hello2", null, processInstance.getId());
		kSession.signalEvent("Hello1", null, processInstance.getId());
		
		WorkItem workItem = handler.getWorkItem("UserTask");;
		while(workItem == null) {
			workItem = handler.getWorkItem("UserTask");
		}
		handler.completeWorkItem(workItem, kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}