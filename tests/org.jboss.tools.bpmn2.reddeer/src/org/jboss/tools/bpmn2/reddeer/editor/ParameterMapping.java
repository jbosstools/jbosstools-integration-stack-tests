package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * A parameter mapping from source to target.
 * 
 * E.g. from an incoming message to a process variable or vice versa.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 *
 */
public interface ParameterMapping {

	/**
	 * Add this parameter mapping to the process or task.
	 * @param to
	 */
	void add();
	
	/**
	 * Remove this parameter mapping from the process or task.
	 */
	void remove();
	
}
