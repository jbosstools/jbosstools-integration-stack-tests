package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.RadioButton;

public class RadioSetUp implements SetUpAble {
	
	private String radioValue;
	private String tabLabel;
	
	public RadioSetUp(String radioValue, String tabLabel) {
		super();
		this.radioValue = radioValue;
		this.tabLabel = tabLabel;
	}

	@Override
	public void setUpCTab() {
		new RadioButton(radioValue).click();
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
