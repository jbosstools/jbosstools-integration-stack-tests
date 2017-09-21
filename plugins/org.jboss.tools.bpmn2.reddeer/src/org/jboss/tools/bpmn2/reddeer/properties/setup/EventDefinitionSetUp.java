package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.EventDefinitionTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class EventDefinitionSetUp implements SetUpAble {

	private EventDefinition definition;

	public EventDefinitionSetUp(EventDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (new DefaultTable(new DefaultSection("Event Definitions")).rowCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!definition.getClass().getSimpleName().startsWith(eventOnTabType)) {
				new SectionToolItem("Event Definitions", "Remove").click();
				new SectionToolItem("Event Definitions", "Add").click();
				new EventDefinitionTypeDialog().add(definition.label());
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		definition.setUp();
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.EVENT_TAB;
	}

}
