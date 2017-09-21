package org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes;

import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
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

	public Lane(Element element) {
		super(element);
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

	public void changeOrientation() {
		click();
		new ContextMenuItem("Change Lane Orientation").select();

	}

}
