package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class MessageStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public MessageStartEvent(String name) {
		super(name, ConstructType.MESSAGE_START_EVENT);
	}

	/**
	 * 
	 * @param messageName
	 * @param dataType
	 */
	public void setMessageMapping(String message, String dataType, String target) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Message");
		String messageNameLabel  = message + "(" + dataType + ")";
		if (properties.contains(nameBox, messageNameLabel)) {
			nameBox.setSelection(messageNameLabel);
		} else {
			/*
			 * Click Add
			 */
			new PushButton(0).click();
			bot.shell("Create New Message").activate();
			
			new LabeledText("Name").setText(message);
			try {
				new LabeledCombo("Data Type").setSelection(dataType);
			} catch (SWTLayerException e) {
				new PushButton(0).click();
				bot.shell("Create Data Type").activate();
				new LabeledText("Structure").setText(dataType);
				new PushButton("OK").click();
			}

			new PushButton("OK").click();
			new LabeledCombo("Target").setSelection(target);
		}
		
		properties.toolbarButton("Message Event Definition Details", "Close").click();
	}
	
}