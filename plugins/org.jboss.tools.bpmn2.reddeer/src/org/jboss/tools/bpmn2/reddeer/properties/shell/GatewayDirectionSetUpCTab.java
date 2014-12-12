package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;

public class GatewayDirectionSetUpCTab extends AbstractSetUpCTab {

	private Direction direction;
	
	public GatewayDirectionSetUpCTab(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public void setUpCTab() {
		new LabeledCombo("Gateway Direction").setSelection(direction.label());
	}

	@Override
	public String getTabLabel() {
		return "Gateway";
	}

}
