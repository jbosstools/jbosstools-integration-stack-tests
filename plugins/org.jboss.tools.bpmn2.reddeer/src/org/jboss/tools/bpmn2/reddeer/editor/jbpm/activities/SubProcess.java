package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddLocalVariableSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class SubProcess extends ElementContainer {

	private static final String SUB_PROCESS_TAB = "Sub Process";

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ElementType.SUB_PROCESS);
	}
	
	public SubProcess(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param varName
	 * @param dataType
	 */
	public void addLocalVariable(String varName, String dataType) {
		propertiesHandler.setUp(new AddLocalVariableSetUp(SUB_PROCESS_TAB, varName, dataType));
	}
	
	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(SUB_PROCESS_TAB, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExitScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(SUB_PROCESS_TAB, "On Eexit Script", language, script));
	}
}
