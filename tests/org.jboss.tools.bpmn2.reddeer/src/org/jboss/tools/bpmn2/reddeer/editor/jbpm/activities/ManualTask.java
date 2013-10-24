package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ManualTask extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public ManualTask(String name) {
		super(name, ConstructType.MANUAL_TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
