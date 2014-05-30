package org.jboss.tools.teiid.reddeer;

public enum ModelType {

	SOURCE("Source Model"), VIEW("View Model"), DATATYPE("Datatype Model"), EXTENSION("Model Class Extension"), FUNCTION(
			"User Defined Function");

	private String text;

	private ModelType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
