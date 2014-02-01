package org.jboss.tools.bpmn2.reddeer.editor.switchyard.activities;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping;

public class SwitchYardServiceTask extends AbstractTask{

	private static final int ON_ENTRY = 0;
	private static final int ON_EXIT = 1;
	
	public SwitchYardServiceTask(String name) {
		super(name, ConstructType.SWITCHYARD_SERVICE_TASK);
	}
	
	public void setOperation(String name) {
		properties.selectTab("Service Task");
		 
		new PushButton(0).click();
		SWTBot viewBot = bot.shell("Create New Operation").bot();
		viewBot.textWithLabel("Name").setText(name);
		viewBot.button("OK").click();
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping)
	 */
	@Override
	public void addParameterMapping(ParameterMapping parameter) {
		super.addParameterMapping(parameter);
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
		/*new DefaultCombo("Script Language").setSelection(scriptLang);
		new LabeledText("Script").setText(text);*/
		bot.comboBoxWithLabel("Script Language", type);
		bot.textWithLabel("Script", type);
	}

}
