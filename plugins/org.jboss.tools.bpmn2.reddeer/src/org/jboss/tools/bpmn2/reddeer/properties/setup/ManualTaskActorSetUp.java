package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ManualTaskActorDialog;

public class ManualTaskActorSetUp implements SetUpAble {
	
	private String tabLabel;
	private String actorId;
	
	public ManualTaskActorSetUp(String tabLabel, String actorId) {
		this.tabLabel = tabLabel;
		this.actorId = actorId;
	}

	@Override
	public void setUpCTab() {
		new PushButton(1).click();
		new ManualTaskActorDialog().addActor(actorId);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}
	
	

}
