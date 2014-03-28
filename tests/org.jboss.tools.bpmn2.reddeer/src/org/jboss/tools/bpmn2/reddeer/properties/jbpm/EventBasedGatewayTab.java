package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.EventBasedGateway.Type;
import org.jboss.tools.reddeer.DefaultCheckBox;

/**
 * 
 */
public class EventBasedGatewayTab extends GatewayTab {

	/**
	 * 
	 * @param value
	 */
	public void setInstantiate(boolean value) {
		new DefaultCheckBox("Instantiate").setChecked(value);
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		new LabeledCombo("Event Gateway Type").setSelection(type.label());
	}
	
}
