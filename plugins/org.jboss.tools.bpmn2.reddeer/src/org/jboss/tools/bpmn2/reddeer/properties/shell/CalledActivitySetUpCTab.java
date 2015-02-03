package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.CalledActivityDialog;

public class CalledActivitySetUpCTab extends AbstractSetUpCTab {

	private String activityName;
	
	public CalledActivitySetUpCTab(String activityName) {
		this.activityName = activityName;
	}
	
	@Override
	public void setUpCTab() {
		new PushButton(0).click();
		new CalledActivityDialog().add(activityName);
		
	}

	@Override
	public String getTabLabel() {
		return "Call Activity";
	}

}
