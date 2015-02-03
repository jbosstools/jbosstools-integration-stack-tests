package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;

public class CheckBoxSetUpCTab extends AbstractSetUpCTab {

	private boolean value;
	private String tabLabel;
	private String checkBoxLabel;
	
	public CheckBoxSetUpCTab(String tabLabel, String checkBoxLabel, boolean value) {
		this.tabLabel = tabLabel;
		this.checkBoxLabel = checkBoxLabel;
		this.value = value;
	}
	
	@Override
	public void setUpCTab(){
		new DefaultCheckBox(checkBoxLabel).setChecked(value);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
