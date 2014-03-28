package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.tools.bpmn2.reddeer.properties.jbpm.GatewayTab;

/**
 * 	
 */
public abstract class AbstractGateway extends Construct {

	/**
	 * 
	 */
	public enum Direction {
		CONVERGING, DIVERGING, MIXED, UNSPECIFIED;
		
		public String label() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractGateway(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		properties.getTab("Gateway", GatewayTab.class).setDirection(direction);
	}
	
}
