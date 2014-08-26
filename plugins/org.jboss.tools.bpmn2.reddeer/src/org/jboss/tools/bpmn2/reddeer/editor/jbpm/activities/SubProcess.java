package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.SubProcessTab;

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
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		properties.getTab("Sub Process", SubProcessTab.class).addLocalVariable(name, dataType);
	}
}
