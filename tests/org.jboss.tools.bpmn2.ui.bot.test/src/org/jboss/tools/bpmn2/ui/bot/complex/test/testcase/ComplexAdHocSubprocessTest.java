package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.AdHocSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-AdHocSubProcess.bpmn2",
							saveAs="BPMN2-AdHocSubProcess.bpmn2")
public class ComplexAdHocSubprocessTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		
		AdHocSubProcess subprocess = (AdHocSubProcess) start.append("Hello", ElementType.AD_HOC_SUB_PROCESS, Position.SOUTH);
		subprocess.setCompletionCondition("Rule", "getActivityInstanceAttribute(\"numberOfActiveInstances\") == 0");
		subprocess.connectTo(new ScriptTask("Goodbye"));		

		ScriptTask task1 = (ScriptTask) subprocess.add("Hello1", ElementType.SCRIPT_TASK);
		task1.setScript("Java", "System.out.println(\"Hello World 1\");");
		
		ScriptTask task2 = 
			(ScriptTask) subprocess.addRelativeToElement("Hello2", ElementType.SCRIPT_TASK, task1, new Point(0,80));
		task2.setScript("Java", "System.out.println(\"Hello World 2\");");
		task2.append("UserTask", ElementType.USER_TASK);
 	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList("StartProcess" ,
				"Hello", "Goodbye", "EndProcess", "Hello1", "Hello2", "UserTask"), null);
		kSession.addEventListener(triggeredNodes);
	    
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2AdHocSubProcess");
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
