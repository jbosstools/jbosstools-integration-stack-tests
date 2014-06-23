package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

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
		new DefaultCheckBox("Cancel Activity").setChecked(value);
	}
	
}
