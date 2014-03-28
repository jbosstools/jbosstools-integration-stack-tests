package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.AbstractEventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class ConditionalEventDefinition extends AbstractEventDefinition {

	private String language, script;
	
	/**
	 * 
	 * @param e
	 */
	public ConditionalEventDefinition(Expression e) {
		this(e.getLanguage(), e.getScript());
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public ConditionalEventDefinition(String language, String script) {
		this.language = language;
		this.script = script;
	}
	
	@Override
	public void setUp() {
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
		new DefaultSection("Conditional Event Definition Details").getToolbarButton("Close").click();
	}
	
}
