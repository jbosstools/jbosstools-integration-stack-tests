package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

public enum TimerType {
	INTERVAL, DURATION;
	
	public String label() {
	    return name().charAt(0) + name().substring(1).toLowerCase();
	}
}