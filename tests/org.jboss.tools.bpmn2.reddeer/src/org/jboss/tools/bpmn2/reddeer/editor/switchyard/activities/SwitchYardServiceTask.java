package org.jboss.tools.bpmn2.reddeer.editor.switchyard.activities;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.OperationDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;

/**
 * TODO: apodhrad
 */
public class SwitchYardServiceTask extends AbstractTask{

	@SuppressWarnings("unused")
	private static final int ON_ENTRY = 0;
	@SuppressWarnings("unused")
	private static final int ON_EXIT = 1;
	
	public SwitchYardServiceTask(String name) {
		super(name, ConstructType.SWITCHYARD_SERVICE_TASK);
	}
	
	public void setOperation(String name) {
		properties.selectTab("Service Task");
		 
		new PushButton(0).click();
		new OperationDialog().add(name, null, null, null);
	}
	
	public void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}
	
	public void setTaskAttribute(String attribute, String text){
		properties.selectTab("SwitchYard Service Task");
		new LabeledText(attribute).setText(text);
	}
	
	/**
	 * 
	 * @param scriptLang
	 * @param text
	 * @param type ON_ENTRY|ON_EXIT
	 */
	public void setScript(String scriptLang, String text, int type){
		properties.selectTab("SwitchYard Service Task");
		/*new LabeledCombo("Script Language").setSelection(scriptLang);
		new LabeledText("Script").setText(text);*/
		bot.comboBoxWithLabel("Script Language", type);
		bot.textWithLabel("Script", type);
	}

}
