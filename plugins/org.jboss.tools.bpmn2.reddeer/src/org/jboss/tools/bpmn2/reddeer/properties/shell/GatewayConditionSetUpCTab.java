package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class GatewayConditionSetUpCTab extends AbstractSetUpCTab {

	private String flow;
	private String lang;
	private String condition;
	
	public GatewayConditionSetUpCTab(String flow, String lang, String condition) {
		this.flow = flow;
		this.lang = lang;
		this.condition = condition;
	}
	
	@Override
	public void setUpCTab() {
		new DefaultTable(new DefaultSection("Sequence Flow List")).select(flow);
		new SectionToolItem("Sequence Flow List", "Edit").click();
		new PushButton("Add Condition").click();
		new LabeledCombo("Condition Language").setSelection(lang);
		new LabeledText("Condition").setText(condition);
		new SectionToolItem("Sequence Flow Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return "Gateway";
	}

}
