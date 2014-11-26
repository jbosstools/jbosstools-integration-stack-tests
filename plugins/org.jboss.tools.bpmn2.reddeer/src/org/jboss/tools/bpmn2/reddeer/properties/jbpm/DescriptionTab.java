package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 *
 */
public class DescriptionTab {

	/**
	 * 
	 * @param text
	 */
	public void setName(String text) {
		new LabeledText(new DefaultSection("Attributes"), "Name").setText(text);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setDocumentation(String text) {
		new LabeledText("Documentation").setText(text);
	}
	
}
