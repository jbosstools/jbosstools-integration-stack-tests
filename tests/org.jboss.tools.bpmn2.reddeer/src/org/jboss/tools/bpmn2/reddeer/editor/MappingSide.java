package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * Represents one side in the equation of a ParameterMapping.
 * 
 * @see ParameterMapping
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public interface MappingSide {

	/**
	 * Add this mapping side. E.g. from variable. 
	 */
	void add();
	
	/**
	 * Get the value of this mapping side. E.g. the variable name or
	 * script value etc.
	 * 
	 * @return
	 */
	String getValue();
	
}
