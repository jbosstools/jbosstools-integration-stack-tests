package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * 
 */
public enum ConnectionType {
	
	DATA_ASSOCIATION,
	ASSOCIATION_UNDIRECTED,
	ASSOCIATION_ONE_WAY,
	SEQUENCE_FLOW;

	/**
	 * 
	 * @return
	 */
	public String toName() {
		StringBuilder r = new StringBuilder();
		for (String w : name().split("_")) {
			r.append(w.charAt(0) + w.substring(1).toLowerCase());
			r.append(" ");
		}
		
		// ugly but fast way to transform to toolname
		return r.toString()
			.replace("Undirected", "(undirected)")
			.replace("One Way", "(one-way)")
			.trim();
	}
	
}
