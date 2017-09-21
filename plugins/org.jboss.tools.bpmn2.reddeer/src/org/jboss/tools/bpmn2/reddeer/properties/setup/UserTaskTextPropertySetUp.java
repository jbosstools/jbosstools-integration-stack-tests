package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;

public class UserTaskTextPropertySetUp implements SetUpAble{
	
	private int propertyOrder;
	private String propertyValue;
	
	public UserTaskTextPropertySetUp(int propertyOrder, String propertyValue) {
		this.propertyOrder = propertyOrder;
		this.propertyValue = propertyValue;
	}

	@Override
	public void setUpCTab() {
		new DefaultShell("Edit User Task");
		new DefaultTabItem("User Task").activate();
		new PushButton(propertyOrder).click();
		new RadioButton("Expression").click();
		new LabeledText("Script").setText(propertyValue);
		new PushButton("OK").click();
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.USER_TASK_TAB;
	}

}
