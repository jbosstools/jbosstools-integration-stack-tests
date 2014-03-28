package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;

/**
 *
 */
public class ScriptTaskTab {

	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		new DefaultCheckBox("Is For Compensation").setChecked(value);
	}

	/**
	 * 
	 * @param expression
	 */
	public void set(Expression expression) {
		setScript(expression.getLanguage(), expression.getScript());
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setScript(String language, String script) {
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
	}
	
}
