package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ComboSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.GatewayConditionSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.GatewayDefaultBranchSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.GatewayPrioritySetUp;

/**
 * 
 */
public class ExclusiveGateway extends ElementWithParamMapping {

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
		propertiesHandler.setUp(new GatewayConditionSetUp(flow, lang, condition));
	}

	/**
	 * 
	 * @param flow
	 */
	public void setDefaultBranch(String flow) {
		propertiesHandler.setUp(new GatewayDefaultBranchSetUp(flow));
	}
	
	/**
	 * 
	 * @param flow
	 * @param priority
	 */
	public void setPriority(String flow, String priority) {
		propertiesHandler.setUp(new GatewayPrioritySetUp(flow, priority));
	}
	
	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.GATEWAY_TAB, "Gateway Direction", direction.label()));
	}
}
