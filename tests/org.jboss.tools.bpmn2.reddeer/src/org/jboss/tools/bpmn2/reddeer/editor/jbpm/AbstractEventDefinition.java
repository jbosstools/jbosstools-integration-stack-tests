package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.reddeer.UIControlsHandler;

/**
 *
 */
public abstract class AbstractEventDefinition implements UIControlsHandler {

	/**
	 * 
	 * @return
	 */
	public String label() {
		return getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim();
	}

}
