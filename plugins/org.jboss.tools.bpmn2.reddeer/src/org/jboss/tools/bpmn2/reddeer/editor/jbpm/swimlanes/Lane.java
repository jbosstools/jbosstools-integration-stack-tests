package org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;

/**
 * 
 */
public class Lane extends ElementContainer {

	public Lane(String name) {
		super(name, ElementType.LANE);
	}

	@Override
	public void connectTo(Element construct) {
		// See connectTo(Construct, ConnectionType)
	}

	@Override
	public void connectTo(Element construct, ConnectionType connectionType) {
		// No operation! Lane cannot have any appendices It's only an organizational
		// structure.
	}
	
	
	
}
