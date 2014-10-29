package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.AdHocSubProcessTab;

/**
 * 
 */
public class AdHocSubProcess extends ElementContainer {

	/**
	 * 
	 * @param name
	 */
	public AdHocSubProcess(String name) {
		super(name, ElementType.AD_HOC_SUB_PROCESS);
	}
	
	public void setCompletionCondition(String language, String script) {
		properties.getTab("AdHoc Sub Process", AdHocSubProcessTab.class).setCompletionCondition(new Expression(language, script));
		
	}
	
}
