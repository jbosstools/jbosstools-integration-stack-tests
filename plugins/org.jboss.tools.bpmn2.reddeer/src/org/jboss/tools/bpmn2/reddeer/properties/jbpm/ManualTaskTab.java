package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;

/**
 * 
 */
public class ManualTaskTab extends GeneralPropertiesTab {

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		new DefaultCheckBox("Is For Compensation").setChecked(value);
	}
	
}
