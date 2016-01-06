package org.jboss.tools.teiid.reddeer;

public enum ModelBuilder {

	TRANSFORM_EXISTING("Transform from an existing model"),
	COPY_EXISTING("Copy from an existing model of the same model class"),
	BUILD_FROM_XML_SCHEMA("Build XML documents from XML schema"),
	BUILD_FROM_WSDL_URL("Build from existing WSDL file(s) or URL");

	private String text;

	private ModelBuilder(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
