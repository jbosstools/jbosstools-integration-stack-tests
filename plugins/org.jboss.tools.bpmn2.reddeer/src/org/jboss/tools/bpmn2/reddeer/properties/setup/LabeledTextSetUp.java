package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class LabeledTextSetUp implements SetUpAble {

	private String tabLabel;
	private String textLabel;
	private String value;
	
	public LabeledTextSetUp(String tabLabel, String textLabel, String value) {
		this.tabLabel = tabLabel;
		this.textLabel = textLabel;
		this.value = value;
	}
	
	@Override
	public void setUpCTab() {
		new LabeledText(textLabel).setText(value);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
