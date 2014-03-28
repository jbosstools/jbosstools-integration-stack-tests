package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class ParallelGateway extends AbstractGateway {

	/**
	 * 
	 * @param name
	 */
	public ParallelGateway(String name) {
		super(name, ConstructType.PARALLEL_GATEWAY);
	}

}
