package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class AddActorSetUp implements SetUpAble {
	
	private String tabName;
	private String actorName;
	
	public AddActorSetUp(String tabName, String actorName) {
		this.tabName = tabName;
		this.actorName = actorName;
	}

	@Override
	public void setUpCTab() {
		new SectionToolItem("Actors", "Add").click();
		new LabeledText("Name").setText(actorName);
		new SectionToolItem("Actor Details", "Close").click(); 
	}

	@Override
	public String getTabLabel() {
		return tabName;
	}

}
