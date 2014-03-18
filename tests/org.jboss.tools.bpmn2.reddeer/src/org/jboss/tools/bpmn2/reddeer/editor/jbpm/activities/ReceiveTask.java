package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ReceiveTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ReceiveTask(String name) {
		super(name, ConstructType.RECEIVE_TASK);
	}
	
	/**
	 * 
	 * @param implementation
	 */
	public void setImplementation(String implementation) {
		properties.selectTab("Receive Task");
		new LabeledCombo("Implementation").setSelection(implementation);
		// TBD: add code to add a user defined implementation
	}
	
	/**
	 * 
	 * @param operation
	 */
	public void setOperation(String operation, String inMessage, String outMessage) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * TODO: Add browse and add import or will we use a predefined project?
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		properties.selectTab("Receive Task");
		
		SWTBotCombo messageBox = bot.comboBoxWithLabel("Message");
		String messageName = name + "(" + dataType + ")";
		if (properties.contains(messageBox, messageName)) {
			messageBox.setSelection(messageName);
		} else {
			new PushButton(3).click();
			
			SWTBot newMessageBot = bot.shell("Create New Message").bot();
			newMessageBot.textWithLabel("Name").setText(name);
			newMessageBot.button(0).click();
			
			SWTBot newDataTypeBot = bot.shell("Create New Data Type").bot();
			newDataTypeBot.textWithLabel("Structure").setText(dataType);
			newDataTypeBot.button("OK").click();

			newMessageBot.button("OK").click();
		}
		
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping)
	 */
	@Override
	public void addParameterMapping(ParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
