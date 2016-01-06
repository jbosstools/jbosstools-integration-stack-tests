package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

/**
 *
 */
public class EventDefinition {

	/**
	 * 
	 * @return
	 */
	public String label() {
		return getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim();
	}

	/**
	 * Take care of the UI user actions required to set up the definition.
	 */
	public void setUp() {
		throw new UnsupportedOperationException();
	}

}
