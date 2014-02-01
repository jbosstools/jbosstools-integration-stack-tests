package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractEvent extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractEvent(String name, ConstructType type) {
		super(name, type);
	}

	/**
	 * 
	 * @param mapping
	 */
	protected void addParameterMapping(ParameterMapping mapping) {
		properties.selectTab("Event");
		mapping.add();
	}
	
	/**
	 * 
	 * @param mapping
	 */
	protected void removeParameterMapping(ParameterMapping mapping) {
		properties.selectTab("Event");
		mapping.remove();
	}
	
}
