package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;

public class GeneralPropertiesTab {
	/**
	 * 
	 * @param expression
	 */
	public void setOnEntryScript(Expression expression) {
		expression.setUp("On Entry Script");
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setOnExitScript(Expression expression) {
		expression.setUp("On Exit Script");
	}

}
