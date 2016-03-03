package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class LabeledTextSetUp implements SetUpAble {

	private String tabLabel;
	private String textLabel;
	private String value;
	private boolean putByTyping;

	public LabeledTextSetUp(String tabLabel, String textLabel, String value) {
		this(tabLabel, textLabel, value, false);
	}
	
	public LabeledTextSetUp(String tabLabel, String textLabel, String value, boolean putByTyping) {
		this.tabLabel = tabLabel;
		this.textLabel = textLabel;
		this.value = value;
		this.putByTyping = putByTyping;
	}

	@Override
	public void setUpCTab() {
		LabeledText textbox = new LabeledText(textLabel);
		if(putByTyping) {
			textbox.typeText(value);
		} else {
			textbox.setText(value);
		}
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
