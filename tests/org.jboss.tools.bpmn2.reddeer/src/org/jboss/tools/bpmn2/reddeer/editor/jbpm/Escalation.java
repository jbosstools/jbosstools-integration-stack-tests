package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.reddeer.UIControlsHandler;

/**
 * 
 */
public class Escalation implements UIControlsHandler {

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
	
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
	}
	
}
