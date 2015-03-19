package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.tools.bpmn2.reddeer.DefaultCombo;

public class ComboSetUp implements SetUpAble {

	private String tabLabel;
	private String comboLabel;
	private String value;
	
	public ComboSetUp(String tabLabel, String comboLabel, String value) {
		this.tabLabel = tabLabel;
		this.comboLabel = comboLabel;
		this.value = value;
	}
	
	@Override
	public void setUpCTab() {
		DefaultCombo combo = new DefaultCombo(comboLabel);
		
		if (!combo.contains(value)) {
			throw new IllegalArgumentException("There is no such item: " + value);
		}
		combo.setSelection(value);

	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
