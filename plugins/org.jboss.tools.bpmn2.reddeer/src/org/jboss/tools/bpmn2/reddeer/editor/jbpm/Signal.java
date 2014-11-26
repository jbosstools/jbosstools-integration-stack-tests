package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * @author jomarko
 */
public class Signal {
	private String name;
	
	
	public Signal(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUp() {
		new LabeledText(new DefaultSection("Signal Details"), "Name").setText(name);
	}
	

}
