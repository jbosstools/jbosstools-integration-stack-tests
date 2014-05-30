package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class Expression {

	private String language;
	private String script;

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public Expression(String language, String script) {
		this.language = language;
		this.script = script;
	}

	/**
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @return
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUp() {
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
	}
	
	/**
	 * 
	 * @param sectionName
	 */
	public void setUp(String sectionName) {
		DefaultSection secion = new DefaultSection(sectionName);
		secion.getComboBox("Script Language").setSelection(language);
		secion.getText("Script").setText(script);
	}
	
}
