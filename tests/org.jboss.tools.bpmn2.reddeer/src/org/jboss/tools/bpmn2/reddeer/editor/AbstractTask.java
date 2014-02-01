package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractTask extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractTask(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param value
	 */
	protected void setIsForCompensation(boolean value) {
		properties.selectTab(type.toToolName());
		properties.selectCheckBox(new CheckBox("Is For Compensation"), value);
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	protected void setOnEntryScript(String language, String script) {
		properties.selectTab(type.toToolName());
		bot.comboBoxWithLabel("Script Language").setSelection(language);
		bot.textWithLabel("Script").setText(script);
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	protected void setOnExistScript(String language, String script) {
		properties.selectTab(type.toToolName());
		bot.comboBoxWithLabel("Script Language", 1).setSelection(language);
		bot.textWithLabel("Script", 1).setText(script);
	}
	
	/**
	 * 
	 * @param mapping
	 */
	protected void addParameterMapping(ParameterMapping mapping) {
		properties.selectTab("I/O Parameters");
		mapping.add();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#addEvent(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType)
	 */
	@Override
	public void addEvent(String name, ConstructType eventType) {
		super.addEvent(name, eventType);
	}
	
}
