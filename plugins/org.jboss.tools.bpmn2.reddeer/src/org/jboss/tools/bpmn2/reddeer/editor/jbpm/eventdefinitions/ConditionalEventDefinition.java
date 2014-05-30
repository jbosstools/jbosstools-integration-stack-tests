package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class ConditionalEventDefinition extends EventDefinition {
	
	private Expression expression;
	
	/**
	 * 
	 * @param expression
	 */
	public ConditionalEventDefinition(Expression expression) {
		this.expression = expression;
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public ConditionalEventDefinition(String language, String script) {
		this(new Expression(language, script));
	}
	
	@Override
	public void setUp() {
		expression.setUp();
		new DefaultSection("Conditional Event Definition Details").getToolbarButton("Close").click();
	}
	
}
