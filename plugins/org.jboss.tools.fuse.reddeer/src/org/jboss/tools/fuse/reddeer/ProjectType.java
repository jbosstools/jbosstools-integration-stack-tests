package org.jboss.tools.fuse.reddeer;

public enum ProjectType {

	JAVA("Java DSL"),
	SPRING("Spring DSL", "camel-context.xml", "beans"),
	BLUEPRINT("Blueprint DSL", "blueprint.xml", "blueprint");

	private String description;
	private String camelContext;
	private String rootElement;

	private ProjectType(String description) {
		this(description, null);
	}

	private ProjectType(String description, String camelContext) {
		this(description, camelContext, null);
	}

	private ProjectType(String description, String camelContext, String rootElement) {
		this.description = description;
		this.camelContext = camelContext;
		this.rootElement = rootElement;
	}

	public String getDescription() {
		return description;
	}

	public String getCamelContext() {
		return camelContext;
	}

	public String getRootElement() {
		return rootElement;
	}
}