package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class MetaDataSetUp implements SetUpAble {
	
	private String key;
	private String value;
	
	public MetaDataSetUp(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void setUpCTab() {
		new SectionToolItem("Metadata", "Add").click();
		new LabeledText(new DefaultSection("Meta Data Type Details"), "Name").setText(key);
		new LabeledText(new DefaultSection("Meta Data Type Details"), "Value").setText(value);
		new SectionToolItem("Meta Data Type Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return "General";
	}

}
