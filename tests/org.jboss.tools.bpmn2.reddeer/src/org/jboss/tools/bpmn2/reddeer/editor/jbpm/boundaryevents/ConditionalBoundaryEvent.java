package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author mbaluch
 */
public class ConditionalBoundaryEvent extends BoundaryEvent {

	public ConditionalBoundaryEvent(String name) {
		super(name, ConstructType.CONDITIONAL_BOUNDARY_EVENT);
	}
	
	public void setScript(String language, String script) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		if (language != null) {
			new LabeledCombo("Script Language").setSelection(language);
		}
		new LabeledText("Script").setText(script);
		
		properties.toolbarButton("Conditional Event Definition Details", "Close").click();
	}
	
}
