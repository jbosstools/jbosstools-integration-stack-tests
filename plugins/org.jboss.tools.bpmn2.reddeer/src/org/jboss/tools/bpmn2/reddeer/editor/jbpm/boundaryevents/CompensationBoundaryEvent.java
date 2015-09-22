package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CompensationActivitySetUp;

public class CompensationBoundaryEvent extends BoundaryEvent {

	public CompensationBoundaryEvent(String name) {
		super(name, ElementType.COMPENSATION_BOUNDARY_EVENT);
	}
	
	public CompensationBoundaryEvent(Element element) {
		super(element);
	}

	public void setCompensationActivity(String activityName, boolean waitForCompletion) {
		propertiesHandler.setUp(new CompensationActivitySetUp(activityName, waitForCompletion));
	}
}
