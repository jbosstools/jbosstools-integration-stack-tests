package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.InclusiveGatewayTab;

/**
 * 
 */
public class InclusiveGateway extends AbstractGateway {

	/**
	 * 
	 * @param name
	 */
	public InclusiveGateway(String name) {
		super(name, ConstructType.INCLUSIVE_GATEWAY);
	}
	
	/**
	 * 
	 * @param flow
	 * @param lang
	 * @param condition
	 */
	public void setCondition(String flow, String lang, String condition) {
		properties.getTab("Gateway", InclusiveGatewayTab.class).setCondition(flow, lang, condition);
	}

	/**
	 * 
	 * @param flow
	 */
	public void setDefaultBranch(String flow) {
		properties.getTab("Gateway", InclusiveGatewayTab.class).setDefaultBranch(flow);
	}
	
	/**
	 * 
	 * @param flow
	 * @param priority
	 */
	public void setPriority(String flow, String priority) {
		properties.getTab("Gateway", InclusiveGatewayTab.class).setPriority(flow, priority);
	}
	
}
