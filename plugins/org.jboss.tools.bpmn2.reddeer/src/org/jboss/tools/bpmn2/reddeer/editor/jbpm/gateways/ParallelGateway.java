package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ComboSetUp;

/**
 * 
 */
public class ParallelGateway extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 */
	public ParallelGateway(String name) {
		super(name, ElementType.PARALLEL_GATEWAY);
	}
	
	public ParallelGateway(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}

	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.GATEWAY_TAB, "Gateway Direction", direction.label()));
	}
	
}
