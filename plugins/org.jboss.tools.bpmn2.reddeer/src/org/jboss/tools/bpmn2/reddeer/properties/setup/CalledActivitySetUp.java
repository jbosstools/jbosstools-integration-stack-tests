package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.CalledActivityDialog;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;

public class CalledActivitySetUp implements SetUpAble {

	private String activityName;

	public CalledActivitySetUp(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public void setUpCTab() {
		new PushButton(0).click();
		new CalledActivityDialog().add(activityName);

	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.CALL_ACTIVITY_TAB;
	}

}
