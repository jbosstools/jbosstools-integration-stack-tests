package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.EventDefinitionTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.AbstractEventDefinition;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class EventTab {

	/**
	 * 
	 * @param definition
	 */
	public void set(AbstractEventDefinition definition) {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (getEventCount() == 1) {
			section.getTable().select(0);
			String eventOnTabType = section.getTable().getTableItem(0).getText(0);
			if (!definition.getClass().getSimpleName().startsWith(eventOnTabType)) {
				section.getToolbarButton("Remove").click();
				section.getToolbarButton("Add").click();
				new EventDefinitionTypeDialog().add(definition.label());
			} else {
				section.getToolbarButton("Edit").click();
			}
		}
		definition.setUp();
	}
	
//	/**
//	 * 
//	 * @param definition
//	 */
//	public void set(AbstractEventDefinition definition) {
//		DefaultSection section = new DefaultSection("Event Definitions");
//		// Cannot just remove the old one and add the new one. If the event definition 
//		// type changes then the 'Event', on which the method was called will become 
//		// detached! The above (commented out) implementation is nice, but once called
//		// then no other construct can be appended to the 'Event'.
//		if (getEventCount() == 1) {
//			String eventOnTabType = section.getTable().getTableItem(0).getText(0);
//			if (!definition.getClass().getSimpleName().startsWith(eventOnTabType)) {
//				throw new UnsupportedOperationException("Event definition '" + definition + 
//						"' is not of type '" + eventOnTabType + "'.");
//			}
//		}
//		section.getTable().select(0);
//		section.getToolbarButton("Edit").click();
//		definition.setUp();
//	}

	/**
	 * 
	 */
	public void removeDefinition() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (getEventCount() == 0) {
			throw new IllegalStateException();
		}
		section.getTable().select(0);
		section.getToolbarButton("Remove").click();
	}
	
	public int getEventCount() {
		return new DefaultSection("Event Definitions").getTable().rowCount();
	}
	
}
