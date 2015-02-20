package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImplementationDialog;

public class ImplementationSetUp implements SetUpAble {
	
	private String tabName;
	private String implementationUri;
	
	public ImplementationSetUp(String tabName, String implementationUri) {
		this.tabName = tabName;
		this.implementationUri = implementationUri;
	}

	@Override
	public void setUpCTab() {
		DefaultCombo combo = new DefaultCombo("Implementation");
		if (!combo.contains(implementationUri)) {
			new PushButton(0).click();
			new ImplementationDialog().add(implementationUri);
		}
		combo.setSelection(implementationUri);
	}

	@Override
	public String getTabLabel() {
		return tabName;
	}

}
