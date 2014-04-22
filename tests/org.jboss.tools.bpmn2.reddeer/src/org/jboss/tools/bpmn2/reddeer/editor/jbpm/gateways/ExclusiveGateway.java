package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ExclusiveGatewayTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.GatewayTab;

/**
 * 
 */
public class ExclusiveGateway extends Element {

	/**
	 * 
	 * @param name
	 */
	public ExclusiveGateway(String name) {
		super(name, ElementType.EXCLUSIVE_GATEWAY);
	}
	
	/**
	 * 
	 * @param flow
	 * @param lang
	 * @param condition
	 */
	public void setCondition(String flow, String lang, String condition) {
		properties.getTab("Gateway", ExclusiveGatewayTab.class).setCondition(flow, lang, condition);
	}

	/**
	 * 
	 * @param flow
	 */
	public void setDefaultBranch(String flow) {
		properties.getTab("Gateway", ExclusiveGatewayTab.class).setDefaultBranch(flow);
	}
	
	/**
	 * 
	 * @param flow
	 * @param priority
	 */
	public void setPriority(String flow, String priority) {
		properties.getTab("Gateway", ExclusiveGatewayTab.class).setPriority(flow, priority);
	}
	
	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		properties.getTab("Gateway", GatewayTab.class).setDirection(direction);
	}
}
