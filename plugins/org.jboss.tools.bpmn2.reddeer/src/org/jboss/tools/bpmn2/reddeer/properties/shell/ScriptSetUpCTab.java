package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

public class ScriptSetUpCTab extends AbstractSetUpCTab {

	private String language;
	private String script;
	private String tabLabel;
	
	public ScriptSetUpCTab(String tabLabel, String language, String script) {
		this.tabLabel = tabLabel;
		this.language = language;
		this.script = script;
	}
	
	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Attributes");
		new LabeledCombo(section, "Script Language").setSelection(language);
		new LabeledText(section, "Script").setText(script);
	}

	@Override
	public String getTabLabel() {
		return tabLabel;
	}

}
