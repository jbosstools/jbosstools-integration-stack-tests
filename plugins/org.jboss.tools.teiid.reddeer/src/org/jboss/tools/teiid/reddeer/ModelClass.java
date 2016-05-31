package org.jboss.tools.teiid.reddeer;

public enum ModelClass {

	RELATIONAL("Relational"),
	XML("XML (Deprecated)"),
	XSD("XML Schema (XSD) (Deprecated)"),
	WEBSERVICE("Web Service (Deprecated)"),
	MODEL_EXTENSION("Model Extension (Deprecated)"),
	FUNCTION("Function (Deprecated)");

	private String text;

	private ModelClass(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
