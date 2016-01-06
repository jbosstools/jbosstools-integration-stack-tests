package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;

public class CheckBoxSetUp implements SetUpAble {

	private boolean value;
	private String tabLabel;
	private String checkBoxLabel;

	public CheckBoxSetUp(String tabLabel, String checkBoxLabel, boolean value) {
		this.tabLabel = tabLabel;
		this.checkBoxLabel = checkBoxLabel;
		this.value = value;
	}

	@Override
	public void setUpCTab() {
		new LabeledCheckBox(checkBoxLabel).toggle(value);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
