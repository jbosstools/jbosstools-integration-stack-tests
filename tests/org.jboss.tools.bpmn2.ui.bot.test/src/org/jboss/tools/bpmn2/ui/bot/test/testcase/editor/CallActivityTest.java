package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.InputParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.OutputParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.CallActivity;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CallActivity", project="EditorTestProject")
public class CallActivityTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		BPMN2Process process = new BPMN2Process("BPMN2-CallActivity");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ConstructType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new InputParameterMapping(new FromVariable("BPMN2-CallActivity/y"), new ToDataInput("subX")));
		call.addParameterMapping(new OutputParameterMapping(new FromDataOutput("subY"), new ToVariable("BPMN2-CallActivity/x")));
		
		call.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}