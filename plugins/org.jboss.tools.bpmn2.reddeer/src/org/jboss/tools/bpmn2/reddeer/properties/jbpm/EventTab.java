package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.EventDefinitionTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.SignalDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class EventTab {

	/**
	 * 
	 * @param definition
	 */
	public void set(EventDefinition definition) {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (getEventCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!definition.getClass().getSimpleName().startsWith(eventOnTabType)) {
				new SectionToolItem("Event Definitions","Remove").click();
				new SectionToolItem("Event Definitions", "Add").click();
				new EventDefinitionTypeDialog().add(definition.label());
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		definition.setUp();
	}
	
	/**
	 * Example of usage: If I have Signal start event, and I want to specify Signal to this event, but
	 * I ha not needed signal declared in process.
	 * @param signal
	 */
	public void addNewSignal(Signal signal) {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (getEventCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!signal.getClass().getSimpleName().startsWith(eventOnTabType)) {
				new SectionToolItem("Event Definitions", "Remove").click();
				new SectionToolItem("Event Definitions", "Add").click();
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		new PushButton(new DefaultSection("Signal Event Definition Details"), 0).click();
		new SignalDialog().add(signal);
	}
	
	/**
	 * 
	 */
	public void removeDefinition() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (getEventCount() == 0) {
			throw new IllegalStateException();
		}
		new DefaultTable(section).select(0);
		new SectionToolItem("Event Definitions", "Remove").click();
	}
	
	public int getEventCount() {
		return new DefaultTable(new DefaultSection("Event Definitions")).rowCount();
	}
	
}
