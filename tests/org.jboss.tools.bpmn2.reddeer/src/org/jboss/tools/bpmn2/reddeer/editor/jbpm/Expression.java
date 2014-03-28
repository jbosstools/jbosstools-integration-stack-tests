package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.reddeer.UIControlsHandler;

/**
 * 
 */
public class Expression implements UIControlsHandler {

	private String language;
	private String script;

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public Expression(String language, String script) {
		this.language = language;
		this.script = script;
	}

	/**
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @return
	 */
	public String getScript() {
		return script;
	}
	
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
	}
	
}
