package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;

public class MultipleInstanceCollectionSetUp implements SetUpAble {

	private String tabLabel;
	private String value;

	public MultipleInstanceCollectionSetUp(String tabLabel, String value) {
		super();
		this.tabLabel = tabLabel;
		this.value = value;
	}

	@Override
	public void setUpCTab() {
		new RadioButton(new DefaultSection("Multi-Instance Loop Characteristics"), "Collection of Data Items").click();
		DefaultCombo combo = new DefaultCombo("Input Data Collection");
		combo.setSelection(value);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
