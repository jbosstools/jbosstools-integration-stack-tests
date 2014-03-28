package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ContainerConstruct;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.SubProcessTab;

/**
 * 
 */
public class SubProcess extends ContainerConstruct {

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ConstructType.SUB_PROCESS);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		properties.getTab("Sub Process", SubProcessTab.class).addVariable(name, dataType);
	}
}
