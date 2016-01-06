package org.jboss.tools.bpmn2.reddeer.editor.properties;

public enum SectionToolItemButton {
	ADD("Add"), EDIT("Edit");

	private String label;

	private SectionToolItemButton(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
