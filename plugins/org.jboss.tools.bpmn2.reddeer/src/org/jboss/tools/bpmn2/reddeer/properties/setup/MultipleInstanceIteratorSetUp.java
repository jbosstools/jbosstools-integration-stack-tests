package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataInputDialog;

public class MultipleInstanceIteratorSetUp implements SetUpAble {

	private String tabLabel;
	private String value;

	public MultipleInstanceIteratorSetUp(String tabLabel, String value) {
		super();
		this.tabLabel = tabLabel;
		this.value = value;
	}

	@Override
	public void setUpCTab() {
		new PushButton(new DefaultSection(new DefaultSection("Multi-Instance Loop Characteristics"), "Input Data Items"), 1).click();
		new DataInputDialog().add(value, "java.lang.Object");
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
