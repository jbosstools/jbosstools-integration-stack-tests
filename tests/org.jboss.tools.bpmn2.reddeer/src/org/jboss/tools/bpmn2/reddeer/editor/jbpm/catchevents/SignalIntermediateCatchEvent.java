package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

public class SignalIntermediateCatchEvent extends IntermediateCatchEvent {

	public SignalIntermediateCatchEvent(String name) {
		super(name, ConstructType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
	}
	
	public void setSignalMapping(String signal, String target) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Signal");
		if (properties.contains(nameBox, signal)) {
			nameBox.setSelection(signal);
		} else {
			new PushButton(0).click();
			new DefaultShell("Create New Signal").setFocus();
			new LabeledText("Name").setText(signal);
			new PushButton("OK").click();
		}
		new DefaultCombo("Target").setSelection(target);
		
		properties.toolbarButton("Signal Event Definition Details", "Close").click();
	}
	
	
}
