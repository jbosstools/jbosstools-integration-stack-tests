package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.reddeer.DefaultCheckBox;

/**
 * 
 */
public class BoundaryEventTab extends EventTab {

	/**
	 * 
	 * @param value
	 */
	public void setCancelActivity(boolean value) {
		new DefaultCheckBox(new DefaultSection("Attributes"), "Cancel Activity").setChecked(value);
	}
	
}
