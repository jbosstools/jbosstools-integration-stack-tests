package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class AddLocalVariableSetUp implements SetUpAble {

	private String tabName;
	private String varName;
	private String dataType;

	public AddLocalVariableSetUp(String tabName, String varName, String dataType) {
		this.tabName = tabName;
		this.varName = varName;
		this.dataType = dataType;
	}

	@Override
	public void setUpCTab() {
		new SectionToolItem("Local Variable List", "Add").click();
		DefaultSection variableDetails = new DefaultSection("Local Variable Details");
		new LabeledText(variableDetails, "Name").setText(varName);
		DefaultCombo combo = new DefaultCombo("Data Type");
		if (!combo.contains(dataType)) {
			throw new UnsupportedOperationException("Adding variable of type: " + dataType + " is not supported yet");
		}
		combo.setSelection(dataType);
		new SectionToolItem("Local Variable Details", "Close").click();

	}

	@Override
	public String getTabLabel() {
		return tabName;
	}

}
