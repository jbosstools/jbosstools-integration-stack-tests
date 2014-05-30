package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

/**
 * 
 */
public class InclusiveGatewayTab extends GatewayTab {

	@Override
	public void setCondition(String flow, String lang, String condition) {
		super.setCondition(flow, lang, condition);
	}

	@Override
	public void setDefaultBranch(String flow) {
		super.setDefaultBranch(flow);
	}

	@Override
	public void setPriority(String flow, String priority) {
		super.setPriority(flow, priority);
	}
	
}
