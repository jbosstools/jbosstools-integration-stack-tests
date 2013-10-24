package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ContainerConstruct;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class AdHocSubProcess extends ContainerConstruct {

	/**
	 * 
	 * @param name
	 */
	public AdHocSubProcess(String name) {
		super(name, ConstructType.AD_HOC_SUB_PROCESS);
	}
	
}
