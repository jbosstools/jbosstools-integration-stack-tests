package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * A process variable.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public interface Variable {

	/**
	 * Add this variable to the process.
	 */
	void add();
	
	/**
	 * Remove this variable from the process.
	 */
	void remove();
	
}
