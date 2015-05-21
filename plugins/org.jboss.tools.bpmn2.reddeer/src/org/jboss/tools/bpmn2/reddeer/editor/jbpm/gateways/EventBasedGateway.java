package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ComboSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.GatewayConditionSetUp;

/**
 *  
 */
public class EventBasedGateway extends ElementWithParamMapping {
	
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
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.GATEWAY_TAB, "Event Gateway Type", type.label()));
	}

	/**
	 * 
	 * @param value
	 */
	public void setInstantiate(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.GATEWAY_TAB, "Instantiate", value));
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
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.GATEWAY_TAB, "Gateway Direction", direction.label()));
	}
}
