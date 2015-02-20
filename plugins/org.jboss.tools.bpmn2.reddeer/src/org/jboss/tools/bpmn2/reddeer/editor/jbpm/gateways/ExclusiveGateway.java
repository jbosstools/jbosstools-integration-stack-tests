package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayConditionSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayDefaultBranch;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayDirectionSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayPrioritySetUpCTab;

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
	
	public ExclusiveGateway(org.jboss.tools.bpmn2.reddeer.editor.Element element){
		super(element);
	}
	
	/**
	 * 
	 * @param flow
	 * @param lang
	 * @param condition
	 */
	public void setCondition(String flow, String lang, String condition) {
		propertiesHandler.setUp(new GatewayConditionSetUpCTab(flow, lang, condition));
	}

	/**
	 * 
	 * @param flow
	 */
	public void setDefaultBranch(String flow) {
		propertiesHandler.setUp(new GatewayDefaultBranch(flow));
	}
	
	/**
	 * 
	 * @param flow
	 * @param priority
	 */
	public void setPriority(String flow, String priority) {
		propertiesHandler.setUp(new GatewayPrioritySetUpCTab(flow, priority));
	}
	
	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		propertiesHandler.setUp(new GatewayDirectionSetUpCTab(direction));
	}
}
