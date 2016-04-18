package org.jboss.tools.common.reddeer.view;

/**
 * Represents a message from 'Messages View'
 * 
 * @author tsedmik
 */
public class Message {

	int traceId;
	String traceNode;

	public int getTraceId() {
		return traceId;
	}

	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}

	public String getTraceNode() {
		return traceNode;
	}

	public void setTraceNode(String traceNode) {
		this.traceNode = traceNode;
	}
}
