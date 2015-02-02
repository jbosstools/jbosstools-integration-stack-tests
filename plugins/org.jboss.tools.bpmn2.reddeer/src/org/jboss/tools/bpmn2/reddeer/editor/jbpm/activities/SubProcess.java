package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.SubProcessTab;

/**
 * 
 */
public class SubProcess extends ElementContainer {

	private static final String SUB_PROCESS_TAB = "Sub Process";

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ElementType.SUB_PROCESS);
	}
	
	public SubProcess(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		properties.getTab(SUB_PROCESS_TAB, SubProcessTab.class).addLocalVariable(name, dataType);
	}
	
	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab(SUB_PROCESS_TAB, SubProcessTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExitScript(String language, String script) {
		properties.getTab(SUB_PROCESS_TAB, SubProcessTab.class).setOnExitScript(new Expression(language, script));
	}
}
