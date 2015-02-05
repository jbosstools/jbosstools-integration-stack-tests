package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

public class CompensationIntermediateThrowEvent extends Element {
	
	public CompensationIntermediateThrowEvent(String name) {
		super(name, ElementType.COMPENSATION_INTERMEDIATE_THROW_EVENT);
	}
	
	public void setCompensationActivity(String activityName) {
		graphitiProperties.setUpTabs(new CompensationActivitySetUpCTab(activityName));
	}
}
