package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class Escalation {

	private String name;
	private String code;

	/**
	 * 
	 * @param name
	 * @param code
	 */
    public Escalation(String name, String code) {
    	this.name = name;
    	this.code = code;
    	
    	// default value
    	if (this.name == null || this.name.isEmpty()) {
    		this.name = "Escalation Code: " + code;
    	}
    }

    /**
     * 
     * @return
     */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUp() {
		new LabeledText("Name").setText(name);
		new LabeledText("Escalation Code").setText(code);
	}
	
}
