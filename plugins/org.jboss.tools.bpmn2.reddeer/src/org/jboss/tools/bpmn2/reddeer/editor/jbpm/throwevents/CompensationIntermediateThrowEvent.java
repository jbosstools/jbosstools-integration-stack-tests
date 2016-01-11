package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CompensationActivitySetUp;

public class CompensationIntermediateThrowEvent extends Element {

	public CompensationIntermediateThrowEvent(String name) {
		super(name, ElementType.COMPENSATION_INTERMEDIATE_THROW_EVENT);
	}

	public CompensationIntermediateThrowEvent(Element element) {
		super(element);
	}

	public void setCompensationActivity(String activityName, boolean waitForCompletion) {
		propertiesHandler.setUp(new CompensationActivitySetUp(activityName, waitForCompletion));
	}
}
