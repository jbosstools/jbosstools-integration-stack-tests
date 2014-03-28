package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.reddeer.UIControlsHandler;

/**
 * 
 */
public class Message implements UIControlsHandler {

	private String name;
	private String dataType;

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public Message(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
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
	public String getDataType() {
		return dataType;
	}

	@Override
	public void setUp() {
		// TODO Auto-generated method stub
	}
	
}
