package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;

/**
 * Represents one side in the equation of a ParameterMapping.
 * 
 * @see ParameterMapping
 */
public interface MappingSide {

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Perform user actions which are required to set up this object in the UI.
	 */
	void setUp();

}
