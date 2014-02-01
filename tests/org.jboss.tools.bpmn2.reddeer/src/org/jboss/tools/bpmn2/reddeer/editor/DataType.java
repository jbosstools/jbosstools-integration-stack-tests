package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * A data type.
 * 
 * E.g. message type, variable type, etc.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public interface DataType {

	/**
	 * Add this data type to the process.
	 */
	void add();
	
	/**
	 * Remove this data type from the process. 
	 */
	void remove();
	
}
