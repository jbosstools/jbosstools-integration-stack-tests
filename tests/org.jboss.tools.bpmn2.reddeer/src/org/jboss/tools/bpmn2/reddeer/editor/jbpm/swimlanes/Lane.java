package org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Construct;
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

	@Override
	public void connectTo(Construct construct) {
		// See connectTo(Construct, ConnectionType)
	}

	@Override
	public void connectTo(Construct construct, ConnectionType connectionType) {
		// No operation! Lane cannot have any appendies It's only an organizational
		// structure.
	}
	
	
	
}
