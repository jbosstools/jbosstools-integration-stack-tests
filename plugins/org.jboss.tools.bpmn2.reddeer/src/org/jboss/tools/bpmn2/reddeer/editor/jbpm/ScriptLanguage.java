package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

public enum ScriptLanguage {
	JAVA("java");
	
	private String scriptLanguage;
	
	private ScriptLanguage(String scriptLanguage) {
		this.scriptLanguage = scriptLanguage;
	}
	
	public String enumAsString() {
		return scriptLanguage;
	}
}
