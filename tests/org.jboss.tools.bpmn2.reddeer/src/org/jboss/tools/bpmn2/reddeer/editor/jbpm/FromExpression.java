package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping from an expression.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class FromExpression implements MappingSide {

	private String scriptLanguage;
	
	private String script;
	
	/**
	 * Creates a new instance of FromExpression.
	 * 
	 * @param variableName
	 */
	public FromExpression(String scriptLanguage, String script) {
		this.scriptLanguage = scriptLanguage;
		this.script = script;
	}
	
	@Override
	public void add() {
		new RadioButton("Expression").click();
		new LabeledCombo("Script Language").setSelection(scriptLanguage);
		new LabeledText("Script").setText(script);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getValue() {
		return script;
	}
	
}
