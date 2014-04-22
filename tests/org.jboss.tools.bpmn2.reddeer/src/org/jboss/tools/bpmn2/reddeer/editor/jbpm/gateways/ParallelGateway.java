package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.GatewayTab;

/**
 * 
 */
public class ParallelGateway extends Element {

	/**
	 * 
	 * @param name
	 */
	public ParallelGateway(String name) {
		super(name, ElementType.PARALLEL_GATEWAY);
	}

	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		properties.getTab("Gateway", GatewayTab.class).setDirection(direction);
	}
	
}
