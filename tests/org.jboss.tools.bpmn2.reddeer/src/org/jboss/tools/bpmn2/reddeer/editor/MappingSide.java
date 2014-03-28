package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.reddeer.UIControlsHandler;

/**
 * Represents one side in the equation of a ParameterMapping.
 * 
 * @see ParameterMapping
 */
public interface MappingSide extends UIControlsHandler {
	
	/**
	 * 
	 * @return
	 */
	String getName();
	
}
