package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ComboSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayConditionSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayDefaultBranch;
import org.jboss.tools.bpmn2.reddeer.properties.shell.GatewayDirectionSetUpCTab;

/**
 *  
 */
public class EventBasedGateway extends Element {
	
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
		super(name, ElementType.EVENT_BASED_GATEWAY);
	}
	
	public EventBasedGateway(org.jboss.tools.bpmn2.reddeer.editor.Element element){
		super(element);
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		propertiesHandler.setUp(new ComboSetUpCTab("Gateway", "Event Gateway Type", type.label()));
	}

	/**
	 * 
	 * @param value
	 */
	public void setInstantiate(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab("Gateway", "Instantiate", value));
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
		propertiesHandler.setUp(new GatewayDefaultBranch(flow));
	}
	
	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		propertiesHandler.setUp(new GatewayDirectionSetUpCTab(direction));
	}
}
