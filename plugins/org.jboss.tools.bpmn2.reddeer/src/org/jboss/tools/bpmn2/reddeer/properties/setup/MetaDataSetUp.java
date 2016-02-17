package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

public class MetaDataSetUp implements SetUpAble {
	
	private String key;
	private String value;
	
	public MetaDataSetUp(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void setUpCTab() {
		new DefaultSection("Metadata").setExpanded(true);;
		new PushButton(new DefaultSection("Metadata"), 0).click();
		new LabeledText(new DefaultSection("Metadata"), "Name").setText(key);
		new LabeledText(new DefaultSection("Metadata"), "Value").setText(value);
	}

	@Override
	public String getTabLabel() {
		return "General";
	}

}
