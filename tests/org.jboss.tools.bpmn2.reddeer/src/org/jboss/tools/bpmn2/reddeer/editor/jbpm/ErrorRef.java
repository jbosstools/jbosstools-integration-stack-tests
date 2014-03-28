package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.reddeer.UIControlsHandler;

/**
 * 
 */
public class ErrorRef implements UIControlsHandler {

	private String name;
	private String code;
	private String dataType;

	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public ErrorRef(String name, String code, String dataType) {
		this.name = name;
		this.code = code;
		this.dataType = dataType;
		// default value
		if (this.name == null || this.name.isEmpty()) {
			this.name = "Error Code: " + code;
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