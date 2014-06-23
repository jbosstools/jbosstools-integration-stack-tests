package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 *
 */
public class DescriptionTab {

	/**
	 * 
	 * @param text
	 */
	public void setName(String text) {
		new LabeledText("Name").setText(text);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setDocumentation(String text) {
		new LabeledText("Documentation").setText(text);
	}
	
}
