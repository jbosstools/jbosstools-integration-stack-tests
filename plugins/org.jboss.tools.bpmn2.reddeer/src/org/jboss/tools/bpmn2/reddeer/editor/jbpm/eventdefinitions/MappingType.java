package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

public enum MappingType {
	SOURCE, TARGET;

	public String label() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}
