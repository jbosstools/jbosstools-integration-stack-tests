package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.AddLocalVariableSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

/**
 * 
 */
public class SubProcess extends ElementContainer {

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
		propertiesHandler.setUp(new AddLocalVariableSetUp(PropertiesTabs.SUB_PROCESS_TAB, varName, dataType));
	}
	
	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.SUB_PROCESS_TAB, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExitScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.SUB_PROCESS_TAB, "On Eexit Script", language, script));
	}
}
