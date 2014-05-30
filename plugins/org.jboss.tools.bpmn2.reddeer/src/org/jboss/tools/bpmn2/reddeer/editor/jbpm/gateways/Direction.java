package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

/**
 * 
 */
public enum Direction {

	CONVERGING, DIVERGING, MIXED, UNSPECIFIED;

	/**
	 * 
	 * @return
	 */
	public String label() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
	
}
