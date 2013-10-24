package org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ContainerConstruct;


/**
 * 
 * @author mbaluch
 */
public class Lane extends ContainerConstruct {

	public Lane(String name) {
		super(name, ConstructType.LANE);
	}
	
}
