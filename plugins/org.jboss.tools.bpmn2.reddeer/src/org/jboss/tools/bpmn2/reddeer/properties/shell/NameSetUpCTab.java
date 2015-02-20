package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.text.LabeledText;


public class NameSetUpCTab implements SetUpAble {
	private String name;
	
	public NameSetUpCTab(String name) {
		this.name = name;
	}

	@Override
	public void setUpCTab() {
		new LabeledText("Name").setText(name);
	}

	@Override
	public String getTabLabel() {
		return "General";
	}	
}
