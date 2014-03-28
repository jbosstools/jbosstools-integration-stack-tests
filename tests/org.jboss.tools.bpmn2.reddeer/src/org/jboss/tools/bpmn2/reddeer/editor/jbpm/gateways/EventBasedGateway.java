package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventBasedGatewayTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.InclusiveGatewayTab;

/**
 *  
 */
public class EventBasedGateway extends AbstractGateway {
	
	public enum Type {
		EXCLUSIVE, PARALLEL;
		
		public String label() {
		    return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
	/**
	 * 
	 * @param name
	 */
	public EventBasedGateway(String name) {
		super(name, ConstructType.EVENT_BASED_GATEWAY);
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		properties.getTab("Gateway", EventBasedGatewayTab.class).setType(type);
	}

	/**
	 * 
	 * @param value
	 */
	public void setInstantiate(boolean value) {
		properties.getTab("Gateway", EventBasedGatewayTab.class).setInstantiate(value);
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
	
}
