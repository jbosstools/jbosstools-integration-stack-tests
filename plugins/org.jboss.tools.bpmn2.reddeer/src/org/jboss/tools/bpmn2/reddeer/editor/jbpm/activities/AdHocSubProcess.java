package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

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
	
	public AdHocSubProcess(Element element) {
		super(element);
	}
	
	public void setCompletionCondition(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab("AdHoc Sub Process", "Attributes", language, script));
		
	}
	
}
