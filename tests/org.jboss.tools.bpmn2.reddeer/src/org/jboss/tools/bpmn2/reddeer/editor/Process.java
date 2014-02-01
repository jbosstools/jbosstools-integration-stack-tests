package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Process extends ContainerConstruct {

	/**
	 *
	 * @param name
	 */
	public Process(String name) {
		super(name, ConstructType.PROCESS);
	}
	
 }
