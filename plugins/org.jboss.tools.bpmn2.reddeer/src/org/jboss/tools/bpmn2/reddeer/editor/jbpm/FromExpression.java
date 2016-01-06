package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Mapping from an expression.
 */
public class FromExpression implements MappingSide {

	private String scriptLanguage;

	private String script;

	/**
	 * 
	 * @param scriptLanguage
	 * @param script
	 */
	public FromExpression(String scriptLanguage, String script) {
		this.scriptLanguage = scriptLanguage;
		this.script = script;
	}

	@Override
	public void setUp() {
		new RadioButton("Expression").click();
		new LabeledText("Script").setText(script);
	}

	@Override
	public String getName() {
		return script;
	}

}
