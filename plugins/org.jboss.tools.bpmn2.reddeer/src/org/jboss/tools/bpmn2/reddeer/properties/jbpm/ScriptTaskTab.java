package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;

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
		expression.setUp("Attributes");
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setScript(String language, String script) {
		set(new Expression(language, script));
	}
	
}
