package org.jboss.tools.teiid.reddeer;

public enum ModelClass {

	RELATIONAL("Relational"), XML("XML"), XSD("XML Schema (XSD)"), WEBSERVICE("Web Service"), MODEL_EXTENSION(
			"Model Extension (Deprecated)"), FUNCTION("Function (Deprecated)");

	private String text;

	private ModelClass(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
